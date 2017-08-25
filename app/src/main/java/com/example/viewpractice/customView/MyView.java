package com.example.viewpractice.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.viewpractice.R;

/**
 * Created by zhengj on 2017/8/24.
 */

public class MyView extends View {
    private int bgColor = Color.BLUE;
    private int lineColor = Color.WHITE;

    private Paint paint;
    private Path outerCircle;
    private Path innerCircle;
    private Path triangle1;
    private Path triangle2;
    private int outerRadius = 250;
    private int innerRadius = 200;
    private float centerX;
    private float centerY;
    private PathMeasure pathMeasure;
    float currentDis = 0;
    Path current1 = new Path();
    Path current2 = new Path();
    Handler handler;
    Runnable runnable;
    ValueAnimator valueAnimator;

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyView,defStyleAttr,0);
        int n = a.getIndexCount();
        for(int i=0;i<n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.MyView_bgColor:
                    bgColor = a.getColor(attr,Color.BLUE);
                    break;
                case R.styleable.MyView_lineColor:
                    lineColor = a.getColor(attr,Color.WHITE);
                    break;
            }
        }
        a.recycle();


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(currentDis<1){
                    currentDis += 0.01f;
                    postInvalidate();
                }

            }
        };
        valueAnimator = ValueAnimator.ofFloat(0,1).setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentDis = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        initPaint();
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        valueAnimator.start();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {

        this(context, attrs,0);
    }

    private void initPaint(){
        paint = new Paint();
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.BEVEL);
    }

    private void initPath(){
        centerX = getLeft()+0.5f*getWidth();
        centerY = getTop()+0.5f*getHeight();

        outerCircle = new Path();
        innerCircle = new Path();
        triangle1 = new Path();
        triangle2 = new Path();

        outerCircle.addCircle(centerX,centerY,outerRadius, Path.Direction.CCW);
        innerCircle.addCircle(centerX,centerY,innerRadius, Path.Direction.CW);

        pathMeasure = new PathMeasure();
        pathMeasure.setPath(innerCircle,false);

        //三角形1
        //上边的点
        float[] pos = new float[2];
        pathMeasure.getPosTan((1f/4f)*pathMeasure.getLength(),pos,null);
        triangle1.moveTo(pos[0],pos[1]);

        //左下的点
        pathMeasure.getPosTan((7f/12f)*pathMeasure.getLength(),pos,null);
        triangle1.lineTo(pos[0],pos[1]);

        //右下的点
        pathMeasure.getPosTan((11f/12f)*pathMeasure.getLength(),pos,null);
        triangle1.lineTo(pos[0],pos[1]);

        triangle1.close();

        //三角形2
        //第一个点
        pathMeasure.getPosTan((1f/12f)*pathMeasure.getLength(),pos,null);
        triangle2.moveTo(pos[0],pos[1]);

        //第二个点
        pathMeasure.getPosTan((5f/12f)*pathMeasure.getLength(),pos,null);
        triangle2.lineTo(pos[0],pos[1]);

        //第三个点
        pathMeasure.getPosTan((3f/4f)*pathMeasure.getLength(),pos,null);
        triangle2.lineTo(pos[0],pos[1]);

        triangle2.close();

    }

    public MyView(Context context) {

        this(context,null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景
        canvas.drawColor(bgColor);

        initPath();
        canvas.drawPath(outerCircle,paint);
        canvas.drawPath(innerCircle,paint);
//        canvas.drawPath(triangle1,paint);
//        canvas.drawPath(triangle2,paint);
        pathMeasure.setPath(triangle1,false);
        current1.reset();
//        pathMeasure.getSegment((currentDis-0.05f)*pathMeasure.getLength(),currentDis*pathMeasure.getLength(),current1,true);
        pathMeasure.getSegment(0,currentDis*pathMeasure.getLength(),current1,true);

        pathMeasure.setPath(triangle2,false);
        current2.reset();
        pathMeasure.getSegment(0,currentDis*pathMeasure.getLength(),current2,true);

        canvas.drawPath(current1,paint);
        canvas.drawPath(current2,paint);


//        handler.postDelayed(runnable,50);

    }
}
