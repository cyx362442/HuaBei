package com.zhongbang.huabei.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhongbang.huabei.R;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;
import com.zhongbang.huabei.webview.WebActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionActivity extends AppCompatActivity {

    @BindView(R.id.tv_money)
    TextView mTvMoney;
    private final String urlWexin="http://chinaqmf.cn/pay_wangye/wxpay.html?username=";
    private final String urlZhifubao="http://chinaqmf.cn/pay_wangye/alipay.html?username=";
    private final String urlYinlian="http://chinaqmf.cn/pay_wangye/unionpay.html?username=";
    private String mAccount;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();
        mIntent = new Intent(this, WebActivity.class);
        ButterKnife.bind(this);
    }

    String str="";
    @OnClick({R.id.tv_one, R.id.tv_two, R.id.tv_three, R.id.ll_weixin, R.id.tv_four, R.id.tv_five, R.id.tv_six, R.id.img_zhifubao, R.id.tv_seven, R.id.tv_eight, R.id.tv_nine, R.id.tv_zero, R.id.tv_dot, R.id.doubleZero, R.id.ll_yinlian})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_one:
                intputMsg("1");
                break;
            case R.id.tv_two:
                intputMsg("2");
                break;
            case R.id.tv_three:
                intputMsg("3");
                break;
            case R.id.ll_weixin:
                if(TextUtils.isEmpty(str)||Float.parseFloat(str)<=0){
                    ToastUtil.showShort(this,"请输入金额");
                    return;
                }
                mIntent.putExtra("url",urlWexin+mAccount+"&amount="+str);
                mIntent.putExtra(getString(R.string.webtitle),"微信收款");
                startActivity(mIntent);
                break;
            case R.id.tv_four:
                intputMsg("4");
                break;
            case R.id.tv_five:
                intputMsg("5");
                break;
            case R.id.tv_six:
                intputMsg("6");
                break;
            case R.id.img_zhifubao:
                mIntent.putExtra("url",urlZhifubao+mAccount+"&amount="+str);
                mIntent.putExtra(getString(R.string.webtitle),"支付宝收款");
                startActivity(mIntent);
                break;
            case R.id.tv_seven:
                intputMsg("7");
                break;
            case R.id.tv_eight:
                intputMsg("8");
                break;
            case R.id.tv_nine:
                intputMsg("9");
                break;
            case R.id.tv_zero:
                intputMsg("0");
                break;
            case R.id.tv_dot:
                if(TextUtils.isEmpty(str)||str.contains(".")){
                    return;
                }
                intputMsg(".");
                break;
            case R.id.doubleZero:
                if(str.length()>0){
                    str=str.substring(0,str.length()-1);
                    mTvMoney.setText("去收款￥"+str);
                }
                break;
            case R.id.ll_yinlian:
                mIntent.putExtra("url",urlYinlian+mAccount+"&amount="+str);
                mIntent.putExtra(getString(R.string.webtitle),"银联收款");
                startActivity(mIntent);
                break;
        }
    }

    private void intputMsg(String num) {
        str=str+num;
        mTvMoney.setText("去收款￥"+str);
    }
}
