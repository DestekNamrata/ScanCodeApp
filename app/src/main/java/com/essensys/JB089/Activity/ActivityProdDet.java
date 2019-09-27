package com.essensys.JB089.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.essensys.JB089.CustomView.CustomButton;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.R;
public class ActivityProdDet extends AppCompatActivity implements View.OnClickListener {
    private CustomTextView mTxtName,mTxtPrice,mTxtDesc;
    private CustomButton mBtnAmazon,mBtnFlipcart;
    private ImageView mImgProd,mImgBack;
    private LinearLayout mLinearDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_det);
        mTxtName=(CustomTextView)findViewById(R.id.txt_ProdName);
        mTxtPrice=(CustomTextView)findViewById(R.id.txt_PriceProd);
        mTxtDesc=(CustomTextView)findViewById(R.id.txt_descProd);
        mImgBack=(ImageView)findViewById(R.id.img_bckProd);
        mImgProd=(ImageView)findViewById(R.id.img_prodDet);
        mBtnAmazon=(CustomButton)findViewById(R.id.btn_amazone);
        mBtnFlipcart=(CustomButton)findViewById(R.id.btn_flipcart);
        mLinearDesc=(LinearLayout)findViewById(R.id.linear_desc);
        mImgBack.setOnClickListener(this);
        getData();

    }
    //method to get data
    private void getData()
    {
        mTxtName.setText(getIntent().getStringExtra("name"));
        mTxtPrice.setText(getIntent().getStringExtra("price"));
        if(getIntent().getStringExtra("desc").equalsIgnoreCase(""))
        {
           mLinearDesc.setVisibility(View.GONE);
        }
        else
        {
            mLinearDesc.setVisibility(View.VISIBLE);
            mTxtDesc.setText(getIntent().getStringExtra("desc"));

        }
        if(getIntent().getStringExtra("amazoneUrl").equalsIgnoreCase(""))
        {
          mBtnAmazon.setVisibility(View.GONE);
          mBtnFlipcart.setVisibility(View.VISIBLE);
          mBtnFlipcart.setOnClickListener(this);
        }else if(getIntent().getStringExtra("flipcartUrl").equalsIgnoreCase(""))
        {
            mBtnFlipcart.setVisibility(View.GONE);
            mBtnAmazon.setVisibility(View.VISIBLE);
            mBtnAmazon.setOnClickListener(this);
        }else
        {
            mBtnAmazon.setVisibility(View.VISIBLE);
            mBtnFlipcart.setVisibility(View.VISIBLE);
            mBtnAmazon.setOnClickListener(this);
            mBtnFlipcart.setOnClickListener(this);
        }
        Glide.with(this)
                .load(getIntent().getStringExtra("image"))
                .placeholder(R.drawable.ic_vt_logo)
                .into(mImgProd);
    }
    @Override
    public void onClick(View view)
    {
        Intent viewIntent=null;
        switch (view.getId())
        {
            case R.id.btn_amazone:
                viewIntent = new Intent("android.intent.action.VIEW",
                                Uri.parse(getIntent().getStringExtra("amazoneUrl")));
                startActivity(viewIntent);
                break;
            case R.id.btn_flipcart:
                viewIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse(getIntent().getStringExtra("flipcartUrl")));
                startActivity(viewIntent);
                break;
            case R.id.img_bckProd:
                onBackPressed();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,ActivityDashBoard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
