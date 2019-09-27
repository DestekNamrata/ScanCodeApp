package com.essensys.JB089;
import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        logUser();

    }
    private void logUser() {
        Crashlytics.setUserEmail("vdl@essensys.co.in");
    }
}
