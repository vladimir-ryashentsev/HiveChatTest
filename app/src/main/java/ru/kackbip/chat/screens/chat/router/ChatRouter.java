package ru.kackbip.chat.screens.chat.router;

import android.app.Activity;
import android.content.Intent;

import ru.kackbip.chat.app.currentActivityProvider.ICurrentActivityProvider;
import ru.kackbip.chat.screens.botSettings.view.BotSettingsViewActivity;

/**
 * Created by Владимир on 07.12.2016.
 */

public class ChatRouter implements IChatRouter {

    private ICurrentActivityProvider currentActivityProvider;

    public ChatRouter(ICurrentActivityProvider currentActivityProvider){
        this.currentActivityProvider = currentActivityProvider;
    }

    @Override
    public void goToBotSettings() {
        Activity activity = currentActivityProvider.getCurrentActivity();
        activity.startActivity(new Intent(activity, BotSettingsViewActivity.class));
    }
}