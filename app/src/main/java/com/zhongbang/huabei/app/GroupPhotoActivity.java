package com.zhongbang.huabei.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.fragment.dialog.GroupImageFragment;
import com.zhongbang.huabei.http.UploadUtil;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;
import com.zhongbang.huabei.yunmai.CameraManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupPhotoActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_group)
    ImageView mImgGroup;
    private String mAccount;

    private final String urlCommit="http://chinaqmf.cn:8088/ihuabei/app/user/submitHandCardImg.app";
    public static int TAKE_PHOTO_REQUEST_CODE=100;
    public static final String strDir= Environment.getExternalStorageDirectory()+"/zhongbang/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_photo);
        ButterKnife.bind(this);
        mTvTitle.setText("人卡合影");

        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==TAKE_PHOTO_REQUEST_CODE){
            final Bitmap bmp = getScaleBitmap(this, getTempImage().getPath());
            final File file = new File(strDir, "group.jpg");
            Glide.with(this).load(file).skipMemoryCache(true).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    error(R.mipmap.id_reverse).centerCrop().into(mImgGroup);
            //图片压缩并保有存
            new Thread(new Runnable() {
                @Override
                public void run() {
                    compressBmpToFile(bmp,file);
                }
            }).start();
        }
    }

    @OnClick({R.id.img_return, R.id.rl_group, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.rl_group:
                GroupImageFragment fragment = GroupImageFragment.newInstance();
                fragment.show(getFragmentManager(),getString(R.string.groupimage));
                break;
            case R.id.btn_commit:
                final File file = new File(strDir, "group.jpg");
                final HashMap<String, String> map = new HashMap<>();
                map.put("username",mAccount);
                final Map<String, File> mapFiles = new HashMap<String, File>();
                mapFiles.put("handCardImg",file);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String post = UploadUtil.post(urlCommit, map, mapFiles);
                            JSONObject jsonObject = new JSONObject(post);
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("msg");
                            ToastUtil.showShort(GroupPhotoActivity.this,msg);
                            if("1".equals(code)){
                                finish();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
    }

    public static File getTempImage() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File tempFile = new File(strDir, "group.jpg");
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tempFile;
        }
        return null;
    }

    public static Bitmap getScaleBitmap(Context ctx, String filePath) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, opt);

        int bmpWidth = opt.outWidth;
        int bmpHeght = opt.outHeight;

        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        opt.inSampleSize = 1;
        if (bmpWidth > bmpHeght) {
            if (bmpWidth > screenWidth)
                opt.inSampleSize = bmpWidth / screenWidth;
        } else {
            if (bmpHeght > screenHeight)
                opt.inSampleSize = bmpHeght / screenHeight;
        }
        opt.inJustDecodeBounds = false;

        bmp = BitmapFactory.decodeFile(filePath, opt);
        return bmp;
    }
    public static void compressBmpToFile(Bitmap bmp,File file){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
