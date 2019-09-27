package com.essensys.JB089.Fragment;
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
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FragmentEmail extends Fragment implements View.OnClickListener {
    private View view;
    private CustomEditText medtEmail,mEdtPass,mEdtVerCode,mEdtFullName;
    private CustomButton mBtnLogin,mBtnReg;
    private CustomTextView mTxtReg,mTxtForgotPass,mTxtSend,mTxtSecs;
    private Bundle bundle;
    private LinearLayout mLinVerCodeReg,mLinearFullName;
    private RelativeLayout mRelNewUser;
    private ImageView mImgShowPass,mImgHidePass;
    private TransparentProgressDialog mProgress;
    private AQuery mAquery;
    private CustomTextView mTxtTerms;
    private LinearLayout mLinearTerms;
    private String mStrLogin="",mStrReg="",StrOtp="",mStrOTP="",mStrEmailFlg="";
    public static String regId="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_email,container,false);
        mProgress = new TransparentProgressDialog(getActivity(), R.drawable.ic_loader_image);
        mAquery = new AQuery(getActivity());
        mEdtFullName=(CustomEditText)view.findViewById(R.id.edt_fullname);
        mLinearFullName=(LinearLayout) view.findViewById(R.id.linear_fullName);
        medtEmail=(CustomEditText)view.findViewById(R.id.edt_email);
        mEdtPass=(CustomEditText)view.findViewById(R.id.edt_pass);
        mBtnLogin=(CustomButton)view.findViewById(R.id.btn_EmailLogin);
        mBtnReg=(CustomButton)view.findViewById(R.id.btn_EmailReg);
        mRelNewUser=(RelativeLayout)view.findViewById(R.id.rel_new_userEmail);
        mTxtReg=(CustomTextView)view.findViewById(R.id.txt_newUserEmail);
        mTxtForgotPass=(CustomTextView)view.findViewById(R.id.txt_forPassEmail);
        mTxtSend=(CustomTextView)view.findViewById(R.id.txt_sendCodeEmail);
        mEdtVerCode=(CustomEditText)view.findViewById(R.id.edt_otpEmail);
        mLinVerCodeReg=(LinearLayout)view.findViewById(R.id.linear_otpReg);
        mImgShowPass=(ImageView)view.findViewById(R.id.img_showPass);
        mImgHidePass=(ImageView)view.findViewById(R.id.img_hidePass);
        mLinearTerms=(LinearLayout)view.findViewById(R.id.lin_termCondEmail);
        mTxtTerms = (CustomTextView)view.findViewById(R.id.txt_termsEmail);
        mTxtSecs=(CustomTextView)view.findViewById(R.id.txt_secsCodeEmail);
        mTxtTerms.setOnClickListener(this);
        mImgShowPass.setOnClickListener(this);
        mImgHidePass.setOnClickListener(this);
        mTxtForgotPass.setOnClickListener(this);
        mTxtReg.setOnClickListener(this);
        mTxtSecs.setVisibility(View.GONE);
        mTxtSend.setVisibility(View.VISIBLE);
        mTxtSend.setText("Send");
        //for focus to come on next line of otp
        medtEmail.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                String str = medtEmail.getText().toString();
                System.out.println("last char = " + str.charAt(str.length() - 1));
                // TODO Auto-generated method stub

                if(String.valueOf(str.charAt(str.length() - 1)).equalsIgnoreCase("m"))     //size as per your requirement
                {
                    mEdtVerCode.requestFocus();
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
            mTxtForgotPass.setVisibility(View.VISIBLE);
            mBtnLogin.setOnClickListener(this);
            mRelNewUser.setVisibility(View.VISIBLE);
            mLinVerCodeReg.setVisibility(View.GONE);
            mLinearFullName.setVisibility(View.GONE);

        }else
        {
            mLinearFullName.setVisibility(View.VISIBLE);
            mBtnLogin.setVisibility(View.GONE);
            mLinearTerms.setVisibility(View.VISIBLE);
            mTxtForgotPass.setVisibility(View.GONE);
            mBtnReg.setOnClickListener(this);
            mRelNewUser.setVisibility(View.GONE);
            mLinVerCodeReg.setVisibility(View.VISIBLE);
            mTxtSend.setOnClickListener(this);

        }
        //to get device_token_number
        SharedPreferences prefs = getActivity().getSharedPreferences("Token", MODE_PRIVATE);
        regId = prefs.getString("token", null);
        return view;
    }
    //method for validation
    private boolean ValidationEmail()
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
        if(medtEmail.getString().isEmpty())
        {
            medtEmail.setError(getString(R.string.str_email));
            medtEmail.requestFocus();
            return false;
        }
        else if(!Validation.checkEmail(medtEmail.getText().toString().trim()))
        {
            medtEmail.setError(getString(R.string.str_Validemail));
            medtEmail.requestFocus();
            return false;
        }
        else if(mLinVerCodeReg.getVisibility()==View.VISIBLE)
        {
            if(mEdtVerCode.getString().isEmpty())
            {
                mEdtVerCode.setError(getString(R.string.str_otp));
                mEdtVerCode.requestFocus();
                return false;
            }else if(!mEdtVerCode.getText().toString().equalsIgnoreCase(StrOtp))
            {
                mEdtVerCode.setError(getString(R.string.str_validOtp));
                mEdtVerCode.requestFocus();
                return false;
            }else if(mEdtPass.getString().isEmpty())
            {
                mEdtPass.setError(getString(R.string.str_pass));
                mEdtPass.requestFocus();
                return false;
            }else if(mEdtPass.getText().toString().length()<6)
            {
                mEdtPass.setError(getString(R.string.str_Validpass));
                mEdtPass.requestFocus();
                return false;
            }

        }else if(mEdtPass.getString().isEmpty())
        {
            mEdtPass.setError(getString(R.string.str_pass));
            mEdtPass.requestFocus();
            return false;
        }else if(mEdtPass.getText().toString().length()<6)
        {
            mEdtPass.setError(getString(R.string.str_Validpass));
            mEdtPass.requestFocus();
            return false;
        }
        return true;
    }
    //webservice for login
    private void getLogin()
    {
        mStrLogin=getString(R.string.ws_host)+getString(R.string.ws_signIn);
        HashMap<String, Object> mParams = new HashMap<>();
        mParams.put("userEmail", medtEmail.getText().toString());
        mParams.put("password", mEdtPass.getText().toString());
        mParams.put("flag", "EMAIL");
        mParams.put("userMobile","");
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
        mHash.put("userEmail",medtEmail.getText().toString());
        mHash.put("password",mEdtPass.getText().toString());
        mHash.put("registered_from","android-app");
        mHash.put("flag","EMAIL");
        mHash.put("userMobile","");
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
    //webservice for verification code
    private void sendVerificationCode()
    {
      mStrOTP=getString(R.string.ws_host)+getString(R.string.ws_send_verification_code);
      HashMap<String,Object> mHash=new HashMap<>();
      mHash.put("userEmail",medtEmail.getText().toString());
      mHash.put("flag","EMAIL");
      mHash.put("userMobile","");
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
                            if(jsonObject.getString("msg").equalsIgnoreCase("1"))
                            {
                                Toast.makeText(getActivity(),jsonObject.getString("msg_string"),Toast.LENGTH_LONG).show();
                                StrOtp=jsonObject.getString("otp");
//                                mEdtVerCode.setText(StrOtp);
                                CountDownTimer();
                            }else
                            {
                                showAlert(jsonObject.getString("msg_string"),-1);
                            }

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

                                mStrEmailFlg=jsonObject.getString("flag");

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
                intent.putExtra("FlgMobEmail", SessionClass.getUserEmail(getActivity()));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                getActivity().finish();
                break;
            case 2:
                intent = new Intent(getActivity(), ActivityLogin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            case R.id.btn_EmailLogin:
                if(ValidationEmail())
                {
                    getLogin();
                }
                break;
            case R.id.btn_EmailReg:
                if(ValidationEmail())
                {
                    Register();
                }
                break;
            case R.id.txt_newUserEmail:
                intent=new Intent(getActivity(),ActivityRegister.class);
                startActivity(intent);
                break;
            case R.id.txt_sendCodeEmail:
                if(medtEmail.getString().isEmpty())
                {
                    medtEmail.setError(getString(R.string.str_Validemail));
                    medtEmail.requestFocus();
                }else {
                    sendVerificationCode();
                    }
                break;
            case R.id.img_showPass:
                mImgShowPass.setVisibility(View.GONE);
                mImgHidePass.setVisibility(View.VISIBLE);
                mEdtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case R.id.img_hidePass:
                mImgShowPass.setVisibility(View.VISIBLE);
                mImgHidePass.setVisibility(View.GONE);
                mEdtPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                break;

            case R.id.txt_forPassEmail:
                intent=new Intent(getActivity(),ActivityForgotPass.class);
                intent.putExtra("ForPass","0");
                startActivity(intent);
                break;

            case R.id.txt_termsEmail:
                intent=new Intent(getActivity(),ActivityTermsCondition.class);
                startActivity(intent);
                break;
        }
    }

    //method for countDownTimer
    private void CountDownTimer()
    {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                String secs= String.valueOf(millisUntilFinished / 1000);
                mTxtSecs.setText(secs+"s");
                mTxtSecs.setVisibility(View.VISIBLE);
                mTxtSend.setVisibility(View.GONE);
                //here you can have your logic to set text to edittext
            }

            public void onFinish()
            {
                mTxtSecs.setVisibility(View.GONE);
                mTxtSend.setVisibility(View.VISIBLE);
                mTxtSend.setText("Resend");
            }

        }.start();
    }
}
