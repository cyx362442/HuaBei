package com.zhongbang.huabei.app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.event.Dismiss;
import com.zhongbang.huabei.fragment.NoticeDialog;
import com.zhongbang.huabei.fragment.UpdateFragment;
import com.zhongbang.huabei.http.DownHTTP;
import com.zhongbang.huabei.http.VolleyResultListener;
import com.zhongbang.huabei.utils.ShapreUtis;
import com.zhongbang.huabei.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity{
    private final String updateUrl = "http://chinaqmf.cn/app/return_Homepage.aspx";
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.img_password)
    ImageView mImgPassword;
    @BindView(R.id.btn_land)
    Button mBtnLand;
    @BindView(R.id.tv_forget)
    TextView mTvForget;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    private HashMap<String, String> map;
    private String mVersion = "1.0";
    private String mAuthorContact = "";

    private boolean isShow=false;
    private final String urlLand="http://chinaqmf.cn/app/landing.aspx";
    private final String urlLogo="http://7xpj8w.com1.z0.glb.clouddn.com/logo80.png";
    private Intent mIntent;
    private ShapreUtis mShapreUtis;
    private File currentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        map = new HashMap<>();
        NoticeDialog dialog = new NoticeDialog();
        dialog.show(getSupportFragmentManager(), getString(R.string.noticeDialog));

        mShapreUtis = ShapreUtis.getInstance(this);
        String account = mShapreUtis.getAccount();
        if(!TextUtils.isEmpty(account)){
            mEtAccount.setText(account);
        }
        Http_checkVersion();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Bitmap bitmap = Glide.with(LoginActivity.this)
//                            .load(urlLogo)
//                            .asBitmap()
//                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                            .get();
//                    if (bitmap != null){
//                        // 在这里执行图片保存方法
//                        saveImageToGallery(bitmap);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
    }

    private void Http_checkVersion() {
        DownHTTP.postVolley(updateUrl, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    mVersion = jsonObject.getString("system_version");
                    mAuthorContact = jsonObject.getString("author_contact");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe
    public void isDismiss(Dismiss event) {
        if (Float.parseFloat(mVersion) > getAppVersionName()) {
            UpdateFragment fragment = UpdateFragment.newInstance(mAuthorContact);
            fragment.show(getSupportFragmentManager(), getString(R.string.update));
        }
    }

    private int getAppVersionName() {
        String versionName = "";
        int versioncode = 1;
        try {
            // ---get the package info---
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
        }
        return versioncode;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.btn_land, R.id.tv_forget, R.id.tv_register,R.id.img_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_land:
                Http_land();
                break;
            case R.id.tv_forget:
                mIntent = new Intent(this, ForgetActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_register:
                mIntent = new Intent(this, RegisterActivity.class);
                startActivity(mIntent);
                break;
            case R.id.img_password:
                isShow=!isShow;
                if(isShow==true){
                    mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mImgPassword.setImageResource(R.drawable.ic_visibility_blue_24dp);
                }else{
                    mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mImgPassword.setImageResource(R.drawable.ic_visibility_gray_24dp);
                }
                break;
        }
    }

    private void Http_land() {
        final String account = mEtAccount.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("username",account);
        map.put("password",password);
        DownHTTP.postVolley(urlLand, map, new VolleyResultListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
            @Override
            public void onResponse(String response) {
                if(!response.equals("登陆失败")){
                    mShapreUtis.setAccount(account);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                ToastUtil.showShort(LoginActivity.this,response);
            }
        });
    }

    public void saveImageToGallery(Bitmap bmp) {
        // 首先保存图片
        String strDir = Environment.getExternalStorageDirectory()+"/zhongbang/";
        String fileName ="huabei.jpg";
        File appDir = new File(strDir);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        currentFile = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 最后通知图库更新
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
    }
}
