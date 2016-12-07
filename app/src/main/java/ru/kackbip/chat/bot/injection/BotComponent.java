package ru.kackbip.chat.bot.injection;

import dagger.Subcomponent;
import ru.kackbip.chat.bot.BotService;

/**
 * Created by Владимир on 05.12.2016.
 */

@Subcomponent(modules = {BotModule.class})
@Bot
public interface BotComponent {
    void inject(BotService botService);
}
