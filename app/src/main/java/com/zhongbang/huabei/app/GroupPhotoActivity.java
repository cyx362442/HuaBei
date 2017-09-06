package com.zhongbang.huabei.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.fragment.dialog.GroupImageFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupPhotoActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_group)
    ImageView mImgGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_photo);
        ButterKnife.bind(this);
        mTvTitle.setText("人卡合影");
    }

    @OnClick({R.id.img_return, R.id.rl_group, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.rl_group:
                GroupImageFragment fragment = GroupImageFragment.newInstance();
                fragment.show(getFragmentManager(),getString(R.string.groupimage));
                break;
            case R.id.btn_commit:
                break;
        }
    }
}
