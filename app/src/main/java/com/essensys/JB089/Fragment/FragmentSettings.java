package com.essensys.JB089.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.Activity.ActivityDashBoard;
import com.essensys.JB089.Activity.ActivityEmailUpdate;
import com.essensys.JB089.Activity.ActivityMobileUpdate;
import com.essensys.JB089.Activity.ActivityProfileUpdate;
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
public class FragmentSettings extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private View view;
    private CustomEditText mEdtName,mEdtEmail,mEdtPhone;
    private AQuery mAquery;
    private ImageView mImgEditName,mImgEditEmail,mImgEditMob;
    private TransparentProgressDialog mProgress;
    private String StrGender="",mStrGetSett="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_settings,container,false);
        mEdtName=(CustomEditText)view.findViewById(R.id.edt_name_sett);
        mEdtEmail=(CustomEditText)view.findViewById(R.id.edt_email_sett);
        mEdtPhone=(CustomEditText)view.findViewById(R.id.edt_mob_sett);

        mAquery=new AQuery(getActivity());
        mProgress=new TransparentProgressDialog(getActivity(),R.drawable.ic_loader_image);
        mImgEditName=(ImageView)view.findViewById(R.id.img_editName);
        mImgEditName.setOnClickListener(this);
        mImgEditEmail=(ImageView)view.findViewById(R.id.img_editEmail);
        mImgEditEmail.setOnClickListener(this);
        mImgEditMob=(ImageView)view.findViewById(R.id.img_editMob);
        mImgEditMob.setOnClickListener(this);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((CustomTextView)getActivity().findViewById(R.id.txt_title)).setText("Settings");
        ((ImageView)getActivity().findViewById(R.id.img_edit)).setVisibility(View.GONE);
        setEditing(false);
        getSettings();
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.img_editName:
                intent=new Intent(getActivity(),ActivityProfileUpdate.class);
                intent.putExtra("name",mEdtName.getText().toString());
                intent.putExtra("gender",StrGender);
                startActivity(intent);
                break;
            case R.id.img_editEmail:
                intent=new Intent(getActivity(),ActivityEmailUpdate.class);
                intent.putExtra("email",mEdtEmail.getText().toString());
                startActivity(intent);
                break;
            case R.id.img_editMob:
                intent=new Intent(getActivity(),ActivityMobileUpdate.class);
                intent.putExtra("mobile",mEdtPhone.getText().toString());
                startActivity(intent);
                break;
        }
    }

    //method to enable
    private void setEditing(boolean b)
    {
        mEdtName.setEnabled(b);
        mEdtEmail.setEnabled(b);
        mEdtPhone.setEnabled(b);

    }

    //webservice for get det settings
    private void getSettings()
    {
     mStrGetSett=getString(R.string.ws_host)+getString(R.string.ws_get_sett);
        HashMap<String,Object> mParams=new HashMap<>();
        mParams.put("uid",SessionClass.getUserId(getActivity()));
        if(AvailableNetwork.isConnectingToInternet(getActivity()))
        {
            mAquery.progress(mProgress).ajax(mStrGetSett,mParams,JSONObject.class,ajaxCallbackUpdate);
        }else
        {
            showAlert(getString(R.string.str_not_internet_connection),-1);
        }

    }

    //reponse
    AjaxCallback<JSONObject> ajaxCallbackUpdate=new AjaxCallback<JSONObject>()
    {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if(object!=null)
            {
               if(url.equalsIgnoreCase(mStrGetSett))
               {
                   try {
                       JSONObject jsonObject=object.getJSONObject("result");
                       mEdtName.setText(jsonObject.getString("ufull_name"));
                       mEdtName.setTextColor(getResources().getColor(R.color.black));

                       mEdtEmail.setText(jsonObject.getString("uemail"));
                       mEdtEmail.setTextColor(getResources().getColor(R.color.black));

                       mEdtPhone.setText(jsonObject.getString("umobile"));
                       mEdtPhone.setTextColor(getResources().getColor(R.color.black));
                       StrGender=jsonObject.getString("ugender");


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
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                getActivity().finish();
                break;

        }
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    }
}
