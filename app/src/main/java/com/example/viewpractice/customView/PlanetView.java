package com.example.viewpractice.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by zhengj on 2017/8/25.
 */

public class PlanetView extends View {
    private Paint paint;
    private float centerX;
    private float centerY;
    private Path oval1;
    private Path oval2;
    private Path guy1;
    private Path guy2;
    private RectF rect1;
    private RectF rect2;
    private PathMeasure pathMeasure;
    private float[] pos = new float[2];
    private ValueAnimator valueAnimator;
    private float progress;

    public PlanetView(Context context) {
        this(context,null);
    }

    public PlanetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlanetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paint.setAntiAlias(true);

        setLayerType(LAYER_TYPE_SOFTWARE,null);



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("PlanetView-----","onSizeChanged");
        centerX = getLeft()+0.5f*getWidth();
        centerY = getTop()+0.5f*getHeight();

        //两个椭圆形
        oval1 = new Path();
        oval2 = new Path();
        rect1 = new RectF(centerX-150,centerY-50,centerX+150,centerY+50);
        rect2 = new RectF(centerX-150,centerY-50,centerX+150,centerY+50);
        oval1.addOval(rect1, Path.Direction.CW);
        oval2.addOval(rect2, Path.Direction.CW);

        //两个小东西
        guy1 = new Path();
        guy2 = new Path();

        pathMeasure = new PathMeasure();

        valueAnimator = ValueAnimator.ofFloat(0,1).setDuration(1000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progress = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(progress<0.5f){
            canvas.translate(0,-progress*100);
            drawGuy2(canvas);
            drawCircle(canvas);
            drawGuy1(canvas);
        }else {
            canvas.translate(0,-(1-progress)*100);
            drawGuy1(canvas);
            drawCircle(canvas);
            drawGuy2(canvas);
        }



        //画椭圆
//        canvas.save();
//        canvas.rotate(30,centerX,centerY);
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(3);
//        canvas.drawPath(oval1,paint);
//        canvas.rotate(15,centerX,centerY);
//        canvas.drawPath(oval2,paint);
//        canvas.restore();





    }

    private void drawCircle(Canvas canvas){
        //画圆
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(centerX,centerY,100,paint); //半径100
    }

    private void drawGuy1(Canvas canvas){
        //第一个小东西
        canvas.save();
        canvas.rotate(15,centerX,centerY);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        guy1.reset();
        pathMeasure.setPath(oval1,false);
        pathMeasure.getPosTan(progress*pathMeasure.getLength(),pos,null);
        pathMeasure.getSegment((progress-0.05f)*pathMeasure.getLength(),progress*pathMeasure.getLength(),guy1,true);
        canvas.drawPath(guy1,paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pos[0],pos[1],5,paint);
        if(progress<0.05f){
            paint.setStyle(Paint.Style.STROKE);
            pathMeasure.getPosTan((1-(0.05f-progress))*pathMeasure.getLength(),pos,null);
            pathMeasure.getSegment((1-(0.05f-progress))*pathMeasure.getLength(),pathMeasure.getLength(),guy1,true);
            canvas.drawPath(guy1,paint);
        }
        canvas.restore();
    }

    private void drawGuy2(Canvas canvas){
        //第二个小东西
        canvas.save();
        canvas.rotate(25+180,centerX,centerY);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        guy2.reset();
        pathMeasure.setPath(oval2,false);
        pathMeasure.getPosTan(progress*pathMeasure.getLength(),pos,null);
        pathMeasure.getSegment((progress-0.05f)*pathMeasure.getLength(),progress*pathMeasure.getLength(),guy2,true);
        canvas.drawPath(guy2,paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pos[0],pos[1],5,paint);
        if(progress<0.05f){
            paint.setStyle(Paint.Style.STROKE);
            pathMeasure.getPosTan((1-(0.05f-progress))*pathMeasure.getLength(),pos,null);
            pathMeasure.getSegment((1-(0.05f-progress))*pathMeasure.getLength(),pathMeasure.getLength(),guy2,true);
            canvas.drawPath(guy2,paint);
        }
        canvas.restore();
    }
}
