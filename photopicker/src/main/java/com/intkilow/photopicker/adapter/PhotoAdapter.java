package com.intkilow.photopicker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.intkilow.photopicker.R;
import com.intkilow.photopicker.entity.PhotoEntity;
import com.intkilow.photopicker.utils.DisplayUtil;
import com.intkilow.photopicker.view.ImageItem;

import java.util.LinkedList;

/**
 * Copyright (C), 2015/6/12, 日照安泰科技发展有限公司
 * Author: flyzhang
 * Date: 2019/12/11 16:01
 * Description:
 * <p>
 * </p>
 * History:
 * <author>      <time>      <version>      <desc>
 * 作者姓名       修改时间     版本号         描述
 */
public class PhotoAdapter extends RecyclerView.Adapter {

    private LinkedList<PhotoEntity> mList;

    private Context mContext;
    private float mW = 0;
    private int mMargin = DisplayUtil.dpToPx(2);
    private int color = Color.parseColor("#4C4C4C");


    public PhotoAdapter(LinkedList<PhotoEntity> list) {
        mList = list;
        mW = (DisplayUtil.getScreenWidth() - 5 * mMargin) / 4;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.photo_items, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.image.getLayoutParams();

//            layoutParams.width = (int) w;
        layoutParams.height = (int) mW;
        Glide.with(mContext)
                .load(mList.get(position).getFilePath())
                .placeholder(new ColorDrawable(color))
                .error(new ColorDrawable(color))
                .into(viewHolder.image);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageItem image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
