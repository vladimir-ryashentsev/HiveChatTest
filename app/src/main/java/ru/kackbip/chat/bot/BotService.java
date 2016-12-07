package ru.kackbip.chat.bot;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.kackbip.chat.R;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.bot.injection.BotComponent;
import ru.kackbip.chat.bot.messagesProvider.IMessagesProvider;
import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.data.entities.IMessagesRepository;
import ru.kackbip.chat.screens.chat.view.ChatViewActivity;
import rx.Emitter;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Владимир on 03.12.2016.
 */

public class BotService extends Service {

    private Subscription subscription;

    private IMessagesRepository repository;
    private IMessagesProvider messagesProvider;
    private BotComponent botComponent;
    private BotMode botMode = BotMode.WEAK;

    @Inject
    public void inject(IMessagesRepository repository,
                       IMessagesProvider messagesProvider) {
        this.repository = repository;
        this.messagesProvider = messagesProvider;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BotBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ChatApplication app = (ChatApplication) getApplication();
        botComponent = app.getBotComponent();
        botComponent.inject(this);

        sendMessage();
    }

    private Notification createNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_media_route_on_02_dark)
                        .setContentTitle(getString(R.string.activeBotNotificationTitle))
                        .setContentText(getString(R.string.activeBotNotificationText));

        Intent resultIntent = new Intent(this, ChatViewActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder.build();
    }

    private Observable<Void> delayObservable() {
        return Observable.<Void>just(null).delay(2 + new Random().nextInt(2), TimeUnit.SECONDS);
    }

    private void sendMessage() {
        subscription = delayObservable()
                .flatMap(aVoid -> messagesProvider.next())
                .subscribe(
                        s -> {
                            IMessage message = repository.create(getString(R.string.bot_name), s, new Date());
                            repository.add(message).subscribe();
                            sendMessage();
                        }
                );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
        ChatApplication app = (ChatApplication) getApplication();
        app.removeBotComponent();
    }

    public class BotBinder extends Binder {

        public BotMode getMode() {
            return botMode;
        }

        public void setMode(BotMode mode) {
            botMode = mode;
            if (mode == BotMode.WEAK) {
                stopForeground(true);
            } else {
                startForeground(1, createNotification());
            }
        }

    }

    public enum BotMode {
        MEGATRONE,
        WEAK
    }
}
