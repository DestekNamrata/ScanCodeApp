package com.essensys.JB089;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.JB089.Activity.ActivityScan;
public class MainActivity extends AppCompatActivity {

    private TextView tvScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvScan = findViewById(R.id.tvScan);
        //tvScan.setText("Loading...");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvScan.setText("");
                scanCode();
            }
        });
        //ImageView ivSwipeImageView = findViewById(R.id.ivSwipeImageView);

    }

    private void scanCode() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, ActivityScan.class);
            startActivityForResult(intent, 101);
        } else {
            String[] perms = {"android.permission.CAMERA"};
            ActivityCompat.requestPermissions(this,perms, 102);
            Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 101) {
            if (data != null && data.hasExtra("scan")) {
                  Toast.makeText(this, data.getStringExtra("scan"), Toast.LENGTH_LONG).show();
                String strScan = data.getStringExtra("scan");
                tvScan.setText(strScan);
                //if (strScan != null && !strScan.equalsIgnoreCase("")) {
                //}
            }

        }
    }
}
