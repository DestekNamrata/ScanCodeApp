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
import android.widget.RadioButton;
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
public class ActivityEmailUpdate extends AppCompatActivity implements View.OnClickListener {
    private CustomEditText mEdtEmail,mEdtVerCode;
    private CustomTextView mTxtSend,mTxtSecs;
    private CustomButton mBtnUpdate;
    private ImageView mImageBack;
    private AQuery mAquery;
    private TransparentProgressDialog mProgress;
    private String StrOtp="",mStrUpdate="",mStrCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_update);
        mAquery=new AQuery(this);
        mProgress=new TransparentProgressDialog(this,R.drawable.ic_loader_image);
        mImageBack=(ImageView)findViewById(R.id.img_bckEmail);
        mEdtEmail=(CustomEditText)findViewById(R.id.edt_EmailUpdate);
        mEdtVerCode=(CustomEditText) findViewById(R.id.edt_otpEmailUpdate);
        mTxtSecs=(CustomTextView) findViewById(R.id.txt_secsCodeEmailUpdate);
        mTxtSend=(CustomTextView) findViewById(R.id.txt_sendCodeEmailUpdate);       mBtnUpdate=(CustomButton)findViewById(R.id.btn_UpdateProf) ;
        mBtnUpdate=(CustomButton)findViewById(R.id.btn_UpdateEmail) ;
        mTxtSend.setOnClickListener(this);
        mImageBack.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);
        mTxtSend.setVisibility(View.VISIBLE);
        mTxtSecs.setVisibility(View.GONE);
        mTxtSend.setText("Send");
        getData();
    }
    //method to get data
    private void getData()
    {
        mEdtEmail.setText(getIntent().getStringExtra("email"));
        mEdtEmail.setTextColor(getResources().getColor(R.color.black));
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_bckEmail:
                onBackPressed();
                break;
            case R.id.btn_UpdateEmail:
                if(mEdtEmail.isEmpty())
                {
                    mEdtEmail.setError(getString(R.string.str_email));
                    mEdtEmail.requestFocus();
                }else if(!Validation.checkEmail(mEdtEmail.getText().toString()))
                {
                    mEdtEmail.setError(getString(R.string.str_email));
                    mEdtEmail.requestFocus();
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
                    UpdateEmail();
                }
                break;
            case R.id.txt_sendCodeEmailUpdate:
                if(mEdtEmail.isEmpty())
                {
                    mEdtEmail.setError(getString(R.string.str_email));
                    mEdtEmail.requestFocus();
                }else if(!Validation.checkEmail(mEdtEmail.getText().toString()))
                {
                    mEdtEmail.setError(getString(R.string.str_email));
                    mEdtEmail.requestFocus();
                }else
                {
                    sendVerificationCode();
                }
                break;
        }
    }
    //method for countDownTimer
    private void CountDownTimer()
    {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                String secs= String.valueOf(millisUntilFinished / 1000);
                    mTxtSecs.setVisibility(View.VISIBLE);
                    mTxtSend.setVisibility(View.GONE);
                    mTxtSecs.setText(secs+"s");


                //here you can have your logic to set text to edittext
            }

            public void onFinish()
            {
                mTxtSend.setVisibility(View.VISIBLE);
                mTxtSecs.setVisibility(View.GONE);
                mTxtSend.setText("Resend");

            }

        }.start();
    }
    //webservice for code
    private void sendVerificationCode()
    {
        mStrCode=getString(R.string.ws_host)+getString(R.string.ws_send_verification_code);
        HashMap<String,Object> mHash=new HashMap<>();
        mHash.put("userEmail",mEdtEmail.getText().toString());
        mHash.put("flag","EMAIL");
        mHash.put("userMobile","");
        if(AvailableNetwork.isConnectingToInternet(this))
        {
            mAquery.ajax(mStrCode, mHash, JSONObject.class, ajaxCallbackUpdate);

        }else
        {
            showAlert(getString(R.string.str_not_internet_connection), -1);

        }
    }
    //webservice for update email
    private void UpdateEmail() {
        mStrUpdate=getString(R.string.ws_host)+getString(R.string.ws_update_prof);
        HashMap<String,Object> mParams=new HashMap<>();
        mParams.put("uid",SessionClass.getUserId(this));
        mParams.put("userName","");
        mParams.put("userEmail",mEdtEmail.getText().toString());
        mParams.put("userGender","");
        mParams.put("userMobile","");
        mParams.put("flag","EMAIL");
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
                            showAlert("Email Updated Successfully.",1);
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
                            Toast.makeText(ActivityEmailUpdate.this,jsonObject.getString("msg_string"),Toast.LENGTH_LONG).show();
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
