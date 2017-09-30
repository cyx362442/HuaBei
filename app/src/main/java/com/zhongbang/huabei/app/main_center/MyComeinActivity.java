package com.zhongbang.huabei.app.main_center;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.bean.MyComing;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.webview.WebActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyComeinActivity extends AppCompatActivity {

    private final String urlData = "http://chinaqmf.cn:8088/IHB/POST";
    @BindView(R.id.image_return)
    ImageView mImageReturn;
    @BindView(R.id.tv_totalRemaining)
    TextView mTvTotalRemaining;
    @BindView(R.id.tv_totalIncoming)
    TextView mTvTotalIncoming;
    @BindView(R.id.tv_wdTotalIncoming)
    TextView mTvWdTotalIncoming;
    @BindView(R.id.tv_cloudTotalIncoming)
    TextView mTvCloudTotalIncoming;
    @BindView(R.id.tv_benefitTotalIncoming)
    TextView mTvBenefitTotalIncoming;
    @BindView(R.id.tv_unsettledMoney)
    TextView mTvUnsettledMoney;
    @BindView(R.id.tv_totalWXPay)
    TextView mTvTotalWXPay;
    @BindView(R.id.tv_unsettledWxpay)
    TextView mTvUnsettledWxpay;
    @BindView(R.id.tv_WXchaxun)
    TextView mTvWXchaxun;
    @BindView(R.id.text2_xiangqing1)
    RelativeLayout mText2Xiangqing1;
    @BindView(R.id.tv_totalAlipay)
    TextView mTvTotalAlipay;
    @BindView(R.id.tv_unsettledAlipay)
    TextView mTvUnsettledAlipay;
    @BindView(R.id.tv_zhifubaojiesuan)
    TextView mTvZhifubaojiesuan;
    @BindView(R.id.tv_totalUnionpay)
    TextView mTvTotalUnionpay;
    @BindView(R.id.tv_unsettledUnionpay)
    TextView mTvUnsettledUnionpay;
    @BindView(R.id.tv_yinlianChanxun)
    TextView mTvYinlianChanxun;
    @BindView(R.id.my_hongbao)
    ImageView mMyHongbao;
    @BindView(R.id.relativeLayout1)
    RelativeLayout mRelativeLayout1;
    @BindView(R.id.textView8)
    TextView mTextView8;
    @BindView(R.id.textView10)
    TextView mTextView10;
    @BindView(R.id.tv_xiangqing)
    TextView mTvXiangqing;
    @BindView(R.id.textView11)
    TextView mTextView11;

    private String mAccount;
    private View mViewload;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comein);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ButterKnife.bind(this);
        mViewload = findViewById(R.id.include);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();

        final HashMap<String, String> map = new HashMap<>();
        map.put("type_all", "channel");
        map.put("username", mAccount);
        DownHTTP.postVolley(urlData, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                MyComing myComing = gson.fromJson(response, MyComing.class);
                mTvTotalRemaining.setText(myComing.getTotalRemaining());
                mTvTotalIncoming.setText(myComing.getTotalIncoming());
                mTvWdTotalIncoming.setText(myComing.getWdTotalIncoming());
                mTvCloudTotalIncoming.setText(myComing.getCloudTotalIncoming());
                mTvBenefitTotalIncoming.setText(myComing.getBenefitTotalIncoming());
                mTvUnsettledMoney.setText(myComing.getUnsettledMoney());
                mTvTotalWXPay.setText(myComing.getTotalWxpay());
                mTvUnsettledWxpay.setText(myComing.getUnsettledWxpay());
                mTvTotalAlipay.setText(myComing.getTotalAlipay());
                mTvUnsettledAlipay.setText(myComing.getUnsettledAlipay());
                mTvTotalUnionpay.setText(myComing.getTotalUnionpay());
                mTvUnsettledUnionpay.setText(myComing.getUnsettledUnionpay());

                mViewload.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({R.id.image_return, R.id.text_zhangdan, R.id.tv_xiangqing,R.id.tv_jiesuan1,
    R.id.tv_WXchaxun,R.id.tv_yinlianChanxun,R.id.tv_zhifubaojiesuan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_return:
                finish();
                break;
            case R.id.text_zhangdan:
                toMyBillActivity();
                break;
            case R.id.tv_xiangqing:
                toFRWebView();
                break;
            case R.id.tv_jiesuan1:
                toFRWebView();
                break;
            case R.id.tv_WXchaxun:
                toMyBillActivity();
                break;
            case R.id.tv_yinlianChanxun:
                toMyBillActivity();
                break;
            case R.id.tv_zhifubaojiesuan:
                mIntent=new Intent(this,BalanceActivity.class);
                mIntent.putExtra("channel", "支付宝");
                startActivity(mIntent);
                break;
        }
    }

    private void toMyBillActivity() {
        mIntent = new Intent(this, MyBillActivity.class);
        startActivity(mIntent);
    }

    private void toFRWebView() {
        mIntent=new Intent(this, WebActivity.class);
        String url="http://chinaqmf.cn/tg/customer_list.html?id=";
        mIntent.putExtra("url",url+mAccount);
        mIntent.putExtra(getString(R.string.webtitle),"今日分润");
        startActivity(mIntent);
    }
}
