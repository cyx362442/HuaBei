package com.zhongbang.huabei.webview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhongbang.huabei.R;

public class WebActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWv;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String registerAgreement = intent.getStringExtra(getString(R.string.registerAgreement));
        String serverAgreement = intent.getStringExtra(getString(R.string.serverAgreement));
        String webTitle = intent.getStringExtra(getString(R.string.webtitle));

        mWv = (WebView) findViewById(R.id.webview);
        mPb = (ProgressBar) findViewById(R.id.progress);
        mPb.setVisibility(View.VISIBLE);
        findViewById(R.id.img_return).setOnClickListener(this);

        WebSettings settings = mWv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提
        settings.setBuiltInZoomControls(true);
        mWv.setWebViewClient(new MyWebViewClient());
        mWv.loadUrl(url);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        if(!TextUtils.isEmpty(registerAgreement)){
            tvTitle.setText(registerAgreement);
        }else if(!TextUtils.isEmpty(serverAgreement)){
            tvTitle.setText(serverAgreement);
        }else if(!TextUtils.isEmpty(webTitle)){
            tvTitle.setText(webTitle);
        }
    }

    class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            mPb.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWv.canGoBack()) {
            mWv.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if(mWv.canGoBack()){
            mWv.goBack();
        }else{
            finish();
        }
    }
}
