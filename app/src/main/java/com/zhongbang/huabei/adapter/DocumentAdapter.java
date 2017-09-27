package com.zhongbang.huabei.adapter;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhongbang.huabei.R;
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
    }
}
