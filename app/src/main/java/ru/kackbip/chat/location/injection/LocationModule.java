package ru.kackbip.chat.location.injection;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import ru.kackbip.chat.location.ILocationProvider;
import ru.kackbip.chat.location.LocationProvider;

/**
 * Created by Владимир on 06.12.2016.
 */

@Module
public class LocationModule {

    @Provides
    @Location
    public ILocationProvider providerLocationProvider(Application application) {
        return new LocationProvider(application);
    }

}
