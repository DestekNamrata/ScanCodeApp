package com.essensys.JB089.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.CustomView.CustomButton;
import com.essensys.JB089.CustomView.CustomEditText;
import com.essensys.JB089.CustomView.TransparentProgressDialog;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;
import com.essensys.JB089.Utility.AvailableNetwork;
import com.essensys.JB089.Utility.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
public class ActivityForgotPass extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mLinForgotMob,mLinForgotEmail;
    private CustomEditText mEdtForgotMob,mEdtFogotEmail;
    private CustomButton mBtnSendOtp;
    private ImageView mImgBack;
    private AQuery mAquery;
    private TransparentProgressDialog mProgress;
    private String mStrForPass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        mAquery=new AQuery(this);
        mProgress=new TransparentProgressDialog(this,R.drawable.ic_loader_image);
        mLinForgotMob=(LinearLayout)findViewById(R.id.linear_phoneForgot);
        mLinForgotEmail=(LinearLayout)findViewById(R.id.linear_emailForgot);
        mImgBack=(ImageView)findViewById(R.id.img_bckForgot);
        mEdtFogotEmail = (CustomEditText) findViewById(R.id.edt_forgotEmail);
        mEdtForgotMob = (CustomEditText) findViewById(R.id.edt_forgotMobNo);
        mBtnSendOtp = (CustomButton) findViewById(R.id.btn_sendOtp);
        mBtnSendOtp.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        if(getIntent().getStringExtra("ForPass").equalsIgnoreCase("0"))
        {
            mLinForgotEmail.setVisibility(View.VISIBLE);
            mLinForgotMob.setVisibility(View.GONE);
        }else
        {
            mLinForgotEmail.setVisibility(View.GONE);
            mLinForgotMob.setVisibility(View.VISIBLE);
        }
    }
    //method for validation
    private boolean ValidationForPass() {
        if(mLinForgotEmail.getVisibility()==View.VISIBLE)
        {
          if(mEdtFogotEmail.getString().isEmpty())
          {
              mEdtFogotEmail.setError(getString(R.string.str_email));
              mEdtFogotEmail.requestFocus();
              return false;
          }else if(!Validation.checkEmail(mEdtFogotEmail.getText().toString()))
          {
              mEdtFogotEmail.setError(getString(R.string.str_Validemail));
              mEdtFogotEmail.requestFocus();
              return false;
          }
        }else
        {
            if(mEdtForgotMob.getString().isEmpty())
            {
                mEdtForgotMob.setError(getString(R.string.str_mobNo));
                mEdtForgotMob.requestFocus();
                return false;
            }else if(mEdtForgotMob.getText().toString().length()<10)
            {
                mEdtForgotMob.setError(getString(R.string.str_10mobNo));
                mEdtForgotMob.requestFocus();
                return false;
            }
        }
        return true;
    }
   // webservice for forgotpass
    private void forgotPass()
    {
        mStrForPass=getString(R.string.ws_host)+getString(R.string.ws_forgotPass);
        HashMap<String,Object> mHash=new HashMap<>();
        if(getIntent().getStringExtra("ForPass").equalsIgnoreCase("0"))
        {
          mHash.put("userEmail",mEdtFogotEmail.getText().toString());
            mHash.put("flag","EMAIL");

        }else
        {
            mHash.put("userEmail",mEdtForgotMob.getText().toString());
            mHash.put("flag","MOBILE");
            }
        if(AvailableNetwork.isConnectingToInternet(this))
        {
            mAquery.progress(mProgress).ajax(mStrForPass,mHash,JSONObject.class,ajaxCallBackForPass);
        }else
        {
            showAlert(getString(R.string.str_not_internet_connection),-1);
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
                intent = new Intent(this, ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("FlgMobEmail", SessionClass.getUserMob(this));
                startActivity(intent);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                finish();
                break;

        }
    }
    //response
    AjaxCallback<JSONObject> ajaxCallBackForPass=new AjaxCallback<JSONObject>()
    {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if(object!=null)
            {
                if(url.equalsIgnoreCase(mStrForPass))
                {
                    try {
                        JSONObject jsonObject=object.getJSONObject("result");
                        if(jsonObject.getString("msg").equalsIgnoreCase("1"))
                        {
                            showAlert(jsonObject.getString("msg_string"),1);
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
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_bckForgot:
                onBackPressed();
                break;
            case R.id.btn_sendOtp:
                if(ValidationForPass())
                {
                    forgotPass();
                }
                break;
        }
    }

}
