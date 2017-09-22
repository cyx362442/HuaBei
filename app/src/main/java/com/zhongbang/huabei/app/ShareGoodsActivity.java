package com.zhongbang.huabei.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zhongbang.huabei.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareGoodsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_goods);
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
                break;
            case R.id.rl2:
                break;
            case R.id.rl3:
                break;
            case R.id.rl4:
                break;
            case R.id.rl5:
                break;
        }
    }
}
