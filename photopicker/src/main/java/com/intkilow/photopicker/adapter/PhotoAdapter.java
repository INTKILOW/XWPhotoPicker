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
    private LinkedList<PhotoEntity> mSelectPhotoList = new LinkedList<>();
    private Context mContext;
    private float mW = 0;
    private int mMargin = DisplayUtil.dpToPx(2);
    private int color = Color.parseColor("#4C4C4C");

    private int count = 0;


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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        PhotoEntity photoEntity = mList.get(position);
        boolean select = false;
        for (int i = 0; i < mSelectPhotoList.size(); i++) {
            if(photoEntity.getFilePath() .equals(mSelectPhotoList.get(i).getFilePath())) {
                photoEntity.setCount(i + 1);
                select = true;
                break;
            }

        }
        photoEntity.setSelect(select);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.image.getLayoutParams();

//            layoutParams.width = (int) w;
        layoutParams.height = (int) mW;
        Glide.with(mContext)
                .load(photoEntity.getFilePath())
                .placeholder(new ColorDrawable(color))
                .error(new ColorDrawable(color))
                .into(viewHolder.image);
        viewHolder.image.setEnableSelect(photoEntity.isCanSelect());
        viewHolder.image.setIsGIF(photoEntity.isiGif());
        viewHolder.image.setIsVIDEO(photoEntity.isiGif());
        if(photoEntity.getCount() == mSelectPhotoList.size()){
            viewHolder.image.setSelect(select,photoEntity.getCount(),true);
        }else{
            viewHolder.image.setSelect(select,photoEntity.getCount(),false);
        }


        viewHolder.image.setImageClickCall( new ImageItem.ImageClickCall() {
            @Override
            public void onRectClick() {
                boolean select = mList.get(position).isSelect();


                if(!select){
                    mSelectPhotoList.add(mList.get(position));
                }else{
                    mSelectPhotoList.remove(mList.get(position));
                }




//                mList.get(position).setCount(count);
               // mList.get(position).setSelect(!select);
//                notifyItemChanged(position);

                  notifyDataSetChanged();
            }

            @Override
            public void onImageClick() {

            }
        });


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
