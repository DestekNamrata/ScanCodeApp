package com.essensys.JB089.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.R;

public class FragmentAboutUs extends Fragment {
    private View view;
    private ProgressBar mProgress;
    private WebView mWebAbout;
    private String url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_about_us,container,false);
        mProgress=(ProgressBar)view.findViewById(R.id.progressBarAbt);
        mWebAbout=(WebView)view.findViewById(R.id.webAboutUs);
        
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((CustomTextView)getActivity().findViewById(R.id.txt_title)).setText("About Us");
        ((ImageView)getActivity().findViewById(R.id.img_edit)).setVisibility(View.GONE);

        GetWebView();


    }
    //method to get mWebAbout
    private void GetWebView() 
    {
        mWebAbout.getSettings().setJavaScriptEnabled(true);
        mWebAbout.getSettings().setDomStorageEnabled(true);
        mWebAbout.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mWebAbout.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(),"page started",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mWebAbout.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
//                Toast.makeText(getActivity(),"page finished",Toast.LENGTH_LONG).show();

            }

        });

        mWebAbout.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (progress < 100 && mProgress.getVisibility() == View.GONE) {
                    mProgress.setVisibility(View.VISIBLE);
                }
                mProgress.setProgress(progress);
                if (progress == 100) {
                    mProgress.setVisibility(View.GONE);
                }
            }

        });
        url = getString(R.string.ws_web_about);
        if (!TextUtils.isEmpty(url)) {
            String newUA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36";
            mWebAbout.getSettings().setUserAgentString(newUA);
            mWebAbout.loadUrl(url);
        }
    }
}
