package ru.kackbip.chat.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.kackbip.chat.R;

import javax.inject.Inject;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.screens.chat.view.ChatViewActivity;
import rx.Subscription;

/**
 * Created by Владимир on 06.12.2016.
 */

public class NotificationsService extends Service {

    private static final int MIN_ID = 500;

    private IMessagesRepository repository;
    private Subscription subscription;
    private int nextId = MIN_ID;

    @Inject
    public void inject(IMessagesRepository repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ChatApplication app = (ChatApplication) getApplication();
        app.getNotificationsComponent().inject(this);

        subscription = repository.observeNew().subscribe(this::createNotification);
    }

    private void createNotification(IMessage message) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cast_ic_notification_0)
                        .setContentTitle(message.getAuthor())
                        .setContentText(message.getText());

        Intent resultIntent = new Intent(this, ChatViewActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(nextId, mBuilder.build());
        nextId++;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeNotifications();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        ChatApplication app = (ChatApplication) getApplication();
        app.removeNotificationsComponent();
    }

    private void removeNotifications() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
