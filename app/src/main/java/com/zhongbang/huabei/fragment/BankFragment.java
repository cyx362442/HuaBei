package com.zhongbang.huabei.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.event.Bank;
import com.zhongbang.huabei.service.RecognizeService;
import com.zhongbang.huabei.utils.FileUtil;
import com.zhongbang.huabei.yunmai.ACameraActivity;
import org.greenrobot.eventbus.EventBus;
import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class BankFragment extends Fragment implements View.OnClickListener
        ,PopupMenu.OnMenuItemClickListener {
    private static final int BANKCARD_FRONT = 110;
    private static final int BANDCARD_REVERSE=120;
    private static final int BANKCARD_FRONT_PHOTO=130;
    private static final int BANKCARD_REVERSE_PHOTO=140;
    public static final String action="bankcard.scan";
    private Intent mIntent;
    private PopupMenu mPopupMenu;
    private int mImageId;
    private ImageView mImgBankfront;
    private ImageView mImgBankreverse;
    private Handler mHandler=new Handler();

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
            switch (requestCode) {
                case BANKCARD_FRONT_PHOTO:
                    if(uri==null){
                        return;
                    }
                    setImageView(uri,mImgBankfront);
                    break;
                case BANKCARD_REVERSE_PHOTO:
                    if(uri==null){
                        return;
                    }
                    setImageView(uri,mImgBankfront);
                    break;
                case BANKCARD_FRONT:
                    RecognizeService.recBankCard(FileUtil.getSaveFile(getString(R.string.bankfront)).getAbsolutePath(),
                            new RecognizeService.ServiceListener() {
                                @Override
                                public void onResult(BankCardResult result) {
                                    setBankData(mImgBankfront,result.getBankName(),result.getBankCardNumber(),getString(R.string.bankfront));
                                }
                            });
                    break;
                case BANDCARD_REVERSE:
                    final File file = FileUtil.getSaveFile(getString(R.string.bankreverse));
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getActivity()).load(file).skipMemoryCache(true).
                                    diskCacheStrategy(DiskCacheStrategy.NONE).
                                    error(R.mipmap.id_reverse).centerCrop().into(mImgBankreverse);
                        }
                    });
                    break;
            }
        }
    }

    private void setImageView(Uri uri, final ImageView view) {
        String uriPath = getUriAbstractPath(uri);
        final File file1 = new File(uriPath);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Glide.with(getActivity()).load(file1).skipMemoryCache(true).
                        diskCacheStrategy(DiskCacheStrategy.NONE).
                        error(R.mipmap.id_reverse).centerCrop().into(view);
            }
        });
    }

    public void setBankFrontImg(String url){
        Glide.with(getActivity()).load(url).into(mImgBankfront);
    }
    public void setImgBankreverseImg(String url){
        Glide.with(getActivity()).load(url).into(mImgBankreverse);
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

    //设置图片
    private void setBankData(final ImageView image, String str1, String str2, String imgName) {
        EventBus.getDefault().post(new Bank(str1,str2));
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
    public boolean onMenuItemClick(MenuItem item) {
        if (mImageId == R.id.img_bank_front) {//银行卡正面
            if (item.getItemId() == R.id.photo) {//从相册获取
                choiseImage(BANKCARD_FRONT_PHOTO);
            } else {//拍照获取
                Intent intent = new Intent(getActivity(), CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        FileUtil.getSaveFile(getString(R.string.bankfront)).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, BANKCARD_FRONT);
            }
        } else if (mImageId == R.id.img_bank_reverse) {//身份证反面
            if (item.getItemId() == R.id.photo) {//从相册获取
                choiseImage(BANKCARD_REVERSE_PHOTO);
            } else {//拍照获取
                mIntent = new Intent(getActivity(), ACameraActivity.class);
                mIntent.putExtra(getString(R.string.imageName), getString(R.string.bankreverse));
                startActivityForResult(mIntent, BANDCARD_REVERSE);
            }
        }
        return false;
    }

    private void choiseImage(int code) {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, code);
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
