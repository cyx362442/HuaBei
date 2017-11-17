package com.zhongbang.huabei.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.RenZhengActivity;
import com.zhongbang.huabei.bean.IDCard;
import com.zhongbang.huabei.bean.IDCard_Reverse;
import com.zhongbang.huabei.event.ID_Front;
import com.zhongbang.huabei.event.StartAnim;
import com.zhongbang.huabei.utils.FileUtil;
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
public class IdCardFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private int REQUEST_CODE=1;
    private static byte[] bytes;
    private static String extension;
    private final int IMPORT_CODE=1;
    private final int REQUEST_CODE_CAMERA=2;
    private final int IDRESVERSE_CODE=3;
    public static final String action="idcard.scan";
    private ImageView mImgIDFront;
    private ImageView mImgIDReverse;
    private Intent mIntent;
    private PopupMenu mPopupMenu;
    private int mImageId;

    public IdCardFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_card, container, false);
        mImgIDFront = (ImageView) inflate.findViewById(R.id.img_id_front);
        mImgIDReverse = (ImageView) inflate.findViewById(R.id.img_id_reverse);
        mImgIDFront.setOnClickListener(this);
        mImgIDReverse.setOnClickListener(this);
        return inflate;
    }

    public void setIDFrontImg(String imgUrl){
        Glide.with(getActivity()).load(imgUrl).into(mImgIDFront);
    }
    public void setIDReverseImg(String imgUrl){
        Glide.with(getActivity()).load(imgUrl).into(mImgIDReverse);
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
                case REQUEST_CODE_CAMERA:
//                    setIDFront(result);
                        String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                        String filePath = FileUtil.getSaveFile(getString(R.string.idfront)).getAbsolutePath();
                        Log.e("filePath=====",filePath);
                        if (!TextUtils.isEmpty(contentType)) {
                            if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
//                                recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                            } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
//                                recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                            }
                        }
                    break;
                case IDRESVERSE_CODE:
                    setIDReverse(result);
                    break;
            }
        }
    }
    //设置身份证反面
    private void setIDReverse(String result) {
        Gson gson = new Gson();
        IDCard_Reverse cardReverse = gson.fromJson(result, IDCard_Reverse.class);
        String valid_period = cardReverse.getData().getItem().getValid_period();
        setIdData(mImgIDReverse,valid_period,"",getString(R.string.idreverse));
    }
    //设置身份证正面
    private void setIDFront(String reslut) {
//        Gson gson = new Gson();
//        IDCard idCard = gson.fromJson(reslut, IDCard.class);
//        IDCard.DataBean.ItemBean itemBean = idCard.getData().getItem();
//        setIdData(mImgIDFront,itemBean.getName(),itemBean.getCardno(),getString(R.string.idfront));
        Log.e("resutlt=====",reslut+"a");
    }
    //设置图片
    private void setIdData(ImageView image,String str1,String str2,String imgName) {
        EventBus.getDefault().post(new ID_Front(str1,str2));
        File file = new File(CameraManager.strDir, imgName);
        Glide.with(this).load(file).skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.NONE).
                error(R.mipmap.id_reverse).centerCrop().into(image);
    }

    @Override
    public void onClick(View v) {
        mImageId = v.getId();
        if(mImageId ==R.id.img_id_front){
            showPopu(mImgIDFront);
        }else if(mImageId ==R.id.img_id_reverse){
            showPopu(mImgIDReverse);
        }
    }

    private void showPopu(final View view) {
        mPopupMenu = new PopupMenu(getActivity(),view);
        mPopupMenu.getMenuInflater().inflate(R.menu.popumenu, mPopupMenu.getMenu());
        mPopupMenu.show();
        mPopupMenu.setOnMenuItemClickListener(this);
    }

    private void choiseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (mImageId == R.id.img_id_front) {//身份证正面
            if (item.getItemId() == R.id.photo) {//从相册获取
                choiseImage();
            } else {//拍照获取
//                mIntent = new Intent(getActivity(), ACameraActivity.class);
//                mIntent.putExtra(getString(R.string.imageName), getString(R.string.idfront));
//                startActivityForResult(mIntent, TAKEPHOTO_CODE);
                if(checkTokenStatus()){
                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                            FileUtil.getSaveFile(getString(R.string.idfront)).getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }
            }
        } else if (mImageId == R.id.img_id_reverse) {//身份证反面
            if (item.getItemId() == R.id.photo) {//从相册获取
                choiseImage();
            } else {//拍照获取
                mIntent = new Intent(getActivity(), ACameraActivity.class);
                mIntent.putExtra(getString(R.string.imageName), getString(R.string.idreverse));
                startActivityForResult(mIntent, IDRESVERSE_CODE);
            }
        }
        return false;
    }

    class MyAsynTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            EventBus.getDefault().post(new StartAnim(getString(R.string.idcommit)));
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
        if(mImageId==R.id.img_id_front){
            setIDFront(result);
        }else if(mImageId==R.id.img_id_reverse){
            setIDReverse(result);
        }
    }
    public static String startScan(){
        String xml = HttpUtil.getSendXML(action,extension);
        return HttpUtil.send(xml,bytes);
    }

    private boolean checkTokenStatus() {
        if (!RenZhengActivity.hasGotToken) {
            Toast.makeText(getActivity(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return RenZhengActivity.hasGotToken;
    }
}
