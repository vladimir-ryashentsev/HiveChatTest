package ru.kackbip.chat.screens.botSettings.injection;

import dagger.Subcomponent;
import ru.kackbip.chat.screens.botSettings.view.BotSettingsViewActivity;

/**
 * Created by Владимир on 07.12.2016.
 */

@Subcomponent(modules = {BotSettingsModule.class})
@BotSettings
public interface BotSettingsComponent {
    void inject(BotSettingsViewActivity botSettingsViewActivity);
}
