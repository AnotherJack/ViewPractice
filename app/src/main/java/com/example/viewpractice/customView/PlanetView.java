package com.example.viewpractice.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhengj on 2017/8/25.
 */

public class PlanetView extends View {
    private Paint paint;

    public PlanetView(Context context) {
        this(context,null);
    }

    public PlanetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PlanetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
