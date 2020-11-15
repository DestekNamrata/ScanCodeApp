package com.vayortricks.vtproductscanner.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vayortricks.vtproductscanner.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class MaterialScanActivity extends AppCompatActivity implements MaterialBarcodeScanner.OnResultListener{
    public static final String BARCODE_KEY = "BARCODE";

    private Barcode barcodeResult;

    private TextView result,format;
    private String formatType="",date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_scan);
        result = (TextView) findViewById(R.id.barcodeResult);
        format = (TextView) findViewById(R.id.barcodeResult1);
        date = new SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.getDefault()).format(new Date());

    }
    @Override
    protected void onResume() {
        super.onResume();
        startScan();
    }
    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
//                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
//                    @Override
//                    public void onResult(Barcode barcode) {
//                        barcodeResult = barcode;
//                        result.setText(barcode.rawValue);
//                        decodeFormat(barcode.format);
//                        format.setText(formatType);
//                        Intent intent=new Intent(MaterialScanActivity.this,ActivityScanViewDet.class);
//                        intent.putExtra("scanText",barcode.rawValue);
//                        intent.putExtra("date",date);
//                        intent.putExtra("imgScan","");
//                        intent.putExtra("formatName",formatType);
//                        startActivity(intent);
//                        finish();
//                    }
//                })
                .build();
        materialBarcodeScanner.startScan();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(BARCODE_KEY, barcodeResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
            return;
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }
    @Override
    public void onResult(Barcode barcode)
    {
        if (barcode != null && barcode.rawValue != null) {
            decodeFormat(barcode.format);
            format.setText(formatType);
            Intent intent = new Intent(MaterialScanActivity.this, ActivityScanViewDet.class);
            intent.putExtra("scanText", barcode.rawValue);
            intent.putExtra("date", date);
            intent.putExtra("imgScan", "");
            intent.putExtra("formatName", formatType);
            startActivity(intent);
            finish();
        }else
        {
            Toast.makeText(this, "Scanning error....", Toast.LENGTH_SHORT).show();

        }
    }
}
