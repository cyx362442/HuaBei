package com.zhongbang.huabei.app;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.fragment.NotificationFragment;
import com.zhongbang.huabei.fragment.RenZhengFragment;
import com.zhongbang.huabei.fragment.ServerFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
