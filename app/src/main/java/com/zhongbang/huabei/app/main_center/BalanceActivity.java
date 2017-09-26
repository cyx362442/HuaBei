package com.zhongbang.huabei.app.main_center;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BalanceActivity extends AppCompatActivity implements View.OnClickListener {
    private HashMap<String, String> HashMap_post = new HashMap<String, String>();
    private HashMap<String,String>hashMap_user=new HashMap<String,String>();
    private String mPhone;
    private EditText edittext;
    private TextView tv_money;
    private TextView tv_money2;
    private TextView tv_money3;
    private TextView tv_money4;
    private Toast mToast;
    private String mIdString;
    private String mChanel;
    private EditText mEdittext2;
    private String mSettlementString;
    private View mViewLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        mChanel = getIntent().getStringExtra("channel");
        mPhone = ShapreUtis.getInstance(this).getAccount();
        initUI();
        Http_Balance();
        //实际到金额跟随提现金额变动
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!s.toString().equals("")) {
                    if (Float.parseFloat(s.toString()) < 6.0) {
                        tv_money4.setText("0");
                    } else {
                        tv_money4.setText((Float.parseFloat(s.toString())
                                - Float.parseFloat(mSettlementString) + ""));
                    }
                } else {
                    tv_money4.setText("0");
                }
            }
        });
    }
    private void initUI() {
        mViewLoad = findViewById(R.id.include);
        edittext = (EditText) findViewById(R.id.editText1);
        mEdittext2 = (EditText) findViewById(R.id.editText2);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_money2 = (TextView) findViewById(R.id.tv_money2);
        tv_money3 = (TextView) findViewById(R.id.tv_money3);
        tv_money4 = (TextView) findViewById(R.id.tv_money4);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_OK).setOnClickListener(this);
    }
    private void Http_Balance() {
        // TODO Auto-generated method stub
        HashMap<String, String> has = new HashMap<String, String>();
        has.put("username", mPhone);
        has.put("channel_name", mChanel);
        String url ="http://chinaqmf.cn/app/channel_get.aspx";
        DownHTTP.postVolley(url, has, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                mViewLoad.setVisibility(View.GONE);
                ToastUtil.showShort(BalanceActivity.this,"网络异常");
            }
            @Override
            public void onResponse(String arg0) {
                try {
                    JSONObject object = new JSONArray(arg0).getJSONObject(0);
                    String reaminString = object.getString("remaining");
                    String maxString = object.getString("settlement_max");
                    mSettlementString = object.getString("settlement_early");
                    mIdString = object.getString("Id");
                    tv_money.setText(reaminString);
                    tv_money2.setText(maxString);
                    tv_money3.setText(mSettlementString);
                    mViewLoad.setVisibility(View.GONE);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mViewLoad.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_OK:
                mViewLoad.setVisibility(View.GONE);
                toBanlance();
                break;
            default:
                break;
        }
    }
    private void toBanlance() {
        HashMap_post.clear();
        HashMap_post.put("channel_Id", mIdString);
        HashMap_post.put("amount", edittext.getText().toString());
//        HashMap_post.put("alipay_account", mEdittext2.getText().toString());
        String urlPost_all="http://chinaqmf.cn/app/settlement_set.aspx";
        DownHTTP.postVolley(urlPost_all, HashMap_post,
                new VolleyResultListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        ToastUtil.showShort(BalanceActivity.this,"网络异常");
                        mViewLoad.setVisibility(View.GONE);
                    }
                    @Override
                    public void onResponse(String arg0) {
                        if ("申请结算成功".equals(arg0)) {
                            Intent intent = new Intent(BalanceActivity.this,
                                    ToBalanceActivity.class);
                            intent.putExtra("V1", edittext.getText().toString());
                            intent.putExtra("V2", tv_money4.getText().toString());
                            startActivity(intent);
                            finish();
                            mViewLoad.setVisibility(View.GONE);
                        }
                        showDalog(arg0);
                        edittext.setText("");
                    }
                });
    }
    protected void showDalog(String arg0) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(arg0);
        builder.setPositiveButton("确定", null);// 第二个参数为事件监听器
        builder.show();
        // Dialogutil.getInstance().show(this);
    }
}
