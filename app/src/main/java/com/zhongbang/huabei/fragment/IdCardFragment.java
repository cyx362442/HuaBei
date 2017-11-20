package com.zhongbang.huabei.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.RenZhengActivity;
import com.zhongbang.huabei.contract.Config;
import com.zhongbang.huabei.event.ID_Front;
import com.zhongbang.huabei.utils.FileUtil;
import com.zhongbang.huabei.utils.ToastUtil;
import com.zhongbang.huabei.yunmai.CameraManager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class IdCardFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private int REQUEST_FRONT=100;
    private int REQUEST_BAKC=200;
    private final int REQUEST_CODE_CAMERA=2;
    public static final String action="idcard.scan";
    private ImageView mImgIDFront;
    private ImageView mImgIDReverse;
    private Intent mIntent;
    private PopupMenu mPopupMenu;
    private int mImageId;
    private Handler mHandler=new Handler();
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
        if(getString(R.string.audited).equals(Config.audit)){
            mImgIDFront.setEnabled(false);
            mImgIDReverse.setEnabled(false);
        }
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
            switch (requestCode) {
                case 100:
                    if(uri==null){
                        return;
                    }
                    setImages(uri,mImgIDFront);
                    break;
                case 200:
                    if(uri==null){
                        return;
                    }
                    setImages(uri,mImgIDReverse);
                    break;
                case REQUEST_CODE_CAMERA:
                        String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                        if (!TextUtils.isEmpty(contentType)) {
                            if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                                String idfrontPath = FileUtil.getSaveFile(getString(R.string.idfront)).getAbsolutePath();
                                recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, idfrontPath);
                            } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                                String idbackPath = FileUtil.getSaveFile(getString(R.string.idback)).getAbsolutePath();
                                recIDCard(IDCardParams.ID_CARD_SIDE_BACK, idbackPath);
                            }
                        }
                    break;
            }
        }
    }

    private void setImages(Uri uri, final ImageView view) {
        final String uriPath = getUriAbstractPath(uri);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                File file = new File(uriPath);
                Glide.with(getActivity()).load(file).skipMemoryCache(true).
                        diskCacheStrategy(DiskCacheStrategy.NONE).
                        error(R.mipmap.id_reverse).centerCrop().into(view);
            }
        });
    }

    private void recIDCard(final String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    if(idCardSide.equals(IDCardParams.ID_CARD_SIDE_FRONT)){
                        setIdData(mImgIDFront,result.getName().getWords(),
                                result.getIdNumber().getWords(),getString(R.string.idfront));
                    }else if(idCardSide.equals(IDCardParams.ID_CARD_SIDE_BACK)){
                        setIdData(mImgIDReverse,result.getSignDate().getWords()+"-"+result.getExpiryDate().getWords(),
                                "",getString(R.string.idback));
                    }
                }
            }
            @Override
            public void onError(final OCRError error) {
               mHandler.post(new Runnable() {
                   @Override
                   public void run() {
                       ToastUtil.showShort(getActivity(),error+"");
                   }
               });
            }
        });
    }
    //设置图片
    private void setIdData(final ImageView image, String str1, String str2, final String imgName) {
        EventBus.getDefault().post(new ID_Front(str1,str2));
        final File file=FileUtil.getSaveFile(imgName);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getActivity()).load(file).skipMemoryCache(true).
                        diskCacheStrategy(DiskCacheStrategy.NONE).
                        error(R.mipmap.id_reverse).centerCrop().into(image);
            }
        });
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

    private void choiseImage(int code) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, code);
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
                choiseImage(REQUEST_FRONT);
            } else {//拍照获取
                if(checkTokenStatus()){
                    mIntent = new Intent(getActivity(), CameraActivity.class);
                    mIntent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                            FileUtil.getSaveFile(getString(R.string.idfront)).getAbsolutePath());
                    mIntent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                    startActivityForResult(mIntent, REQUEST_CODE_CAMERA);
                }
            }
        } else if (mImageId == R.id.img_id_reverse) {//身份证反面
            if (item.getItemId() == R.id.photo) {//从相册获取
                choiseImage(REQUEST_BAKC);
            } else {//拍照获取
                mIntent = new Intent(getActivity(), CameraActivity.class);
                mIntent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getString(R.string.idback)).getAbsolutePath());
                mIntent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(mIntent, REQUEST_CODE_CAMERA);
            }
        }
        return false;
    }

    private boolean checkTokenStatus() {
        if (!RenZhengActivity.hasGotToken) {
            Toast.makeText(getActivity(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return RenZhengActivity.hasGotToken;
    }
}
