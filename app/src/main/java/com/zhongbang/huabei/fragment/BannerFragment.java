package com.zhongbang.huabei.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.utils.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerFragment extends Fragment {
    private final String imgUrl1="http://chinaqmf.cn:8088/ihuabei/attached/rotation/1.jpg";
    private final String imgUrl2="http://chinaqmf.cn:8088/ihuabei/attached/rotation/2.jpg";
    private final String imgUrl3="http://chinaqmf.cn:8088/ihuabei/attached/rotation/3.jpg";
    private List<String> listUrl=new ArrayList<>();
    public BannerFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_banner, container, false);
        listUrl.add(imgUrl1);
        listUrl.add(imgUrl2);
        listUrl.add(imgUrl3);

        Banner banner = (Banner) inflate.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(listUrl);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        return inflate;
    }
}
