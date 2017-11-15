package com.zhongbang.huabei.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.App;
import com.zhongbang.huabei.bean.Document;
import com.zhongbang.huabei.utils.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2017-09-27.
 */

public class DocumentAdapter extends BaseQuickAdapter<Document> implements View.OnClickListener {
    private Context mContext;
    private final PopupWindow mPopupWindow;
    private Document mDocument;

    public DocumentAdapter(List<Document> data,Context context) {
        super(R.layout.recy_document_item,data);
        mContext=context;
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.popu_item, null, true);
        mPopupWindow = new PopupWindow(inflate,500,80,true);
        inflate.findViewById(R.id.tv_saveImg).setOnClickListener(this);
        inflate.findViewById(R.id.tv_copyText).setOnClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Document document) {
        baseViewHolder.setText(R.id.tv_document,document.getContent());
        baseViewHolder.setText(R.id.tv_time,document.getCreateTime());
        RecyclerView rvItem = baseViewHolder.getView(R.id.recy_item);
        if(TextUtils.isEmpty(document.getImages())){
            rvItem.setVisibility(View.GONE);
        }else{
            rvItem.setLayoutManager(new GridLayoutManager(App.getContext(),3));
            rvItem.addItemDecoration(new SpaceItemDecoration(3));
            rvItem.setItemAnimator(new DefaultItemAnimator());
            String[] imgUrl = document.getImages().split(",");
            ImageAdapter adapter = new ImageAdapter(mContext,imgUrl);
            rvItem.setAdapter(adapter);
        }
        baseViewHolder.setOnClickListener(R.id.image, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.showAsDropDown(view);
                mDocument=document;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_saveImg){
            ToastUtil.showShort(mContext,"保存图片");
        }else if(view.getId()==R.id.tv_copyText){
            ToastUtil.showShort(mContext,"复制文案");
            String content = mDocument.getContent();
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", content);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
        }
    }
}
