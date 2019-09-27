package com.essensys.JB089.Session;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

/**
 * Created by andriod_pc on 19/1/16.
 */
public class SessionClass {

    private static SharedPreferences mSharedPreferance;

    private static String VAYORTRICKS = "VAYORTRICKS";
    private static String USER_ID = "uid";
    private static String USER_TYPE = "utype";
    private static String USER_EMAIL = "uemail";
    private static String USER_MOB="umobile";
    private static String USER_FLAG="flag";

    private static String USER_IMAGE = "profile_image";
    private static String USER_GENDER="gender";
    private static String USER_PIN="pincode";
    private static String USER_Pass="password";

    private static SharedPreferences.Editor mShEditor;

    private static void init(Context mContext) {
        mSharedPreferance = mContext.getSharedPreferences(VAYORTRICKS, Context.MODE_PRIVATE);
//        mSharedPreferance=mContext.getSharedPreferences(USER_Pass,Context.MODE_PRIVATE);
    }

    public static void login(Context mContext,
                             String mUserId,
                             String mUserType,
                             String mUserEmail,
                             String mUserMob,
                             String mUserFlag
                             )

    {
        if (mSharedPreferance == null) {
            init(mContext);
        }
        mShEditor = mSharedPreferance.edit();
        mShEditor.putString(USER_ID, mUserId);

        mShEditor.putString(USER_TYPE, mUserType);
        mShEditor.putString(USER_EMAIL, mUserEmail);
        mShEditor.putString(USER_MOB,mUserMob);
        mShEditor.putString(USER_FLAG,mUserFlag);
        mShEditor.commit();
    }

    public static String getUSER_Pass(Context mContext) {
        String mUserPass="";
        if(mSharedPreferance==null)
        {
            init(mContext);
        }
        mUserPass=mSharedPreferance.getString(USER_Pass,"");
        return mUserPass;
    }
    public static void logout(Context mContext) {
        if (mSharedPreferance == null) {
            init(mContext);
        }
        mShEditor = mSharedPreferance.edit();
        mShEditor.clear();
        mShEditor.commit();

    }

    public static String getUserId(Context mContext) {
        String userId = "";
        if (mSharedPreferance == null) {
            init(mContext);
        }
        userId = mSharedPreferance.getString(USER_ID, "");
        return userId;
    }
    public static void setUserId(Context mContext, String mUserId)
    {
        if (mSharedPreferance == null) {
            init(mContext);
        }
        mShEditor = mSharedPreferance.edit();
        mShEditor.putString(USER_ID, mUserId);
        mShEditor.commit();
    }
    public static String getUserFlag(Context mContext) {
        String flag = "";
        if (mSharedPreferance == null) {
            init(mContext);
        }
        flag = mSharedPreferance.getString(USER_FLAG, "");
        return flag;
    }
    public static void setUserFlag(Context mContext, String mUserFlag)
    {
        if (mSharedPreferance == null) {
            init(mContext);
        }
        mShEditor = mSharedPreferance.edit();
        mShEditor.putString(USER_FLAG, mUserFlag);
        mShEditor.commit();
    }
    public static String getUserEmail(Context mContext) {
        String userEmail = "";
        if (mSharedPreferance == null) {
            init(mContext);
        }
        userEmail = mSharedPreferance.getString(USER_EMAIL, "");
        return userEmail;
    }
    public static void setUserEmail(Context mContext, String mUserEmail)
    {
        if (mSharedPreferance == null) {
            init(mContext);
        }
        mShEditor = mSharedPreferance.edit();
        mShEditor.putString(USER_EMAIL, mUserEmail);
        mShEditor.commit();
    }
    public static String getUserType(Context mContext) {
        String userType = "";
        if (mSharedPreferance == null) {
            init(mContext);
        }
        userType = mSharedPreferance.getString(USER_TYPE, "");
        return userType;
    }
    public static String getUserMob(Context mContext) {
        String userMob = "";
        if (mSharedPreferance == null) {
            init(mContext);
        }
        userMob = mSharedPreferance.getString(USER_MOB, "");
        return userMob;
    }
    public static void setUserMob(Context mContext, String mUserMob)
    {
        if (mSharedPreferance == null) {
            init(mContext);
        }
        mShEditor = mSharedPreferance.edit();
        mShEditor.putString(USER_MOB, mUserMob);
        mShEditor.commit();
    }

    public static String getUserImage(Context mContext) {
        String userImag = "";
        if (mSharedPreferance == null) {
            init(mContext);
        }
        userImag = mSharedPreferance.getString(USER_IMAGE, "");
        return userImag;
    }

    public static void udpdaetPrfileImage(Context mContext, String mImgProfile) {
        if (mSharedPreferance == null) {
            init(mContext);
        }
        mShEditor = mSharedPreferance.edit();
        mShEditor.putString(USER_IMAGE, mImgProfile);
        mShEditor.commit();
    }


    public static void setUserType(Context mContext, String mUserType)
    {
        if (mSharedPreferance == null) {
            init(mContext);
        }
        mShEditor = mSharedPreferance.edit();
        mShEditor.putString(USER_TYPE, mUserType);
        mShEditor.commit();
    }

}
