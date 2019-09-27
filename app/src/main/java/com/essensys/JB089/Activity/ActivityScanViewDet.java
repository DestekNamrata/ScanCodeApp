package com.essensys.JB089.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.CustomView.TransparentProgressDialog;
import com.essensys.JB089.Fragment.FragmentHome;
import com.essensys.JB089.Fragment.FragmentScanHistory;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;
import com.essensys.JB089.Utility.RetrofitClient;
import com.essensys.JB089.Utility.RetrofitInterfaces;
import com.journeyapps.barcodescanner.CameraPreview;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ActivityScanViewDet extends AppCompatActivity implements View.OnClickListener {
    private CameraPreview cameraPreview;
    private CustomTextView mTxtScan,mTxtDate,mTxtFormat;
    private ImageView mImgScan,mImgBack;
    private TransparentProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_view_det);
//        cameraPreview = (CameraPreview)findViewById(R.id.camera_preview);
        mTxtScan=(CustomTextView)findViewById(R.id.txt_scanText);
        mTxtDate=(CustomTextView)findViewById(R.id.txt_scanDate);
        mTxtFormat=(CustomTextView)findViewById(R.id.txt_formatName);
        mImgScan=(ImageView)findViewById(R.id.img_scanImg);
        mImgBack=(ImageView)findViewById(R.id.img_bckScanDet);
        mImgBack.setOnClickListener(this);
        progressBar=new TransparentProgressDialog(this,R.drawable.ic_loader_image);
        getData();

    }
    //webservice in retrofit
    private void ScanRetrofit(final String contents, final String barcodeImage,final String format)
    {
        progressBar.dismiss();
        final HashMap<String, RequestBody> scanCode=new HashMap<>();
        scanCode.put("uid", RetrofitClient.getRequestBodyFromString(SessionClass.getUserId(this)));
        scanCode.put("scan_details", RetrofitClient.getRequestBodyFromString(contents));
        scanCode.put("format_name",RetrofitClient.getRequestBodyFromString(format));
        List<MultipartBody.Part> partList = new ArrayList<>();

        if (barcodeImage!=null) {

            File file = new File(barcodeImage);
            // RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part regDocumentPart = MultipartBody.Part.createFormData("scan_image",
                    file.getName(), RetrofitClient.getRequestBodyFromStringFile(barcodeImage));
            partList.add(regDocumentPart);
            scanCode.put("isFileUpload", RetrofitClient.getRequestBodyFromString("true"));
//            showAlert(String.valueOf(scanCode),-1);

        } else {
            scanCode.put("isFileUpload", RetrofitClient.getRequestBodyFromString("false"));
        }

        RetrofitClient
                .getClient(getString(R.string.ws_host)+getString(R.string.ws_add_productScan))
                .create(RetrofitInterfaces.IScanCode.class)
                .post(scanCode, partList)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result=response.body().string();
                            JSONObject jobj = new JSONObject(result);
                            JSONObject jsonObject=jobj.getJSONObject("result");
                            if (jsonObject.getString("msg").equalsIgnoreCase("1")) {
                                progressBar.dismiss();
//                                FragmentHome.scanFlag="0";
//                                if(getIntent().hasExtra("flagHome"))
//                                {
//                                    onBackPressed();

//                                }
                            } else {
                                showAlert(jsonObject.getString("msg_string"), -1);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });

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

    //method for get Data
    private void getData()
    {
        mTxtDate.setText(getIntent().getStringExtra("date"));
        mTxtScan.setText(getIntent().getStringExtra("scanText"));
        mTxtFormat.setText(getIntent().getStringExtra("formatName"));
        Glide.with(this)
                .load(getIntent().getStringExtra("imgScan"))
                .placeholder(R.drawable.ic_vt_logo)
                .into(mImgScan);
             if(FragmentScanHistory.scanFlag!="0")
             {
                 ScanRetrofit(getIntent().getStringExtra("scanText"),getIntent().getStringExtra("imgScan"),getIntent().getStringExtra("formatName"));

             }

             }
    @Override
    public void onBackPressed() {
//        if(getIntent().hasExtra("flagscanfrag"))
//        {
//            Intent intent=new Intent(this,ActivityDashBoard.class);
//            intent.putExtra("flagScan","0");
////        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//            //        Toast.makeText(this,"Back",Toast.LENGTH_LONG).show();
//
//        }else {

            FragmentHome.offset = "0";
//            ActivityScanHistory.offset = "0";
            ActivityScanHistory.scanFlag = "0";
            Intent intent = new Intent(this, ActivityScanHistory.class);
            startActivity(intent);
            finish();
//        }
    }
    @Override
    public void onPause() {
        super.onPause();
//        cameraPreview.pauseAndWait();
    }

    @Override
    public void onResume() {
        super.onResume();
//        cameraPreview.resume();
    }
    @Override
    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.img_bckScanDet:
//                FragmentHome.offset="0";
//                FragmentScanHistory.offset="0";
//                FragmentScanHistory.scanFlag="0";
//                intent = new Intent(this, ActivityScanHistory.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
                break;
        }
    }
}
