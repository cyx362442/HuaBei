package com.zhongbang.huabei.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.bean.NotifMessage;

import java.util.List;

/**
 * Created by Administrator on 2017-09-28.
 */

public class NotifactionAdapter extends BaseQuickAdapter<NotifMessage>{
    public NotifactionAdapter(List<NotifMessage> data) {
        super(R.layout.notif_item,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NotifMessage notifMessage) {
        baseViewHolder.setText(R.id.tv_title,notifMessage.getTitle());
        baseViewHolder.setText(R.id.tv_time,notifMessage.getTime());
    }
}
