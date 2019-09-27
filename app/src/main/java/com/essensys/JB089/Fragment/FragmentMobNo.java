package com.essensys.JB089.Fragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.Activity.ActivityDashBoard;
import com.essensys.JB089.Activity.ActivityForgotPass;
import com.essensys.JB089.Activity.ActivityLogin;
import com.essensys.JB089.Activity.ActivityRegister;
import com.essensys.JB089.Activity.ActivityTermsCondition;
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

import static android.content.Context.MODE_PRIVATE;
public class FragmentMobNo extends Fragment implements View.OnClickListener {
    private View view;
    private CustomEditText mEdtMobNo,mEdtOtp,mEdtForgotMob,mEdtPass,mEdtFullName;
    private CustomButton mBtnLogin,mBtnReg,mBtnSendOtp,mBtnCancel;
    private Bundle bundle;
    private CustomTextView mTxtReg,mTxtForgotPass,mTxtSendCode,mTxtMsgPassCode,mTxtTerms,mTxtSecs,mTxtResend;
    private RelativeLayout mRelNewUser;
    private String StrOtp="";
    private ImageView mImgShowPass,mImgHidePass;
    private LinearLayout mLinForgotMob,mLinForgotEmail,mLinearPhonePass,mLinearPhoneVerCode,mLinearTerms,mLinearFullName;
    private Dialog mDialogForgotPass;
    private AQuery mAquery;
    private TransparentProgressDialog mProgress;
    private String mStrOTP="",mStrReg="",mStrLogin="";
    private String regId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mobno, container, false);
        mAquery=new AQuery(getActivity());
        mProgress=new TransparentProgressDialog(getActivity(),R.drawable.ic_loader_image);
        mEdtMobNo=(CustomEditText)view.findViewById(R.id.edt_mobNo);
        mLinearFullName=(LinearLayout)view.findViewById(R.id.linear_fullNameMob);
        mEdtFullName=(CustomEditText)view.findViewById(R.id.edt_fullnameMob);
        mEdtOtp=(CustomEditText)view.findViewById(R.id.edt_otp);
        mBtnLogin=(CustomButton)view.findViewById(R.id.btn_MobLogin);
        mBtnReg=(CustomButton)view.findViewById(R.id.btn_MobReg);
        mRelNewUser=(RelativeLayout)view.findViewById(R.id.rel_new_user);
        mTxtReg=(CustomTextView)view.findViewById(R.id.txt_newUserMob);
//        mTxtForgotPass=(CustomTextView)view.findViewById(R.id.txt_forPassMob);
        mTxtResend=(CustomTextView)view.findViewById(R.id.txt_ResendCode);
        mTxtSendCode=(CustomTextView) view.findViewById(R.id.txt_sendCode) ;
//        mLinearPhonePass=(LinearLayout)view.findViewById(R.id.linear_PhonePass);
//        mTxtMsgPassCode=(CustomTextView)view.findViewById(R.id.txt_msgPassCode);
        mLinearPhoneVerCode=(LinearLayout)view.findViewById(R.id.linear_phoneVerCode);
        mLinearTerms=(LinearLayout)view.findViewById(R.id.lin_termCond);
        mTxtTerms=(CustomTextView)view.findViewById(R.id.txt_termsMob);
        mTxtSecs=(CustomTextView)view.findViewById(R.id.txt_secsCode);
//        mEdtPass=(CustomEditText)view.findViewById(R.id.edt_PassMob) ;
//        mImgShowPass=(ImageView)view.findViewById(R.id.img_showPassMob);
//        mImgHidePass=(ImageView)view.findViewById(R.id.img_hidePassMob);
//        mImgShowPass.setOnClickListener(this);
//        mImgShowPass.setOnClickListener(this);
       mTxtSecs.setVisibility(View.GONE);
       mTxtResend.setVisibility(View.GONE);
       mTxtSendCode.setVisibility(View.VISIBLE);
//        mTxtMsgPassCode.setOnClickListener(this);
        mTxtSendCode.setOnClickListener(this);
//        mTxtForgotPass.setOnClickListener(this);
        mTxtReg.setOnClickListener(this);
        mTxtTerms.setOnClickListener(this);
        mEdtMobNo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(mEdtMobNo.getText().toString().length()==10)     //size as per your requirement
                {
                    mEdtOtp.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        bundle=getArguments();
        if(bundle.getString("flg").equalsIgnoreCase("0"))
        {
            mBtnLogin.setVisibility(View.VISIBLE);
            mLinearTerms.setVisibility(View.GONE);
            mBtnLogin.setOnClickListener(this);
            mRelNewUser.setVisibility(View.VISIBLE);
            mLinearFullName.setVisibility(View.GONE);
//            mTxtMsgPassCode.setVisibility(View.VISIBLE);
//            mTxtMsgPassCode.setText("Use Password ?");
        }else
        {
            mLinearFullName.setVisibility(View.VISIBLE);
            mBtnLogin.setVisibility(View.GONE);
            mLinearTerms.setVisibility(View.VISIBLE);
            mBtnReg.setOnClickListener(this);
            mRelNewUser.setVisibility(View.GONE);
//            mTxtMsgPassCode.setVisibility(View.GONE);

        }
        //to get device_token_number
        SharedPreferences prefs = getActivity().getSharedPreferences("Token", MODE_PRIVATE);
        regId = prefs.getString("token", null);
        return view;
    }
    //method for Validation
    private boolean ValidationMobNo()
    {
        if(mLinearFullName.getVisibility()==View.VISIBLE)
        {
            if(mEdtFullName.isEmpty())
            {
                mEdtFullName.setError(getString(R.string.str_name));
                mEdtFullName.requestFocus();
                return false;
            }
        }
        if(mEdtMobNo.getString().isEmpty())
        {
            mEdtMobNo.setError(getString(R.string.str_mobNo));
            mEdtMobNo.requestFocus();
            return false;
        }else if(mEdtMobNo.getText().toString().length()<10)
        {
            mEdtMobNo.setError(getString(R.string.str_10mobNo));
            mEdtMobNo.requestFocus();
            return false;
        }else if(mEdtOtp.getString().isEmpty())
            {
                mEdtOtp.setError(getString(R.string.str_otp));
                mEdtOtp.requestFocus();
                return false;
            }
            else if(!mEdtOtp.getText().toString().equalsIgnoreCase(StrOtp))
            {
                mEdtOtp.setError(getString(R.string.str_validOtp));
                mEdtOtp.requestFocus();
                return false;

                }


        return true;
    }
    //webservice for login
    private void getLogin()
    {
        mStrLogin=getString(R.string.ws_host)+getString(R.string.ws_signIn);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("userEmail","");
        mParams.put("password", "");
        mParams.put("flag", "MOBILE");
        mParams.put("userMobile",mEdtMobNo.getText().toString());
        mParams.put("device_platform","android");
        mParams.put("device_token_number",regId);
        if (AvailableNetwork.isConnectingToInternet(getActivity())) {
            mAquery.progress(mProgress).ajax(mStrLogin, mParams, JSONObject.class, ajaxcallback);
        }
        else {
            showAlert(getString(R.string.str_not_internet_connection), -1);
        }
    }
    //webservice for reg
    private void Register()
    {
        mStrReg=getString(R.string.ws_host)+getString(R.string.ws_signUp);
        HashMap<String,Object> mHash=new HashMap<>();
        mHash.put("userEmail","");
        mHash.put("password","");
        mHash.put("registered_from","android-app");
        mHash.put("flag","MOBILE");
        mHash.put("userMobile",mEdtMobNo.getText().toString());
        mHash.put("fullName",mEdtFullName.getText().toString());
        mHash.put("device_platform","android");
        mHash.put("device_token_number",regId);
        if (AvailableNetwork.isConnectingToInternet(getActivity())) {
            mAquery.progress(mProgress).ajax(mStrReg, mHash, JSONObject.class, ajaxcallback);
        }
        else {
            showAlert(getString(R.string.str_not_internet_connection), -1);
        }
    }

    //method for dialog
    private void DialogForgotPass()
    {
        mDialogForgotPass = new Dialog(getActivity(), R.style.DialogAnimation);
        mDialogForgotPass.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogForgotPass.setContentView(R.layout.dialog_forgot_pass);
        mDialogForgotPass.setCancelable(false);
        mLinForgotMob=(LinearLayout)mDialogForgotPass.findViewById(R.id.linear_phoneForgot);
        mLinForgotEmail=(LinearLayout)mDialogForgotPass.findViewById(R.id.linear_emailForgot);

        mLinForgotMob.setVisibility(View.VISIBLE);
        mLinForgotEmail.setVisibility(View.GONE);

        mEdtForgotMob = (CustomEditText) mDialogForgotPass.findViewById(R.id.edt_forgotMobNo);
        mBtnSendOtp = (CustomButton) mDialogForgotPass.findViewById(R.id.btn_sendOtp);
        mBtnCancel = (CustomButton) mDialogForgotPass.findViewById(R.id.btn_cancel);

//        mDialogForgotPass.findViewById(R.id.img_forgot_close).setOnClickListener(this);
        mBtnSendOtp.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mDialogForgotPass.show();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        mDialogForgotPass.getWindow().setLayout((6 * width) / 7, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    //method for countDownTimer
    private void CountDownTimer()
    {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                String secs= String.valueOf(millisUntilFinished / 1000);
                mTxtSendCode.setVisibility(View.GONE);
                mTxtResend.setVisibility(View.GONE);
                mTxtSecs.setVisibility(View.VISIBLE);
                mTxtSecs.setText(secs+"s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish()
            {
                mTxtSecs.setVisibility(View.GONE);
                mTxtSendCode.setVisibility(View.GONE);
                mTxtResend.setVisibility(View.VISIBLE);
                mTxtResend.setOnClickListener(FragmentMobNo.this) ;

            }

        }.start();
    }
    //webservice for verification code
    private void sendVerificationCode(String flag)
    {
        mStrOTP=getString(R.string.ws_host)+getString(R.string.ws_send_verification_code);
        HashMap<String,Object> mHash=new HashMap<>();
        mHash.put("userEmail","");
        if(flag.equalsIgnoreCase("mobile_login"))
        {
            mHash.put("flag","MOBILE_LOGIN");
        }else
        {
            mHash.put("flag","MOBILE");
        }

        mHash.put("userMobile",mEdtMobNo.getText().toString());
        if(AvailableNetwork.isConnectingToInternet(getActivity()))
        {
            mAquery.ajax(mStrOTP, mHash, JSONObject.class, ajaxcallback);

        }else
        {
            showAlert(getString(R.string.str_not_internet_connection), -1);

        }
    }

    //response
    AjaxCallback<JSONObject> ajaxcallback=new AjaxCallback<JSONObject>()
    {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if(object!=null)
            {
                if(url.equalsIgnoreCase(mStrOTP))
                {
                    try {
                        JSONObject jsonObject=object.getJSONObject("result");
                        Toast.makeText(getActivity(),jsonObject.getString("msg_string"),Toast.LENGTH_LONG).show();
                        StrOtp=jsonObject.getString("otp");
//                        mEdtOtp.setText(StrOtp);
                        CountDownTimer();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(url.equalsIgnoreCase(mStrReg))
                {
                    try {
                        JSONObject jsonObject=object.getJSONObject("result");
                        if(jsonObject.getString("msg").equalsIgnoreCase("1"))
                        {
                            showAlert("Registration has been done successfully.",1);
                            SessionClass.setUserId(getActivity(),jsonObject.getString("uid"));
                            SessionClass.setUserType(getActivity(),jsonObject.getString("utype"));
                            SessionClass.setUserEmail(getActivity(),jsonObject.getString("uemail"));
                            SessionClass.setUserMob(getActivity(),jsonObject.getString("umobile"));
                            SessionClass.setUserFlag(getActivity(),jsonObject.getString("flag"));

                        }else if(jsonObject.getString("msg").equalsIgnoreCase("0"))
                        {
                            showAlert("This user is already been registered.",-1);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(url.equalsIgnoreCase(mStrLogin))
                {
                    try {
                        JSONObject jsonObject=object.getJSONObject("result");
                        if(jsonObject.getString("msg").equalsIgnoreCase("1"))
                        {
                            showAlert("Logged in successfully.",1);
                            SessionClass.login(getActivity(),jsonObject.getString("uid"),
                                    jsonObject.getString("utype"),
                                    jsonObject.getString("uemail"),
                                    jsonObject.getString("umobile"),
                                    jsonObject.getString("flag")
                            );
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
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
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
                intent = new Intent(getActivity(), ActivityDashBoard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("FlgMobEmail", SessionClass.getUserMob(getActivity()));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                getActivity().finish();
                break;

        }
    }

    @Override
    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.btn_MobLogin:
//                intent=new Intent(getActivity(),ActivityDashBoard.class);
//                startActivity(intent);
//                getActivity().finish();
                if(ValidationMobNo())
                {
                    getLogin();
                }
                break;
            case R.id.btn_MobReg:
                if(ValidationMobNo())
                {

                    Register();
                }
                break;
            case R.id.txt_newUserMob:
                intent=new Intent(getActivity(),ActivityRegister.class);
                startActivity(intent);
                break;
            case R.id.txt_sendCode:
                if(mEdtMobNo.getString().isEmpty())
                {
                    mEdtMobNo.setError(getString(R.string.str_10mobNo));
                    mEdtMobNo.requestFocus();
                }else if(mBtnLogin.getVisibility()==View.VISIBLE) {

                    sendVerificationCode("mobile_login");
                }else
                {
                    sendVerificationCode("mobile");
                }
                break;
            case R.id.txt_ResendCode:
                if(mEdtMobNo.getString().isEmpty())
                {
                    mEdtMobNo.setError(getString(R.string.str_10mobNo));
                    mEdtMobNo.requestFocus();
                }else if(mBtnLogin.getVisibility()==View.VISIBLE) {

                    sendVerificationCode("mobile_login");
                }else
                {
                    sendVerificationCode("mobile");
                }
                break;
            case R.id.txt_termsMob:
                intent=new Intent(getActivity(),ActivityTermsCondition.class);
                startActivity(intent);
                break;
//            case R.id.txt_forPassMob:
//                intent=new Intent(getActivity(),ActivityForgotPass.class);
//                intent.putExtra("ForPass","1");
//                startActivity(intent);
//                break;
//            case R.id.txt_msgPassCode:
//                if(mTxtMsgPassCode.getText().toString().equalsIgnoreCase("Use Password ?"))
//                {
//                    mLinearPhonePass.setVisibility(View.VISIBLE);
//                    mLinearPhoneVerCode.setVisibility(View.GONE);
//                    mTxtMsgPassCode.setText("Use verification code ?");
//                    mTxtForgotPass.setVisibility(View.VISIBLE);
//                }else
//                {
//                    mLinearPhonePass.setVisibility(View.GONE);
//                    mLinearPhoneVerCode.setVisibility(View.VISIBLE);
//                    mTxtMsgPassCode.setText("Use Password ?");
//                    mTxtForgotPass.setVisibility(View.GONE);
//                }
//                break;
//            case R.id.img_hidePassMob:
//                mImgShowPass.setVisibility(View.VISIBLE);
//                mImgHidePass.setVisibility(View.GONE);
//                mEdtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                break;
//
//            case R.id.img_showPassMob:
//                mImgShowPass.setVisibility(View.GONE);
//                mImgHidePass.setVisibility(View.VISIBLE);
//                mEdtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                break;
        }
    }

}
