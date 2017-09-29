package com.zhongbang.huabei.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;
import com.zhongbang.huabei.webview.WebActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.img_return)
    ImageView mImgReturn;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_yanzhengma)
    EditText mEtYanzhengma;
    @BindView(R.id.tv_getCode)
    TextView mTvGetCode;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_introducer)
    EditText mEtIntroducer;
    @BindView(R.id.checkbox)
    CheckBox mCheckbox;

    private final String urlCode="http://chinaqmf.cn:8088/IHBZC/ZhuCe";
    private final String urlRegister="http://chinaqmf.cn:8088/ihuabei/app/account/register.app";
    private boolean agreed=false;
    private Intent mIntent;
    private String mWebUrl;
    private ShapreUtis mShapreUtis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mShapreUtis = ShapreUtis.getInstance(this);

        mTvTitle.setText("注册");
        mCheckbox.setOnCheckedChangeListener(this);
    }

    int times;
    Runnable run = null;
    @OnClick({R.id.img_return, R.id.tv_getCode,R.id.btn_register,R.id.tv_registerAgreement,R.id.tv_serverAgreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv_getCode:
                String phone = mEtPhone.getText().toString().trim();
                if(isMobileNO(phone)){
                    Http_getCode(phone);
                    getMsgCode();
                }else{
                    ToastUtil.showShort(this,"手机号码有误");
                }
                break;
            case R.id.btn_register:
                String name = mEtName.getText().toString().trim();
                String phone2 = mEtPhone.getText().toString().trim();
                String code = mEtYanzhengma.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                String introduce = mEtIntroducer.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    ToastUtil.showShort(this,"请输入姓名");
                }else if(TextUtils.isEmpty(phone2)){
                    ToastUtil.showShort(this,"请输入手机号码");
                }else if(TextUtils.isEmpty(code)){
                    ToastUtil.showShort(this,"请输入验证码");
                }else if(TextUtils.isEmpty(password)||password.length()<6){
                    ToastUtil.showShort(this,"请输入不小于6位数密码");
                }else if(TextUtils.isEmpty(introduce)){
                    ToastUtil.showShort(this,"请输入推荐人手机号");
                }else if(agreed==false){
                    ToastUtil.showShort(this,"您未同意爱花呗支付协议");
                }else{
                    Http_register(name, phone2, code, password, introduce);
                }
                break;
            case R.id.tv_registerAgreement:
                mWebUrl = "http://chinaqmf.cn:8088/ihuabei/attached/right.html";
                mIntent = new Intent(this, WebActivity.class);
                mIntent.putExtra("url", mWebUrl);
                mIntent.putExtra(getString(R.string.registerAgreement),getString(R.string.registerAgreement));
                startActivity(mIntent);
                break;
            case R.id.tv_serverAgreement:
                mWebUrl="http://chinaqmf.cn:8088/IHBZC/zhuce/agreement.html";
                mIntent = new Intent(this, WebActivity.class);
                mIntent.putExtra("url", mWebUrl);
                mIntent.putExtra(getString(R.string.serverAgreement),getString(R.string.serverAgreement));
                startActivity(mIntent);
                break;
        }
    }

    private void Http_register(String name, final String phone2, String code, String password, String introduce) {
        HashMap<String,String> map=new HashMap<>();
        map.put("name",name);
        map.put("phone",phone2);
        map.put("password",password);
        map.put("verifyCode",code);
        map.put("referrer",introduce);
        DownHTTP.postVolley(urlRegister, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("msg");
                    if("1".equals(code)){
                        mShapreUtis.setAccount(phone2);
                        finish();
                    }
                    ToastUtil.showShort(RegisterActivity.this,msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getMsgCode() {
        mTvGetCode.setEnabled(false);
        times=30;
        final Handler handler = new Handler();
        handler.postDelayed(run=new Runnable() {
            @Override
            public void run() {
                if(times>0){
                    mTvGetCode.setText(times+"秒后重发");
                    times--;
                    handler.postDelayed(run,1000);
                }else{
                    mTvGetCode.setEnabled(true);
                    mTvGetCode.setText("获取验证码");
                    handler.removeCallbacks(run);
                }
            }
        },0);
    }

    private void Http_getCode(String phone) {
        HashMap<String,String> map = new HashMap<>();
        map.put("type_all","duanxin");
        map.put("phone",phone);
        DownHTTP.postVolley(urlCode, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
            }
        });
    }

    public boolean isMobileNO(String mobile) {
        Pattern p = Pattern.compile("^1[34578]\\d{9}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        agreed=isChecked;
    }
}
