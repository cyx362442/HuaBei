package com.zhongbang.huabei.fragment;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.YijianfankuiActivity;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;
import com.zhongbang.huabei.webview.WebActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFragment extends Fragment {
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_grade)
    TextView mTvGrade;
    Unbinder unbinder;

    private final String urlUser="http://chinaqmf.cn/app/user_data_return.aspx";
    private final String updateUrl = "http://chinaqmf.cn/app/return_Homepage.aspx";
    private String mAccount;
    private String mName;
    private View mViewLoad;
    private String mWebUrl;

    public ServerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_server, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        mViewLoad = inflate.findViewById(R.id.include);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(getActivity());
        mAccount = shapreUtis.getAccount();
        mName = shapreUtis.getName();
        mTvPhone.setText(mAccount);
        Http_User();
        return inflate;
    }

    private void Http_User() {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", mAccount);
        DownHTTP.postVolley(urlUser, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mViewLoad.setVisibility(View.GONE);
                ToastUtil.showShort(getActivity(),"网络异常");
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String userTypes = jsonObject.getString("user_types");
                    mTvGrade.setText(mName+"    "+userTypes);
                    mViewLoad.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_back, R.id.rl1, R.id.rl2, R.id.rl3, R.id.rl4, R.id.rl5,
            R.id.rl6, R.id.rl7, R.id.rl8, R.id.rl9, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                break;
            case R.id.rl1:
                break;
            case R.id.rl2:
                mWebUrl = "http://chinaqmf.cn:8088/ihuabei/attached/contact.html";
                toWebView(mWebUrl,"企业介绍");
                break;
            case R.id.rl3:
                break;
            case R.id.rl4:
                mWebUrl="http://chinaqmf.cn:8088/ihuabei/attached/right.html";
                toWebView(mWebUrl,"商户协议");
                break;
            case R.id.rl5:
                mWebUrl="http://chinaqmf.cn:8088/ihuabei/attached/prohibition.html";
                toWebView(mWebUrl,"禁售协议");
                break;
            case R.id.rl6:
                mWebUrl="http://chinaqmf.cn:8088/ihuabei/attached/right.html";
                toWebView(mWebUrl,"注册使用权");
                break;
            case R.id.rl7:
                Intent intent = new Intent(getActivity(), YijianfankuiActivity.class);
                startActivity(intent);
                break;
            case R.id.rl8:
                mViewLoad.setVisibility(View.VISIBLE);
                HashMap<String, String> map = new HashMap<>();
                DownHTTP.postVolley(updateUrl, map, new VolleyResultListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mViewLoad.setVisibility(View.GONE);
                        ToastUtil.showShort(getActivity(),"网络异常");
                    }
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String systemVersion = jsonObject.getString("system_version");
                            String authorContact = jsonObject.getString("author_contact");
                            if(Integer.parseInt(systemVersion)>getAppVersionName()){
                                UpdateFragment fragment = UpdateFragment.newInstance(authorContact);
                                fragment.show(getActivity().getSupportFragmentManager(), getString(R.string.update));
                            }
                            mViewLoad.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.rl9:
                break;
            case R.id.btn_exit:
                getActivity().finish();
                break;
        }
    }

    private void toWebView(String webUrl,String title) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url",webUrl);
        intent.putExtra(getString(R.string.webtitle),title);
        startActivity(intent);
    }

    private int getAppVersionName() {
        int versioncode = 1;
        try {
            // ---get the package info---
            PackageManager pm = getActivity().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
        }
        return versioncode;
    }
}
