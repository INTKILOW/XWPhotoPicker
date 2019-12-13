package com.intkilow.photopicker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.intkilow.photopicker.R;
import com.intkilow.photopicker.entity.FolderEntity;

import java.util.List;


public class FolderPreviewAdapter extends RecyclerView.Adapter {

    private List<FolderEntity> mList;
    private Context mContext;
    private int color = Color.parseColor("#4C4C4C");
    private ItemClick mItemClick;
    private int mLastPosition = 0;

    public FolderPreviewAdapter(List<FolderEntity> list) {
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.folder_choose_items, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        FolderEntity folderEntity = mList.get(position);

        Glide.with(mContext)
                .asBitmap()
                .load(folderEntity.getCover())
                .placeholder(new ColorDrawable(color))
                .error(new ColorDrawable(color))
                .dontAnimate()
                .into(viewHolder.cover);
        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mItemClick) {
                    mItemClick.onItemClick(position);
                }

            }
        });

        if (folderEntity.isSelect()) {
            viewHolder.select.setVisibility(View.VISIBLE);
        } else {
            viewHolder.select.setVisibility(View.GONE);
        }
        viewHolder.count.setText(String.valueOf(folderEntity.getCount()));
        viewHolder.title.setText(folderEntity.getTitle());
    }

    public void updateSelect(int p) {
        if (p != mLastPosition) {
            mList.get(mLastPosition).setSelect(false);
            notifyItemChanged(mLastPosition);
            notifyItemChanged(p);
            mList.get(p).setSelect(true);
            mLastPosition = p;
        }
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

    public List<FolderEntity> getList() {
        return mList;
    }

    public void setList(List<FolderEntity> mList) {
        this.mList = mList;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover, select;
        ConstraintLayout constraintLayout;
        TextView title, count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.cover);
            select = itemView.findViewById(R.id.select);
            title = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);
            constraintLayout = itemView.findViewById(R.id.layout);
        }
    }

    public interface ItemClick {
        void onItemClick(int position);
    }


}
