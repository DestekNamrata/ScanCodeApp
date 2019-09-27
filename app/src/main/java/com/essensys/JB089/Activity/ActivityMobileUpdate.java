package com.essensys.JB089.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.CustomView.CustomButton;
import com.essensys.JB089.CustomView.CustomEditText;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.CustomView.TransparentProgressDialog;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;
import com.essensys.JB089.Utility.AvailableNetwork;
import com.essensys.JB089.Utility.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
public class ActivityMobileUpdate extends AppCompatActivity implements View.OnClickListener {
    private CustomEditText mEdtMob,mEdtVerCode;
    private CustomTextView mTxtSend,mTxtSecs,mTxtResend;
    private CustomButton mBtnUpdate;
    private ImageView mImageBack;
    private AQuery mAquery;
    private TransparentProgressDialog mProgress;
    private String StrOtp="",mStrUpdate="",mStrCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_update);
        mAquery=new AQuery(this);
        mProgress=new TransparentProgressDialog(this,R.drawable.ic_loader_image);
        mImageBack=(ImageView)findViewById(R.id.img_bckMob);
        mEdtMob=(CustomEditText)findViewById(R.id.edt_MobUpdate);
        mEdtVerCode=(CustomEditText) findViewById(R.id.edt_otpMobUpdate);
        mTxtSecs=(CustomTextView) findViewById(R.id.txt_secsCodeMobUpdate);
        mTxtSend=(CustomTextView) findViewById(R.id.txt_sendCodeMobUpdate);
        mTxtResend=(CustomTextView)findViewById(R.id.txt_ResendCodeMobUpdate);
        mBtnUpdate=(CustomButton)findViewById(R.id.btn_UpdateProf) ;
        mBtnUpdate=(CustomButton)findViewById(R.id.btn_UpdateMob) ;
        mTxtResend.setOnClickListener(this);
        mTxtSend.setOnClickListener(this);
        mImageBack.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mTxtSend.setVisibility(View.VISIBLE);
        mTxtSecs.setVisibility(View.GONE);
        mTxtResend.setVisibility(View.GONE);
        getData();
    }
    //method to get data
    private void getData()
    {
        mEdtMob.setText(getIntent().getStringExtra("mobile"));
        mEdtMob.setTextColor(getResources().getColor(R.color.black));
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_bckMob:
                onBackPressed();
                break;
            case R.id.btn_UpdateMob:
                if(mEdtMob.isEmpty())
                {
                    mEdtMob.setError(getString(R.string.str_mobNo));
                    mEdtMob.requestFocus();
                }else if(mEdtMob.getText().toString().length()<10)
                {
                    mEdtMob.setError(getString(R.string.str_10mobNo));
                    mEdtMob.requestFocus();
                }else if(mEdtVerCode.isEmpty())
                {
                    mEdtVerCode.setError(getString(R.string.str_otp));
                    mEdtVerCode.requestFocus();
                }else if(!mEdtVerCode.getText().toString().equalsIgnoreCase(StrOtp))
                {
                    mEdtVerCode.setError(getString(R.string.str_validOtp));
                    mEdtVerCode.requestFocus();
                }
                else
                {
                    UpdateMob();
                }
                break;
            case R.id.txt_sendCodeMobUpdate:
                if(mEdtMob.isEmpty())
                {
                    mEdtMob.setError(getString(R.string.str_mobNo));
                    mEdtMob.requestFocus();
                }else if(mEdtMob.getText().toString().length()<10)
                {
                    mEdtMob.setError(getString(R.string.str_10mobNo));
                    mEdtMob.requestFocus();
                }else
                {
                    sendVerificationCode();
                }
                break;
            case R.id.txt_ResendCodeMobUpdate:
                if(mEdtMob.isEmpty())
                {
                    mEdtMob.setError(getString(R.string.str_mobNo));
                    mEdtMob.requestFocus();
                }else if(mEdtMob.getText().toString().length()<10)
                {
                    mEdtMob.setError(getString(R.string.str_10mobNo));
                    mEdtMob.requestFocus();
                }else
                {
                    sendVerificationCode();
                }
                break;

        }
    }

    //webservice for code
    private void sendVerificationCode()
    {
        mStrCode=getString(R.string.ws_host)+getString(R.string.ws_send_verification_code);
        HashMap<String,Object> mHash=new HashMap<>();
        mHash.put("userEmail","");
        mHash.put("flag","MOBILE");
        mHash.put("userMobile",mEdtMob.getText().toString());
        if(AvailableNetwork.isConnectingToInternet(this))
        {
            mAquery.ajax(mStrCode, mHash, JSONObject.class, ajaxCallbackUpdate);

        }else
        {
            showAlert(getString(R.string.str_not_internet_connection), -1);

        }
    }

    //method for countDownTimer
    private void CountDownTimer()
    {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                String secs= String.valueOf(millisUntilFinished / 1000);
                mTxtSecs.setVisibility(View.VISIBLE);
                mTxtResend.setVisibility(View.GONE);
                mTxtSend.setVisibility(View.GONE);
                mTxtSecs.setText(secs+"s");


                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mTxtSend.setVisibility(View.GONE);
                mTxtSecs.setVisibility(View.GONE);
                mTxtResend.setVisibility(View.VISIBLE);
                mTxtResend.setOnClickListener(ActivityMobileUpdate.this);
            }

        }.start();
    }

    //webservice for update email
    private void UpdateMob() {
        mStrUpdate=getString(R.string.ws_host)+getString(R.string.ws_update_prof);
        HashMap<String,Object> mParams=new HashMap<>();
        mParams.put("uid",SessionClass.getUserId(this));
        mParams.put("userName","");
        mParams.put("userEmail","");
        mParams.put("userGender","");
        mParams.put("userMobile",mEdtMob.getText().toString());
        mParams.put("flag","MOBILE");
        if(AvailableNetwork.isConnectingToInternet(this))
        {
            mAquery.progress(mProgress).ajax(mStrUpdate,mParams,JSONObject.class,ajaxCallbackUpdate);
        }else
        {
            showAlert(getString(R.string.str_not_internet_connection),-1);
        }

    }
    //response
    AjaxCallback<JSONObject> ajaxCallbackUpdate=new AjaxCallback<JSONObject>()
    {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if(object!=null)
            {
                if(url.equalsIgnoreCase(mStrUpdate))
                {
                    try {
                        JSONObject jsonObject=object.getJSONObject("result");
                        if(jsonObject.getString("msg").equalsIgnoreCase("1"))
                        {
                            showAlert("Phone Number Updated Successfully.",1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(url.equalsIgnoreCase(mStrCode))
                {
                    try {
                        JSONObject jsonObject=object.getJSONObject("result");
                        if(jsonObject.getString("msg").equalsIgnoreCase("1"))
                        {
                            Toast.makeText(ActivityMobileUpdate.this,jsonObject.getString("msg_string"),Toast.LENGTH_LONG).show();
                           StrOtp=jsonObject.getString("otp");
//                            mEdtVerCode.setText(jsonObject.getString("otp"));
                            CountDownTimer();
                        }else
                        {
                            showAlert(jsonObject.getString("msg_string"),-1);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    };
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
                onBackPressed();
                break;

        }
    }
}
