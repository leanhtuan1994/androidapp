package ttuananhle.android.simplegameengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import ttuananhle.android.simplegameengine.SMLEngine.Scene;
import ttuananhle.android.simplegameengine.SMLEngine.Sprite;

/**
 * Created by leanh on 6/2/2017.
 */

public class SpashScene extends Scene {

    private Point visibleSize;

    private Sprite bgSprite;


    private float timer = 0;

    public SpashScene(Context context){
        super(context);
        Log.i("Scene", "Splash Scene....");
        visibleSize = GameView.getInstance(context).getVisibleSize();



        bgSprite = new Sprite();
        bgSprite.setBtmSprite(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bg), visibleSize.x, visibleSize.y);
        bgSprite.setPosition( new Point(0, 0));
        this.addChild(bgSprite);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void update(float delta) {

        // Go to main menu Scene
        timer += delta;
        if ( timer > 3000 ){
            GameView.getInstance(getContext()).replaceCurrentScene( 0 , new MainMenuScene(getContext()));
        }
    }

    @Override
    public void controls() {
        super.controls();

    }


}
