package ru.kackbip.chat.location;

import android.location.Location;

import rx.Observable;

/**
 * Created by Владимир on 03.12.2016.
 */

public interface ILocationProvider {
    Observable<Location> getLocation();
}
