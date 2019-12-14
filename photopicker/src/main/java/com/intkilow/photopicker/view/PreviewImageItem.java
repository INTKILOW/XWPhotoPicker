package com.intkilow.photopicker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.intkilow.photopicker.utils.DisplayUtil;


public class PreviewImageItem extends AppCompatImageView {
    private Paint paint = new Paint();
    private boolean select = false;
    private boolean delete = false;
    public PreviewImageItem(Context context) {
        super(context);
        init();
    }

    public PreviewImageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setStrokeWidth(DisplayUtil.dpToPx(6));
        paint.setColor(Color.parseColor("#07C15C"));
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (select) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }

        if (delete && !select) {
            canvas.drawARGB(160, 255, 255, 255);//不能选择的白色透明度
        }
    }

    public void setDelete(boolean delete) {
        this.delete = delete;

    }

    public void setSelect(boolean select) {
        this.select = select;

    }

    public void updateUI() {
        invalidate();
    }


}
