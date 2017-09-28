package com.zhongbang.huabei.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.adapter.MyDecoration;
import com.zhongbang.huabei.adapter.NotifactionAdapter;
import com.zhongbang.huabei.bean.NotifMessage;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private final String url = "http://chinaqmf.cn:8088/ihuabei/app/system/announcements.app";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.recycleView)
    RecyclerView mRecycleView;
    Unbinder unbinder;
    @BindView(R.id.rl_load)
    RelativeLayout mRlLoad;

    private List<NotifMessage> mList = new ArrayList<>();
    private NotifactionAdapter mAdapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        mTvTitle.setText("消息中心");

        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NotifactionAdapter(mList);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));

        Http_Msg();
        return inflate;
    }

    private void Http_Msg() {
        DownHTTP.getVolley(url, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRlLoad.setVisibility(View.GONE);
                ToastUtil.showShort(getActivity(),"网络异常");
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<NotifMessage>>() {
                }.getType();
                List<NotifMessage> list = gson.fromJson(response, type);
                mAdapter.setNewData(list);
                mRlLoad.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
