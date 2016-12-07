package ru.kackbip.chat.screens.botSettings.interactors;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import ru.kackbip.chat.bot.BotService;
import ru.kackbip.chat.screens.chat.interactors.IInteractor;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Владимир on 07.12.2016.
 */

public class GetBotModeInteractor implements IInteractor<BotService.BotMode, Void> {

    private Application application;

    public GetBotModeInteractor(Application application) {
        this.application = application;
    }

    @Override
    public Observable<BotService.BotMode> doIt(Void parameter) {
        return Observable.<BotService.BotMode>fromEmitter(
                emitter -> application
                        .bindService(
                                new Intent(application, BotService.class),
                                new ServiceConnection() {
                                    @Override
                                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                                        BotService.BotBinder binder = (BotService.BotBinder) iBinder;
                                        emitter.onNext(binder.getMode());
                                        emitter.onCompleted();
                                        application.unbindService(this);
                                    }

                                    @Override
                                    public void onServiceDisconnected(ComponentName componentName) {
                                    }
                                },
                                0),
                Emitter.BackpressureMode.ERROR)
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
