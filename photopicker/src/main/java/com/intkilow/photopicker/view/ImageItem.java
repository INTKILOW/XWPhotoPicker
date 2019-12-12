package com.intkilow.photopicker.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.appcompat.widget.AppCompatImageView;

import com.intkilow.photopicker.utils.DisplayUtil;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class ImageItem extends AppCompatImageView {



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
    private String mTime = "00:40";//视频时间长度
    int paddingTop = DisplayUtil.dpToPx(6);//圆距离上边距
    int paddingRight = DisplayUtil.dpToPx(6);//圆距离右边距
    int radius = DisplayUtil.dpToPx(10);//圆半径
    int radiusW = DisplayUtil.dpToPx(1);//圆环宽度

    private int clickW = DisplayUtil.dpToPx(40);//点击区域大小
    private int scaleW = DisplayUtil.dpToPx(10);//圆圈缩放大小

    private int mGIFPaddingLeft = DisplayUtil.dpToPx(6);
    private int mGIFPaddingBottom = DisplayUtil.dpToPx(6);

    private boolean mEnableSelect = true;//是否可以选中
    private boolean mIsGIF = false;//是不是gif
    private boolean mIsVIDEO = false;//是不是video
    private int mVideoRectW = DisplayUtil.dpToPx(15);
    private int mVideoRectH = DisplayUtil.dpToPx(12);

    private int mTraH = DisplayUtil.dpToPx(7);//梯形高度

    private int mTraPaddingLeft = DisplayUtil.dpToPx(2);//梯形距离矩形左边距
    private int mTraFixH = DisplayUtil.dpToPx(1);//梯形矩形padding

    private int mTimePaddingLeft = DisplayUtil.dpToPx(5);

    private Path mTraPath = new Path();
    private Rect mMeasureRect = new Rect();
    private int color = Color.parseColor("#07C15C");
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

//        mPathPaint.setColor(Color.parseColor("#CC779BC8"));
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
                                mImageClickCall.onRectClick(mEnableSelect);
                            }
                            //setSelect(!mIsSelect);
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

        long start = System.currentTimeMillis();

        canvas.drawARGB(30, 0, 0, 0);
        int cx = getWidth() - radius - paddingRight;
        int cy = radius + paddingTop;
        if (!mIsSelect || !mEnableSelect) {
            paint.setStrokeWidth(radiusW);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(cx, cy, radius, bgPaint);
            canvas.drawCircle(cx, cy, radius, paint);
            mTextPaint.setColor(Color.WHITE);
    /*
            //画对勾省略
            Path path = new Path();
            path.moveTo(cx - rectW, cy);
            path.lineTo(cx, cy + rectW);
            path.lineTo(cx + rectW, cy - rectW);
            canvas.drawPath(path, paint);*/
        } else {

            //选中
            mTextPaint.setColor(color);
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawARGB(100, 0, 0, 0);//画选中蒙层黑色透明度
            canvas.drawCircle(cx, cy, radius - mOffset, paint);
            String temp = String.valueOf(mCount);
            float textWidth = countPaint.measureText(temp);
            float baseLineY = Math.abs(countPaint.ascent() + countPaint.descent()) / 2;
            canvas.drawText(temp, cx - textWidth / 2, cy + baseLineY, countPaint);
        }


        if (mClickRect.bottom <= 0) {
            mClickRect.left = getWidth() - clickW;
            mClickRect.top = 0;
            mClickRect.right = getWidth();
            mClickRect.bottom = clickW;
        }



        /**
         * 不能选中 并且 当前没有选中 画白色蒙层
         */
        if (!mEnableSelect) {
            canvas.drawARGB(160, 255, 255, 255);//不能选择的白色透明度
        }


        if(mIsGIF){
            canvas.drawText("GIF",mGIFPaddingLeft,getHeight() -mGIFPaddingBottom  ,mTextPaint);
        }

        if (mIsVIDEO && !mIsGIF) {

            canvas.drawRect(mGIFPaddingLeft,getHeight() - mGIFPaddingBottom -mVideoRectH,mGIFPaddingLeft+mVideoRectW,getHeight() - mGIFPaddingBottom,paint);

            int x1[] = {mGIFPaddingLeft + mVideoRectW + mTraPaddingLeft,getHeight() - mGIFPaddingBottom - mVideoRectH/3*2};

            int x2[] = {mGIFPaddingLeft + mVideoRectW + mTraPaddingLeft,getHeight() - mGIFPaddingBottom - mVideoRectH/3*1};

            int x3[] = {mGIFPaddingLeft + mVideoRectW + mTraPaddingLeft + mTraH,getHeight() - mGIFPaddingBottom - mTraFixH};

            int x4[] = {mGIFPaddingLeft + mVideoRectW + mTraPaddingLeft + mTraH,getHeight() - mGIFPaddingBottom - mVideoRectH + mTraFixH};

            mTraPath.moveTo(x1[0],x1[1]);

            mTraPath.lineTo(x2[0],x2[1]);
            mTraPath.lineTo(x3[0],x3[1]);
            mTraPath.lineTo(x4[0],x4[1]);
            mTraPath.lineTo(x1[0],x1[1]);
            canvas.drawPath(mTraPath,paint);



            mTextPaint.getTextBounds(mTime,0,mTime.length(),mMeasureRect);


            canvas.drawText(mTime,mGIFPaddingLeft + mVideoRectW + mTraPaddingLeft + mTraH + mTimePaddingLeft,getHeight() - mGIFPaddingBottom -(mVideoRectH - mMeasureRect.height())/2 ,mTextPaint);

        }




        Log.e("TAG","canvas time" + (System.currentTimeMillis() - start));







    }


    public void setEnableSelect(boolean mEnableSelect) {
        this.mEnableSelect = mEnableSelect;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }



    public void setTime(String mTime) {
        this.mTime = mTime;
    }



    public void setIsGIF(boolean mIsGIF) {
        this.mIsGIF = mIsGIF;
    }

    public boolean ismIsVIDEO() {
        return mIsVIDEO;
    }

    public void setIsVIDEO(boolean mIsVIDEO) {
        this.mIsVIDEO = mIsVIDEO;
    }

    public void setSelect(boolean select,int count,boolean animation){
        mCount = count;
        setSelect(select,animation);
    }

    /**
     * 设置选中
     *
     * @param select
     */
    private void setSelect(boolean select,boolean animation) {
        if (!mEnableSelect) {
            return;
        }

        mIsSelect = select;

        if (!mIsSelect ) {
            invalidate();
        } else {
            if(animation){
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
            }else{
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



    public void setImageClickCall(ImageClickCall mImageClickCall) {
        this.mImageClickCall = mImageClickCall;
    }

    public interface ImageClickCall {

        void onRectClick(boolean enableClick);

        void onImageClick();
    }


}
