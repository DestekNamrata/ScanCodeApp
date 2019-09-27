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
import android.widget.RadioButton;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.CustomView.CustomButton;
import com.essensys.JB089.CustomView.CustomEditText;
import com.essensys.JB089.CustomView.TransparentProgressDialog;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;
import com.essensys.JB089.Utility.AvailableNetwork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
public class ActivityProfileUpdate extends AppCompatActivity implements View.OnClickListener {
    private CustomEditText mEdtName;
    private RadioButton mRdoMale,mRdoFemale;
    private CustomButton mBtnUpdate;
    private ImageView mImageBack;
    private AQuery mAquery;
    private String mStrUpdate="";
    private TransparentProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        mAquery=new AQuery(this);
        mProgress=new TransparentProgressDialog(this,R.drawable.ic_loader_image);
        mImageBack=(ImageView)findViewById(R.id.img_bckName);
        mEdtName=(CustomEditText)findViewById(R.id.edt_NameUpdate);
        mRdoMale=(RadioButton)findViewById(R.id.rdo_male);
        mRdoFemale=(RadioButton)findViewById(R.id.rdo_female);
        mBtnUpdate=(CustomButton)findViewById(R.id.btn_UpdateProf) ;
        mImageBack.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);

        getData();

    }
    //method to get data from previous one
    private void getData()
    {
     mEdtName.setText(getIntent().getStringExtra("name"));
     mEdtName.setTextColor(getResources().getColor(R.color.black));
     if(getIntent().getStringExtra("gender").equalsIgnoreCase("male"))
     {
         mRdoMale.setChecked(true);
         mRdoFemale.setChecked(false);
     }else
     {
         mRdoFemale.setChecked(true);
         mRdoMale.setChecked(false);
     }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_bckName:
                onBackPressed();
                break;
            case R.id.btn_UpdateProf:
                if(mEdtName.isEmpty())
                {
                    mEdtName.setError("Please enter Name");
                    mEdtName.requestFocus();
                }else if(!mRdoMale.isChecked()&&!mRdoFemale.isChecked())
                {
                   showAlert("Please select gender",-1);
                }else
                {
                    UpdateProf();
                }
                break;
        }
    }
    //webservice for update Prof
    private void UpdateProf()
    {
        mStrUpdate=getString(R.string.ws_host)+getString(R.string.ws_update_prof);
        HashMap<String,Object> mParams=new HashMap<>();
        mParams.put("uid",SessionClass.getUserId(this));
        mParams.put("userName",mEdtName.getText().toString());
        mParams.put("userEmail","");
        mParams.put("userGender",mRdoMale.isChecked()?"male":"female");
        mParams.put("userMobile","");
        mParams.put("flag","BASIC_INFO");
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
                            showAlert("Account Updated Successfully.",1);
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
