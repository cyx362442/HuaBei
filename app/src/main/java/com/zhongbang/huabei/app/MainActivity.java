package com.zhongbang.huabei.app;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.fragment.NotificationFragment;
import com.zhongbang.huabei.fragment.RenZhengFragment;
import com.zhongbang.huabei.fragment.ServerFragment;
import com.zhongbang.huabei.utils.ToastUtil;

public class MainActivity extends AppCompatActivity{
    private Class[]fragment={RenZhengFragment.class,
            NotificationFragment.class,
            ServerFragment.class};
    private String[]text={"实名认证","通知","客服"};
    private int[]imgRes={R.drawable.selector_renzheng,
    R.drawable.selector_notification,
    R.drawable.selector_server};
    private FragmentTabHost mTabHost;
    private boolean isClick=false;
    private Handler mHandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realContent);
        for (int i = 0; i < fragment.length; i++) {
            View inflate = getLayoutInflater().inflate(R.layout.tab_item, null);
            TextView tv = (TextView) inflate.findViewById(R.id.tabText);
            ImageView image = (ImageView) inflate.findViewById(R.id.tabImageView);
            tv.setText(text[i]);
            image.setImageResource(imgRes[i]);
            mTabHost.addTab(mTabHost.newTabSpec(text[i]).setIndicator(inflate), fragment[i], null);
        }
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            final int index=i;
            mTabHost.getTabWidget().getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentTab = mTabHost.getCurrentTab();
                    if(currentTab==0){
                        if(isClick==true){
                            Intent intent = new Intent(MainActivity.this, RenZhengActivity.class);
                            startActivity(intent);
                            isClick=false;

                        }else{
                            mTabHost.setCurrentTab(index);
                            isClick=true;
                        }
                    }else{
                        mTabHost.setCurrentTab(index);
                        isClick=false;
                    }
                }
            });
        }
    }

    int count=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            if(count<=0){
                ToastUtil.showShort(this,"再次点击将退出应用");
                count++;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count=0;
                    }
                },2000);
            }else{
                finish();
            }
        }
        return false;
    }
}
