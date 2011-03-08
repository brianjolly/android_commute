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

        float biggerx = ((newy*100f)*-1)*2;
        float biggery = (newx*100f)*2;

        canvas.drawColor(Color.WHITE);

        Paint paint = new Paint();

        //             (startx, starty, endx, endy)
        canvas.drawLine(0, biggery, this.getWidth(), biggery, paint);  //latitude
        canvas.drawLine(biggerx, 0, biggerx, this.getHeight(), paint);   //longitude
    }

}
