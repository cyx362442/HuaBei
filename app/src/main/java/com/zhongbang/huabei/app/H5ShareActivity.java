package com.zhongbang.huabei.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.utils.ShapreUtis;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class H5ShareActivity extends AppCompatActivity {
    @BindView(R.id.webview)
    WebView mWebview;

    private final String url = "http://chinaqmf.cn/saoyisao/index.html?user=";
    private String mUrlShare;
    @BindView(R.id.img_back)
    ImageView mImgBack;
    @BindView(R.id.tv_share)
    TextView mTvShare;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.pb)
    ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_share);
        ButterKnife.bind(this);
        String account = ShapreUtis.getInstance(this).getAccount();
        mUrlShare = "http://chinaqmf.cn:8088/IHBZC/zhuce/registered.html?id="+account;
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提
        settings.setBuiltInZoomControls(true);
        mWebview.setWebViewClient(new MyWebViewClient());
        mWebview.loadUrl(url + account);
    }

    @OnClick({R.id.img_back, R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"爱花呗-信用卡花呗刷卡提现秒到账");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "爱花呗-信用卡花呗刷卡提现秒到账费率只需0.28%，推荐朋友使用还能赚刷卡分润推荐佣金，分润秒结。\n"+mUrlShare);
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                break;
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            mPb.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }
}
