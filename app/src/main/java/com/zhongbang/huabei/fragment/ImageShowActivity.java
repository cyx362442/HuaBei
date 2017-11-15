package com.zhongbang.huabei.fragment;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongbang.huabei.R;

public class ImageShowActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private String[] mImgs;
    private int mIndex;
    private TextView mTvIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        Intent intent = getIntent();
        mImgs = intent.getStringArrayExtra("imgs");
        mIndex = intent.getIntExtra("index", 0);
        mTvIndex = (TextView) findViewById(R.id.tv_index);
        mTvIndex.setText((mIndex+1)+"/"+mImgs.length);
        ViewPager vp = (ViewPager) findViewById(R.id.viewPager);
        MyAdapter adapter = new MyAdapter();
        vp.setAdapter(adapter);
        vp.setCurrentItem(mIndex);
        vp.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        mTvIndex.setText((position+1)+"/"+mImgs.length);
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mImgs.length;
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(ImageShowActivity.this);
            Glide.with(ImageShowActivity.this).load(mImgs[position]).fitCenter().into(imageView);
            container.addView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return imageView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View)object);
        }
    }
}
