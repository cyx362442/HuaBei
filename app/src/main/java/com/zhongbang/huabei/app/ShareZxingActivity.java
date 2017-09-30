package com.zhongbang.huabei.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.zxing.EncodingHandler;

import java.io.WriteAbortedException;

public class ShareZxingActivity extends AppCompatActivity implements View.OnClickListener {
    private final String zxingUrl="http://chinaqmf.cn:8088/IHBZC/zhuce/registered.html?id=";
    private String mAccount;
    private ImageView mImgZxing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_zxing);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();

        findViewById(R.id.img_return).setOnClickListener(this);
        mImgZxing = (ImageView) findViewById(R.id.img_zxing);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText("分享二维码");
        createCode();//生成二维码
    }
    private void createCode() {
        String contentString=zxingUrl+mAccount;
        if(contentString!=null&&contentString.trim().length()>0){
            try {
                //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                Bitmap logo= BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo80);//中间logo
                Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350,logo);
                mImgZxing.setImageBitmap(qrCodeBitmap);
            } catch (WriteAbortedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
