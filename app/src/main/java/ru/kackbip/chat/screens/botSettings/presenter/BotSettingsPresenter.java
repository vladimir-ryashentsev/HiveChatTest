package ru.kackbip.chat.screens.botSettings.presenter;

import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.bot.BotService;
import ru.kackbip.chat.screens.botSettings.interactors.GetBotModeInteractor;
import ru.kackbip.chat.screens.botSettings.interactors.SwitchBotModeInteractor;
import ru.kackbip.chat.screens.botSettings.view.IBotSettingsView;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Владимир on 07.12.2016.
 */

public class BotSettingsPresenter {

    private final GetBotModeInteractor getBotModeInteractor;
    private final SwitchBotModeInteractor switchBotModeInteractor;
    private ChatApplication application;
    private IBotSettingsView view;
    private CompositeSubscription viewSubscriptions = new CompositeSubscription();
    private CompositeSubscription interactorsSubscriptions = new CompositeSubscription();
    private BotService.BotMode botMode;
    private boolean modeEnabled = false;

    public BotSettingsPresenter(GetBotModeInteractor getBotModeInteractor,
                                SwitchBotModeInteractor switchBotModeInteractor,
                                ChatApplication application) {
        this.getBotModeInteractor = getBotModeInteractor;
        this.switchBotModeInteractor = switchBotModeInteractor;
        this.application = application;
        bootstrap();
    }

    private void bootstrap() {
        Subscription s = getBotModeInteractor.doIt(null).subscribe(this::setMode);
        viewSubscriptions.add(s);
    }

    private void setMode(BotService.BotMode mode) {
        botMode = mode;
        modeEnabled = true;
        if (view != null) {
            view.showMode(botMode);
            view.setModeEnabled(true);
        }
    }

    public void setView(IBotSettingsView view) {
        viewSubscriptions = new CompositeSubscription();
        this.view = view;
        bootstrapView();
    }

    private void bootstrapView() {
        view.setModeEnabled(modeEnabled);
        if (modeEnabled)
            view.showMode(botMode);
        viewSubscriptions.add(
                view.observeModeSwitch().subscribe(this::switchMode)
        );
        viewSubscriptions.add(
                view.observeStop().subscribe(aVoid -> viewSubscriptions.unsubscribe())
        );
        viewSubscriptions.add(
                view.observeDestroy().subscribe(aVoid -> {
                    interactorsSubscriptions.unsubscribe();
                    application.removeBotSettingsComponent();
                })
        );
    }

    private void switchMode(BotService.BotMode mode) {
        if (mode == botMode) return;
        botMode = botMode == BotService.BotMode.WEAK ? BotService.BotMode.MEGATRONE : BotService.BotMode.WEAK;
        view.showMode(botMode);
        interactorsSubscriptions.add(switchBotModeInteractor.doIt(botMode).subscribe());
    }
}
