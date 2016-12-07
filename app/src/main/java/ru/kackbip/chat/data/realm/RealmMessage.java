package ru.kackbip.chat.data.realm;

import java.util.Date;

import io.realm.RealmObject;
import ru.kackbip.chat.data.entities.IMessage;

/**
 * Created by Владимир on 05.12.2016.
 */

public class RealmMessage extends RealmObject implements IMessage {

    private String author;
    private String text;
    private Date date;

    public RealmMessage() {
    }

    public RealmMessage(String author,
                        String text,
                        Date date) {
        this.author = author;
        this.text = text;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(IMessage message) {
        return date.compareTo(message.getDate());
    }
}
