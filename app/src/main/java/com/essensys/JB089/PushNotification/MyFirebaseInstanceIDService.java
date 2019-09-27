package com.essensys.JB089.PushNotification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import static android.content.Context.MODE_PRIVATE;
/**
 * Created by Infinitum on 15/11/2018.
 */
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    String refreshedToken;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                refreshedToken = instanceIdResult.getToken();
                Log.e("Token",refreshedToken);
                Log.d(TAG, "onTokenRefresh called");
                // Saving reg id to shared preferences
                storeRegIdInPref(refreshedToken);

                // sending reg id to your server
                sendRegistrationToServer(refreshedToken);

            }
        });
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
//    @Override
//    public void onTokenRefresh() {
//        super.onTokenRefresh();
////        String refreshedToken = FirebaseInstanceId.getInstance().getInstanceId();
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                 refreshedToken = instanceIdResult.getToken();
//                Log.e("Token",refreshedToken);
//                Log.d(TAG, "onTokenRefresh called");
//                // Saving reg id to shared preferences
//                storeRegIdInPref(refreshedToken);
//
//                // sending reg id to your server
//                sendRegistrationToServer(refreshedToken);
//
//            }
//        });
//        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//        registrationComplete.putExtra("token", refreshedToken);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
//    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.d(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
//        SessionClass.setUserToken(getApplicationContext(),token);

        SharedPreferences.Editor editor = getSharedPreferences("Token", MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
//        SharedPreferencesUtil.getInstance(getApplicationContext()).putData(FCM_TOKEN, token);
    }
}
//public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
//
//
//    @Override
//    public void onTokenRefresh() {
//
//        //For registration of token
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        sendRegistrationToServer(refreshedToken);
//        //To displaying token on logcat
//        Log.d("TOKEN: ", refreshedToken);
//
//    }
//    private void sendRegistrationToServer(String token) {
//        // Add custom implementation, as needed.
//        SessionClass.setUserToken(getApplicationContext(),token);
//    }
//
//}
