package ttuananhle.android.simplegameengine.SMLEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by leanh on 6/2/2017.
 */

public class Sprite extends Node{

    private Bitmap btmSprite;

    public Sprite(Bitmap bitmap){
        this.btmSprite = bitmap;
        this.setBoundingBox( new Rect());
        this.setPosition( new Point(0, 0));
    }

    public Sprite(){
        this.setBoundingBox( new Rect());
        this.setPosition( new Point(0, 0));
    }

    /**
     * Set bitmap sprite with width & height
     * @param bitmap ref of bitmap sprite
     */
    public void setBtmSprite(Bitmap bitmap, int width, int height ){
        this.btmSprite = Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * draw Sprite
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(btmSprite, getPosition().x, getPosition().y, paint);
    }

    /**
     * Update Bouding Box if current position bitmap is change
     */
    @Override
    public void update() {
        this.updateBoudingBox( getPosition().x, getPosition().y, getPosition().x + getWidth(), getPosition().y + getHeight());
    }

    /**
     *  get Width of Srpite
     * @return int of width
     */
    public int getWidth(){
        return btmSprite.getWidth();
    }

    /**
     * get Height of Sprite
     * @return
     */
    public int getHeight(){
        return btmSprite.getHeight();
    }

    /**
     * Set Scale bitmap Sprite follow int scale param using Bitmap.createScaledBitmap
     * @param scale
     */
    public  void setScale(int scale){
        this.btmSprite = Bitmap.createScaledBitmap(this.btmSprite, btmSprite.getWidth() * scale, btmSprite.getHeight() * scale, true);
    }

    public void setPositionX(int x){
        this.getPosition().x = x;
    }

    public void setPositionY( int y){
        this.getPosition().y = y;
    }

    /**
     *
     * @param btmSprite
     */
    public void setBtmSprite(Bitmap btmSprite) {
        this.btmSprite = btmSprite;
    }
}
