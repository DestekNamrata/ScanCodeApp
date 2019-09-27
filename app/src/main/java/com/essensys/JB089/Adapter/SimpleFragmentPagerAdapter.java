package com.essensys.JB089.Adapter;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.essensys.JB089.Fragment.FragmentMobNo;
import com.essensys.JB089.Fragment.FragmentEmail;
import com.essensys.JB089.R;
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private String flg;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm,String flg) {
        super(fm);
        mContext = context;
        this.flg=flg;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0) {
            Bundle bundle=new Bundle();
            bundle.putString("flg",flg);
            fragment=new FragmentMobNo();
            fragment.setArguments(bundle);

            return fragment;

        } else {
            Bundle bundle=new Bundle();
            bundle.putString("flg",flg);
            fragment=new FragmentEmail();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.MobNo);

            case 1:
                return mContext.getString(R.string.Email);

            default:
                return null;
        }
    }

}
