package com.zhongbang.huabei.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.event.ID_Front;
import com.zhongbang.huabei.event.StartAnim;
import com.zhongbang.huabei.fragment.IdCardFragment;
import com.zhongbang.huabei.http.UploadUtil;
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
    private String mProvince;
    private String mCity;
    private String mArea;

    private final String urlCommit = " http://chinaqmf.cn:8088/ihuabei/app/user/submitUserData.app";
    private String mAccount;
    private HashMap<String, String> mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ren_zheng);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        mMap = new HashMap<>();
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();
        mTvTitle.setText("实名认证 ");
        initFragment();
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

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_card, new IdCardFragment()).commit();
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
    public void showLoad(StartAnim event){
        if(event.getCommit().equals(getString(R.string.idcommit))){
            mRlLoad.setVisibility(View.VISIBLE);
        }
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
                File file = new File(CameraManager.strDir,getString(R.string.idfront));
                File file2=new File(CameraManager.strDir,getString(R.string.idreverse));
                String name = mEtName.getText().toString().trim();
                String idno = mEtIdcardfront.getText().toString().trim();
                String idtime = mEtIdcardreverse.getText().toString().trim();
                String merchant = mEtMerchant.getText().toString().trim();
                String address = mEtAddress.getText().toString().trim();
                if(file.length()<=0){
                    ToastUtil.showShort(this,"请上传身份证正面");
                }else if(file2.length()<=0){
                    ToastUtil.showShort(this,"请上传身份证反面");
                }else if(TextUtils.isEmpty(name)){
                    ToastUtil.showShort(this,"请输入姓名");
                }else if(TextUtils.isEmpty(idno)){
                    ToastUtil.showShort(this,"请输入身份证号");
                }else if(TextUtils.isEmpty(idtime)){
                    ToastUtil.showShort(this,"请输入有效期");
                }else if(TextUtils.isEmpty(merchant)){
                    ToastUtil.showShort(this,"请输入商户名称");
                }else if(TextUtils.isEmpty(mProvince)){
                    ToastUtil.showShort(this,"请输入省市");
                }else if(TextUtils.isEmpty(address)){
                    ToastUtil.showShort(this,"请输入详细地址");
                }else{
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
                    mMap.put("idcardFrontImg","idcardFrontImg");
                    mMap.put("idcardBackImg","idcardBackImg");
                    final Map<String, File> mapFiles = new HashMap<String, File>();
                    mapFiles.put("idcardFrontImg", file);
                    mapFiles.put("idcardBackImg",file2);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String request = UploadUtil.post(urlCommit,mMap, mapFiles);
                                JSONObject jsonObject = new JSONObject(request);
                                String code = jsonObject.getString("code");
                                String msg = jsonObject.getString("msg");
                                if(code.equals("1")){
                                    Intent intent = new Intent(RenZhengActivity.this, BankActivity.class);
                                    startActivity(intent);
                                }
                                ToastUtil.showShort(RenZhengActivity.this,msg);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
