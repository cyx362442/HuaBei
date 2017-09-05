package com.zhongbang.huabei.yunmai;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import com.zhongbang.huabei.fragment.BankFragment;
import com.zhongbang.huabei.fragment.IdCardFragment;
import android.view.SurfaceHolder.Callback;

import com.zhongbang.huabei.R;

public class ACameraActivity extends AppCompatActivity implements Callback{
    protected static final String tag = "ACameraActivity";
    private CameraManager mCameraManager;
    private SurfaceView mSurfaceView;
    private ProgressBar pb;
    private ImageButton mShutter;
    private SurfaceHolder mSurfaceHolder;
    private String flashModel = Camera.Parameters.FLASH_MODE_OFF;
    private byte[] jpegData = null;

    private Handler mHandler=new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getBaseContext(), "鎷嶇収澶辫触", Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    break;
                case 1:
                    jpegData=(byte[]) msg.obj;
                    if(jpegData!=null && jpegData.length>0){
                        pb.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if((jpegData.length>(1000*1024*5))){
                                    mHandler.sendMessage(mHandler.obtainMessage(3, getResources().getString(R.string.photo_too_lage)));
                                    return;
                                }
                                String result=null;
                                boolean isavilable= NetUtil.isNetworkConnectionActive(ACameraActivity.this);
                                if(isavilable){
                                    if(mImageName.equals(getString(R.string.idfront))||mImageName.equals(getString(R.string.idreverse))){//身份证
                                        result = Scan(IdCardFragment.action,jpegData,"jpg");
                                    }else if(mImageName.equals(getString(R.string.bankfront))||mImageName.equals(getString(R.string.bankreverse))){//银行卡
                                        result = Scan(BankFragment.action,jpegData,"jpg");
                                    }
                                    Log.d("tag", "<----->>"+result);
                                    if(result.equals("-2")){
                                        result="杩炴帴瓒呮椂锛�";
                                        mHandler.sendMessage(mHandler.obtainMessage(3, result));
                                    }else if(HttpUtil.connFail.equals(result)){
                                        mHandler.sendMessage(mHandler.obtainMessage(3, result));
                                    }else{
                                        mHandler.sendMessage(mHandler.obtainMessage(4, result));
                                    }
                                }else{
                                    mHandler.sendMessage(mHandler.obtainMessage(3, "鏃犵綉缁滐紝璇风‘瀹氱綉缁滄槸鍚﹁繛鎺�!"));
                                }
                            }
                        }).start();
                    }
                    break;
                case 3:
                    pb.setVisibility(View.GONE);
                    String str=msg.obj+"";
                    Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    mShutter.setEnabled(true);
                    break;
                case 4:
                    mShutter.setEnabled(true);
                    pb.setVisibility(View.GONE);
                    String result=msg.obj+"";
                    Intent intent=new Intent();
                    intent.putExtra("result", result);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                    break;
                case 5:
                    String filePath=msg.obj+"";
                    byte[] data= FileUtil.getByteFromPath(filePath);
                    Log.d(tag, "data length:"+data.length);
                    if(data!=null && data.length>0){
                        mHandler.sendMessage(mHandler.obtainMessage(1,data));
                    }else{
                        mHandler.sendMessage(mHandler.obtainMessage(0));
                    }
                    break;
                case 6:
                    Toast.makeText(getBaseContext(), "璇锋彃鍏ュ瓨鍌ㄥ崱锛�", Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    break;
            }
        };
    };
    private String mImageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acamera);
        mImageName = getIntent().getStringExtra(getString(R.string.imageName));
        mCameraManager = new CameraManager(mHandler, mImageName);
        initViews();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getBaseContext(), "内存卡读取失败", Toast.LENGTH_LONG).show();
            finish();
        }
        File dir = new File(CameraManager.strDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    private void initViews() {
        pb = (ProgressBar) findViewById(R.id.reco_recognize_bar);
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        mShutter = (ImageButton) findViewById(R.id.camera_shutter);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(ACameraActivity.this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mShutter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCameraManager.requestFocuse();
                mShutter.setEnabled(false);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCameraManager.openCamera(mSurfaceHolder);
            if(flashModel ==null || !mCameraManager.isSupportFlash(flashModel)){
                flashModel =mCameraManager.getDefaultFlashMode();
            }
            mCameraManager.setCameraFlashMode(flashModel);
        }catch(RuntimeException e){
            Toast.makeText(ACameraActivity.this, R.string.camera_open_error,Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            Toast.makeText(ACameraActivity.this, R.string.camera_open_error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(width>height){
            mCameraManager.setPreviewSize(width,height);
        }else{
            mCameraManager.setPreviewSize(height,width);
        }
        mCameraManager.initPreView();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.closeCamera();
    }

    public static String Scan(String type,byte[] file,String ext){
        String xml = HttpUtil.getSendXML(type, ext);
        return HttpUtil.send(xml, file);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
