package ru.kackbip.chat.screens.chat.injection;

import dagger.Subcomponent;
import ru.kackbip.chat.screens.chat.view.ChatViewActivity;

/**
 * Created by Владимир on 05.12.2016.
 */

@Subcomponent(modules = {ChatModule.class})
@Chat
public interface ChatComponent {
    void inject(ChatViewActivity view);
}
