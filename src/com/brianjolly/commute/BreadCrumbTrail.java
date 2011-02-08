package com.brianjolly.commute;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;

import android.util.Log;

public class BreadCrumbTrail extends View {

  private PathShape mDrawable;

  public BreadCrumbTrail(Context context, AttributeSet attrs) {

    super(context, attrs);

    int x = 10;
    int y = 10;
    int width = 300;
    int height = 50;

    mDrawable = new PathShape(new Path(), 300f, 300f);
    Log.i("BreadCrumbTrain","     -------"+mDrawable.toString());
    //mDrawable = new ShapeDrawable(new OvalShape());
    //mDrawable.getPaint().setColor(0xff74AC23);
    //mDrawable.setBounds(x, y, x + width, y + height);
    //mDrawable.draw(0f,0f);
    //mDrawable.draw(10f,0f);
    //mDrawable.draw(15f,5f);
    //mDrawable.draw(25f,15f);
    //mDrawable.draw(45f,25f);
  }

  protected void onDraw(Canvas canvas) {
    //mDrawable.draw(canvas);
  }
}
