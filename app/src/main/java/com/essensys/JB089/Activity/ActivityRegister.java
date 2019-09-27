package com.essensys.JB089.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.essensys.JB089.Adapter.SimpleFragmentPagerAdapter;
import com.essensys.JB089.R;
public class ActivityRegister extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private SimpleFragmentPagerAdapter adapter;
    private TabLayout tabLayout;
    private ImageView mImgBckReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewPager = (ViewPager) findViewById(R.id.viewpagerReg);
        tabLayout= (TabLayout) findViewById(R.id.sliding_tabsReg);
        mImgBckReg=(ImageView)findViewById(R.id.imgbckRegister);
        mImgBckReg.setOnClickListener(this);
        adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager(),"1");
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgbckRegister:
                onBackPressed();
                break;
        }
    }
}
