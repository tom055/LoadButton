package com.mybutton.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mybutton.R;

/**
 * Created by nbzl on 2016/12/19.
 */
public class LoadView extends RelativeLayout{

    private View view;
    private TextView txt;
    private ProgressBar mProgressBar;
    private RelativeLayout.LayoutParams params_txt,params_progress;
    private Context context;

    private float txtSize=20;//字体大小
    private String title="确定";//button字
    private int dur=400;//动画时间
    private int start_angle=10,end_angle,current_angle;//开始角度
    private int speed_rect=0;

    private Paint paint_rect;
    private Rect rect;
    private RectF rectF_left,rectF_right;
    private int width_radus=50;

    private int width_view,height_view;


    private ObjectAnimator alphaAnimotion,progressAnimotion,progressScrollXAnimotion,progressScrollYAnimotion;
    private AnimatorSet animatorSet;

    public LoadView(Context context) {
        this(context,null);
    }


    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        this.context=context;
        paint_rect=new Paint();
        paint_rect.setStyle(Paint.Style.FILL);
        paint_rect.setColor(context.getResources().getColor(R.color.colorAccent));
        paint_rect.setAntiAlias(true);

    }

    public void startAnimator(){
        animatorSet=new AnimatorSet();
        animatorSet.setDuration(dur);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        alphaAnimotion=ObjectAnimator.ofFloat(txt,"alpha",1f,0f);
        alphaAnimotion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator.getAnimatedValue() instanceof Float){
                    current_angle=(int)(((float)end_angle-(float)start_angle)*(1.0f-(float)valueAnimator.getAnimatedValue())+(float)start_angle);
                    speed_rect=(int)((1-(float)valueAnimator.getAnimatedValue())*((float)width_view-(float)height_view))/2;
                    //speed_circle=speed_rect;
                    invalidate();
                }
            }
        });

        progressAnimotion=ObjectAnimator.ofFloat(mProgressBar,"alpha",0f,1f);
        progressScrollXAnimotion=ObjectAnimator.ofFloat(mProgressBar,"scaleX",0f,1f);
        progressScrollYAnimotion=ObjectAnimator.ofFloat(mProgressBar,"scaleY",0f,1f);
        progressScrollYAnimotion.setStartDelay(dur-100);
        progressScrollXAnimotion.setStartDelay(dur-100);
        progressAnimotion.setStartDelay(dur-100);
        progressScrollYAnimotion.setDuration(dur);
        progressScrollXAnimotion.setDuration(dur);
        progressAnimotion.setDuration(dur);

        animatorSet.play(alphaAnimotion).with(progressAnimotion).with(progressScrollXAnimotion).with(progressScrollYAnimotion);

        animatorSet.start();
        //alphaAnimotion.setInterpolator(new DecelerateInterpolator());//减速
    }

    public void backAnimator(){
        animatorSet=new AnimatorSet();
        animatorSet.setDuration(dur);
        animatorSet.setInterpolator(new DecelerateInterpolator());

        alphaAnimotion=ObjectAnimator.ofFloat(txt,"alpha",0f,1f);
        alphaAnimotion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator.getAnimatedValue() instanceof Float){
                    current_angle=(int)(((float)end_angle-(float)start_angle)*(1.0f-(float)valueAnimator.getAnimatedValue())+(float)start_angle);
                    speed_rect=(int)((1-(float)valueAnimator.getAnimatedValue())*((float)width_view-(float)height_view))/2;
                    //speed_circle=speed_rect;
                    invalidate();
                }
            }
        });

        progressAnimotion=ObjectAnimator.ofFloat(mProgressBar,"alpha",1f,0f);

        animatorSet.play(alphaAnimotion).with(progressAnimotion);
        animatorSet.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("Mytext","onMeasure");
        width_view=getWidth();
        height_view=getHeight();
        end_angle=height_view/2;
        //Log.e("Mytext","width-->"+getWidth()+"--height-->"+getHeight());
        if(animatorSet==null&&getWidth()>0&&getHeight()!=0){
            //startAnimator();
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("Mytext","onDraw");

        rect=new Rect(height_view/2+speed_rect,0,width_view-height_view/2-speed_rect,height_view);
        canvas.drawRect(rect,paint_rect);

        rectF_left=new RectF(0+speed_rect,0,height_view+speed_rect,height_view);
        canvas.drawRoundRect(rectF_left,current_angle,current_angle,paint_rect);

        rectF_right=new RectF(width_view-height_view-speed_rect,0,width_view-speed_rect,height_view);
        canvas.drawRoundRect(rectF_right,current_angle,current_angle,paint_rect);

        super.onDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("Mytext","onFinishInflate"+getHeight());
        txt=new TextView(context);
        params_txt=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_txt.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params_txt.addRule(RelativeLayout.CENTER_VERTICAL);
        params_txt.setMargins(0,20,0,20);
        txt.setLayoutParams(params_txt);
        txt.setText(title);
        txt.setTextColor(Color.WHITE);
        txt.setTextSize(txtSize);

        mProgressBar=new ProgressBar(context);
        params_progress=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_progress.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params_progress.addRule(RelativeLayout.CENTER_VERTICAL);
        mProgressBar.setLayoutParams(params_progress);
        //mProgressBar.setPadding(10,10,10,10);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setAlpha(0f);
        mProgressBar.getIndeterminateDrawable()
                .mutate()
                .setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        //this.addView(btn);
        this.addView(txt);
        this.addView(mProgressBar);
    }
}
