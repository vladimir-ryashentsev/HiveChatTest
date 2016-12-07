package ru.kackbip.chat.notifications.injection;

import dagger.Subcomponent;
import ru.kackbip.chat.notifications.NotificationsService;

/**
 * Created by Владимир on 06.12.2016.
 */

@Subcomponent
@Notification
public interface NotificationsComponent {
    void inject(NotificationsService notificationsService);
}
