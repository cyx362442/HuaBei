package com.zhongbang.huabei.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.google.gson.Gson;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.bean.CommitUser;
import com.zhongbang.huabei.event.FinishEvent;
import com.zhongbang.huabei.event.ID_Front;
import com.zhongbang.huabei.event.StartAnim;
import com.zhongbang.huabei.fragment.IdCardFragment;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.UploadUtil;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.region.RegionSelectActivity;
import com.zhongbang.huabei.utils.DBCopyUtil;
import com.zhongbang.huabei.utils.MD5Utils;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;
import com.zhongbang.huabei.yunmai.CameraManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenZhengActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_idcardfront)
    EditText mEtIdcardfront;
    @BindView(R.id.et_idcardreverse)
    EditText mEtIdcardreverse;
    @BindView(R.id.et_merchant)
    EditText mEtMerchant;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_city)
    TextView mEtCity;
    @BindView(R.id.rl_load)
    RelativeLayout mRlLoad;

    private static final int REGION_REQUEST_CODE = 001;
    @BindView(R.id.msg)
    TextView mMsg;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    private String mProvince;
    private String mCity;
    private String mArea;

    private final String urlUsers = "http://chinaqmf.cn:8088/ihuabei/app/user/userInfo.app";
    private final String urlCommit = " http://chinaqmf.cn:8088/ihuabei/app/user/submitUserData.app";
    private String mAccount;
    private HashMap<String, String> mMap;
    private IdCardFragment mIdCardFragment;
    private String mAudit;
    public static boolean hasGotToken = false;
    private Handler mHandler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_zheng);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        mMap = new HashMap<>();
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAudit = shapreUtis.getAudit();
        String name = shapreUtis.getName();
        mAccount = shapreUtis.getAccount();
        mTvTitle.setText("实名认证 ");
        initFragment();
        if (!getString(R.string.unaudit).equals(mAudit)) {
            Http_User(name);
        }
        if(getString(R.string.audited).equals(mAudit)){
            mMsg.setText(getString(R.string.auditMsg));
            mBtnNext.setText("继续查看");
        }

        initAccessTokenWithAkSk();
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(final OCRError error) {
                error.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RenZhengActivity.this,
                                "AK，SK方式获取token失败"+error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, getApplicationContext(), "2rNcuv5oBZB0SW4yXvOBguZq", "MYgWkzgw4V4HgndmfGpR7oQMebdT6oar");
    }

    private void Http_User(String name) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", mAccount);
        map.put("name", name);
        String md5 = "name=" + name + "&username=" + mAccount;
        map.put("sign", MD5Utils.setMD5(md5));
        DownHTTP.postVolley(urlUsers, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CommitUser user = gson.fromJson(response, CommitUser.class);
                CommitUser.DataBean data = user.getData();
                mEtName.setText(data.getName());
                mEtIdcardfront.setText(data.getIdNumber());
                mEtIdcardreverse.setText(data.getIdcardExpireDate());
                mEtMerchant.setText(data.getMerchantName());
                mEtCity.setText(data.getProvince() + data.getCity());
                mEtAddress.setText(data.getAddress());
                mIdCardFragment.setIDFrontImg(data.getIdcardFrontImg());
                mIdCardFragment.setIDReverseImg(data.getIdcardBackImg());
                mProvince = data.getProvince();
                mCity = data.getCity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGION_REQUEST_CODE && resultCode == 200) {
            mProvince = data.getStringExtra(RegionSelectActivity.REGION_PROVINCE);
            mCity = data.getStringExtra(RegionSelectActivity.REGION_CITY) +
                    " " + data.getStringExtra(RegionSelectActivity.REGION_AREA);
            mArea = data.getStringExtra(RegionSelectActivity.REGION_AREA);
            mEtCity.setText(mProvince + " " + mCity);
        }
    }

    private void initFragment() {
        mIdCardFragment = new IdCardFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_card, mIdCardFragment).commit();
    }

    @Subscribe
    public void getIDFront(ID_Front event) {
        if (TextUtils.isEmpty(event.cardno)) {
            mEtIdcardreverse.setText(event.name);
        } else {
            mEtName.setText(event.name);
            mEtIdcardfront.setText(event.cardno);
        }
        mRlLoad.setVisibility(View.GONE);
    }

    @Subscribe
    public void showLoad(StartAnim event) {
        if (event.getCommit().equals(getString(R.string.idcommit))) {
            mRlLoad.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void finishEvent(FinishEvent event){
        finish();
    }

    @OnClick({R.id.img_return, R.id.btn_next, R.id.et_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.et_city:
                DBCopyUtil.copyDataBaseFromAssets(this, "region.db");
                startActivityForResult(new Intent(this, RegionSelectActivity.class),
                        REGION_REQUEST_CODE);
                break;
            case R.id.btn_next:
                if(getString(R.string.audited).equals(mAudit)){
                    toBankActivity();
                    return;
                }
                File file = new File(CameraManager.strDir, getString(R.string.idfront));
                File file2 = new File(CameraManager.strDir, getString(R.string.idreverse));
                String name = mEtName.getText().toString().trim();
                String idno = mEtIdcardfront.getText().toString().trim();
                String idtime = mEtIdcardreverse.getText().toString().trim();
                String merchant = mEtMerchant.getText().toString().trim();
                String address = mEtAddress.getText().toString().trim();
                if (file.length() <= 0) {
                    ToastUtil.showShort(this, "请上传身份证正面");
                } else if (file2.length() <= 0) {
                    ToastUtil.showShort(this, "请上传身份证反面");
                } else if (TextUtils.isEmpty(name)) {
                    ToastUtil.showShort(this, "请输入姓名");
                } else if (TextUtils.isEmpty(idno)) {
                    ToastUtil.showShort(this, "请输入身份证号");
                } else if (TextUtils.isEmpty(idtime)) {
                    ToastUtil.showShort(this, "请输入有效期");
                } else if (TextUtils.isEmpty(merchant)) {
                    ToastUtil.showShort(this, "请输入商户名称");
                } else if (TextUtils.isEmpty(mProvince)) {
                    ToastUtil.showShort(this, "请输入省市");
                } else if (TextUtils.isEmpty(address)) {
                    ToastUtil.showShort(this, "请输入详细地址");
                } else {
                    mRlLoad.setVisibility(View.VISIBLE);
                    mMap.clear();
                    mMap.put("username", mAccount);
                    mMap.put("name", name);
                    mMap.put("idNumber", idno);
                    mMap.put("idcardExpireDate", idtime);
                    mMap.put("merchantName", merchant);
                    mMap.put("province", mProvince);
                    mMap.put("city", mCity);
                    mMap.put("address", address);
                    mMap.put("idcardFrontImg", "idcardFrontImg");
                    mMap.put("idcardBackImg", "idcardBackImg");
                    final Map<String, File> mapFiles = new HashMap<String, File>();
                    mapFiles.put("idcardFrontImg", file);
                    mapFiles.put("idcardBackImg", file2);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String request = UploadUtil.post(urlCommit, mMap, mapFiles);
                                JSONObject jsonObject = new JSONObject(request);
                                String code = jsonObject.getString("code");
                                String msg = jsonObject.getString("msg");
                                if (code.equals("1")) {
                                    toBankActivity();
                                }
                                ToastUtil.showShort(RenZhengActivity.this, msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mRlLoad.setVisibility(View.GONE);
                                }
                            });
                        }
                    }).start();
                }
                break;
        }
    }

    private void toBankActivity() {
        Intent intent = new Intent(RenZhengActivity.this, BankActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 释放内存资源
        OCR.getInstance().release();
    }
}
