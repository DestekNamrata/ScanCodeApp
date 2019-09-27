package com.essensys.JB089.PushNotification;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.essensys.JB089.Activity.ActivityDashBoard;
import com.essensys.JB089.Activity.ActivityLogin;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;


/**
 * Created by Ravi on 31/03/15.
 */
/**
 * Created by Ravi on 31/03/15.
 */
//public class NotificationUtils extends ContextWrapper{
//    private NotificationManager mManager;
//    public static final String ANDROID_CHANNEL_ID = "com.chikeandroid.tutsplustalerts.ANDROID";
//    public static final String IOS_CHANNEL_ID = "com.chikeandroid.tutsplustalerts.IOS";
//    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
//    public static final String IOS_CHANNEL_NAME = "IOS CHANNEL";
//
//    private static String TAG = NotificationUtils.class.getSimpleName();
//    private Context mContext;
//    //vvimp   :-  icons above 5.0 are not visible so make the image size small and make it transparent or use material icons
//
//
//    public NotificationUtils(Context mContext) {
//        super(mContext);
//        this.mContext = mContext;
//        createChannels();
//    }
//
//    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
//        showNotificationMessage(title, message, timeStamp, intent, null);
//    }
//
//    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl)
//    {
//        // Check for empty push message
//        if (TextUtils.isEmpty(message))
//            return;
//        // notification icon
//        final int icon = R.drawable.output_onlinepngtools;
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        final PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                mContext);
////        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
////                + "://" + mContext.getPackageName() + "/raw/notification");
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        if (!TextUtils.isEmpty(imageUrl)) {
//            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
//                Bitmap bitmap = getBitmapFromURL(imageUrl);
//                if (bitmap != null) {
//                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//                } else {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        showNotification(mContext,title,message,intent);
//                    }else {
//                        showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//                    }
//
//                }
//            }
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                showNotification(mContext,title,message,intent);
//            }else {
//                showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//            }
//
//            playNotificationSound();
//        }
//    }
//
//    @SuppressLint("NewApi")
//    public void showNotification(Context context, String title, String body, Intent intent) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int notificationId = 1;
//        String channelId = "channel-01";
//        String channelName = "Channel Name";
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(
//                    channelId, channelName, importance);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
//                .setSmallIcon( R.drawable.output_onlinepngtools)
//                .setContentTitle(title)
//                .setContentText(body);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
//                0,
//                PendingIntent.FLAG_UPDATE_CURRENT
//        );
//        mBuilder.setContentIntent(resultPendingIntent);
//
//        notificationManager.notify(notificationId, mBuilder.build());
//    }
//
//    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
//        try {
//            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String id = title;
//                // The user-visible name of the channel.
//                CharSequence name = title;
//                // The user-visible description of the channel.
//                String description = message;
//                int importance = NotificationManager.IMPORTANCE_MAX;
//                @SuppressLint("WrongConstant") android.app.NotificationChannel mChannel = new android.app.NotificationChannel(id, name, importance);
//                // Configure the notification channel.
//                mChannel.setDescription(description);
//                mChannel.enableLights(true);
//                // Sets the notification light color for notifications posted to this
//                // channel, if the device supports this feature.
//                //mChannel.setLightColor(Color.RED);
//                notificationManager.createNotificationChannel(mChannel);
//            }
//
//            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//            inboxStyle.addLine(message);
//            Notification notification;
//            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                    .setAutoCancel(true)
//                    .setContentTitle(title)
//                    .setContentIntent(resultPendingIntent)
//                    .setSound(alarmSound)
//                    .setStyle(inboxStyle)
//                    .setWhen(getTimeMilliSec(timeStamp))
//                    .setSmallIcon( R.drawable.output_onlinepngtools)  //vvimp   :-  icons above 5.0 are not visible so make the image size small and make it transparent
//                    .setContentText(message)
//                    .build();
//            notificationManager.notify(Config.NOTIFICATION_ID, notification);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
//
//        try {
//            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//            bigPictureStyle.setBigContentTitle(title);
//            bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
//            bigPictureStyle.bigPicture(bitmap);
//            Notification notification;
//            notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                    .setAutoCancel(true)
//                    .setContentTitle(title)
//                    .setContentIntent(resultPendingIntent)
//                    .setSound(alarmSound)
//                    .setStyle(bigPictureStyle)
//                    .setWhen(getTimeMilliSec(timeStamp))
//                    .setSmallIcon( R.drawable.output_onlinepngtools)
//                    .setContentText(message)
//                    .build();
//
//            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Downloading push notification image before displaying it in
//     * the notification tray
//     */
//    public Bitmap getBitmapFromURL(String strURL) {
//        try {
//            URL url = new URL(strURL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Playing notification sound
//    public void playNotificationSound() {
//        try {
////            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + "/raw/notification");
////            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.getPackageName() + R.raw.slow_spring_board);
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Method checks if the app is in background or not
//     */
//    public static boolean isAppIsInBackground(Context context) {
//        boolean isInBackground = true;
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
//            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
//            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
//                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    for (String activeProcess : processInfo.pkgList) {
//                        if (activeProcess.equals(context.getPackageName())) {
//                            isInBackground = false;
//                        }
//                    }
//                }
//            }
//        } else {
//            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//            ComponentName componentInfo = taskInfo.get(0).topActivity;
//            if (componentInfo.getPackageName().equals(context.getPackageName())) {
//                isInBackground = false;
//            }
//        }
//
//        return isInBackground;
//    }
//
//    // Clears notification tray messages
//    public static void clearNotifications(Context context) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();
//    }
//
//    public static long getTimeMilliSec(String timeStamp) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = format.parse(timeStamp);
//            return date.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//
//    public void createChannels() {
//
//        // create android channel
//        NotificationChannel androidChannel = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
//                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
//            // Sets whether notifications posted to this channel should display notification lights
//            androidChannel.enableLights(true);
//            // Sets whether notification posted to this channel should vibrate.
//            androidChannel.enableVibration(true);
//            // Sets the notification light color for notifications posted to this channel
//            androidChannel.setLightColor(Color.GREEN);
//            // Sets whether notifications posted to this channel appear on the lockscreen or not
//            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//            getManager().createNotificationChannel(androidChannel);
//
//            // create ios channel
//            NotificationChannel iosChannel = new NotificationChannel(IOS_CHANNEL_ID,
//                    IOS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//            iosChannel.enableLights(true);
//            iosChannel.enableVibration(true);
//            iosChannel.setLightColor(Color.GRAY);
//            iosChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            getManager().createNotificationChannel(iosChannel);
//        }
//    }
//
//    private NotificationManager getManager() {
//        if (mManager == null) {
//            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        return mManager;
//    }
//}


public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "com.chikeandroid.tutsplustalerts.ANDROID";
    public static final String IOS_CHANNEL_ID = "com.chikeandroid.tutsplustalerts.IOS";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    public static final String IOS_CHANNEL_NAME = "IOS CHANNEL";
    private Context mContext;

    public NotificationUtils(Context mContext) {
        super(mContext);
        this.mContext=mContext;
        createChannels();
    }

    public void createChannels() {

        // create android channel
        NotificationChannel androidChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true);
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true);
            // Sets the notification light color for notifications posted to this channel
            androidChannel.setLightColor(Color.GREEN);
            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            String channelId = UAirship.shared().getPushManager().getChannelId();
//            Log.d("Channel ID" , channelId);

            getManager().createNotificationChannel(androidChannel);

//            // create ios channel
//            NotificationChannel iosChannel = new NotificationChannel(IOS_CHANNEL_ID,
//                    IOS_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
//            iosChannel.enableLights(true);
//            iosChannel.enableVibration(true);
//            iosChannel.setLightColor(Color.GRAY);
//            iosChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            getManager().createNotificationChannel(iosChannel);
        }
    }

    private NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
       /**
          * Method checks if the app is in background or not
          */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
//        public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
//        showNotificationMessage(title, message, timeStamp, intent);
//    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        // notification icon
        int icon = R.drawable.ic_transpvt;

        //to redirect to next page
//        if(flag.equalsIgnoreCase("1"))
//        {
        if(SessionClass.getUserId(mContext).equalsIgnoreCase(""))
        {
            intent = new Intent(this, ActivityLogin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else
        {
            intent = new Intent(this, ActivityDashBoard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

//            intent.putExtra("Notification", TrueOrFalse);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext);

//        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                + "://" + mContext.getPackageName() + "/raw/notification");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);
                //for  kitkat and below that it is used
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                 icon=R.drawable.ic_transpvt;
                } else {
                    icon=R.drawable.vt_transp;

                }
                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent,bitmap, alarmSound);
                } else {
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, bitmap,alarmSound);
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, null,alarmSound);
            playNotificationSound();
        }
    }
//
//
    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent,Bitmap imageurl, Uri alarmSound) {

//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.addLine(message);
//             .setStyle(new NotificationCompat.BigTextStyle()
//                .bigText(emailObject.getSubjectAndSnippet()))

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setWhen(getTimeMilliSec(timeStamp))
                .setStyle(new NotificationCompat.BigTextStyle()  //used for big text messages
                .bigText(message))
                .setChannelId(ANDROID_CHANNEL_ID) //setting channel id is important for oreo and image size should be upto 1kb to be set for notification
//                .setSmallIcon(R.drawable.vt_login)
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_vt_logo))
                .setContentText(message)
                .setColor(getResources().getColor(R.color.orange))
                .build();

//        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,
//                "Default")
//                .setSmallIcon(R.drawable.ic_transpvt)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                        R.drawable.ic_menu_profile))
//                .setContentTitle(title)
//                .setContentText(title)
//                .setAutoCancel(true)
////                .setSound(defaultSoundUri)
//                .setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NotificationID.getId(), notification);
    }

    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent,Bitmap imageurl, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);
        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setColor(getResources().getColor(R.color.orange))
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(message)) //used for big text messages
                .setStyle(new NotificationCompat.BigPictureStyle()  //used for big picture
                        .bigPicture(imageurl).bigLargeIcon(null))
                .setWhen(getTimeMilliSec(timeStamp))
                .setChannelId(ANDROID_CHANNEL_ID)  //setting channel id is important for oreo and image size should be upto 1kb to be set for notification
//                .setSmallIcon(R.drawable.output_onlinepngtools)
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_vt_logo))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
    }
    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    public void playNotificationSound() {
        try {
//            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                    + "://" + mContext.getPackageName() + "/raw/notification");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

//public class NotificationUtils {
//
//    private static String TAG = NotificationUtils.class.getSimpleName();
//
//    private Context mContext;
//
//    public NotificationUtils(Context mContext) {
//        this.mContext = mContext;
//    }
//
//    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
//        showNotificationMessage(title, message, timeStamp, intent, null);
//    }
//
//    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl) {
//        // Check for empty push message
//        if (TextUtils.isEmpty(message))
//            return;
//
//
//        // notification icon
//        final int icon = R.mipmap.ic_launcher;
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        final PendingIntent resultPendingIntent =
//                PendingIntent.getActivity(
//                        mContext,
//                        0,
//                        intent,
//                        PendingIntent.FLAG_CANCEL_CURRENT
//                );
//
//        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                mContext);
//
//        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                + "://" + mContext.getPackageName() + "/raw/notification");
//
//        if (!TextUtils.isEmpty(imageUrl)) {
//
//            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
//
//                Bitmap bitmap = getBitmapFromURL(imageUrl);
//
//                if (bitmap != null) {
//                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//                } else {
//                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//                }
//            }
//        } else {
//            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
//            playNotificationSound();
//        }
//    }
//
//
//    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
//
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//        inboxStyle.addLine(message);
//
//        Notification notification;
//        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setContentIntent(resultPendingIntent)
//                .setSound(alarmSound)
//                .setStyle(inboxStyle)
//                .setWhen(getTimeMilliSec(timeStamp))
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                .setContentText(message)
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(Config.NOTIFICATION_ID, notification);
//    }
//
//    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
//        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
//        bigPictureStyle.setBigContentTitle(title);
//        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
//        bigPictureStyle.bigPicture(bitmap);
//        Notification notification;
//        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setContentIntent(resultPendingIntent)
//                .setSound(alarmSound)
//                .setStyle(bigPictureStyle)
//                .setWhen(getTimeMilliSec(timeStamp))
//                .setSmallIcon(R.drawable.brochure_icon)
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
//                .setContentText(message)
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
//    }
//
//    /**
//     * Downloading push notification image before displaying it in
//     * the notification tray
//     */
//    public Bitmap getBitmapFromURL(String strURL) {
//        try {
//            URL url = new URL(strURL);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return myBitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Playing notification sound
//    public void playNotificationSound() {
//        try {
//            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                    + "://" + mContext.getPackageName() + "/raw/notification");
//            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Method checks if the app is in background or not
//     */
//    public static boolean isAppIsInBackground(Context context) {
//        boolean isInBackground = true;
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
//            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
//            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
//                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    for (String activeProcess : processInfo.pkgList) {
//                        if (activeProcess.equals(context.getPackageName())) {
//                            isInBackground = false;
//                        }
//                    }
//                }
//            }
//        } else {
//            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//            ComponentName componentInfo = taskInfo.get(0).topActivity;
//            if (componentInfo.getPackageName().equals(context.getPackageName())) {
//                isInBackground = false;
//            }
//        }
//
//        return isInBackground;
//    }
//
//    // Clears notification tray messages
//    public static void clearNotifications(Context context) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();
//    }
//
//    public static long getTimeMilliSec(String timeStamp) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            Date date = format.parse(timeStamp);
//            return date.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//}