package com.virtuzo.abhishek.Views.Activity.Others;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.modal.MyFunctions;

public class WebPortalActivity extends AppCompatActivity {

    public static final String WEB_PORTAL = "WebPortal";
    public static final String VIRTUZO_IN = "Virtuzo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_portal);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        final TextView title = (TextView) findViewById(R.id.actionBarTitle);
//        title.setText("Loading Page...");

        getSupportActionBar().hide();

        WebView mywebview = (WebView) findViewById(R.id.webView);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.getSettings().setAllowContentAccess(true);
        mywebview.getSettings().setAllowFileAccess(true);
        mywebview.getSettings().setAppCacheEnabled(true);
        mywebview.getSettings().setDomStorageEnabled(true);
        mywebview.getSettings().setBuiltInZoomControls(true);
        mywebview.getSettings().setUseWideViewPort(true);
        mywebview.setVerticalScrollBarEnabled(true);
        mywebview.setWebChromeClient(new WebChromeClient());

        mywebview.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                title.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                title.setVisibility(View.GONE);
//            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        String WebLink = getIntent().getExtras().getString("WebLink");
        String ClientCode = getIntent().getExtras().getString("ClientCode", "");
        String UserName = getIntent().getExtras().getString("UserName", "");
        String Password = getIntent().getExtras().getString("Password", "");
        switch(WebLink) {
            case WEB_PORTAL:
                mywebview.loadUrl("https://pos.gstmadeeasy.net/login.aspx" +
                        "?sd=" + ClientCode +
                        "&un=" + UserName +
                        "&pwd=" + Password);
                break;
            case VIRTUZO_IN:
                mywebview.loadUrl("http://www.virtuzo.in/");
                break;
        }
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

}
