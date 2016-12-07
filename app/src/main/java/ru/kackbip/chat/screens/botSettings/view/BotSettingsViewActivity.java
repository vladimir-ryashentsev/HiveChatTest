package ru.kackbip.chat.screens.botSettings.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;

import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.kackbip.chat.R;

import javax.inject.Inject;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.bot.BotService;
import ru.kackbip.chat.screens.botSettings.presenter.BotSettingsPresenter;
import rx.Observable;
import rx.subjects.PublishSubject;

public class BotSettingsViewActivity extends AppCompatActivity implements IBotSettingsView {

    private Switch megatroneMode;
    private PublishSubject<Void> stopSubject = PublishSubject.create();
    private PublishSubject<Void> destroySubject = PublishSubject.create();

    @Inject
    public void inject(BotSettingsPresenter presenter) {
        presenter.setView(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot_settings_view);

        megatroneMode = (Switch) findViewById(R.id.botSettings_megatroneMode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatApplication app = (ChatApplication) getApplication();
        app.getBotSettingsComponent().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSubject.onNext(null);
        stopSubject.onCompleted();
        if (isFinishing()) {
            destroySubject.onNext(null);
            destroySubject.onCompleted();
        }
    }

    @Override
    public void showMode(BotService.BotMode mode) {
        megatroneMode.setChecked(mode == BotService.BotMode.MEGATRONE);
    }

    @Override
    public void setModeEnabled(boolean enabled) {
        megatroneMode.setEnabled(enabled);
    }

    @Override
    public Observable<BotService.BotMode> observeModeSwitch() {
        return RxCompoundButton.checkedChanges(megatroneMode)
                .map(b -> b ? BotService.BotMode.MEGATRONE : BotService.BotMode.WEAK);
    }

    @Override
    public Observable<Void> observeStop() {
        return stopSubject;
    }

    @Override
    public Observable<Void> observeDestroy() {
        return destroySubject;
    }
}
