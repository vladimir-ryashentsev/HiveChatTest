package ru.kackbip.chat.app.currentActivityProvider;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Update current activity during onResume
 */

public class CurrentActivityProvider implements Application.ActivityLifecycleCallbacks, ICurrentActivityProvider {

    private Activity currentActivity;

    public CurrentActivityProvider(Application application){
        application.registerActivityLifecycleCallbacks(this);
    }

    public Activity getCurrentActivity(){
        return currentActivity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}