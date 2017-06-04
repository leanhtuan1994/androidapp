package ttuananhle.android.simplegameengine.SMLEngine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by leanh on 6/2/2017.
 */

public abstract class Node {

    /**
     * Interface to set event touch for Node and child
     */
    public interface TouchEventListener {
        void onTouchBegan(MotionEvent event);
        void onTouchEnded(MotionEvent event);
    }

    // Bouding Box - Rect to set check touch event and set physics for node
    protected Rect boundingBox;
    private Point position;
    protected Paint paint;
    protected TouchEventListener touchEventListener;


    /**
     *
     */
    public Node(){
        paint = new Paint();
    }

    /**
     * Draw Everything of Node to Canvas
     * @param canvas
     */
    public abstract void draw(Canvas canvas);

    /**
     * Update..
     */
    public void update(){}

    /**
     * Set event listener for node
     * @param event
     */
    public void setEventListener(MotionEvent event){}

    /**
     * get BoudingBox
     * @return
     */
    public Rect getBoundingBox() {
        return boundingBox;
    }

    /**
     * Set BoudingBox
     * @param boundingBox
     */
    public void setBoundingBox(Rect boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * Update BoudingBox everytime when change position bitmap of node
     */
    public void updateBoudingBox( int left, int top, int right, int bottom){
        this.boundingBox.left = left;
        this.boundingBox.top = top;
        this.boundingBox.right = right;
        this.boundingBox.bottom = bottom;
    }

    /**
     * Set listener touch event
     * @param listener
     */
    public void setTouchEventListener(TouchEventListener listener){
        this.touchEventListener = listener;
    }

    /**
     * Get position of node
     * @return
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Set position of node
     * @param position
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
