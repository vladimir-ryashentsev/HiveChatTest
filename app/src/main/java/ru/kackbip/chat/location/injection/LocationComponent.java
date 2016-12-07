package ru.kackbip.chat.location.injection;

import dagger.Subcomponent;
import ru.kackbip.chat.location.LocationService;

/**
 * Created by Владимир on 06.12.2016.
 */

@Subcomponent(modules = {LocationModule.class})
@Location
public interface LocationComponent {
    void inject(LocationService locationService);
}
