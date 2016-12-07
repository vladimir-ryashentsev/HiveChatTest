package ru.kackbip.chat.screens.chat.interactors;

import java.util.Date;

import ru.kackbip.chat.data.entities.IMessagesRepository;
import rx.Observable;

/**
 * Created by Владимир on 04.12.2016.
 */

public class SendMessageInteractor implements IInteractor<Void, String> {

    private IMessagesRepository repository;
    private String name;

    public SendMessageInteractor(IMessagesRepository repository,
                                 String name) {
        this.repository = repository;
        this.name = name;
    }

    @Override
    public Observable<Void> doIt(String text) {
        return repository.add(repository.create(name, text, new Date()));
    }
}
