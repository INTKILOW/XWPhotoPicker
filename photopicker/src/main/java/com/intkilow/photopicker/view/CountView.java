package com.intkilow.photopicker.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.intkilow.photopicker.utils.DisplayUtil;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class CountView extends AppCompatImageView {
    private Paint mTextPaint = new Paint();
    private boolean mIsSelect = false;//默认补选中

    private float mOffset = 0;

    Paint paint = new Paint();
    private Paint countPaint = new Paint();
    private Paint bgPaint = new Paint();
    private int mCount = 1;//当前数量
    int radius = DisplayUtil.dpToPx(12);//圆半径
    int radiusW = DisplayUtil.dpToPx(1);//圆环宽度

    private int scaleW = DisplayUtil.dpToPx(11);//圆圈缩放大小
    private int rectW = DisplayUtil.dpToPx(4);

    private boolean mEnableSelect = true;//是否可以选中
    private int color = Color.parseColor("#07C15C");
    Path path = new Path();

    public CountView(Context context) {
        super(context);
        init();
    }

    public CountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(DisplayUtil.dpToPx(12));
        paint.setAntiAlias(true);

        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor("#4E4D4B"));
        countPaint.setAntiAlias(true);
        countPaint.setTextSize(DisplayUtil.dpToPx(15));
        countPaint.setAntiAlias(true);
        countPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        canvas.drawCircle(cx, cy, radius - radiusW, bgPaint);
        if (!mIsSelect || !mEnableSelect) {
            paint.setStrokeWidth(radiusW);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(cx, cy, radius, paint);
            mTextPaint.setColor(Color.WHITE);

            //画对勾省略

            path.moveTo(cx - rectW, cy);
            path.lineTo(cx, cy + rectW);
            path.lineTo(cx + rectW, cy - rectW);
            canvas.drawPath(path, paint);
        } else {

            //选中
            mTextPaint.setColor(color);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, radius - mOffset, paint);
            String temp = String.valueOf(mCount);
            float textWidth = countPaint.measureText(temp);
            float baseLineY = Math.abs(countPaint.ascent() + countPaint.descent()) / 2;
            canvas.drawText(temp, cx - textWidth / 2, cy + baseLineY, countPaint);
        }


    }

    public boolean isIsSelect() {
        return mIsSelect;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public void setIsSelect(boolean mIsSelect) {
        this.mIsSelect = mIsSelect;
    }

    public void setSelect(boolean select, int count, boolean animation) {
        mCount = count;
        setSelect(select, animation);
    }

    /**
     * 设置选中
     *
     * @param select
     */
    private void setSelect(boolean select, boolean animation) {
        if (!mEnableSelect) {
            return;
        }

        mIsSelect = select;

        if (!mIsSelect) {
            invalidate();
        } else {
            if (animation) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f);
                valueAnimator.setInterpolator(new SpringScaleInterpolator());
                valueAnimator.setDuration(800);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float f = (float) animation.getAnimatedValue();
                        mOffset = scaleW - f * scaleW;
                        invalidate();
                    }
                });
                valueAnimator.start();
            } else {
                invalidate();
            }

        }
    }

    public class SpringScaleInterpolator implements Interpolator {
        float factor = 0.4f;

        @Override
        public float getInterpolation(float x) {
            return (float) (pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1);
            //      return (float) (pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1);
            //  return cubicHermite(x,0,1,4,4);
            //这个公式在http://inloop.github.io/interpolator/中测试获取
        }
    }
}
