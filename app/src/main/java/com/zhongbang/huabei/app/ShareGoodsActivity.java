package com.zhongbang.huabei.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhongbang.huabei.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareGoodsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_goods);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
        mTvTitle.setText("分享有礼");
    }

    @OnClick({R.id.img_return, R.id.rl1, R.id.rl2, R.id.rl3, R.id.rl4, R.id.rl5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.rl1:
                mIntent = new Intent(this, ShareZxingActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rl2:
                mIntent=new Intent(this,ShareManageActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rl3:
                mIntent=new Intent(this,LocalOpenActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rl4:
                break;
            case R.id.rl5:
                mIntent=new Intent(this,H5ShareActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}
