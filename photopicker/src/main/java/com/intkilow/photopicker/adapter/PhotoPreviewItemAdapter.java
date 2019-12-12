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
import com.intkilow.photopicker.view.PreviewImageItem;

import java.util.List;


public class PhotoPreviewItemAdapter extends RecyclerView.Adapter {

    private List<PhotoEntity> mList;
    private Context mContext;
    private int color = Color.parseColor("#4C4C4C");

    private ItemClick mItemClick;

    public PhotoPreviewItemAdapter(List<PhotoEntity> list) {
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.photo_preivew_select_items, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        PhotoEntity photoEntity = mList.get(position);

        Glide.with(mContext)
                .asBitmap()
                .load(photoEntity.getFilePath())
                .placeholder(new ColorDrawable(color))
                .error(new ColorDrawable(color))
                .dontAnimate()
                .into(viewHolder.image);


        viewHolder.image.setSelect(photoEntity.isSelect());
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemClick) {
                    mItemClick.onItemClick(position);
                }
            }
        });
    }


    public ItemClick getItemClick() {
        return mItemClick;
    }

    public void setItemClick(ItemClick mItemClick) {
        this.mItemClick = mItemClick;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<PhotoEntity> getList() {
        return mList;
    }

    public void setList(List<PhotoEntity> mList) {
        this.mList = mList;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        PreviewImageItem image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    public interface ItemClick {
        void onItemClick(int position);
    }


}
