package ru.kackbip.chat.data.entities;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by Владимир on 04.12.2016.
 */

public interface IMessagesRepository {
    IMessage create(String author, String text, Date date);

    Observable<Void> add(IMessage message);

    Observable<IMessage> observeNew();

    Observable<List<IMessage>> getAll();
}
