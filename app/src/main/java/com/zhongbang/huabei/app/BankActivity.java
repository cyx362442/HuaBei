package com.zhongbang.huabei.app;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.event.Bank;
import com.zhongbang.huabei.event.StartAnim;
import com.zhongbang.huabei.fragment.BankFragment;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.UploadUtil;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.region.RegionSelectActivity;
import com.zhongbang.huabei.utils.DBCopyUtil;
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

public class BankActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_bankName)
    EditText mEtBankName;
    @BindView(R.id.et_bankcard)
    EditText mEtBankcard;
    @BindView(R.id.et_city)
    TextView mEtCity;
    @BindView(R.id.et_zhihang)
    EditText mEtZhihang;
    @BindView(R.id.pb)
    ProgressBar mPb;
    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.rl_load)
    RelativeLayout mRlLoad;

    private String mAccount;
    private static final int REGION_REQUEST_CODE = 002;
    private String mProvince;
    private String mCity;
    private String mArea;
    private String zhihangCode="100012";

    private final String urlCommit="http://chinaqmf.cn:8088/ihuabei/app/user/submitBankCard.app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();
        mTvTitle.setText("银行卡认证");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_bandcard, new BankFragment()).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGION_REQUEST_CODE && resultCode == 200) {
            mProvince = data.getStringExtra(RegionSelectActivity.REGION_PROVINCE);
            mCity = data.getStringExtra(RegionSelectActivity.REGION_CITY);
            mArea = data.getStringExtra(RegionSelectActivity.REGION_AREA);
            mEtCity.setText(mProvince + " " + mCity + " " + mArea);
        }
    }

    @Subscribe
    public void showLoad(StartAnim event){
        if(event.getCommit().equals(getString(R.string.bankcommit))){
            mRlLoad.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void getBankData(Bank event){
        mEtBankName.setText(event.name);
        mEtBankcard.setText(event.cardno);
        mRlLoad.setVisibility(View.GONE);
    }

    @OnClick({R.id.img_return, R.id.btn_next,R.id.et_city})
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
                 File file = new File(CameraManager.strDir,getString(R.string.bankfront));
                 File file2=new File(CameraManager.strDir,getString(R.string.bankreverse));
                String bankName = mEtBankName.getText().toString().trim();
                String bankCard = mEtBankcard.getText().toString().trim();
                String city = mEtCity.getText().toString().trim();
                 String zhihang = mEtZhihang.getText().toString().trim();
                if(file.length()<=0){
                    ToastUtil.showShort(this,"请上传银行卡正面照");
                }else if(file2.length()<=0){
                    ToastUtil.showShort(this,"请上传银行卡反面照");
                }else if(TextUtils.isEmpty(bankName)){
                    ToastUtil.showShort(this,"请填写所属银行");
                }else if(TextUtils.isEmpty(bankCard)){
                    ToastUtil.showShort(this,"请填写银行卡号");
                }else if(TextUtils.isEmpty(city)){
                    ToastUtil.showShort(this,"请填写所属地区");
                }else if(TextUtils.isEmpty(zhihang)){
                    ToastUtil.showShort(this,"请填写支行名称");
                }else{
                    mRlLoad.setVisibility(View.VISIBLE);
                    final HashMap<String, String> map = new HashMap<>();
                    map.put("username",mAccount);
                    map.put("bankCardName",bankName);
                    map.put("bankCardProvince",mProvince);
                    map.put("bankCardCity",mCity);
                    map.put("bankCardBranch",zhihang);
                    map.put("branchCode",zhihangCode);
                    map.put("bankCardNumber",bankCard);
                    final Map<String, File> mapFiles = new HashMap<String, File>();
                    mapFiles.put("bankCardFrontImg",file);
                    mapFiles.put("bankCardBackImg",file2);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String post = UploadUtil.post(urlCommit, map, mapFiles);
                                try {
                                    JSONObject jsonObject = new JSONObject(post);
                                    String code = jsonObject.getString("code");
                                    String msg = jsonObject.getString("msg");
                                    if("1".equals(code)){
                                        Intent intent = new Intent(BankActivity.this, GroupPhotoActivity.class);
                                        startActivity(intent);
                                    }
                                    ToastUtil.showShort(BankActivity.this,msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
