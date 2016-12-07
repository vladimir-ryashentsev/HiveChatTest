package ru.kackbip.chat.screens.chat.interactors;

import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.data.entities.IMessage;
import rx.Observable;

/**
 * Created by Владимир on 04.12.2016.
 */

public class ObserveNewMessagesInteractor implements IInteractor<IMessage, Void> {

    private IMessagesRepository repository;

    public ObserveNewMessagesInteractor(IMessagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<IMessage> doIt(Void aVoid) {
        return repository.observeNew();
    }
}
