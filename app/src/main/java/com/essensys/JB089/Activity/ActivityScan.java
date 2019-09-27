package com.essensys.JB089.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.CustomView.TransparentProgressDialog;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;
import com.essensys.JB089.Utility.AvailableNetwork;
import com.essensys.JB089.Utility.MultiPartUtility;
import com.essensys.JB089.Utility.RetrofitClient;
import com.essensys.JB089.Utility.RetrofitInterfaces;
import com.essensys.JB089.ZXingScanner;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

import static android.app.Activity.RESULT_OK;
public class ActivityScan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    boolean doubleBackToExitPressedOnce = false;
    private Bitmap mBmp;
    private String mStrScan="",encoded="",scanResult="";
    private String selectedFile;
    private String flag="0";
    private TransparentProgressDialog mProgress;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
//        mScannerView = new ZXingScannerView(this);
//        mScannerView.setMinimumHeight(300);// Programmatically initialize the scanner view
        setContentView(R.layout.activity_scan);
        mScannerView=(ZXingScannerView)findViewById(R.id.zx_view);
        mProgress=new TransparentProgressDialog(this,R.drawable.ic_loader_image);
        isStoragePermissionGranted();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.

        mScannerView.startCamera();
//        mScannerView.stopCameraPreview();

// Set the scanner view as the content view
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        //MainActivity.tvresult.setText(rawResult.getText());
//        Intent intent = getIntent();
//        intent.putExtra("scan", rawResult.getText());
//        setResult(101, intent);
//        finish();
        scanResult=rawResult.getText();
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, 1);
        if(flag.equalsIgnoreCase("0"))
        {
            selectImage();

        }
//
    }

    private void sendData(boolean isFinish) {
        Intent intent = getIntent();
        if (isFinish)
            intent.putExtra("noscan", "");
        else intent.putExtra("scan", "");
        setResult(101, intent);
        //onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            sendData(true);
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                sendData(false);
                finish();
            }
        }, 1000);
    }
    //webservice in retrofit
    private void ScanRetrofit(final String scanResult)
    {
//        mProgress.show();
        final HashMap<String, RequestBody> scanCode=new HashMap<>();
        scanCode.put("uid", RetrofitClient.getRequestBodyFromString(SessionClass.getUserId(this)));
        scanCode.put("scan_details", RetrofitClient.getRequestBodyFromString(scanResult));
        List<MultipartBody.Part> partList = new ArrayList<>();

        if (selectedFile!=null) {

            File file = new File(selectedFile);
            // RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part regDocumentPart = MultipartBody.Part.createFormData("scan_image",
                    file.getName(), RetrofitClient.getRequestBodyFromStringFile(selectedFile));
            partList.add(regDocumentPart);
            scanCode.put("isFileUpload ", RetrofitClient.getRequestBodyFromString("true"));


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
//                            mProgress.dismiss();
                            String result=response.body().string();
                            JSONObject jobj = new JSONObject(result);
                            JSONObject jsonObject=jobj.getJSONObject("result");
                            if (jsonObject.getString("msg").equalsIgnoreCase("1")) {
                                Intent intent = getIntent();
//                                intent.putExtra("scan", scanResult);
                                setResult(101, intent);
                                finish();
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
    //using asyntask
    private class ScanCode extends AsyncTask<String,String,String>
    {
        String requestUrl=getString(R.string.ws_host)+getString(R.string.ws_add_productScan);

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<MultipartBody.Part> partList = new ArrayList<>();

                MultiPartUtility multipart=new MultiPartUtility(requestUrl,"UTF-8");
                multipart.addFormField("uid",SessionClass.getUserId(ActivityScan.this));
                multipart.addFormField("scan_detail",scanResult);
                if (selectedFile!=null) {

                    File file = new File(selectedFile);
                    // RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);
                    MultipartBody.Part regDocumentPart = MultipartBody.Part.createFormData("scan_image",
                            file.getName(), RetrofitClient.getRequestBodyFromStringFile(selectedFile));
                    partList.add(regDocumentPart);



                    multipart.addFormField("scan_image", String.valueOf(partList));

                } else {
                    multipart.addFormField("scan_image", "");
                }
                String response = multipart.finish();

                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
//            String result=response.body().string();
//            JSONObject jobj = new JSONObject(result);
//            JSONObject jsonObject=jobj.getJSONObject("result");
//            if (jsonObject.getString("msg").equalsIgnoreCase("1")) {
                Intent intent = getIntent();
                intent.putExtra("scan", scanResult);
                setResult(101, intent);
                finish();
//            } else {
//                showAlert(jsonObject.getString("msg_string"), -1);
//            }


            super.onPostExecute(s);
        }
    }


    //method for runtime permissions
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED ) {
//                Log.v(TAG,"Permission is granted");
                Toast.makeText(this,"Permission is granted",Toast.LENGTH_LONG).show();

                return true;
            } else {

//                Log.v(TAG,"Permission is revoked");
//                Toast.makeText(this,"Permission is revoked",Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");

            Toast.makeText(this,"Permission is granted",Toast.LENGTH_LONG).show();

            return true;
        }
    }
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
    /**
     * Choose the image
     */
    private void selectImage() {

        final CharSequence[] items = getResources().getStringArray(R.array.str_arr_image_option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to upload a picture ?");
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    flag="1";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (item == 1) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_PICK);
//                    startActivityForResult(intent, 2);
                    ScanRetrofit(scanResult);
                    dialog.dismiss();
                    } else if (item == 2) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fos;
                try {
                    destination.createNewFile();
                    fos = new FileOutputStream(destination);
                    selectedFile= destination.getAbsolutePath();
                    fos.write(bytes.toByteArray());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mBmp = thumbnail;
                ScanRetrofit(scanResult);
//                new ScanCode().execute("");


            } else {
                if (requestCode == 2) {
                    try {
                        Uri imageuri = data.getData();
                        InputStream is = getContentResolver().openInputStream(imageuri);
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        mBmp = bm;
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            //            convertToByteArray(mBmp);
        }
    }
    //method to convert bmp to byteArray
    public void convertToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }


}

