package com.zhongbang.huabei.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.App;

/**
 * Created by Administrator on 2017-09-27.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHold>{
    private String[]imgs;
    private final LayoutInflater mLayoutInflater;

    public ImageAdapter(String[] imgs) {
        this.imgs = imgs;
        mLayoutInflater = LayoutInflater.from(App.getContext());
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.image_item, null);
        ViewHold viewHold = new ViewHold(inflate);
        viewHold.mImageView= (ImageView) inflate.findViewById(R.id.image);
        viewHold.mTextView= (TextView) inflate.findViewById(R.id.tv_url);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        String img = imgs[position];
        Glide.with(App.getContext()).load(img).centerCrop().into(holder.mImageView);
        holder.mTextView.setText(imgs[position]);
    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }

    class ViewHold extends RecyclerView.ViewHolder{
        public ViewHold(View itemView) {
            super(itemView);
        }
        ImageView mImageView;
        TextView mTextView;
    }
}
