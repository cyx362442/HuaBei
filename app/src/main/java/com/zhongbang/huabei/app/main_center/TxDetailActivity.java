package com.zhongbang.huabei.app.main_center;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class TxDetailActivity extends AppCompatActivity {
    HashMap<String, String> HashMap_post = new HashMap<String, String>();
    String urlPost ="http://chinaqmf.cn:8088/IHB/POST";
    private Zhangdanxiangqing zD;

    private String zhangdanhao;
    private TextView order_number;
    private TextView Name;
    private TextView type;
    private TextView Kh;
    private TextView take_money;
    private TextView poundage;
    private TextView real_otain;
    private TextView time1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);
        zhangdanhao = getIntent().getStringExtra("dingdanhao");
        initUI();
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        HashMap_post.clear();// 清空
        HashMap_post.put("type_all", "txxq");
        HashMap_post.put("Id", zhangdanhao);// 此时这里就是要传的网页上对应的参数String的值
        http();
    }
    private void initUI() {
        order_number = (TextView) findViewById(R.id.order_number);
        Name = (TextView) findViewById(R.id.Name);
        type = (TextView) findViewById(R.id.type);
        Kh = (TextView) findViewById(R.id.Kh);
        take_money = (TextView) findViewById(R.id.take_money);
        poundage = (TextView) findViewById(R.id.poundage);
        real_otain = (TextView) findViewById(R.id.real_otain);
        time1 = (TextView) findViewById(R.id.time1);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.come_in2, R.anim.exit2);
            }
        });
    }
    private void http() {
		/* 使用POST请求 */
        DownHTTP.postVolley(urlPost, HashMap_post, new VolleyResultListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                ToastUtil.showShort(TxDetailActivity.this,"网络异常");
            }
            @Override
            public void onResponse(String arg0) {
                zD = new Zhangdanxiangqing();
                try {
                    JSONArray array = new JSONArray(arg0);
                    JSONObject jsonObject = array.getJSONObject(0);
                    zD.order_number = jsonObject.getString("order_number");
                    zD.Name = jsonObject.getString("Name");
                    zD.type = jsonObject.getString("type");
                    zD.Kh = jsonObject.getString("Kh");
                    zD.take_money = jsonObject.getString("take_money");
                    zD.poundage = jsonObject.getString("poundage");
                    zD.real_otain = jsonObject.getString("real_otain");
                    zD.time1 = jsonObject.getString("time1");
                    initfuz();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
    private void initfuz() {
        order_number.setText(zD.order_number);
        Name.setText(zD.Name);
        type.setText(zD.type);
        Kh.setText(zD.Kh);
        take_money.setText(zD.take_money);
        poundage.setText(zD.poundage);
        real_otain.setText(zD.real_otain);
        time1.setText(zD.time1);
    }
    class Zhangdanxiangqing {
        String order_number;// -订单号
        String Name;// -姓名
        String type;// -类型(交易、提现)
        String Kh;// -绑定卡号
        String take_money;// -发生金额
        String poundage;// -手续费
        String real_otain;// -实得金额
        String time1;// -时间

    }
}
