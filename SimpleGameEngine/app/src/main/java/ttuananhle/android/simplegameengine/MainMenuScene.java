package ttuananhle.android.simplegameengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import ttuananhle.android.simplegameengine.SMLEngine.Button;
import ttuananhle.android.simplegameengine.SMLEngine.Node;
import ttuananhle.android.simplegameengine.SMLEngine.Scene;
import ttuananhle.android.simplegameengine.SMLEngine.Sprite;

/**
 * Created by leanh on 6/2/2017.
 */

public class MainMenuScene extends Scene {

    private Point visibleSize;

    private Sprite bgSprite;
    private Sprite logoSprite;
    private Sprite birdSprite;
    private Sprite groundSprite;

    private Button btnPlay;
    private Button btnShare;
    private Button btnRate;


    private int logoPositionMaxY;
    private int logoPositionMinY;
    private int groundPositionMinX;
    private int speedAnimate = 1;

    public MainMenuScene(Context context){
        super(context);

        Log.i("Scene", "Main Menu Scene....");

        // Get visible Size
        visibleSize = GameView.getInstance(context).getVisibleSize();

        // Init Background
        Bitmap bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bgsprite);
        bgSprite = new Sprite();
        bgSprite.setBtmSprite(bgBitmap, visibleSize.x, visibleSize.y);
        bgSprite.setPosition( new Point(0, 0));
        this.addChild(bgSprite);

        // Init logo
        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
        logoSprite = new Sprite(logoBitmap);
        logoSprite.setScale(4);
        logoPositionMaxY = visibleSize.y/4 - logoSprite.getHeight()/2 + 50;
        logoPositionMinY = logoPositionMaxY - 50;
        logoSprite.setPosition( new Point( visibleSize.x/2 - logoSprite.getWidth()/2,
                logoPositionMaxY));
        this.addChild( logoSprite );


        Bitmap birdBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bird);
        birdSprite = new Sprite(birdBitmap);
        birdSprite.setScale(4);
        birdSprite.setPosition( new Point( visibleSize.x/2 - birdSprite.getWidth() /2 ,
                (int) ( visibleSize.y/2.5 - birdBitmap.getHeight()/2)));
        this.addChild( birdSprite);


        // Init Button Play
        Bitmap normalBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.normalbuttonplay);
        Bitmap selectedBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.selectedbuttonplay);
        btnPlay = new Button( normalBtn, selectedBtn);
        btnPlay.setScale(2);
        btnPlay.setPosition( new Point( visibleSize.x/3 - btnPlay.getWidth()/2 , visibleSize.y/3*2 - btnPlay.getHeight()/2));
        this.addChild( btnPlay);
        setTouchEventButtonPlay();

        // Init button Share
        Bitmap normalBtnShare = BitmapFactory.decodeResource(context.getResources(), R.drawable.normalbuttonshare);
        Bitmap selectedBtnShare = BitmapFactory.decodeResource(context.getResources(), R.drawable.selectedbuttonshare);
        btnShare = new Button( normalBtnShare, selectedBtnShare);
        btnShare.setScale(2);
        btnShare.setPosition( new Point( visibleSize.x/3*2 - btnShare.getWidth()/2 , visibleSize.y/3*2 - btnShare.getHeight()/2));
        this.addChild( btnShare);
        setTouchEventButtonShare();

        // Init button Rate
        Bitmap normalRate = BitmapFactory.decodeResource(context.getResources(), R.drawable.normalbuttonrate);
        Bitmap selectedRate = BitmapFactory.decodeResource(context.getResources(), R.drawable.selectedbuttonrate);
        btnRate = new Button( normalRate, selectedRate);
        btnRate.setScale(2);
        btnRate.setPosition( new Point( visibleSize.x /2 - btnRate.getWidth()/2, visibleSize.y/2 - btnRate.getHeight()/2));
        this.addChild( btnRate );
        setTouchEventButtonRate();

        Bitmap ground = BitmapFactory.decodeResource( context.getResources(), R.drawable.groundfani);
        groundSprite = new Sprite( ground);
        groundSprite.setScale(3);
        groundSprite.setPosition( new Point( 0, visibleSize.y  - groundSprite.getHeight()));
        groundPositionMinX = visibleSize.x - groundSprite.getWidth();
        this.addChild(groundSprite);

    }

    // Listen touch event
    private void setTouchEventButtonPlay(){
        // Set Button Touch Event
        btnPlay.setTouchEventListener(new Node.TouchEventListener() {
            @Override
            public void onTouchBegan(MotionEvent event) {
                Log.i("Button", "OnTouchBegan....");
                btnPlay.setPosition( new Point( visibleSize.x/3 - btnPlay.getWidth()/2 , visibleSize.y/3*2 - btnPlay.getHeight()/2));

            }

            @Override
            public void onTouchEnded(MotionEvent event) {
                Log.i("Button", "OnTouchEnded....");
                btnPlay.setPosition( new Point( visibleSize.x/3 - btnPlay.getWidth()/2 , visibleSize.y/3*2 - btnPlay.getHeight()/2));


                // Go to GameScene
                GameView.getInstance(getContext()).replaceCurrentScene( 100, new GameScene(getContext()));
            }
        });
    }

    private void setTouchEventButtonShare(){
        // Set Button Touch Event
        btnShare.setTouchEventListener(new Node.TouchEventListener() {
            @Override
            public void onTouchBegan(MotionEvent event) {
                Log.i("Button", "OnTouchBegan....");
                btnShare.setPosition( new Point( visibleSize.x/3*2 - btnShare.getWidth()/2 , visibleSize.y/3*2 - btnShare.getHeight()/2));
            }

            @Override
            public void onTouchEnded(MotionEvent event) {
                Log.i("Button", "OnTouchEnded....");
                btnShare.setPosition( new Point( visibleSize.x/3*2 - btnShare.getWidth()/2 , visibleSize.y/3*2 - btnShare.getHeight()/2));

            }
        });
    }

    private void setTouchEventButtonRate(){
        btnRate.setTouchEventListener(new Node.TouchEventListener() {
            @Override
            public void onTouchBegan(MotionEvent event) {
                Log.i("Button", "OnTouchBegan....");
                btnRate.setPosition( new Point( visibleSize.x /2 - btnRate.getWidth()/2, visibleSize.y/2 - btnRate.getHeight()/2));
            }

            @Override
            public void onTouchEnded(MotionEvent event) {
                Log.i("Button", "OnTouchEnded....");
                btnRate.setPosition( new Point( visibleSize.x /2 - btnRate.getWidth()/2, visibleSize.y/2 - btnRate.getHeight()/2));
            }
        });
    }

    @Override
    public void update(float delta) {
        super.update(delta);


        // Animate for logo
        if ( logoSprite.getPosition().y == logoPositionMaxY || logoSprite.getPosition().y == logoPositionMinY )
            speedAnimate = -speedAnimate;
        logoSprite.getPosition().y += speedAnimate;

        if ( groundSprite.getPosition().x <= groundPositionMinX){
            groundSprite.getPosition().x = 0;
        }
        groundSprite.getPosition().x -= 2;
    }
}
