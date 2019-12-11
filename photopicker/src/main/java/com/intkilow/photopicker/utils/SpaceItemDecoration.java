package com.intkilow.photopicker.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Copyright (C), 2015/6/12, 日照安泰科技发展有限公司
 * Author: flyzhang
 * Date: 2019/12/11 16:08
 * Description:
 * <p>
 * </p>
 * History:
 * <author>      <time>      <version>      <desc>
 * 作者姓名       修改时间     版本号         描述
 */
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