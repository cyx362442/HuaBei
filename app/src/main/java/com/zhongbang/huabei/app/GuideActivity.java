package com.zhongbang.huabei.app;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.utils.ShapreUtis;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    private List<View> listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        boolean land = ShapreUtis.getInstance(this).getLand();
//        if(land){
//            toLand();
//            return;
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        listView=new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(this);
        View inflate1 = inflater.inflate(R.layout.guide_item1, null);
        View inflate2 = inflater.inflate(R.layout.guide_item2, null);
        View inflate3 = inflater.inflate(R.layout.guide_item3, null);
        listView.add(inflate1);
        listView.add(inflate2);
        listView.add(inflate3);
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        MyPagerAdapter adapter = new MyPagerAdapter();
        vp.setAdapter(adapter);

        inflate3.findViewById(R.id.image).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ShapreUtis.getInstance(GuideActivity.this).isLand(true);
        toLand();
    }

    class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return listView.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(listView.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(listView.get(position));
            return listView.get(position);
        }
    }

    private void toLand() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
