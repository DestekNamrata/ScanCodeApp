package com.essensys.JB089.PushNotification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.essensys.JB089.Activity.ActivityLogin;
import com.essensys.JB089.Activity.SplashScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Infinitum on 15/11/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        String message = "";
        String flg="";
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        //used only for simple notification messages which i.e string messages
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
            String title=remoteMessage.getNotification().getTitle();
            String json=remoteMessage.getNotification().getBody();
            try {
                JSONObject jsonObject=new JSONObject(json);
                 message=jsonObject.getString("msg").toString();
                 flg=jsonObject.getString("flag").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            String message=remoteMessage.getNotification().getBody();
            String timestamp = "";

            Intent resultIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            resultIntent.putExtra("message", message);
//            resultIntent.putExtra(FLAG, "2");
            showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent,flg);
        }


        //this is used if messages are in format of jsonobject and used to see the messages in proper format in background app
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }else
        {

        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
//            pushNotification.putExtra(FLAG, "2");
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }
    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent,String flag) {

        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("message", message);
//        intent.putExtra(FLAG, "2");
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent,flag);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
//vvv imp used for notification to be seen even after the app is closed i.e at background
    private void handleDataMessage(JSONObject json) {
        try {
//            JSONObject data = new JSONObject();
            Log.e(TAG, "data : " + json.toString());
            String title = json.getString("title");
            String message = json.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = json.getString("image_url");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
                showNotificationMessage(getApplicationContext(), title, message, "" ,pushNotification,imageUrl);

            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), ActivityLogin.class);
                resultIntent.putExtra("message", message);
                showNotificationMessage(getApplicationContext(), title, message, "" ,resultIntent,imageUrl);

//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, "", resultIntent, imageUrl);
//                }
            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
}
