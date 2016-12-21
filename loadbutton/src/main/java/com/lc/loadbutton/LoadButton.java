package com.lc.loadbutton;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by nbzl on 2016/12/19.
 */
public class LoadButton extends RelativeLayout{
    private View content;
    private Context context;
    private LayoutParams params_txt,params_progress;
    private String str="确定";
    private String loadstr="正在加载";
    private float size=20f;
    private int angle=20;
    private int dur=500;

    private ProgressBar mProgressBar;
    private TextView status;

    private Paint paint_bg;
    private TextPaint textpaint;
    private RectF rectF;

    private int width,height,progress_width,status_width;
    private float str_width,load_width,scale;

    private ObjectAnimator alphaAnimotion,tranXAnimotion_progress,scrollXAnimotion,scrollYAnimotion,tranXAnimotion_text,alphaAnimotion_text;
    private AnimatorSet animatorSet;

    public LoadButton(Context context) {
        this(context,null);
    }

    public LoadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        this.context=context;
        paint_bg=new Paint();
        paint_bg.setAntiAlias(true);
        paint_bg.setStyle(Paint.Style.FILL);
        paint_bg.setColor(context.getResources().getColor(R.color.colorAccent));

        textpaint=new TextPaint();
        str_width=textpaint.measureText(str);
        load_width=textpaint.measureText(loadstr);
        scale=str_width/load_width;

        Log.e("Mytext","----->"+textpaint.measureText(str)+"---->"+textpaint.measureText(loadstr));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rectF=new RectF(0,0,width,height);
        canvas.drawRoundRect(rectF,angle,angle,paint_bg);

        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=getWidth();
        height=getHeight();
        if(width>0&&height>0&&status_width==0){
            status_width=status.getWidth();
            progress_width=mProgressBar.getWidth();
            Log.e("Mytext","width="+width+"  height="+height+"  status_width="+status_width);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mProgressBar=new ProgressBar(context);
        params_progress=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params_progress.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params_progress.addRule(RelativeLayout.CENTER_VERTICAL);
        mProgressBar.setLayoutParams(params_progress);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setAlpha(0f);
        mProgressBar.getIndeterminateDrawable().mutate().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        status=new TextView(context);
        params_txt=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params_txt.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params_txt.addRule(RelativeLayout.CENTER_VERTICAL);
        status.setLayoutParams(params_txt);
        status.setTextColor(Color.WHITE);
        status.setTextSize(size);
        status.setText(str);

        this.addView(mProgressBar);
        this.addView(status);

    }

    public void startAnimator(){
        animatorSet=new AnimatorSet();
        animatorSet.setDuration(dur);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        //status_width=status_width*(int)textpaint.measureText(loadstr)/(int)textpaint.measureText(str);

        alphaAnimotion_text=ObjectAnimator.ofFloat(status,"alpha",1f,0f,1f);
        alphaAnimotion_text.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator.getAnimatedValue() instanceof Float){
                    if((float)valueAnimator.getAnimatedValue()<0.05f){
                        status.setText(loadstr);
                    }
                }
            }
        });

        scrollXAnimotion=ObjectAnimator.ofFloat(mProgressBar,"scaleX",0f,1f);
        scrollYAnimotion=ObjectAnimator.ofFloat(mProgressBar,"scaleY",0f,1f);
        alphaAnimotion=ObjectAnimator.ofFloat(mProgressBar,"alpha",0f,1f);
        tranXAnimotion_progress=ObjectAnimator.ofFloat(mProgressBar,"translationX",0f,(float)(-progress_width/2));
        tranXAnimotion_text=ObjectAnimator.ofFloat(status,"translationX",0f,((float) status_width/scale)/2);
        animatorSet.play(alphaAnimotion).with(alphaAnimotion_text).with(tranXAnimotion_progress).with(scrollXAnimotion).with(scrollYAnimotion).with(tranXAnimotion_text);
        animatorSet.start();
    }

    public void stopAnimator(){

        animatorSet=new AnimatorSet();
        animatorSet.setDuration(dur);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        //status_width=status_width*(int)textpaint.measureText(str)/(int)textpaint.measureText(loadstr);

        alphaAnimotion_text=ObjectAnimator.ofFloat(status,"alpha",1f,0f,1f);
        alphaAnimotion_text.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator.getAnimatedValue() instanceof Float){
                    if((float)valueAnimator.getAnimatedValue()<0.05f){
                        status.setText(str);
                    }
                }
            }
        });

        scrollXAnimotion=ObjectAnimator.ofFloat(mProgressBar,"scaleX",1f,0f);
        scrollYAnimotion=ObjectAnimator.ofFloat(mProgressBar,"scaleY",1f,0f);
        alphaAnimotion=ObjectAnimator.ofFloat(mProgressBar,"alpha",1f,0f);
        tranXAnimotion_progress=ObjectAnimator.ofFloat(mProgressBar,"translationX",(float)(-progress_width/2),0f);
        tranXAnimotion_text=ObjectAnimator.ofFloat(status,"translationX",((float) status_width/scale)/2,0f);
        animatorSet.play(alphaAnimotion).with(alphaAnimotion_text).with(tranXAnimotion_progress).with(scrollXAnimotion).with(scrollYAnimotion).with(tranXAnimotion_text);
        animatorSet.start();
    }
}
