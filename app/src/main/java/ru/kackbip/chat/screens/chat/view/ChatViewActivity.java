package ru.kackbip.chat.screens.chat.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.kackbip.chat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.inject.Inject;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.notifications.NotificationsService;
import ru.kackbip.chat.screens.chat.presenter.ChatPresenter;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

public class ChatViewActivity extends AppCompatActivity implements IChatView {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter = new MessagesAdapter();
    private EditText messageInput;
    private Button send;
    private Switch autoScroll;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private PublishSubject<Void> stopSubject = PublishSubject.create();
    private PublishSubject<Void> destroySubject = PublishSubject.create();

    @Inject
    public void inject(ChatPresenter presenter) {
        presenter.setView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);

        send = (Button) findViewById(R.id.chat_send);
        messageInput = (EditText) findViewById(R.id.chat_input);
        recyclerView = (RecyclerView) findViewById(R.id.chat_messages);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        autoScroll = (Switch) findViewById(R.id.chat_autoScroll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, NotificationsService.class));

        ChatApplication app = (ChatApplication) getApplication();
        app.getChatComponent().inject(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        startService(new Intent(this, NotificationsService.class));
    }

    @Override
    public void showMessages(List<IMessage> messages) {
        adapter.setMessages(new ArrayList<>(messages));
        if (autoScroll.isChecked()
                && adapter.getItemCount() > 0)
            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void showInputText(String text) {
        messageInput.setText(text);
    }

    @Override
    public void setSendEnabled(boolean enabled) {
        send.setEnabled(enabled);
    }

    @Override
    public Observable<String> observeInputText() {
        return RxTextView.textChanges(messageInput)
                .map(CharSequence::toString);
    }

    @Override
    public Observable<Void> observeSend() {
        return RxView.clicks(send);
    }

    @Override
    public Observable<Void> observeStop() {
        return stopSubject;
    }

    @Override
    public Observable<Void> observeGoBotSettings() {
        return RxView.clicks(findViewById(R.id.chat_goToBotSettings));
    }

    @Override
    public Observable<Void> observeDestroy() {
        return destroySubject;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
        stopSubject.onNext(null);
        stopSubject.onCompleted();
        if (isFinishing()) {
            destroySubject.onNext(null);
            destroySubject.onCompleted();
        }
    }

    private void unsubscribe() {
        subscriptions.unsubscribe();
    }
}
