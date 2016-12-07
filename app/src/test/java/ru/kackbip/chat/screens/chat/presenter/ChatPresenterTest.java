package ru.kackbip.chat.screens.chat.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.screens.chat.interactors.GetHistoryInteractor;
import ru.kackbip.chat.screens.chat.interactors.ObserveNewMessagesInteractor;
import ru.kackbip.chat.screens.chat.interactors.SendMessageInteractor;
import ru.kackbip.chat.screens.chat.router.IChatRouter;
import ru.kackbip.chat.screens.chat.view.IChatView;
import ru.kackbip.chat.testEntities.Message;
import rx.Emitter;
import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Владимир on 04.12.2016.
 */
public class ChatPresenterTest {

    private static final String ME = "Автобот";
    private static final String BOT = "Десептикон";
    private static final List<IMessage> HISTORY = Arrays.asList(
            new Message(ME, "Трансформируюсь!", new Date(1)),
            new Message(BOT, "Злобно трансформируюсь!", new Date(2))
    );
    private static final Message NEW_MESSAGE = new Message(ME, "Перетрансформируюсь!", new Date(3));
    private static final String INPUT_TEXT = "Трансформируюсь!";

    private ChatPresenter presenter;
    private SendMessageInteractor sendMessageInteractor;
    private GetHistoryInteractor getHistoryInteractor;
    private ObserveNewMessagesInteractor observeNewMessagesInteractor;
    private IChatView view;
    private IChatRouter router;
    private ChatApplication application;
    private Emitter<String> inputEmitter;
    private Emitter<Void> sendEmitter;
    private Emitter<Void> stopEmitter;
    private Emitter<IMessage> messageEmitter;
    private Emitter<Void> goToBotSettingsEmitter;
    private Emitter<Void> destroyEmitter;

    @Before
    public void setUp() throws Exception {
        sendMessageInteractor = mock(SendMessageInteractor.class);

        getHistoryInteractor = mock(GetHistoryInteractor.class);
        when(getHistoryInteractor.doIt(null)).thenReturn(Observable.just(HISTORY));

        observeNewMessagesInteractor = mock(ObserveNewMessagesInteractor.class);
        when(observeNewMessagesInteractor.doIt(null)).thenReturn(Observable.fromEmitter(emitter -> messageEmitter = emitter, Emitter.BackpressureMode.ERROR));

        view = mock(IChatView.class);
        when(view.observeInputText()).thenReturn(Observable.fromEmitter(emitter -> inputEmitter = emitter, Emitter.BackpressureMode.ERROR));
        when(view.observeSend()).thenReturn(Observable.fromEmitter(emitter -> sendEmitter = emitter, Emitter.BackpressureMode.ERROR));
        when(view.observeStop()).thenReturn(Observable.fromEmitter(emitter -> stopEmitter = emitter, Emitter.BackpressureMode.ERROR));
        when(view.observeDestroy()).thenReturn(Observable.fromEmitter(emitter -> destroyEmitter = emitter, Emitter.BackpressureMode.ERROR));
        when(view.observeGoBotSettings()).thenReturn(Observable.fromEmitter(emitter -> goToBotSettingsEmitter = emitter, Emitter.BackpressureMode.ERROR));

        router = mock(IChatRouter.class);

        application = mock(ChatApplication.class);

        presenter = new ChatPresenter(sendMessageInteractor,
                getHistoryInteractor,
                observeNewMessagesInteractor,
                router,
                application);

        presenter.setView(view);
    }

    @Test
    public void setView() {
        verify(view).observeInputText();
        verify(view).observeSend();
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(view).showMessages(captor.capture());
        List<Message> captured = captor.getValue();
        assertTrue(captured.size() == 2);
        assertTrue(captured.containsAll(HISTORY));
        verify(view).showInputText("");
        verify(view).observeGoBotSettings();
        verify(view).observeStop();
        verify(view).observeDestroy();
    }

    @Test
    public void enableSend() {
        inputEmitter.onNext("123");
        verify(view).setSendEnabled(true);
        inputEmitter.onNext("");
        verify(view).setSendEnabled(false);
    }

    @Test
    public void goToBotSettings() {
        goToBotSettingsEmitter.onNext(null);
        verify(router).goToBotSettings();
    }

    @Test
    public void sendMessage() {
        inputEmitter.onNext(INPUT_TEXT);
        sendEmitter.onNext(null);
        verify(sendMessageInteractor).doIt(INPUT_TEXT);
    }

    @Test
    public void newMessage() {
        verify(view).showMessages(any());
        messageEmitter.onNext(NEW_MESSAGE);
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(view, times(2)).showMessages(captor.capture());
        List<Message> captured = captor.getValue();
        assertTrue(captured.size() == 3);
        assertEquals(NEW_MESSAGE, captured.get(captured.size()-1));
    }

    @Test
    public void destroy() {
        destroyEmitter.onNext(null);
        verify(application).removeChatComponent();
    }

    @Test
    public void stop() {
        stopEmitter.onNext(null);

        goToBotSettingsEmitter.onNext(null);
        verify(router, never()).goToBotSettings();

        messageEmitter.onNext(NEW_MESSAGE);
        verify(sendMessageInteractor, never()).doIt(any());
    }
}