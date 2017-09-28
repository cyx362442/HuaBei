package com.zhongbang.huabei.fragment;

import android.content.Intent;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.adapter.MyDecoration;
import com.zhongbang.huabei.adapter.NotifactionAdapter;
import com.zhongbang.huabei.bean.NotifMessage;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ToastUtil;
import com.zhongbang.huabei.webview.WebActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements BaseQuickAdapter.OnRecyclerViewItemClickListener {

    private final String url = "http://chinaqmf.cn:8088/ihuabei/app/system/announcements.app";
    private final String urlNotification="http://chinaqmf.cn:8088/ihuabei/attached/announcement/";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.recycleView)
    RecyclerView mRecycleView;
    Unbinder unbinder;

    private List<NotifMessage> mList = new ArrayList<>();
    private NotifactionAdapter mAdapter;
    private View mViewLoad;

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
        mViewLoad = inflate.findViewById(R.id.include);

        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NotifactionAdapter(mList);
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new MyDecoration(getActivity(), MyDecoration.VERTICAL_LIST));
        mAdapter.setOnRecyclerViewItemClickListener(this);
        Http_Msg();
        return inflate;
    }

    private void Http_Msg() {
        DownHTTP.getVolley(url, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mViewLoad.setVisibility(View.GONE);
                ToastUtil.showShort(getActivity(),"网络异常");
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<NotifMessage>>() {
                }.getType();
                mList = gson.fromJson(response, type);
                mAdapter.setNewData(mList);
                mViewLoad.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(View view, int i) {
        NotifMessage notifMessage = mList.get(i);
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url",urlNotification+notifMessage.getHtml());
        intent.putExtra(getString(R.string.webtitle),notifMessage.getTitle());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
