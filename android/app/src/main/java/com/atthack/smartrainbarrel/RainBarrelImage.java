package com.atthack.smartrainbarrel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by SWBRADSH on 04/16/2016.
 */
public class RainBarrelImage extends ImageView {

    public RainBarrelImage(Context context) {
        super(context);
    }

    public RainBarrelImage(Context context, AttributeSet attrst) {
        super(context, attrst);
    }

    public RainBarrelImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    public void onDraw(Canvas canvas) {

        canvas.drawColor(Color.TRANSPARENT);
    /*instantiate a bitmap and draw stuff here, it could well be another
    class which you systematically update via a different thread so that you can get a fresh updated
    bitmap from, that you desire to be updated onto the custom ImageView.
   That will happen everytime onDraw has received a call i.e. something like:*/
       // Bitmap myBitmap = bitMapFac.update(); //where update returns the most up  to date Bitmap
        //here you set the rectangles in which you want to draw the bitmap and pass the bitmap
       // canvas.drawBitmap(myBitMap, new Rect(0,0,400,400), new Rect(0,0,240,135) , null);
        super.onDraw(canvas);
        //you need to call postInvalidate so that the system knows that it  should redraw your custom ImageView
        this.postInvalidate();
    }
}