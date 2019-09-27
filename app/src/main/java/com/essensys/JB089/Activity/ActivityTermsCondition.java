package com.essensys.JB089.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.essensys.JB089.R;
public class ActivityTermsCondition extends AppCompatActivity {
    private WebView webview;
    private ProgressBar pBar;
    private ImageView mImgback;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        webview = (WebView) findViewById(R.id.webviewone);
        pBar = (ProgressBar) findViewById(R.id.progressBarWebView);
        mImgback=(ImageView)findViewById(R.id.img_bckTerms);
        mImgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webview.setVisibility(View.GONE);
                pBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webview.setVisibility(View.VISIBLE);
                pBar.setVisibility(View.GONE);
            }

        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (progress < 100 && pBar.getVisibility() == View.GONE) {
                    pBar.setVisibility(View.VISIBLE);
                }
                pBar.setProgress(progress);
                if (progress == 100) {
                    pBar.setVisibility(View.GONE);
                }
            }

        });
        url = getString(R.string.ws_web_terms);
        if (!TextUtils.isEmpty(url)) {
            String newUA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36";
            webview.getSettings().setUserAgentString(newUA);
            webview.loadUrl(url);
        }
    }

}

