package com.zhongbang.huabei.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.App;
import com.zhongbang.huabei.bean.Document;

import java.util.List;

/**
 * Created by Administrator on 2017-09-27.
 */

public class DocumentAdapter extends BaseQuickAdapter<Document> {
    private Context mContext;
    public DocumentAdapter(List<Document> data,Context context) {
        super(R.layout.recy_document_item,data);
        mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Document document) {
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
                View inflate = LayoutInflater.from(mContext).inflate(R.layout.popu_item, null, false);
                PopupWindow popupWindow = new PopupWindow(inflate,400,80,true);
                popupWindow.showAsDropDown(view);
            }
        });
    }
}
