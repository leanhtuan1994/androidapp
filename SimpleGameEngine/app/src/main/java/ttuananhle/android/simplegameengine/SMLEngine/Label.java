package ttuananhle.android.simplegameengine.SMLEngine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * Created by leanh on 6/4/2017.
 */

public class Label extends Node {

    private String text;

    private int size = 80;

    private Paint stPaint;
    public Label(Context context){

        Typeface typeface = Typeface.create( Typeface.createFromAsset(context.getAssets(), "fonts/pixel.TTF"), Typeface.BOLD);
        getPaint().setTypeface(typeface);

        getPaint().setTextSize(size);
        getPaint().setColor(Color.WHITE);
        getPaint().setTextAlign(Paint.Align.CENTER);
        getPaint().setStyle( Paint.Style.FILL);

        stPaint = new Paint();
        stPaint.setTypeface(typeface);
        stPaint.setTextSize(size);
        stPaint.setColor(Color.BLACK);
        stPaint.setTextAlign(Paint.Align.CENTER);
        stPaint.setStyle(Paint.Style.STROKE);
        stPaint.setStrokeCap(Paint.Cap.ROUND);
        stPaint.setStrokeWidth(3);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(text, getPosition().x, getPosition().y, paint);
        canvas.drawText(text, getPosition().x, getPosition().y, stPaint);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setSize(int size){
        this.size = size;
        getPaint().setTextSize(size);
        stPaint.setTextSize(size);

    }

    public void setPositionX(int x){
        this.getPosition().x = x;
    }

    public void setPositionY( int y){
        this.getPosition().y = y;
    }
}
