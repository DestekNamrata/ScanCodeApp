package com.vayortricks.vtproductscanner.Activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.notbytes.barcode_reader.BarcodeReaderFragment;
import com.vayortricks.vtproductscanner.R;

import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.androidquery.util.AQUtility.getContext;
public class ActivityBarcodeReader extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener , View.OnClickListener {
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1208;
    private Detector.Detections<Barcode> barcodeList;
    private String barcodeFormat="",formatType="";
    private String date;
    private Button switchFlashlightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_reader);
        date = new SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.getDefault()).format(new Date());

        switchFlashlightButton = (Button) findViewById(R.id.switch_flashlight);
//        getNightMode();
        launchBarCodeActivity();

//        if (!hasFlash()) {
//            switchFlashlightButton.setVisibility(View.GONE);
//        }
//        switchFlashlightButton.setOnClickListener(this);

    }
    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }
//  private void getNightMode()
//  {
//      int nightModeFlags =
//              this.getResources().getConfiguration().uiMode &
//                      Configuration.UI_MODE_NIGHT_MASK;
//      switch (nightModeFlags) {
//          case Configuration.UI_MODE_NIGHT_YES:
//              Toast.makeText(this,"ON",Toast.LENGTH_LONG).show();
//              break;
//
//          case Configuration.UI_MODE_NIGHT_NO:
//              Toast.makeText(this,"OFF",Toast.LENGTH_LONG).show();
//              break;
//
//          case Configuration.UI_MODE_NIGHT_UNDEFINED:
//              Toast.makeText(this,"UNDEFINED",Toast.LENGTH_LONG).show();
//              break;
//      }
//  }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "error in  scanning", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
//            BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
//                    .setBarcodeFormats(Barcode.ALL_FORMATS)
//                    .build();
            final int value1 = barcode.format;

            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent1.putExtra("android.intent.extra.quickCapture",true);

            decodeFormat(value1);
            ActivityScanHistory.scanFlag="1";
            Intent intent=new Intent(this,ActivityScanViewDet.class);
            intent.putExtra("scanText",barcode.rawValue);
            intent.putExtra("date",date);
            intent.putExtra("imgScan","");
            intent.putExtra("formatName",formatType);
            startActivity(intent);
            finish();
//            updateList(barcodeList);

            Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
//            mTvResultHeader.setText("On Activity Result");
//            mTvResult.setText(barcode.rawValue);
        }

    }
    private String decodeFormat(int format) {
        switch (format){
            case Barcode.CODE_128:
                formatType="CODE_128";
                return formatType;
            case Barcode.CODE_39:
                formatType="CODE_39";
                return formatType;
            case Barcode.CODE_93:
                formatType="CODE_93";
                return formatType;
            case Barcode.CODABAR:
                formatType="CODABAR";
                return formatType;
            case Barcode.DATA_MATRIX:
                return "DATA_MATRIX";
            case Barcode.EAN_13:
                return "EAN_13";
            case Barcode.EAN_8:
                return "EAN_8";
            case Barcode.ITF:
                return "ITF";
            case Barcode.QR_CODE:
                formatType="QR_CODE";
                return formatType;
            case Barcode.UPC_A:
                formatType="UPC_A";
                return formatType;
            case Barcode.UPC_E:
                return "UPC_E";
            case Barcode.PDF417:
                return "PDF417";
            case Barcode.AZTEC:
                return "AZTEC";
            default:
                return "";
        }
    }
//    public void updateList(Detector.Detections<Barcode> barcodeList) {
//        this.barcodeList = barcodeList;
//        final SparseArray<Barcode> detectedItems = barcodeList.getDetectedItems();
//        for (int i = 0; i < detectedItems.size(); i++) {
//            Log.e("Value", "------>" + detectedItems.valueAt(i));
//            final Barcode barcode = detectedItems.valueAt(i);
//            final int value = detectedItems.valueAt(i).valueFormat;
//            final int value1 = barcode.valueFormat;
//
//            switch (value) {
//                case Barcode.DATA_MATRIX:
//                    Log.i("Value", barcode.rawValue);
//
//                    break;
//                case Barcode.QR_CODE:
//                    Log.i("Value", barcode.rawValue);
//                    break;
//                case Barcode.CODABAR:
//                    Log.i("Value", barcode.rawValue);
//                    break;
//                case Barcode.UPC_E:
//                    Log.i("Value", barcode.rawValue);
//                    break;
//                case Barcode.UPC_A:
//                    Log.i("Value", barcode.rawValue);
//                    break;
//                case Barcode.TEXT:
//                    Log.i("Value", barcode.rawValue);
//                    break;
//            }
//        }
//
//        final String noItems = detectedItems.toString();
//    }
    @Override
    public void onScanned(Barcode barcode) {

//        Toast.makeText(this, barcode.rawValue, Toast.LENGTH_SHORT).show();
        decodeFormat(barcode.format);
        ActivityScanHistory.scanFlag="1";
        Intent intent=new Intent(this,ActivityScanViewDet.class);
        intent.putExtra("scanText",barcode.rawValue);
        intent.putExtra("date",date);
        intent.putExtra("imgScan","");
        intent.putExtra("formatName",formatType);
        startActivity(intent);
        finish();
        //  mTvResultHeader.setText("Barcode value from fragment");
        // mTvResult.setText(barcode.rawValue);
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight() {
        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightButton.getText())) {
//            na.setTorchOn();
            turnFlashlightOn();
//            BarcodeReaderFragment.newInstance(true,true).setUseFlash(true);
            switchFlashlightButton.setText(getString(R.string.turn_off_flashlight));
        } else {
//            barcodeScannerView.setTorchOff();
            turnFlashlightOff();
//            BarcodeReaderFragment.newInstance(true,false).setUseFlash(false);
            switchFlashlightButton.setText(getString(R.string.turn_on_flashlight));

        }
    }
    private void turnFlashlightOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String cameraId = null; // Usually front camera is at 0 position.
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(cameraId, true);

                }
            } catch (Exception e) {
//                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        } else {
            Camera mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }

    private void turnFlashlightOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                String cameraId;
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
                    camManager.setTorchMode(cameraId, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Camera mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            mCamera.stopPreview();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.switch_flashlight:
                switchFlashlight();
                break;
        }
    }
}
