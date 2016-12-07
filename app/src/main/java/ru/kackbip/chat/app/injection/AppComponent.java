package ru.kackbip.chat.app.injection;

import javax.inject.Singleton;

import dagger.Component;
import ru.kackbip.chat.bot.injection.BotComponent;
import ru.kackbip.chat.data.realm.injection.RealmMessagesRepositoryModule;
import ru.kackbip.chat.location.injection.LocationComponent;
import ru.kackbip.chat.notifications.injection.NotificationsComponent;
import ru.kackbip.chat.screens.botSettings.injection.BotSettingsComponent;
import ru.kackbip.chat.screens.chat.injection.ChatComponent;

/**
 * Created by Владимир on 05.12.2016.
 */

@Singleton
@Component(modules={AppModule.class, RealmMessagesRepositoryModule.class})
public interface AppComponent {
    BotComponent plusBotComponent();
    ChatComponent plusChatComponent();
    LocationComponent plusLocationComponent();
    NotificationsComponent plusNotificationsComponent();
    BotSettingsComponent plusBotSettingsComponent();
}
