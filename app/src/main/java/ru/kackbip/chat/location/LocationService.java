package ru.kackbip.chat.location;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.kackbip.chat.R;

import java.util.Date;

import javax.inject.Inject;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.data.entities.IMessage;
import ru.kackbip.chat.data.entities.IMessagesRepository;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Владимир on 06.12.2016.
 */

public class LocationService extends Service {

    private static final String LOCATION_SENT = "locationSent";

    private ILocationProvider locationProvider;
    private IMessagesRepository messagesRepository;
    private SharedPreferences sharedPreferences;
    private Subscription subscription;

    @Inject
    public void inject(ILocationProvider locationProvider,
                       IMessagesRepository messagesRepository,
                       SharedPreferences sharedPreferences) {

        this.locationProvider = locationProvider;
        this.messagesRepository = messagesRepository;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ChatApplication app = (ChatApplication) getApplication();
        app.getLocationComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isCoordinateSent()) {
            subscription = locationProvider.getLocation()
                    .flatMap(this::sendMessage)
                    .subscribe(
                            aVoid -> setCoordinateSent(),
                            throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show()
                    );
        } else {
            stop();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private boolean isCoordinateSent() {
        return sharedPreferences.getBoolean(LOCATION_SENT, false);
    }

    private void setCoordinateSent() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOCATION_SENT, true);
        editor.apply();
        stop();
    }

    private void stop() {
        stopSelf();
        ChatApplication app = (ChatApplication) getApplication();
        app.removeLocationComponent();
    }

    private Observable<Void> sendMessage(Location location) {
        String text = getString(R.string.location_message, location.getLongitude(), location.getLatitude());
        IMessage message = messagesRepository.create(getString(R.string.my_name), text, new Date());
        return messagesRepository.add(message);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
    }
}
