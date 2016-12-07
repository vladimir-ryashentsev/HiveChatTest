package ru.kackbip.chat.screens.chat.presenter;

import java.util.ArrayList;
import java.util.List;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.screens.chat.interactors.GetHistoryInteractor;
import ru.kackbip.chat.screens.chat.interactors.ObserveNewMessagesInteractor;
import ru.kackbip.chat.screens.chat.interactors.SendMessageInteractor;
import ru.kackbip.chat.screens.chat.router.IChatRouter;
import ru.kackbip.chat.screens.chat.view.IChatView;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Владимир on 04.12.2016.
 */

public class ChatPresenter {

    private static final int MIN_MESSAGE_LENGTH = 1;
    private final IChatRouter router;
    private final SendMessageInteractor sendMessageInteractor;
    private final GetHistoryInteractor getHistoryInteractor;
    private final ObserveNewMessagesInteractor observeNewMessagesInteractor;
    private ChatApplication application;
    private IChatView view;
    private String inputText = "";
    private List<IMessage> messages = new ArrayList<>();
    private CompositeSubscription viewSubscriptions;
    private CompositeSubscription interactorSubscriptions = new CompositeSubscription();

    public ChatPresenter(SendMessageInteractor sendMessageInteractor,
                         GetHistoryInteractor getHistoryInteractor,
                         ObserveNewMessagesInteractor observeNewMessagesInteractor,
                         IChatRouter router,
                         ChatApplication application) {
        this.router = router;
        this.sendMessageInteractor = sendMessageInteractor;
        this.getHistoryInteractor = getHistoryInteractor;
        this.observeNewMessagesInteractor = observeNewMessagesInteractor;
        this.application = application;
        getHistory();
        observeNewMessages();
    }

    public void setView(IChatView view) {
        viewSubscriptions = new CompositeSubscription();
        this.view = view;
        view.showInputText(inputText);
        view.showMessages(messages);
        viewSubscriptions.add(
                view.observeInputText().subscribe(s -> {
                    inputText = s;
                    view.setSendEnabled(inputText.length() >= MIN_MESSAGE_LENGTH);
                })
        );
        viewSubscriptions.add(
                view.observeSend()
                        .flatMap(aVoid -> sendMessage())
                        .subscribe(aVoid -> clearInputText())
        );
        viewSubscriptions.add(
                view.observeGoBotSettings()
                        .subscribe(aVoid -> router.goToBotSettings())
        );
        viewSubscriptions.add(
                view.observeStop()
                        .subscribe(aVoid -> viewSubscriptions.unsubscribe())
        );
        viewSubscriptions.add(
                view.observeDestroy()
                        .subscribe(aVoid -> {
                            interactorSubscriptions.unsubscribe();
                            application.removeChatComponent();
                        })
        );
    }

    private void observeNewMessages() {
        interactorSubscriptions.add(observeNewMessagesInteractor.doIt(null)
                .subscribe(message -> {
                    messages.add(message);
                    if (view != null) view.showMessages(messages);
                }));
    }

    private void getHistory() {
        interactorSubscriptions.add(getHistoryInteractor.doIt(null)
                .subscribe(history -> {
                    messages.addAll(0, history);
                    if (view != null) view.showMessages(messages);
                }));
    }

    private void clearInputText() {
        inputText = "";
        view.showInputText(inputText);
    }

    private Observable<Void> sendMessage() {
        return sendMessageInteractor.doIt(inputText);
    }
}
