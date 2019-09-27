package com.essensys.JB089.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.Activity.ActivityDashBoard;
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
public class FragmentContactUs extends Fragment {
    private View view;
    private CustomEditText mEdtName,mEdtEmail,mEdtMessage;
    private CustomButton mBtnSend;
    private String mstrContact="";
    private AQuery mAquery;
    private TransparentProgressDialog mProgress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_contact_us,container,false);
        mEdtName=(CustomEditText)view.findViewById(R.id.edt_name);
        mEdtEmail=(CustomEditText)view.findViewById(R.id.edt_email_contct);
        mEdtMessage=(CustomEditText)view.findViewById(R.id.edt_msgs);
        mBtnSend=(CustomButton)view.findViewById(R.id.btn_send_cntct);
        mAquery=new AQuery(getActivity());
        mProgress=new TransparentProgressDialog(getActivity(),R.drawable.ic_loader_image);
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEdtName.isEmpty()){
                    mEdtName.setError("Please enter name.");
                    mEdtName.requestFocus();
                }else if(mEdtEmail.isEmpty())
                {
                    mEdtEmail.setError(getString(R.string.str_email));
                    mEdtEmail.requestFocus();
                }else if(!Validation.checkEmail(mEdtEmail.getText().toString()))
                {
                    mEdtEmail.setError(getString(R.string.str_Validemail));
                    mEdtEmail.requestFocus();
                }else {
                    sendcontact();
                }
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((CustomTextView)getActivity().findViewById(R.id.txt_title)).setText("Contact Us");
        ((ImageView)getActivity().findViewById(R.id.img_edit)).setVisibility(View.GONE);

    }
    //webservice for send contact
    private void sendcontact()
    {
     mstrContact=getString(R.string.ws_host)+getString(R.string.ws_contact_us);
        HashMap<String,Object> mParams=new HashMap<>();
        mParams.put("name",mEdtName.getText().toString());
        mParams.put("email",mEdtEmail.getText().toString());
        mParams.put("message",mEdtMessage.getText().toString());
        if(AvailableNetwork.isConnectingToInternet(getActivity()))
        {
            mAquery.progress(mProgress).ajax(mstrContact,mParams,JSONObject.class,new AjaxCallback<JSONObject>(){
                @Override
                public void callback(String url, JSONObject object, AjaxStatus status) {
                    super.callback(url, object, status);
                    if(object!=null)
                    {
                        if(url.equalsIgnoreCase(mstrContact))
                        {
                            try {
                                JSONObject jsonObject=object.getJSONObject("result");
                                if(jsonObject.getString("msg").equalsIgnoreCase("1"))
                                {
                                    showAlert(jsonObject.getString("msg_string"),1);
//                                    Intent intent = new Intent(getActivity(), ActivityDashBoard.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                    getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
//                                    getActivity().finish();
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
            });
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
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                getActivity().finish();
                break;

        }
    }

}
