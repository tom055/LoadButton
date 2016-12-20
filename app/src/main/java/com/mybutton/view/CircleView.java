package com.mybutton.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nbzl on 2016/12/19.
 */
public class CircleView extends View {

    private int width_c,height_c,radus=50;
    private Paint paint_rect,paint_circle;

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint_rect=new Paint();
        paint_rect.setStyle(Paint.Style.FILL);
        paint_rect.setColor(Color.BLUE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getWidth()>0&&getHeight()>0){
            width_c=getWidth();
            height_c=getHeight();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawRect(new Rect());
    }
}
