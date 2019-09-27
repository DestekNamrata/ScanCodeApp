package com.essensys.JB089.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.CustomView.TransparentProgressDialog;
import com.essensys.JB089.PushNotification.Config;
import com.essensys.JB089.PushNotification.MyFirebaseInstanceIDService;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;
import com.essensys.JB089.Utility.AvailableNetwork;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
public class SplashScreenActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    protected int splash_time = 2000;
    public boolean splashFlag = false;
    private Thread mThread;
    private Boolean firstTime = null;
    String refreshedToken="";
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private String mStrAddToken="";
    private AQuery mAquery;
    private TransparentProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAquery=new AQuery(this);
        mProgress=new TransparentProgressDialog(this,R.drawable.ic_loader_image);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        //to get device_token
        getDeviceToken();
        }
        //method to move next activity
    private void moveToNxtActivity()
    {
        mThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(splash_time);
                    }
                } catch (InterruptedException e) {
                } finally {
                    if (!splashFlag) {
                        splashFlag = true;
                        Intent it;
                        if(isFirstTime())
                        {
                            it = new Intent(SplashScreenActivity.this,ActivityViewPager.class);
                            //to integrate splash screen
//                        if (SessionClass.getUserId(SplashScreenActivity.this).equalsIgnoreCase("")) {
//                            it.setClass(SplashScreenActivity.this, ActivityHome.class);
//                            it.putExtra("key", "8");
//                        } else {
//                            it.setClass(SplashScreenActivity.this, MainDashBoardActivity.class);
//                            it.putExtra("key", "8");
//                        }
                            startActivity(it);
                            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                            finish();
                        }else
                        {
                            //to integrate splash screen
//                        if (SessionClass.getUserId(SplashScreenActivity.this).equalsIgnoreCase("")) {
//                            it.setClass(SplashScreenActivity.this, ActivityHome.class);
//                            it.putExtra("key", "8");
//                        } else {
//                            it.setClass(SplashScreenActivity.this, MainDashBoardActivity.class);
//                            it.putExtra("key", "8");
//                        }
                            if (SessionClass.getUserId(SplashScreenActivity.this).equalsIgnoreCase("")) {
                                it = new Intent(SplashScreenActivity.this, ActivityLogin.class);
                                startActivity(it);
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                finish();
                            }else
                            {
                                it = new Intent(SplashScreenActivity.this, ActivityDashBoard.class);
                                startActivity(it);
                                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                                finish();
                            }
                        }

                    }
                }
            }
        };
        mThread.start();
    }
    //to get device token
    private void getDeviceToken()
    {
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
                if(!SessionClass.getUserId(SplashScreenActivity.this).equalsIgnoreCase(""))
                {
                    //add token no.
                    addTokenNo();
                }else
                {
                    getVersionNo();
                }


            }
        });
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
    //method for first time launch of app
    private boolean isFirstTime() {
        if (firstTime == null) {
            SharedPreferences mPreferences = this.getSharedPreferences("first_time", Context.MODE_PRIVATE);
            firstTime = mPreferences.getBoolean("firstTime", true);
            if (firstTime) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.commit();
            }
        }
        return firstTime;
    }
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
    //method for version name
    private void getVersionNo() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            ((CustomTextView)findViewById(R.id.txt_version)).setText("Version: "+version);
            //method to move next activity
            moveToNxtActivity();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    //webservice for login
    private void addTokenNo()
    {
        mStrAddToken=getString(R.string.ws_host)+getString(R.string.ws_add_token);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("uid",SessionClass.getUserId(this));
        mParams.put("device_platform","android");
        mParams.put("device_token_number",refreshedToken);
        if (AvailableNetwork.isConnectingToInternet(this)) {
            mAquery.ajax(mStrAddToken, mParams, JSONObject.class, new AjaxCallback<JSONObject>()
            {
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    super.callback(url, object, status);
                    if(object!=null)
                    {
                        if(url.equalsIgnoreCase(mStrAddToken))
                        {
                            try {
                                JSONObject jsonObject=object.getJSONObject("result");
                                jsonObject.getString("msg");
                                getVersionNo();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            });
        }
        else {
            showAlert(getString(R.string.str_not_internet_connection), -1);
        }
    }

    /**
     * /**
     * Show alert
     **/
    private void showAlert(String message, final int flg) {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
        aBuilder.setCancelable(false);
        aBuilder.setMessage(message);
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setNextFlg(flg);
            }
        });
        aBuilder.create();
        aBuilder.show();
    }
    private void setNextFlg(int flg) {
        Intent intent;
        switch (flg) {
            case 1:

                break;

        }
    }





}
