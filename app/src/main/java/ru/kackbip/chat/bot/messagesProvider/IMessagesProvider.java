package ru.kackbip.chat.bot.messagesProvider;

import rx.Observable;

/**
 * Created by Владимир on 05.12.2016.
 */

public interface IMessagesProvider {
    Observable<String> next();
}
