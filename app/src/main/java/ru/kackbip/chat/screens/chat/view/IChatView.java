package ru.kackbip.chat.screens.chat.view;

import java.util.List;
import java.util.SortedSet;

import ru.kackbip.chat.bot.BotService;
import ru.kackbip.chat.data.entities.IMessage;
import rx.Observable;

/**
 * Created by Владимир on 04.12.2016.
 */

public interface IChatView {
    void showMessages(List<IMessage> messages);
    void showInputText(String text);
    void setSendEnabled(boolean enabled);
    Observable<String> observeInputText();
    Observable<Void> observeSend();
    Observable<Void> observeStop();
    Observable<Void> observeGoBotSettings();
    Observable<Void> observeDestroy();
}
