package ru.kackbip.chat.data.entities;

import java.util.Date;

/**
 * Created by Владимир on 05.12.2016.
 */

public interface IMessage extends Comparable<IMessage> {
    String getAuthor();
    String getText();
    Date getDate();
}
