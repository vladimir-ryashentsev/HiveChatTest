package ru.kackbip.chat.location;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.kackbip.chat.R;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Владимир on 05.12.2016.
 */

public class LocationProvider implements ILocationProvider, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private Application application;
    private PublishSubject<Location> locationSubject;

    public LocationProvider(Application application) {
        this.application = application;
    }

    @Override
    public Observable<Location> getLocation() {
        if (locationSubject == null ||
                locationSubject.hasCompleted() ||
                locationSubject.hasThrowable()) {
            locationSubject = PublishSubject.create();
            googleApiClient = new GoogleApiClient.Builder(application)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
        return locationSubject;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat
                .checkSelfPermission(
                        application,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat
                .checkSelfPermission(
                        application,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                ) {
            locationSubject.onError(new Throwable(application.getString(R.string.location_permission_denied)));
            googleApiClient.disconnect();
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        locationSubject.onNext(location);
        locationSubject.onCompleted();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        locationSubject.onError(new Throwable(application.getString(R.string.location_cant_get_location)));
    }
}
