package com.zhongbang.huabei.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhongbang.huabei.R;
import com.zhongbang.huabei.app.App;
import com.zhongbang.huabei.app.main_center.OfficalActivity;
import com.zhongbang.huabei.fragment.ImageShowActivity;

/**
 * Created by Administrator on 2017-09-27.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHold>{
    private Context context;
    private String[]imgs;
    private final LayoutInflater mLayoutInflater;

    public ImageAdapter(Context context,String[] imgs) {
        this.context=context;
        this.imgs = imgs;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater.inflate(R.layout.image_item, null);
        ViewHold viewHold = new ViewHold(inflate);
        viewHold.mImageView= (ImageView) inflate.findViewById(R.id.image);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {
        String img = imgs[position];
        Glide.with(App.getContext()).load(img).centerCrop().into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageShowActivity.class);
                intent.putExtra("imgs",imgs);
                intent.putExtra("index",position);
                context.startActivity(intent);
            }
        });
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
    }
}
