package com.lc.loadbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class LoadRectView extends View {
    private Context context;

    // 开始Loading时的回调
    private OnStartListener startListener;

    // 结束Loading时的回调
    private OnFinishListener finishListener;

    // 开始和结束Loading时的回调
    private OnLoadingListener listener;

    // Loading动画旋转周期
    private int rotateDuration = 1000;

    // 按钮缩成Loading动画的时间
    private int reduceDuration = 500;//350;

    // Loading旋转动画控制器
    private Interpolator rotateInterpolator;

    // 按钮缩成Loading动画的控制器
    private Interpolator reduceInterpolator;

    private int firstwidth,firstheight;

    private int width;
    private int height;
    private float length1=0.0f,length2=0.0f;

    private String text;
    private Path mPath;
    private Paint mPaint,concerPaint;
    private PathMeasure measure;
    private float progress= 0f,widthprogress=0.0f;
    private float pathsize=30;
    private float pathsize2=30;

    // 是否在Loading中
    private boolean isLoading = false;

    public LoadRectView(Context context) {
        this(context, null);
    }

    public LoadRectView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadRectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        concerPaint=new Paint();
        concerPaint.setAntiAlias(true);
        concerPaint.setColor(Color.RED);
        concerPaint.setStyle(Paint.Style.FILL);
        concerPaint.setStrokeWidth(5);
        //initpaint();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (width == 0) {
            firstwidth = getMeasuredWidth();
            width=getMeasuredWidth();
        }
        if (height == 0) {
            firstheight = getMeasuredHeight();
            height=getMeasuredHeight();
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(2.5f,2.5f,getWidth()-2.5f,getHeight()-2.5f,getHeight()*widthprogress,getHeight()*widthprogress,concerPaint);
        if(length1>0.0){
            Path dstSearch = new Path();
            PathMeasure pmSearch = new PathMeasure(mPath, false);
            pmSearch.getSegment(0, pmSearch.getLength() * progress, dstSearch, true);
            canvas.drawPath(dstSearch, mPaint);


            //canvas.drawPath(mPath,mPaint);
        }
        //canvas.drawRoundRect(2.5f,2.5f,getWidth()-2.5f,getHeight()-2.5f,getHeight()*widthprogress,getHeight()*widthprogress,concerPaint);
        //canvas.drawPath(mPath,mPaint);
    }

    private void initpaint(){
        mPaint=new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        prepareDraw();
    }

    /**
     * 播放按钮缩成Loading的动画
     */
    public void showStartLoadAnimation() {
        clearAnimation();
        ValueAnimator animator = new ValueAnimator().ofInt(width, firstheight);
        animator.setDuration(reduceDuration);
        if (reduceInterpolator != null) animator.setInterpolator(reduceInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().width = (int) animation.getAnimatedValue();
                widthprogress=Float.valueOf(height)/Float.valueOf(getLayoutParams().width);//Float.valueOf(getWidth())/Float.valueOf(firstwidth-firstheight);
                Log.e("Mytext",getLayoutParams().width+"<---->"+height+"<---->"+(firstwidth-firstheight)+"<---->"+progress);
                requestLayout();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                //setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_main_color_selector));
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showLoadingAnimation();
                //showPaintTxtAnima();
            }
        });
        animator.start();
    }

    /**
     * 播放Loading动画
     */
    private void showLoadingAnimation() {
        clearAnimation();
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(rotateDuration);
        animation.setInterpolator(rotateInterpolator != null ? rotateInterpolator : new LinearInterpolator());
        animation.setRepeatCount(-1);
        //setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle_loading));
        if (startListener != null) {
            startListener.onStart();
        } else if (listener != null) {
            listener.onStart();
        }
        startAnimation(animation);
        isLoading = true;
    }

    /**
     * 播放Loading拉伸成按钮的动画
     */
    private void showFinishLoadAnimation() {
        clearAnimation();
        ValueAnimator animator = new ValueAnimator().ofInt(height, firstwidth);
        animator.setDuration(reduceDuration);
        if (reduceInterpolator != null) animator.setInterpolator(reduceInterpolator);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().width = (int) animation.getAnimatedValue();
                widthprogress=1-Float.valueOf(height)/Float.valueOf(getLayoutParams().width);
                requestLayout();
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                //setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_main_color_selector));
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setEnabled(true);
                if (finishListener != null) {
                    finishListener.onFinish();
                } else if (listener != null) {
                    listener.onFinish();
                }
            }
        });
        animator.start();
        isLoading = false;
    }

    //显示路径动画
    public void showPaintTxtAnima(){
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        initpaint();

        ValueAnimator anima=ValueAnimator.ofFloat(0,1);
        anima.setDuration(800);
        anima.setInterpolator(new AccelerateInterpolator());
        anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress=(float) animation.getAnimatedValue();
                invalidate();
            }
        });
        anima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //showFinishLoadAnimation();
            }
        });
        anima.start();
    }

    private void prepareDraw(){
        mPath=new Path();
        mPath.moveTo(width/2-pathsize,height/2);
        mPath.lineTo(width/2-pathsize/2,height/2+pathsize/2);
        mPath.quadTo(width/2,height/2+pathsize,width/2+pathsize/2,height/2+pathsize/2);
        mPath.lineTo(width/2+pathsize+pathsize2,height/2-pathsize2);
        PathMeasure measure=new PathMeasure(mPath,false);
        length1 = measure.getLength();
        //mPath.lineTo(width/2+height,height);
        //mPath.close();
        /*PathMeasure measure2=new PathMeasure(mPath,false);
        length2 = measure2.getLength();
        measure = new PathMeasure();
        measure.setPath(mPath,false);*/
        //获取坐标
        /*measure.getPosTan(0,serpos,null);
        measure.setPath(ciclePath,false);*/
    }

    /**
     * 开始Loading
     */
    public void startLoading() {
        if (!isLoading) {
            clearAnimation();
            showStartLoadAnimation();
        }
    }

    /**
     * 开始Loading
     *
     * @param listener Loading开始时的回调
     */
    public void startLoading(OnStartListener listener) {
        if (!isLoading) {
            this.startListener = listener;
            clearAnimation();
            showStartLoadAnimation();
        }
    }

    /**
     * 结束Loading
     */
    public void finishLoading() {
        if (isLoading) {
            clearAnimation();
            showPaintTxtAnima();
            //showFinishLoadAnimation();
        }
    }

    /**
     * 结束Loading
     *
     * @param listener Loading结束时的回调
     */
    public void finishLoading(OnFinishListener listener) {
        if (isLoading) {
            this.finishListener = listener;
            clearAnimation();
            showPaintTxtAnima();

        }
    }

    /**
     * 设置Loading开始和结束时的回调接口
     *
     * @param listener
     */
    public void setOnLoadingListener(OnLoadingListener listener) {
        this.listener = listener;
    }

    /**
     * 设置按钮缩成Loading动画的时间
     *
     * @param reduceDuration 时间，单位毫秒
     */
    public void setReduceDuration(int reduceDuration) {
        this.reduceDuration = reduceDuration;
    }

    /**
     * 设置Loading动画旋转周期
     *
     * @param rotateDuration 旋转周期，单位毫秒
     */
    public void setRotateDuration(int rotateDuration) {
        this.rotateDuration = rotateDuration;
    }

    /**
     * 获取是否正在Loading
     *
     * @return
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * 设置Loading旋转动画控制器
     *
     * @param rotateInterpolator
     */
    public void setRotateInterpolator(Interpolator rotateInterpolator) {
        this.rotateInterpolator = rotateInterpolator;
    }

    /**
     * 按钮缩成Loading动画的控制器
     *
     * @param reduceInterpolator
     */
    public void setReduceInterpolator(Interpolator reduceInterpolator) {
        this.reduceInterpolator = reduceInterpolator;
    }

    /**
     * Loading开始时的回调接口
     */
    public interface OnStartListener {
        void onStart();
    }

    /**
     * Loading结束时的回调接口
     */
    public interface OnFinishListener {
        void onFinish();
    }

    /**
     * Loading开始和结束时的回调接口
     */
    public interface OnLoadingListener {
        void onStart();

        void onFinish();
    }
}
