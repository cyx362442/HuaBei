package com.zhongbang.huabei.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.utils.ShapreUtis;

import java.io.File;

public class ShareManageActivity extends AppCompatActivity implements View.OnClickListener {

    private String mUrlShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_manage);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        String account = shapreUtis.getAccount();
        mUrlShare = "http://chinaqmf.cn:8088/IHBZC/zhuce/registered.html?id="+account;

        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("分享经营");
        findViewById(R.id.img_return).setOnClickListener(this);
        findViewById(R.id.btn_shape).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;
            case R.id.btn_shape:
                String strDir = Environment.getExternalStorageDirectory()+"/zhongbang/";
                //由文件得到uri
                Uri imageUri = Uri.fromFile(new File(strDir,"huabei.jpg"));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"爱花呗-信用卡花呗刷卡提现秒到账");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "爱花呗-信用卡花呗刷卡提现秒到账费率只需0.28%，推荐朋友使用还能赚刷卡分润推荐佣金，分润秒结。\n"+mUrlShare);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                shareIntent.setType("image/*");
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                break;
        }
    }
}
