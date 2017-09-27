package com.zhongbang.huabei.adapter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

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
    public DocumentAdapter(List<Document> data) {
        super(R.layout.recy_document_item,data);
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
            ImageAdapter adapter = new ImageAdapter(imgUrl);
            rvItem.setAdapter(adapter);
        }
    }
}