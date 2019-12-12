package com.intkilow.photopicker.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    int margin = DisplayUtil.dpToPx(2);

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = margin / 2;
        outRect.bottom = margin / 2;
        int i = parent.getChildLayoutPosition(view) % 4;
        switch (i) {
            case 0:
                outRect.left = margin;
                outRect.right = margin / 2;
                break;
            case 1:
            case 2:
                outRect.left = margin / 2;
                outRect.right = margin / 2;
                break;
            case 3:
                outRect.left = margin / 2;
                outRect.right = margin;
                break;
        }
    }


}