package com.zhongbang.huabei.app.main_center;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.adapter.DocumentAdapter;
import com.zhongbang.huabei.bean.Document;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfficalActivity extends AppCompatActivity {
    private final String marketingUrl = "http://chinaqmf.cn:8088/ihuabei/app/spread/spread.app?pageNo=1&type=marketing";
    private final String materialUrl="http://chinaqmf.cn:8088/ihuabei/app/spread/spread.app?pageNo=1&type=material";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.view1)
    View mView1;
    @BindView(R.id.view2)
    View mView2;
    @BindView(R.id.recycleView)
    RecyclerView mRecycleView;

    private List<Document> mDocumentList;
    private DocumentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offical);
        ButterKnife.bind(this);
        mDocumentList=new ArrayList<>();

        mTvTitle.setText("中央方案");
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.addItemDecoration(new DividerItemDecoration(this,1));
        mAdapter = new DocumentAdapter(mDocumentList);
        mRecycleView.setAdapter(mAdapter);

        Http_Data(marketingUrl);
    }

    private void Http_Data(String url) {
        DownHTTP.getVolley(url, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                mDocumentList.clear();
                Gson gson = new Gson();
                Type type = new TypeToken<List<Document>>() {
                }.getType();
                List<Document> list = gson.fromJson(response, type);
                mDocumentList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.img_return, R.id.tv1, R.id.tv2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv1:
                Http_Data(marketingUrl);
                mTv1.setTextColor(getResources().getColor(R.color.blue_low));
                mTv2.setTextColor(Color.GRAY);
                mView1.setVisibility(View.VISIBLE);
                mView2.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv2:
                Http_Data(materialUrl);
                mTv1.setTextColor(Color.GRAY);
                mTv2.setTextColor(getResources().getColor(R.color.blue_low));
                mView2.setVisibility(View.VISIBLE);
                mView1.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
