package ttuananhle.android.simplegameengine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.Calendar;
import java.util.Random;

import ttuananhle.android.simplegameengine.SMLEngine.Scene;
import ttuananhle.android.simplegameengine.SMLEngine.Sprite;

/**
 * Created by leanh on 6/3/2017.
 */


public class Pipe {

    public static final double UPPER_SCREEN_PIPE_THRESHOLD = 0.40;
    public static final double SCREEN_PIPE_THRESHOLD = 0.30;
    public static final double LOWER_SCREEN_PIPE_THRESHOLD = 0.20;


    private Point visibleSize;
    private Scene scene;

    private Sprite topPipe;
    private Sprite bottomPipe;

    private Sprite birdSprite;

    private double randomPosition;

    private int speed = 5;

    private boolean passing = false;



    public Pipe(Scene scene){
        visibleSize = GameView.getInstance(scene.getContext()).getVisibleSize();
        this.scene = scene;


        Bitmap bitmapTop = BitmapFactory.decodeResource(scene.getContext().getResources(), R.drawable.toppipe);
        Bitmap bitmapBottom = BitmapFactory.decodeResource(scene.getContext().getResources(), R.drawable.bottompipe);

        topPipe = new Sprite(bitmapTop);
        topPipe.setScale(3);
        scene.addChild(topPipe);

        // Create bottom Pipe
        bottomPipe = new Sprite(bitmapBottom);
        bottomPipe.setScale(3);
        scene.addChild(bottomPipe);

        // Create bird Sprite bird to set space
        Bitmap bird = BitmapFactory.decodeResource(scene.getContext().getResources(), R.drawable.bird);
        birdSprite = new Sprite( bird);
        birdSprite.setScale(3);

    }

    /**
     *  Shawn Pipe
     */
    public void shawnPipe(int width){
        Random generator = new Random();
        randomPosition = generator.nextInt(2) + 1;

        if ( randomPosition == 1){
            randomPosition = LOWER_SCREEN_PIPE_THRESHOLD;
        } else if ( randomPosition == 2 ){
            randomPosition = SCREEN_PIPE_THRESHOLD;
        } else randomPosition = UPPER_SCREEN_PIPE_THRESHOLD;

        //  Create top Pipe
        topPipe.setPosition( new Point(width ,(int) (visibleSize.y*randomPosition - topPipe.getHeight())));
        bottomPipe.setPosition( new Point( width ,
                topPipe.getPosition().y + topPipe.getHeight() + birdSprite.getHeight() * 6 ));


    }

    public void update(float dt){
        topPipe.setPositionX( topPipe.getPosition().x - speed);
        bottomPipe.setPositionX( bottomPipe.getPosition().x - speed);


        // Set pipe start again
        if ( topPipe.getPosition().x < 0 - topPipe.getWidth()){

            Random generator = new Random();
            randomPosition = generator.nextInt(3) ;
            Log.i("Random", randomPosition + "");
            if ( randomPosition == 1){
                randomPosition = LOWER_SCREEN_PIPE_THRESHOLD;
            } else if ( randomPosition == 2 ){
                randomPosition = SCREEN_PIPE_THRESHOLD;
            } else randomPosition = UPPER_SCREEN_PIPE_THRESHOLD;

            topPipe.setPosition( new Point(visibleSize.x ,(int) (visibleSize.y*randomPosition - topPipe.getHeight())));
            bottomPipe.setPosition( new Point( visibleSize.x ,
                    topPipe.getPosition().y + topPipe.getHeight() + birdSprite.getHeight() * 6 ));

            passing = false;
        }
    }

    public void debugBoudingBox(Canvas canvas){
        canvas.drawRect( topPipe.getBoundingBox().left, topPipe.getBoundingBox().top,
                topPipe.getBoundingBox().right, topPipe.getBoundingBox().bottom, topPipe.getPaint());

        canvas.drawRect( bottomPipe.getBoundingBox().left, bottomPipe.getBoundingBox().top,
                bottomPipe.getBoundingBox().right, bottomPipe.getBoundingBox().bottom, bottomPipe.getPaint());
    }

    public Sprite getTopPipe() {
        return topPipe;
    }

    public Sprite getBottomPipe() {
        return bottomPipe;
    }

    public boolean isPassing() {
        return passing;
    }

    public void setPassing(boolean passing) {
        this.passing = passing;
    }
}
