package com.essensys.JB089.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.essensys.JB089.Adapter.NavigationDrawerAdapter;
import com.essensys.JB089.CustomView.CircularImageView;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.DataClass.DataNavigation;
import com.essensys.JB089.Fragment.FragmentAboutUs;
import com.essensys.JB089.Fragment.FragmentContactUs;
import com.essensys.JB089.Fragment.FragmentHome;
import com.essensys.JB089.Fragment.FragmentScanHistory;
import com.essensys.JB089.Fragment.FragmentSettings;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;

import java.util.ArrayList;
public class ActivityDashBoard extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

private DrawerLayout mDrawerLayout;
    private ListView mListViewNav;
    private CircularImageView mProfile_image;
    private ImageButton mImgNavIcon;
    private ImageView mImgEdit;
    private CustomTextView mTxtViewTitle,mTxt_profile_name,mTxt_profile_name1,mTxtVersion;
    private ArrayList<DataNavigation> mNavDrawerList;
    private LinearLayout mlinearLayoutDrawer;
    private int count=0;


    private int[] ic_customer = {
            R.drawable.ic_home,//0
            R.drawable.ic_prod,//1 order now
            R.drawable.ic_contact,//2 order later
            R.drawable.ic_about,//3
            R.drawable.ic_setting,//4
            R.drawable.ic_logout,//5

    };


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//to view full screen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        setUI();
    }

    /**
     * Initialization of widgets
     **/
    private void setUI() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mListViewNav = (ListView) findViewById(R.id.lv_drawer);
        mListViewNav.setOnItemClickListener(this);
        mNavDrawerList = new ArrayList<>();
        mImgNavIcon = (ImageButton) findViewById(R.id.img_nav_icon);
        mImgNavIcon.setOnClickListener(this);
        mlinearLayoutDrawer = (LinearLayout) findViewById(R.id.layout_sidemenu);
        mTxtViewTitle = (CustomTextView) findViewById(R.id.txt_title);
        mTxt_profile_name = (CustomTextView) findViewById(R.id.txt_profile_name);
        mTxtVersion=(CustomTextView)findViewById(R.id.txt_verdash);
        mImgEdit=(ImageView)findViewById(R.id.img_edit);
        getVersionNo();
        setMenu();
//           FragmentScanHistory.scanFlag="0";
//            if(getIntent().hasExtra("flagScan")) //for returning back from scanView to scan history fragment
//            {
//                FragmentScanHistory.scanFlag="0";
//                Fragment fm = new FragmentScanHistory();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, fm).addToBackStack(null).commit();
////                finish();
//            }else {
                Fragment fm = new FragmentHome();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fm).commit();

//            }

    }
    //method for version name
    private void getVersionNo() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            mTxtVersion.setText("Version: "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
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
            }
        });
        aBuilder.create();
        aBuilder.show();
    }

    @Override
    protected void onResume()
    {
        String mob,email;
        super.onResume();
        if(SessionClass.getUserFlag(ActivityDashBoard.this).equalsIgnoreCase("MOBILE_LOGIN")||SessionClass.getUserFlag(ActivityDashBoard.this).equalsIgnoreCase("MOBILE"))
        {
            mob=SessionClass.getUserMob(ActivityDashBoard.this);
            mTxt_profile_name.setText(mob);

        }else
        {
            email=SessionClass.getUserEmail(this);
            mTxt_profile_name.setText(email);
        }
    }

    //to set navigation drawer items
    private void setMenu() {
        //Log.e("", "user_type :" + SessionClass.getUserType(MainDashBoardActivity.this));
        String[] arr_menu = null;
        int[] arr_icons = null;
        if(!SessionClass.getUserId(this).equalsIgnoreCase("")) {
            arr_menu = getResources().getStringArray(R.array.str_array_customer);
            arr_icons = ic_customer;
        }

        mNavDrawerList.clear();
        for (int i = 0; arr_menu != null && i < arr_menu.length; i++) {
            DataNavigation item = new DataNavigation();
            item.setMstrNavTitle(arr_menu[i]);
            item.setmStrNavIcon(arr_icons[i]);
            mNavDrawerList.add(item);
        }
        mListViewNav.setAdapter(new NavigationDrawerAdapter(ActivityDashBoard.this, mNavDrawerList));
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT))
            mDrawerLayout.closeDrawers();
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                showConfirmationDialog();
            } else {
                FragmentHome.offset="0";
//                if(getIntent().hasExtra("flagScan"))
//                {
//                    Fragment fm = new FragmentHome();
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, fm).addToBackStack(null).commit();
//                    finish();
//
//                }
                super.onBackPressed();



                }
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(ActivityDashBoard.this);
        aBuilder.setCancelable(false);
        aBuilder.setMessage("Do you want to exit ?");
        aBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        aBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        aBuilder.create();
        aBuilder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_nav_icon:
//                if (!SessionClass.getUserId(MainDashBoardActivity.this).equalsIgnoreCase("")) {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                        mDrawerLayout.requestDisallowInterceptTouchEvent(true);
                        mDrawerLayout.closeDrawers();
                    } else {
//                        mDrawerLayout.requestDisallowInterceptTouchEvent(true);
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
//                } else {
//                    Intent intent = new Intent(ActivityDashBoard.this, ActivityLogin.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
//                }
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        displayFragment(position);
    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mTxtViewTitle.setVisibility(View.VISIBLE);
        switch (position) {
            case 0:
                fragment=new FragmentHome();
                break;
            case 1:
                FragmentScanHistory.scanFlag="0";
//                fragment=new FragmentScanHistory();
                Intent intent=new Intent(this,ActivityScanHistory.class);
                startActivity(intent);
                break;
            case 2:
                fragment=new FragmentContactUs();
                break;
            case 3:
                fragment=new FragmentAboutUs();
                break;
            case 4:
                fragment=new FragmentSettings();
                break;
            case 5:
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(ActivityDashBoard.this);
                aBuilder.setCancelable(false);
                aBuilder.setMessage("Do you want to Logout?");
                aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //stored in session,so get it from session
                        SessionClass.logout(ActivityDashBoard.this);
                        Intent intent = new Intent(ActivityDashBoard.this, ActivityLogin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
                        startActivity(intent);
                        finish();
                    }
                });
                aBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                aBuilder.create();
                aBuilder.show();

                break;
            case 6:
                break;

                default:
                Toast.makeText(getApplicationContext(), "Functionality under development.", Toast.LENGTH_SHORT).show();
                break;
        }
        if (fragment != null) {
            mDrawerLayout.closeDrawers(); //mDrawerLayout.closeDrawers(Gravity.left) is also given,but sometimes crashes,so used as follows
            ft.replace(R.id.content_frame, fragment).addToBackStack("").commit();
            getFragmentManager().getBackStackEntryCount();
        }
    }

//    //to set the side menu panel according to upgrade and downgrade
//    public MainDashBoardInterface mainDashBoardInterface = new MainDashBoardInterface() {
//        @Override
//        public void setIcon(int icon) {
//
//        }
//
//        @Override
//        public void setTitle(String mStrTitle) {
//        }
//
//        @Override
//        public void setTitleHome() {
//        }
//
//        @Override
//        public void setListener() {
//        }
//
//        @Override
//        public void setProfileImageToTheSidePanel() {
//        }
//
//        @Override
//        public void setMenu() {
//            MainDashBoardActivity.this.setMenu();
//        }
//    };


}
