package ttuananhle.android.simplegameengine.SMLEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by leanh on 6/2/2017.
 */

public class Button extends Node {
    private Bitmap crrBitmap;
    private Bitmap normalBitmap;
    private Bitmap selectedBitmap;



    public Button(Bitmap normal, Bitmap selected){
        this.normalBitmap = normal;
        this.selectedBitmap = selected;

        this.crrBitmap = normalBitmap;

        touchEventListener = new TouchEventListener() {
            @Override
            public void onTouchBegan(MotionEvent event) {}
            @Override
            public void onTouchEnded(MotionEvent event) {}
        };

        this.setBoundingBox( new Rect());
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.crrBitmap, getPosition().x, getPosition().y, this.paint);

    }

    @Override
    public void update() {
        this.updateBoudingBox( getPosition().x, getPosition().y,
               getPosition().x +  crrBitmap.getWidth(), getPosition().y + crrBitmap.getHeight());
    }


    /**
     * set Event listener. Check touch point & set Touch Event Listener for Button
     * @param event
     */
    @Override
    public void setEventListener(MotionEvent event) {

        // Check point touch event contains rect of button
        if ( !this.boundingBox.contains((int)event.getX(),(int) event.getY())) return;

        switch (event.getAction() ){
            case MotionEvent.ACTION_DOWN:
                crrBitmap = selectedBitmap;
                touchEventListener.onTouchBegan(event);
                break;
            case MotionEvent.ACTION_UP:
                crrBitmap = normalBitmap;
                touchEventListener.onTouchEnded(event);
                break;
            default:
                break;
        }

    }

    /**
     * Set Scale for Button - All bitmaps
     * @param scale
     */
    public void setScale(int scale){
        this.crrBitmap = Bitmap.createScaledBitmap( crrBitmap, crrBitmap.getWidth() * scale, crrBitmap.getHeight() * scale, true);
        this.normalBitmap = Bitmap.createScaledBitmap( normalBitmap, normalBitmap.getWidth() * scale, normalBitmap.getHeight() * scale, true);
        this.selectedBitmap = Bitmap.createScaledBitmap( selectedBitmap, selectedBitmap.getWidth() * scale, selectedBitmap.getHeight() * scale, true);
    }
    public int getWidth(){
        return crrBitmap.getWidth();
    }

    public int getHeight(){
        return crrBitmap.getHeight();
    }
}
