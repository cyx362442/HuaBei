package com.zhongbang.huabei.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.bean.BankCard;
import com.zhongbang.huabei.event.Bank;
import com.zhongbang.huabei.event.StartAnim;
import com.zhongbang.huabei.yunmai.ACameraActivity;
import com.zhongbang.huabei.yunmai.CameraManager;
import com.zhongbang.huabei.yunmai.HttpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class BankFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private int REQUEST_CODE=1;
    private static byte[] bytes;
    private static String extension;
    private final int IMPORT_CODE=1;
    private final int TAKEPHOTO_CODE=2;
    private final int IDRESVERSE_CODE=3;
    public static final String action="bankcard.scan";
    private Intent mIntent;
    private PopupMenu mPopupMenu;
    private int mImageId;
    private ImageView mImgBankfront;
    private ImageView mImgBankreverse;

    public BankFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_bank, container, false);
        mImgBankfront = (ImageView) inflate.findViewById(R.id.img_bank_front);
        mImgBankreverse = (ImageView) inflate.findViewById(R.id.img_bank_reverse);
        mImgBankfront.setOnClickListener(this);
        mImgBankreverse.setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        Uri uri = data.getData();
        if(resultCode== Activity.RESULT_OK){
            String result = data.getStringExtra("result");
            switch (requestCode) {
                case IMPORT_CODE:
                    if(uri==null){
                        return;
                    }
                    try {
                        String uriPath = getUriAbstractPath(uri);
                        extension = getExtensionByPath(uriPath);
                        InputStream is=getActivity().getContentResolver().openInputStream(uri);
                        bytes = HttpUtil.Inputstream2byte(is);
                        Log.d("bytes:  ", bytes.length+"");
                        if(!(bytes.length>(1000*1024*5))){
                            new MyAsynTask().execute();
                        }else{
                            Toast.makeText(getActivity(), "图片太大！！！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case TAKEPHOTO_CODE:
                    setBankFront(result);
                    break;
                case IDRESVERSE_CODE:
                    File file = new File(CameraManager.strDir, getString(R.string.bankreverse));
                    Glide.with(this).load(file).skipMemoryCache(true).
                            diskCacheStrategy(DiskCacheStrategy.NONE).
                            error(R.mipmap.id_reverse).centerCrop().into(mImgBankreverse);
                    break;
            }
        }
    }

    public void setBankFrontImg(String url){
        Glide.with(getActivity()).load(url).into(mImgBankfront);
    }
    public void setImgBankreverseImg(String url){
        Glide.with(getActivity()).load(url).into(mImgBankreverse);
    }

    /**
     * 根据路径获取文件扩展名
     * @param path
     */
    private String getExtensionByPath(String path) {
        if(path!=null){
            return path.substring(path.lastIndexOf(".")+1);
        }
        return null;
    }

    /**
     * 根据uri获取绝对路径
     * @param uri
     */
    private String getUriAbstractPath(Uri uri) {
        {
            // can post image
            String [] proj={MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().managedQuery( uri,proj,null,null,null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }
    //设置银行卡正面
    private void setBankFront(String result) {
        Gson gson = new Gson();
        BankCard bankCard = gson.fromJson(result, BankCard.class);
        BankCard.DataBean.ItemBean item = bankCard.getData().getItem();
        setBankData(mImgBankfront,item.getBankname(),item.getCardno(),getString(R.string.bankfront));
    }
    //设置图片
    private void setBankData(ImageView image,String str1,String str2,String imgName) {
        EventBus.getDefault().post(new Bank(str1,str2));
        File file = new File(CameraManager.strDir, imgName);
        Glide.with(this).load(file).skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.NONE).
                error(R.mipmap.id_reverse).centerCrop().into(image);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (mImageId == R.id.img_bank_front) {//银行卡正面
            if (item.getItemId() == R.id.photo) {//从相册获取
                choiseImage();
            } else {//拍照获取
                mIntent = new Intent(getActivity(), ACameraActivity.class);
                mIntent.putExtra(getString(R.string.imageName), getString(R.string.bankfront));
                startActivityForResult(mIntent, TAKEPHOTO_CODE);
            }
        } else if (mImageId == R.id.img_bank_reverse) {//身份证反面
            if (item.getItemId() == R.id.photo) {//从相册获取
                choiseImage();
            } else {//拍照获取
                mIntent = new Intent(getActivity(), ACameraActivity.class);
                mIntent.putExtra(getString(R.string.imageName), getString(R.string.bankreverse));
                startActivityForResult(mIntent, IDRESVERSE_CODE);
            }
        }
        return false;
    }

    private void choiseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    class MyAsynTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            if(mImageId==R.id.img_bank_front){
                EventBus.getDefault().post(new StartAnim(getString(R.string.bankcommit)));
            }
        }
        @Override
        protected String doInBackground(Void... params) {
            return startScan();
        }
        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                handleResult(result);
            }
        }
    }

    /**
     * 处理服务器返回的结果
     * @param result
     */
    private void handleResult(String result) {
        if(mImageId==R.id.img_bank_front){
            setBankFront(result);
        }else if(mImageId==R.id.img_bank_reverse){
            File file = new File(CameraManager.strDir, getString(R.string.bankreverse));
            Glide.with(this).load(file).skipMemoryCache(true).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    error(R.mipmap.id_reverse).centerCrop().into(mImgBankreverse);
        }
    }
    public static String startScan(){
        String xml = HttpUtil.getSendXML(action,extension);
        return HttpUtil.send(xml,bytes);
    }

    @Override
    public void onClick(View v) {
        mImageId=v.getId();
        if(mImageId==R.id.img_bank_front){
            showPopu(mImgBankfront);
        }else if(mImageId==R.id.img_bank_reverse){
            showPopu(mImgBankreverse);
        }
    }
    private void showPopu(final View view) {
        mPopupMenu = new PopupMenu(getActivity(),view);
        mPopupMenu.getMenuInflater().inflate(R.menu.popumenu, mPopupMenu.getMenu());
        mPopupMenu.show();
        mPopupMenu.setOnMenuItemClickListener(this);
    }
}
