package ru.kackbip.chat.testEntities;

import java.util.Date;

import ru.kackbip.chat.data.entities.IMessage;

/**
 * Created by Владимир on 04.12.2016.
 */

public class Message implements IMessage {

    private String author;
    private String text;
    private Date date;

    public Message(String author,
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
    public int hashCode() {
        return (int) date.getTime() + text.hashCode() + author.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Message)) return false;
        Message message = (Message) obj;
        return message.getDate().equals(date)
                && text.equals(message.getText())
                && author.equals(message.getAuthor());
    }

    @Override
    public int compareTo(IMessage message) {
        return date.compareTo(message.getDate());
    }
}
