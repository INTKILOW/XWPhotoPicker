package com.intkilow.photopicker.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.appcompat.widget.AppCompatImageView;

import com.intkilow.photopicker.utils.DisplayUtil;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class ImageItem extends AppCompatImageView {


    private Paint mPathPaint = new Paint();
    private Paint mTextPaint = new Paint();
    private int mRadius = DisplayUtil.dpToPx(5);
    private boolean mIsSelect = false;//默认补选中

    private float mOffset = 0;

    Paint paint = new Paint();
    Rect mClickRect = new Rect();//选择点击区域
    Point mClickPoint = new Point();

    private Paint countPaint = new Paint();
    private Paint bgPaint = new Paint();
    private int mCount = 1;//当前数量
    int paddingTop = DisplayUtil.dpToPx(8);//圆距离上边距
    int paddingRight = DisplayUtil.dpToPx(8);//圆距离右边距
    int radius = DisplayUtil.dpToPx(10);//圆半径
    int radiusW = DisplayUtil.dpToPx(1);//圆环宽度

    private int clickW = DisplayUtil.dpToPx(40);//点击区域大小
    private int scaleW = DisplayUtil.dpToPx(10);//圆圈缩放大小
    private boolean mEnableSelect = true;//是否可以选中
    /**
     * radiusArray[0] = leftTop;
     * radiusArray[1] = leftTop;
     * radiusArray[2] = rightTop;
     * radiusArray[3] = rightTop;
     * radiusArray[4] = rightBottom;
     * radiusArray[5] = rightBottom;
     * radiusArray[6] = leftBottom;
     * radiusArray[7] = leftBottom;
     */
    private float[] radiusArray = {mRadius, mRadius, 0f, 0f, 0f, 0f, 0f, 0f};

    private ImageClickCall mImageClickCall;

    public ImageItem(Context context) {
        super(context);
        init();
    }

    public ImageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        countPaint.setTextSize(DisplayUtil.dpToPx(15));
        countPaint.setAntiAlias(true);
//        countPaint.setStrokeWidth(DisplayUtil.dpToPx(15));
        countPaint.setColor(Color.WHITE);

        mPathPaint.setColor(Color.parseColor("#CC779BC8"));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(DisplayUtil.dpToPx(12));

        bgPaint.setARGB(30, 0, 0, 0);


        bgPaint.setAntiAlias(true);//抗锯齿
        paint.setAntiAlias(true);//抗锯齿
        mTextPaint.setAntiAlias(true);//抗锯齿
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mClickPoint.x = (int) event.getX();
                        mClickPoint.y = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //判断点击对勾区域

                        if (mClickRect.left - mClickPoint.x < 0 && mClickRect.bottom - mClickPoint.y > 0) {
                            //点击右上角矩形
                            if (null != mImageClickCall) {
                                mImageClickCall.onRectClick();
                            }
                            setSelect(!mIsSelect);
                        } else {
                            //点击图片
                            if (null != mImageClickCall) {
                                mImageClickCall.onImageClick();
                            }

                        }
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.drawARGB(30, 0, 0, 0);
        canvas.drawARGB(10, 0, 0, 0);
        int cx = getWidth() - radius - paddingRight;
        int cy = radius + paddingTop;
        if (!mIsSelect) {
            paint.setStrokeWidth(radiusW);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(cx, cy, radius, bgPaint);
            canvas.drawCircle(cx, cy, radius, paint);

    /*
            //画对勾省略
            Path path = new Path();
            path.moveTo(cx - rectW, cy);
            path.lineTo(cx, cy + rectW);
            path.lineTo(cx + rectW, cy - rectW);
            canvas.drawPath(path, paint);*/
        } else {
            //选中
            paint.setColor(Color.parseColor("#07C15C"));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawARGB(100, 0, 0, 0);//画选中蒙层黑色透明度
            canvas.drawCircle(cx, cy, radius - mOffset, paint);
            String temp = String.valueOf(mCount);
            float textWidth = countPaint.measureText(temp);
            float baseLineY = Math.abs(countPaint.ascent() + countPaint.descent()) / 2;
            canvas.drawText(temp, cx - textWidth / 2, cy + baseLineY, countPaint);
        }


        mClickRect.left = getWidth() - clickW;
        mClickRect.top = 0;
        mClickRect.right = getWidth();
        mClickRect.bottom = clickW;


        /**
         * 不能选中 并且 当前没有选中 画白色蒙层
         */
        if (!mEnableSelect && !mIsSelect) {
            canvas.drawARGB(160, 255, 255, 255);//不能选择的白色透明度
        }


    }


    /**
     * 设置选中
     *
     * @param select
     */
    private void setSelect(boolean select) {
        if (!mEnableSelect) {
            return;
        }

        mIsSelect = select;

        if (!mIsSelect) {
            invalidate();
        } else {
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


    public interface ImageClickCall {

        void onRectClick();

        void onImageClick();
    }


}
