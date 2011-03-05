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
        setMinimumWidth(200);
        setMinimumHeight(200);
        setFocusable(true);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                getSuggestedMinimumWidth(),
                getSuggestedMinimumHeight());
    }

    @Override protected void onDraw(Canvas canvas) {

        float lat = (float) model.getLat();
        float lon = (float) model.getLon();

        float newx = lat%1;
        float newy = lon%1;

        float biggerx = (newx*100f)*2;
        float biggery = ((newy*100f)*-1)*2;

        System.out.println("new x: "+biggerx);
        System.out.println("new y: "+biggery);

        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();

        //             (startx, starty, endx, endy)
        canvas.drawLine(biggerx, 0, biggerx, this.getHeight(), paint);
        canvas.drawLine(0, biggery, this.getWidth(), biggery, paint);

        /*
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawLine(56, 0, 56, 100, paint);

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);

        for (int y = 30, alpha = 255; alpha > 2; alpha >>= 1, y += 10) {
            paint.setAlpha(alpha);
            canvas.drawLine(0, y, 100, y, paint);
        }
        */
    }

}
