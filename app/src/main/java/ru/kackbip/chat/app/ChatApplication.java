package ru.kackbip.chat.app;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import ru.kackbip.chat.app.currentActivityProvider.CurrentActivityProvider;
import ru.kackbip.chat.app.injection.AppComponent;
import ru.kackbip.chat.app.injection.AppModule;
import ru.kackbip.chat.app.injection.DaggerAppComponent;
import ru.kackbip.chat.bot.BotService;
import ru.kackbip.chat.bot.injection.BotComponent;
import ru.kackbip.chat.data.realm.injection.RealmMessagesRepositoryModule;
import ru.kackbip.chat.location.LocationService;
import ru.kackbip.chat.location.injection.LocationComponent;
import ru.kackbip.chat.notifications.injection.NotificationsComponent;
import ru.kackbip.chat.screens.botSettings.injection.BotSettingsComponent;
import ru.kackbip.chat.screens.chat.injection.ChatComponent;

/**
 * Created by Владимир on 03.12.2016.
 */

public class ChatApplication extends MultiDexApplication {

    private AppComponent appComponent;
    private BotComponent botComponent;
    private ChatComponent chatComponent;
    private LocationComponent locationComponent;
    private NotificationsComponent notificationsComponent;
    private BotSettingsComponent botSettingsComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this, new CurrentActivityProvider(this)))
                .build();

        startService(new Intent(this, LocationService.class));
        startService(new Intent(this, BotService.class));
    }

    public BotComponent getBotComponent() {
        if (botComponent == null)
            botComponent = appComponent.plusBotComponent();
        return botComponent;
    }
    public void removeBotComponent(){
        botComponent = null;
    }

    public ChatComponent getChatComponent() {
        if (chatComponent == null)
            chatComponent = appComponent.plusChatComponent();
        return chatComponent;
    }
    public void removeChatComponent(){
        chatComponent = null;
    }

    public LocationComponent getLocationComponent() {
        if (locationComponent == null)
            locationComponent = appComponent.plusLocationComponent();
        return locationComponent;
    }
    public void removeLocationComponent(){
        locationComponent = null;
    }

    public NotificationsComponent getNotificationsComponent() {
        if (notificationsComponent == null)
            notificationsComponent = appComponent.plusNotificationsComponent();
        return notificationsComponent;
    }
    public void removeNotificationsComponent(){
        notificationsComponent = null;
    }

    public BotSettingsComponent getBotSettingsComponent() {
        if (botSettingsComponent == null)
            botSettingsComponent = appComponent.plusBotSettingsComponent();
        return botSettingsComponent;
    }
    public void removeBotSettingsComponent(){
        botSettingsComponent = null;
    }

}
