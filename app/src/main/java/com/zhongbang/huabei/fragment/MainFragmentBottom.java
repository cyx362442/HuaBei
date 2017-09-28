package com.zhongbang.huabei.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.webview.WebActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentBottom extends Fragment {
    Unbinder unbinder;
    private String mUrl;

    public MainFragmentBottom() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main_fragment_bottom, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_bottom1, R.id.ll_bottom2, R.id.ll_bottom3, R.id.ll_bottom4, R.id.ll_bottom5, R.id.ll_bottom6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_bottom1:
                mUrl = "https://test.doraemoney.com/wkCube/testPage.html";
                toWebView(mUrl,"信用卡办理");
                break;
            case R.id.ll_bottom2:
                mUrl="https://test.doraemoney.com/wkCube/testPage.html";
                toWebView(mUrl,"无抵押贷款");
                break;
            case R.id.ll_bottom3:
                mUrl="http://chinaqmf.cn:8088/ihuabei/attached/credit-card.html";
                toWebView(mUrl,"信用卡提额");
                break;
            case R.id.ll_bottom4:
                mUrl="http://chinaqmf.cn:8088/ihuabei/attached/huabei.html";
                toWebView(mUrl,"花呗、借呗提额");
                break;
            case R.id.ll_bottom5:
                mUrl="http://chinaqmf.cn:8088/ihuabei/attached/branch.html";
                toWebView(mUrl,"分公司申请");
                break;
            case R.id.ll_bottom6:
                mUrl="http://chinaqmf.cn:8088/ihuabei/attached/oem.html";
                toWebView(mUrl,"渠道贴牌合作");
                break;
        }
    }

    private void toWebView(String url,String title) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url",url);
        intent.putExtra(getString(R.string.webtitle),title);
        startActivity(intent);
    }
}
