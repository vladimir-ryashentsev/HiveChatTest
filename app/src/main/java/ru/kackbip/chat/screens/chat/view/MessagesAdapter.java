package ru.kackbip.chat.screens.chat.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kackbip.chat.R;

import java.util.ArrayList;
import java.util.List;

import ru.kackbip.chat.data.entities.IMessage;

/**
 * Created by Владимир on 04.12.2016.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<IMessage> messages = new ArrayList<>();

    public void setMessages(List<IMessage> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.setMessage(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
