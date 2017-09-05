package com.zhongbang.huabei.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.App;
import com.zhongbang.huabei.service.LocationService;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    private TextView mTvUser;

    public MapFragment() {
        // Required empty public constructor
    }

    private LocationService locationService;
    private TextView LocationResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_map, container, false);
        mTvUser = (TextView) inflate.findViewById(R.id.tv_user);
        LocationResult = (TextView) inflate.findViewById(R.id.tvMap);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        return inflate;
    }

    public void setName(String name){
        mTvUser.setText(name);
    }

    @Override
    public void onStart() {
        super.onStart();
        // -----------location config ------------
        locationService = ((App)getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getActivity().getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.start();// 定位SDK
    }

    @Override
    public void onStop() {
        super.onStop();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
//                sb.append(location.getCity());
                sb.append(location.getAddrStr());
                logMsg(sb.toString());
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };

    public void logMsg(String str) {
        str=str.substring(2,str.indexOf("市"))+"市";
        final String s = str;
        try {
            if (LocationResult != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LocationResult.post(new Runnable() {
                            @Override
                            public void run() {
                                LocationResult.setText(s);
                            }
                        });

                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
