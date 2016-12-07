package ru.kackbip.chat.screens.chat.injection;

import android.app.Application;

import com.kackbip.chat.R;

import dagger.Module;
import dagger.Provides;
import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.app.currentActivityProvider.ICurrentActivityProvider;
import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.screens.chat.interactors.GetHistoryInteractor;
import ru.kackbip.chat.screens.chat.interactors.ObserveNewMessagesInteractor;
import ru.kackbip.chat.screens.chat.interactors.SendMessageInteractor;
import ru.kackbip.chat.screens.chat.presenter.ChatPresenter;
import ru.kackbip.chat.screens.chat.router.ChatRouter;
import ru.kackbip.chat.screens.chat.router.IChatRouter;

/**
 * Created by Владимир on 05.12.2016.
 */

@Module
public class ChatModule {

    @Provides
    @Chat
    public IChatRouter provideChatRouter(ICurrentActivityProvider currentActivityProvider) {
        return new ChatRouter(currentActivityProvider);
    }

    @Provides
    @Chat
    public String provideMyName(Application application) {
        return application.getString(R.string.my_name);
    }

    @Provides
    @Chat
    public SendMessageInteractor provideSendMessageInteractor(IMessagesRepository repository, String myName) {
        return new SendMessageInteractor(repository, myName);
    }

    @Provides
    @Chat
    public ObserveNewMessagesInteractor provideObserveNewMessagesInteractor(IMessagesRepository repository) {
        return new ObserveNewMessagesInteractor(repository);
    }

    @Provides
    @Chat
    public GetHistoryInteractor provideGetHistoryInteractor(IMessagesRepository repository) {
        return new GetHistoryInteractor(repository);
    }

    @Provides
    @Chat
    public ChatPresenter providePresenter(SendMessageInteractor sendMessageInteractor,
                                          ObserveNewMessagesInteractor observeNewMessagesInteractor,
                                          GetHistoryInteractor getHistoryInteractor,
                                          IChatRouter chatRouter,
                                          ChatApplication application) {
        return new ChatPresenter(sendMessageInteractor, getHistoryInteractor, observeNewMessagesInteractor, chatRouter, application);
    }
}
