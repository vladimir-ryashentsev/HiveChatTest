package ru.kackbip.chat.screens.chat.interactors;

import java.util.List;

import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.data.entities.IMessage;
import rx.Observable;

/**
 * Created by Владимир on 04.12.2016.
 */

public class GetHistoryInteractor implements IInteractor<List<IMessage>, Void> {

    private IMessagesRepository repository;

    public GetHistoryInteractor(IMessagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<IMessage>> doIt(Void parameter) {
        return repository.getAll();
    }
}
