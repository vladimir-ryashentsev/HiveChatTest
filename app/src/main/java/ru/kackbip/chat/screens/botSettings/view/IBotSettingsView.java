package ru.kackbip.chat.screens.botSettings.view;

import ru.kackbip.chat.bot.BotService;
import rx.Observable;

/**
 * Created by Владимир on 07.12.2016.
 */

public interface IBotSettingsView {
    void showMode(BotService.BotMode mode);
    void setModeEnabled(boolean enabled);
    Observable<BotService.BotMode> observeModeSwitch();
    Observable<Void> observeStop();
    Observable<Void> observeDestroy();
}
