package ttuananhle.android.simplegameengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.SoundPool;
import android.view.MotionEvent;

import ttuananhle.android.simplegameengine.SMLEngine.Node;
import ttuananhle.android.simplegameengine.SMLEngine.Scene;
import ttuananhle.android.simplegameengine.SMLEngine.Sprite;

/**
 * Created by leanh on 6/3/2017.
 */

public class Bird  extends Sprite{
    private Point visibleSize;
    public static final int GRAVITY = 7;
    public static final int FLAP = 95;

    private SoundPool soundPool;
    private int wingSoundId;


    public Bird(Scene scene, SoundPool soundPool, int wingSoundId){
        super();
        visibleSize = GameView.getInstance(scene.getContext()).getVisibleSize();
        Bitmap bitmap = BitmapFactory.decodeResource(scene.getContext().getResources(), R.drawable.bird);

        this.setBtmSprite(bitmap);
        this.setScale(3);
        this.setPosition( new Point( visibleSize.x/3 - getWidth()/2, visibleSize.y/3 - getHeight()/2));

        this.wingSoundId = wingSoundId;
        this.soundPool = soundPool;
    }

    @Override
    public void update() {
        super.update();

        this.setPositionY( getPosition().y + GRAVITY);
    }

    @Override
    public void setEventListener(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                playSound(soundPool, wingSoundId);
                this.setPositionY( getPosition().y - FLAP);
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                break;
        }
    }

    public void playSound(SoundPool soundPool, int id){
       if ( soundPool != null && id > 0){
           soundPool.play(id, 1, 1, 0, 0, 1);
       }
    }
}
