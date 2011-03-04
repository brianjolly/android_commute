package com.brianjolly.commute.view;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

import android.location.Location;

import android.view.View;
import com.brianjolly.commute.model.MyLocation;

import android.util.Log;

public class BreadCrumbTrail extends View {

    private MyLocation model;
    private Location location;

    public BreadCrumbTrail(Context context, MyLocation model) {
        super(context);
        this.model = model;
        setMinimumWidth(180);
        setMinimumHeight(200);
        setFocusable(true);
    }

    @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(
                    getSuggestedMinimumWidth(),
                    getSuggestedMinimumHeight());
        }

    @Override
        protected void onDraw(Canvas canvas) {

            canvas.drawColor(Color.WHITE);

            Paint paint = new Paint();

            canvas.drawLine(33, 0, 33, 100, paint);

            paint.setColor(Color.RED);
            paint.setStrokeWidth(10);
            canvas.drawLine(56, 0, 56, 100, paint);

            paint.setColor(Color.GREEN);
            paint.setStrokeWidth(5);

            for (int y = 30, alpha = 255; alpha > 2; alpha >>= 1, y += 10) {
                paint.setAlpha(alpha);
                canvas.drawLine(0, y, 100, y, paint);
            }
        }

    public void setModel(MyLocation model) {
        this.model = model;
        System.out.println("new locx: "+location.getLatitude());
        System.out.println("new locy: "+location.getLongitude());
    }

}
