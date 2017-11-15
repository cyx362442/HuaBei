package com.zhongbang.huabei.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocalOpenActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.et_password1)
    EditText mEtPassword1;
    @BindView(R.id.tv_code)
    TextView mTvCode;

    private final String urlCode="http://chinaqmf.cn:8088/IHBZC/ZhuCe";
    private final String urlRegister="http://chinaqmf.cn:8088/ihuabei/app/account/f2fRegister.app";
    private String mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_open);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();
        mTvTitle.setText("面对面开通账号");
    }

    @OnClick({R.id.img_return, R.id.tv_code, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv_code:
                String phone = mEtAccount.getText().toString().trim();
                if (isMobileNO(phone)) {
                    Http_getCode(phone);
                    getMsgCode();
                } else {
                    ToastUtil.showShort(this, "手机号码有误");
                }
                break;
            case R.id.btn_register:
                String account = mEtAccount.getText().toString().trim();
                String code = mEtCode.getText().toString().trim();
                String password1 = mEtPassword1.getText().toString().trim();
                if(TextUtils.isEmpty(account)){
                    ToastUtil.showShort(this,"请输入账号");
                }else if(TextUtils.isEmpty(code)){
                    ToastUtil.showShort(this,"请输入验证码");
                }else if(TextUtils.isEmpty(password1)){
                    ToastUtil.showShort(this,"请输入密码");
                }else{
                    HashMap<String, String> map = new HashMap<>();
                    map.put("phone",account);
                    map.put("password",password1);
                    map.put("verifyCode",code);
                    map.put("referrer",mAccount);
                    DownHTTP.postVolley(urlRegister, map, new VolleyResultListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String code1 = jsonObject.getString("code");
                                String msg = jsonObject.getString("msg");
                                if("1".equals(code1)){
                                    finish();
                                }
                                ToastUtil.showShort(LocalOpenActivity.this,msg);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }
    }

    public boolean isMobileNO(String mobile) {
        Pattern p = Pattern.compile("^1[34578]\\d{9}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    private void Http_getCode(String phone) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type_all", "xgmmyzm");
        map.put("phone", phone);
        DownHTTP.postVolley(urlCode, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }

    int times;
    Runnable run = null;

    private void getMsgCode() {
        mTvCode.setEnabled(false);
        times = 30;
        final Handler handler = new Handler();
        handler.postDelayed(run = new Runnable() {
            @Override
            public void run() {
                if (times > 0) {
                    mTvCode.setText(times + "秒后重发");
                    times--;
                    handler.postDelayed(run, 1000);
                } else {
                    mTvCode.setEnabled(true);
                    mTvCode.setText("获取验证码");
                    handler.removeCallbacks(run);
                }
            }
        }, 0);
    }
}
