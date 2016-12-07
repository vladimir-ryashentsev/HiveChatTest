package ru.kackbip.chat.app.injection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.kackbip.chat.app.ChatApplication;
import ru.kackbip.chat.app.currentActivityProvider.CurrentActivityProvider;
import ru.kackbip.chat.app.currentActivityProvider.ICurrentActivityProvider;

/**
 * Created by Владимир on 05.12.2016.
 */

@Module
public class AppModule {

    private ChatApplication application;
    private CurrentActivityProvider currentActivityProvider;

    public AppModule(ChatApplication application,
                     CurrentActivityProvider currentActivityProvider){
        this.application = application;
        this.currentActivityProvider = currentActivityProvider;
    }

    @Provides
    @Singleton
    public ChatApplication provideChatApplication(){
        return application;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return application;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Application application){
        return application.getSharedPreferences("ChatAppSharedPreferences", 0);
    }

    @Provides
    @Singleton
    public ICurrentActivityProvider provideCurrentActivityProvider(){
        return currentActivityProvider;
    }
}
