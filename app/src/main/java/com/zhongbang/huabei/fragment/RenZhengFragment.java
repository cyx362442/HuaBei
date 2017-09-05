package com.zhongbang.huabei.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.bean.User;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ShapreUtis;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class RenZhengFragment extends Fragment {

    public static String AUDIT="";

    private MapFragment mMapFragment;
    private ShapreUtis mShapreUtis;
    private MainFragmentTop mMainFragmentTop;
    private MainFragmentCenter mMainFragmentCenter;
    private MainFragmentBottom mMainFragmentBottom;

    public RenZhengFragment() {
        // Required empty public constructor
    }

    private final String urlUser="http://chinaqmf.cn/app/user_data_return.aspx";
    private HashMap<String,String>map=new HashMap<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_ren_zheng, container, false);
        mShapreUtis = ShapreUtis.getInstance(getActivity());
        initFragment();

        Http_user();
        return inflate;
    }

    private void initFragment() {
        BannerFragment bannerFragment = new BannerFragment();
        mMapFragment = new MapFragment();
        mMainFragmentTop = new MainFragmentTop();
        mMainFragmentCenter = new MainFragmentCenter();
        mMainFragmentBottom = new MainFragmentBottom();
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_banner,bannerFragment).commit();
        fm.beginTransaction().replace(R.id.fragment_map, mMapFragment).commit();
        fm.beginTransaction().replace(R.id.fragment_top,mMainFragmentTop).commit();
        fm.beginTransaction().replace(R.id.fragment_center,mMainFragmentCenter).commit();
        fm.beginTransaction().replace(R.id.fragment_bottom,mMainFragmentBottom).commit();
    }

    private void Http_user() {
        String account = mShapreUtis.getAccount();
        map.put("username",account);
        DownHTTP.postVolley(urlUser, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                User[] users = gson.fromJson(response, User[].class);
                User user = users[0];
                AUDIT=user.getAudit();
                mMapFragment.setName(user.getName());
            }
        });
    }
}
