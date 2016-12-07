package ru.kackbip.chat.bot.injection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import ru.kackbip.chat.bot.messagesProvider.IMessagesProvider;
import ru.kackbip.chat.bot.messagesProvider.MessagesProvider;

/**
 * Created by Владимир on 05.12.2016.
 */

@Module
public class BotModule {

    @Provides
    @Bot
    public Gson provideGson(){
        return new GsonBuilder().create();
    }

    @Provides
    @Bot
    public IMessagesProvider providerMessagesProvider(Gson gson) {
        return new MessagesProvider(gson);
    }
}
