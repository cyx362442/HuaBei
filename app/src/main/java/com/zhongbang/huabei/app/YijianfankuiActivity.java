package com.zhongbang.huabei.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class YijianfankuiActivity extends AppCompatActivity implements View.OnClickListener {
    private final String url="http://chinaqmf.cn/app/Operation.ashx";
    private String mAccount;
    private EditText edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yijianfankui);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();
        edittext = (EditText) findViewById(R.id.editText1);
        findViewById(R.id.btn_tijiao).setOnClickListener(this);
        findViewById(R.id.imageView_return).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_tijiao){
            http();
        }else if(v.getId()==R.id.imageView_return){
            finish();
        }
    }
    private void http() {
        // TODO Auto-generated method stub
        String text = edittext.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("type", "so");
        map.put("username", mAccount);
        map.put("txt", text);
        DownHTTP.postVolley(url, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                ToastUtil.showShort(YijianfankuiActivity.this,"提交失败");
            }
            @Override
            public void onResponse(String arg0) {
                // TODO Auto-generated method stub
                try {
                    String st = new JSONArray(arg0).getJSONObject(0).getString(
                            "parmone");
                    ToastUtil.showShort(YijianfankuiActivity.this,st);
                    finish();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}
