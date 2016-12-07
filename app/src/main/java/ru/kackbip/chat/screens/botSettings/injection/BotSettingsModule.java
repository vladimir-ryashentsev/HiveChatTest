package ru.kackbip.chat.screens.botSettings.injection;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.screens.botSettings.interactors.GetBotModeInteractor;
import ru.kackbip.chat.screens.botSettings.interactors.SwitchBotModeInteractor;
import ru.kackbip.chat.screens.botSettings.presenter.BotSettingsPresenter;

/**
 * Created by Владимир on 07.12.2016.
 */

@Module
public class BotSettingsModule {

    @Provides
    @BotSettings
    public GetBotModeInteractor provideGetBotModeInteractor(Application application){
        return new GetBotModeInteractor(application);
    }

    @Provides
    @BotSettings
    public SwitchBotModeInteractor provideSwitchBotModeInteractor(Application application){
        return new SwitchBotModeInteractor(application);
    }

    @Provides
    @BotSettings
    public BotSettingsPresenter provideBotSettingsPresenter(GetBotModeInteractor getBotModeInteractor,
                                                            SwitchBotModeInteractor switchBotModeInteractor,
                                                            ChatApplication application){
        return new BotSettingsPresenter(getBotModeInteractor, switchBotModeInteractor, application);
    }

}
