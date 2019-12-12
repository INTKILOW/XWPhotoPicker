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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class PhotoAdapter extends RecyclerView.Adapter {

    private LinkedList<PhotoEntity> mList;
    private Context mContext;
    private float mW = 0;
    private int mMargin = DisplayUtil.dpToPx(2);
    private int color = Color.parseColor("#4C4C4C");
    private boolean canSelect = true;

    private boolean isAddAction = false;

    private HashMap<Integer, PhotoEntity> map = new LinkedHashMap<>();
    private ItemClick itemClick;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.image.getLayoutParams();
        layoutParams.height = (int) mW;
        PhotoEntity photoEntity = mList.get(position);

        boolean select = false;
        int count = 1;
        if (map.containsKey(position)) {
            viewHolder.image.setEnableSelect(true);
            select = true;
            for (Map.Entry<Integer, PhotoEntity> integerPhotoEntityEntry : map.entrySet()) {

                if (integerPhotoEntityEntry.getKey() == position) {
                    photoEntity = integerPhotoEntityEntry.getValue();
                    break;
                }
                count++;
            }
        } else {
            viewHolder.image.setEnableSelect(canSelect);
        }


        Glide.with(mContext)
                .asBitmap()
                .load(photoEntity.getFilePath())
                .placeholder(new ColorDrawable(color))
                .error(new ColorDrawable(color))
                .dontAnimate()
                .into(viewHolder.image);

        viewHolder.image.setIsGIF(photoEntity.isiGif());
        viewHolder.image.setIsVIDEO(photoEntity.isiGif());
        viewHolder.image.setSelect(select, count, count == map.size() && isAddAction);


        viewHolder.image.setImageClickCall(new ImageItem.ImageClickCall() {
            @Override
            public void onRectClick(boolean enable) {
                if (!enable) {
                    if (null != itemClick) {
                        itemClick.onSelect(map.size(), false);
                    }
                    return;
                }
                PhotoEntity photoEntity = mList.get(position);


                if (!map.containsKey(position)) {
                    isAddAction = true;
                    map.put(position, photoEntity);

                    if (map.size() >= 9) {
                        canSelect = false;
                        notifyDataSetChanged();
                    } else {
                        canSelect = true;
                        notifyItemChanged(position);
                    }

                } else {
                    isAddAction = false;
                    canSelect = true;
                    int k = 0;
                    for (Map.Entry<Integer, PhotoEntity> integerPhotoEntityEntry : map.entrySet()) {
                        if (position == integerPhotoEntityEntry.getKey()) {
                            break;
                        }
                        k++;
                    }
                    map.remove(position);
                    if (map.size() + 1 >= 5) {
                        notifyDataSetChanged();
                    } else {
                        int m = 0;
                        for (Map.Entry<Integer, PhotoEntity> integerPhotoEntityEntry : map.entrySet()) {
                            int key = integerPhotoEntityEntry.getKey();
                            if (m >= k) {
                                notifyItemChanged(key);
                            }
                            m++;

                        }
                        notifyItemChanged(position);

                    }
                }

                if (null != itemClick) {
                    itemClick.onSelect(map.size(), true);
                }
            }

            @Override
            public void onImageClick() {
                if (null != itemClick) {
                    itemClick.onImageClick(position);
                }
            }
        });


    }

    public HashMap<Integer, PhotoEntity> getMap() {
        return map;
    }

    public LinkedList<PhotoEntity> getList() {
        return mList;
    }

    public void setList(LinkedList<PhotoEntity> mList) {
        this.mList = mList;
    }

    public ItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
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

    public interface ItemClick {
        void onSelect(int count, boolean enable);

        void onImageClick(int position);
    }


}
