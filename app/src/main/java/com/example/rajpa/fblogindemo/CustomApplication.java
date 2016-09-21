package com.example.rajpa.fblogindemo;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.google.firebase.FirebaseOptions;

/**
 * Created by rajpa on 03-Sep-16.
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
