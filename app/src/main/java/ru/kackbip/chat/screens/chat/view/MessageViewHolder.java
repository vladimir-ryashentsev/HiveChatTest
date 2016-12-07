package ru.kackbip.chat.screens.chat.view;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.kackbip.chat.R;

import java.text.DateFormat;

import ru.kackbip.chat.data.entities.IMessage;

/**
 * Created by Владимир on 04.12.2016.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    private TextView author;
    private TextView text;
    private TextView date;
    private CardView bg;

    public MessageViewHolder(View itemView) {
        super(itemView);
        author = (TextView) itemView.findViewById(R.id.message_author);
        text = (TextView) itemView.findViewById(R.id.message_text);
        date = (TextView) itemView.findViewById(R.id.message_date);
        bg = (CardView) itemView.findViewById(R.id.message_bg);
    }

    public void setMessage(IMessage message) {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM);
        date.setText(df.format(message.getDate()));
        text.setText(message.getText());
        author.setText(message.getAuthor());
        if (isSelf(message.getAuthor())) {
            bg.setCardBackgroundColor(getColor(R.color.myMessageBgColor));
            setTextsGravity(Gravity.START);
        } else {
            bg.setCardBackgroundColor(getColor(R.color.othersMessageBgColor));
            setTextsGravity(Gravity.END);
        }
    }

    private int getColor(int colorId){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return itemView.getResources().getColor(colorId, null);
        }else{
            return itemView.getResources().getColor(colorId);
        }
    }

    private void setTextsGravity(int gravity) {
        date.setGravity(gravity);
        text.setGravity(gravity);
        author.setGravity(gravity);
    }

    private boolean isSelf(String author) {
        return itemView.getResources().getString(R.string.my_name).equals(author);
    }
}
