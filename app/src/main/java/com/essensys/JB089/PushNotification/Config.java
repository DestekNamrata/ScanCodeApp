package com.essensys.JB089.PushNotification;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Created by Infinitum on 15/11/2018.
 */

public class Config {
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";

    //to randomly generate unique Notification Id for multiple notifications
    public static class  NotificationID
    {

        public static int getId()
        {
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            return m;
        }

    }

}
