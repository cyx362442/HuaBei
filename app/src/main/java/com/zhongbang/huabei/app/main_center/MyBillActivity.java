package com.zhongbang.huabei.app.main_center;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.fragment.BillFragment;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.view.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class MyBillActivity extends FragmentActivity implements View.OnClickListener {
    private String mPhone;
    private BillFragment billFragment;
    private ViewPager viewpager;
    private RelativeLayout rl_all;
    private RelativeLayout rl_yishouru;
    private RelativeLayout rl_tixianliushui;
    private RelativeLayout rl_tixianjilu;
    private RelativeLayout ralative_tixianjilu;
    private TextView tv_all;
    private TextView tv_yishouru;
    private TextView tv_tixinaliushui;
    private TextView Tv_tixianjilu;
    String otype = "1";
    private List<TextView> tvlist = new ArrayList<TextView>();
    HashMap<String, String> HashMap_post = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bill);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mPhone=shapreUtis.getAccount();
        initUI();

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < 4; i++) {
                    if (i == position) {
                        tvlist.get(i).setVisibility(View.VISIBLE);
                    } else {
                        tvlist.get(i).setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onPageScrolled(int position, float arg1, int arg2) {
            }
            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
        setViewPagerScrollSpeed();
    }

    private void setViewPagerScrollSpeed() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager.getContext());
            mScroller.set(viewpager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    private void initUI() {
        viewpager = (ViewPager) findViewById(R.id.Viewpager_bill);
        viewpager.setAdapter(new MyApater(getSupportFragmentManager()));
        rl_tixianjilu = (RelativeLayout) findViewById(R.id.ralative_tixianjilu);
        rl_all = (RelativeLayout) findViewById(R.id.Relative_all);
        rl_yishouru = (RelativeLayout) findViewById(R.id.Relative_yishouru);
        rl_tixianliushui = (RelativeLayout) findViewById(R.id.ralative_jinxingzhong);
        ralative_tixianjilu = (RelativeLayout) findViewById(R.id.ralative_tixianjilu);
        tv_all = (TextView) findViewById(R.id.Tv_all);
        tv_yishouru = (TextView) findViewById(R.id.Tv_yishouru);
        tv_tixinaliushui = (TextView) findViewById(R.id.Tv_tixinaliushui);
        Tv_tixianjilu = (TextView) findViewById(R.id.Tv_tixianjilu);
        rl_all.setOnClickListener(this);
        rl_yishouru.setOnClickListener(this);
        rl_tixianliushui.setOnClickListener(this);
        rl_tixianjilu.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        tvlist.add(tv_all);
        tvlist.add(tv_yishouru);
        tvlist.add(tv_tixinaliushui);
        tvlist.add(Tv_tixianjilu);
    }
    class MyApater extends FragmentPagerAdapter {
        public MyApater(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            billFragment = new BillFragment();
            // billFragment.setmap(zhangdan_hold);
            billFragment.setposition((position) + "", mPhone);
            return billFragment;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 4;
        }
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.Relative_all:
                for (int i = 0; i < tvlist.size(); i++) {
                    if (i == 0) {
                        tvlist.get(i).setVisibility(View.VISIBLE);
                    } else {
                        tvlist.get(i).setVisibility(View.GONE);
                    }
                }
                viewpager.setCurrentItem(0, true);
                break;
            case R.id.Relative_yishouru:

                for (int i = 0; i < tvlist.size(); i++) {
                    if (i == 1) {
                        tvlist.get(i).setVisibility(View.VISIBLE);
                    } else {
                        tvlist.get(i).setVisibility(View.GONE);
                    }
                }
                viewpager.setCurrentItem(1, true);
                break;
            case R.id.ralative_jinxingzhong:
                for (int i = 0; i < tvlist.size(); i++) {
                    if (i == 3) {
                        tvlist.get(i).setVisibility(View.VISIBLE);
                    } else {
                        tvlist.get(i).setVisibility(View.GONE);
                    }
                }
                viewpager.setCurrentItem(2, true);
                break;
            case R.id.ralative_tixianjilu:
                for (int i = 0; i < tvlist.size(); i++) {
                    if (i == 4) {
                        tvlist.get(i).setVisibility(View.VISIBLE);
                    } else {
                        tvlist.get(i).setVisibility(View.GONE);
                    }
                }
                viewpager.setCurrentItem(3, true);
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
