package com.zhongbang.huabei.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.main_center.MyBillActivity;
import com.zhongbang.huabei.app.main_center.MyComeinActivity;
import com.zhongbang.huabei.app.main_center.OfficalActivity;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.webview.WebActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragmentCenter extends Fragment {
    Unbinder unbinder;
    private Intent mIntent;
    private String mAccount;
    private String mUrl;

    public MainFragmentCenter() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_main_fragment_center, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(getActivity());
        mAccount = shapreUtis.getAccount();
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_center1, R.id.ll_center2, R.id.ll_center3, R.id.ll_center4, R.id.ll_center5, R.id.ll_center6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_center1:
                mIntent = new Intent(getActivity(), MyComeinActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_center2:
                mIntent=new Intent(getActivity(), MyBillActivity.class);
                startActivity(mIntent);
                break;
            case R.id.ll_center3:
                mUrl = "http://chinaqmf.cn/tg/customer_list.html?id=";
                toWebView(mUrl,"今日分润");
                break;
            case R.id.ll_center4:
                mUrl="http://chinaqmf.cn:8088/ihuabei/app/pay/upgradePage.app?username=";
                toWebView(mUrl,"我要代理");
                break;
            case R.id.ll_center5:
                mUrl="http://chinaqmf.cn/tg/customer.html?id=";
                toWebView(mUrl,"我的团队");
                break;
            case R.id.ll_center6:
                mIntent=new Intent(getActivity(), OfficalActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    private void toWebView(String url,String title) {
        mIntent=new Intent(getActivity(), WebActivity.class);
        mIntent.putExtra("url",url+mAccount);
        mIntent.putExtra(getString(R.string.webtitle),title);
        startActivity(mIntent);
    }
}
