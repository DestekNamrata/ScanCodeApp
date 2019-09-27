package com.essensys.JB089.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.essensys.JB089.Adapter.ViewPagerAdapter;
import com.essensys.JB089.CustomView.CustomButton;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.R;

import java.util.Timer;
public class ActivityViewPager extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener{
    private static final int NUM_PAGES =3 ;
    protected View view;
//    private ImageButton btnNext, btnFinish;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    private int currentPage = 0;
    private Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private LinearLayout mLinearSkip;
    private CustomTextView mBtnSkip,mBtnStarted;


    private int[] mImageResources = {
            R.drawable.vt_login,
            R.drawable.vt_scan,
            R.drawable.vt_history,


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
//        setReference();
        intro_images = (ViewPager)findViewById(R.id.pager_introduction);
        mLinearSkip=(LinearLayout)findViewById(R.id.linear_skip);
        mBtnSkip=(CustomTextView)findViewById(R.id.btn_skip);
        mBtnStarted=(CustomTextView)findViewById(R.id.btn_getStarted);
        mBtnStarted.setOnClickListener(this);
        mBtnSkip.setOnClickListener(this);
//        btnNext = (ImageButton)findViewById(R.id.btn_next);
//        btnFinish = (ImageButton)findViewById(R.id.btn_finish);

        pager_indicator = (LinearLayout)findViewById(R.id.viewPagerCountDots);

//        btnNext.setOnClickListener(this);
//        btnFinish.setOnClickListener(this);

        mAdapter = new ViewPagerAdapter(ActivityViewPager.this, mImageResources);
        intro_images.setAdapter(mAdapter);

//        /*After setting the adapter use the timer */
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == NUM_PAGES-1) {
//                    currentPage = 0;
//                }
//
//                intro_images.setCurrentItem(currentPage++, true);
//            }
//        };
//
//        timer = new Timer(); // This will create a new Thread
//        timer.schedule(new TimerTask() { // task to be scheduled
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, DELAY_MS, PERIOD_MS);
        intro_images.setCurrentItem(0);

        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();

    }
    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 8, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()) {
//            case R.id.btn_next:
////                intro_images.setCurrentItem((intro_images.getCurrentItem() < dotsCount)
////                        ? intro_images.getCurrentItem() + 1 : 0);
//                break;
//
//            case R.id.btn_finish:
//                finish();
//                break;
            case R.id.btn_skip:
                intent=new Intent(this,ActivityLogin.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_getStarted:
                intent=new Intent(this,ActivityLogin.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

//        if (position + 1 == dotsCount) {
//            btnNext.setVisibility(View.GONE);
//            btnFinish.setVisibility(View.VISIBLE);
//        } else {
//            btnNext.setVisibility(View.VISIBLE);
//            btnFinish.setVisibility(View.GONE);
//        }
        if (position == 2) {
            mBtnSkip.setVisibility(View.GONE);
            mBtnStarted.setVisibility(View.VISIBLE);
        } else {
            mBtnSkip.setVisibility(View.VISIBLE);
            mBtnStarted.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Toast.makeText(this,"clicked",Toast.LENGTH_LONG).show();

    }
}
