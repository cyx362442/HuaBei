package com.zhongbang.huabei.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.bean.CommitGroup;
import com.zhongbang.huabei.event.FinishEvent;
import com.zhongbang.huabei.fragment.dialog.GroupImageFragment;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.UploadUtil;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.MD5Utils;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
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
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    private String mAccount;

    private final String urlGroup = "http://chinaqmf.cn:8088/ihuabei/app/user/securityVerifyInfo.app";
    private final String urlCommit = "http://chinaqmf.cn:8088/ihuabei/app/user/submitHandCardImg.app";
    public static int TAKE_PHOTO_REQUEST_CODE = 100;
    public static final String strDir = Environment.getExternalStorageDirectory() + "/zhongbang/";
    private String mAudit;
    private View mViewLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_group_photo);
        ButterKnife.bind(this);
        mViewLoad = findViewById(R.id.rl_load);
        mTvTitle.setText("人卡合影");

        ShapreUtis shapreUtis = ShapreUtis.getInstance(this);
        mAccount = shapreUtis.getAccount();
        String name = shapreUtis.getName();
        mAudit = shapreUtis.getAudit();
        if (!getString(R.string.unaudit).equals(mAudit)) {
            Http_Group(name);
        }
        if (getString(R.string.audited).equals(mAudit)) {
            mTvMsg.setText(getString(R.string.auditMsg));
            mBtnCommit.setText("返回首页");
        }
    }

    private void Http_Group(String name) {
        mViewLoad.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("username", mAccount);
        map.put("name", name);
        String str = "name=" + name + "&username=" + mAccount;
        map.put("sign", MD5Utils.setMD5(str));
        DownHTTP.postVolley(urlGroup, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mViewLoad.setVisibility(View.GONE);
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CommitGroup group = gson.fromJson(response, CommitGroup.class);
                Glide.with(GroupPhotoActivity.this).
                        load(group.getData().getHandCardImg()).
                        into(mImgGroup);
                mViewLoad.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            final Bitmap bmp = getScaleBitmap(this, getTempImage().getPath());
            final File file = new File(strDir, "group.jpg");
            Glide.with(this).load(file).skipMemoryCache(true).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    error(R.mipmap.id_reverse).centerCrop().into(mImgGroup);
            //图片压缩并保有存
            new Thread(new Runnable() {
                @Override
                public void run() {
                    compressBmpToFile(bmp, file);
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
                fragment.show(getFragmentManager(), getString(R.string.groupimage));
                break;
            case R.id.btn_commit:
                if(getString(R.string.audited).equals(mAudit)){
                    finish();
                    EventBus.getDefault().post(new FinishEvent());
                    return;
                }
                mViewLoad.setVisibility(View.VISIBLE);
                final File file = new File(strDir, "group.jpg");
                final HashMap<String, String> map = new HashMap<>();
                map.put("username", mAccount);
                final Map<String, File> mapFiles = new HashMap<String, File>();
                mapFiles.put("handCardImg", file);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String post = UploadUtil.post(urlCommit, map, mapFiles);
                            JSONObject jsonObject = new JSONObject(post);
                            String code = jsonObject.getString("code");
                            final String msg = jsonObject.getString("msg");
                            if ("1".equals(code)) {
                                EventBus.getDefault().post(new FinishEvent());
                                finish();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mViewLoad.setVisibility(View.GONE);
                                    ToastUtil.showShort(GroupPhotoActivity.this, msg);
                                }
                            });
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
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
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

    public static void compressBmpToFile(Bitmap bmp, File file) {
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
