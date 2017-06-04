package ttuananhle.android.simplegameengine;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import ttuananhle.android.simplegameengine.SMLEngine.Label;
import ttuananhle.android.simplegameengine.SMLEngine.Scene;
import ttuananhle.android.simplegameengine.SMLEngine.Sprite;

/**
 * Created by leanh on 6/3/2017.
 */

public class GameScene extends Scene {

    private Point visibleSize;
    private Sprite bgSprite;

    private List<Pipe> pipeList;
    private Sprite groundSprite;
    private Sprite tapToPlaySprite;

    private Bird flappyBird;

    private int score = 0;

    // Lable to show score
    private Label lblScore;

    private int groundPositionMinX;

    // Sound pool to load sounds
    private SoundPool soundPool;
    private int hitSoundId = -1;
    private int wingSoundId = -1;
    private int pointSoundId = -1;

    private boolean isPlaying = false;

    @SuppressWarnings("deprecation")
    public GameScene(Context context){
        super(context);
        visibleSize = GameView.getInstance(context).getVisibleSize();

        // Setup bgSprite
        Bitmap bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bgsprite);
        bgSprite = new Sprite();
        bgSprite.setBtmSprite( bgBitmap,visibleSize.x, visibleSize.y);
        bgSprite.setPosition( new Point( visibleSize.x/2 - bgSprite.getWidth()/2, visibleSize.y/2 - bgSprite.getHeight()/2));
        this.addChild(bgSprite);

        //
        Bitmap tap = BitmapFactory.decodeResource(context.getResources(), R.drawable.taptoplay);
        tapToPlaySprite = new Sprite(tap);
        tapToPlaySprite.setScale(3);
        tapToPlaySprite.setPosition( new Point( visibleSize.x/2 - tapToPlaySprite.getWidth()/2,
                visibleSize.y/3 - tapToPlaySprite.getHeight()/2));
        this.addChild(tapToPlaySprite);


        pipeList = new ArrayList<>();
        // Init Pipe
        for( int i = 0; i < 2; i++){
            Pipe pipe  = new Pipe(this);
            pipe.shawnPipe( (int) (visibleSize.x + visibleSize.x * (i * 0.6)));
            pipeList.add(pipe);
        }


        // Init Sound
        if ( soundPool != null){
            soundPool.release();
            soundPool = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .setMaxStreams(10)
                    .build();
        } else {
              soundPool =  new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Create sound
            descriptor = assetManager.openFd("sounds/Hit.mp3");
            hitSoundId = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("sounds/Wing.mp3");
            wingSoundId = soundPool.load(descriptor, 0);
            descriptor = assetManager.openFd("sounds/Point.mp3");
            pointSoundId = soundPool.load(descriptor, 0);

        } catch (Exception e){
            Log.i("Sound", e.toString());
        }



        // Init FlappyBird
        flappyBird = new Bird(this, soundPool, wingSoundId);

        Bitmap ground = BitmapFactory.decodeResource( context.getResources(), R.drawable.groundfani);
        groundSprite = new Sprite( ground);
        groundSprite.setScale(3);
        groundSprite.setPosition( new Point( 0, visibleSize.y  - groundSprite.getHeight()));
        groundPositionMinX = visibleSize.x - groundSprite.getWidth();
        this.addChild(groundSprite);

        // init labelScore
        lblScore = new Label(getContext());
        lblScore.setText(String.valueOf(score));
        lblScore.setPosition( new Point( visibleSize.x/2, visibleSize.y/6));

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if ( !isPlaying ) return;

        if ( groundSprite.getPosition().x <= groundPositionMinX){
            groundSprite.getPosition().x = 0;
        }
        groundSprite.getPosition().x -= 2 ;

        for ( Pipe pipe : pipeList){
            pipe.update(delta);
        }

        // Check Game Over
        checkGameOver();

        checkScoreGame();
        lblScore.setText(String.valueOf(score));

    }

    @Override
    public void setEventListener(MotionEvent event) {
        super.setEventListener(event);
        // Start Game
        if (!isPlaying) {
            isPlaying = true;
            this.addChild(flappyBird);
            this.addChild(lblScore);

            this.getChildren().remove(tapToPlaySprite);
        }
    }

    public void checkGameOver(){
        for( Pipe pipe : pipeList){
           if (  Rect.intersects( flappyBird.getBoundingBox(), pipe.getTopPipe().getBoundingBox()) ||
                   Rect.intersects( flappyBird.getBoundingBox(), pipe.getBottomPipe().getBoundingBox())){

               if ( soundPool != null && hitSoundId > 0 ){
                   soundPool.play(hitSoundId, 1, 1, 0, 0, 1);
               }

               Log.i("GameOver", "Game Over.....");
               GameOverScene gameOverScene = new GameOverScene(getContext(), score);
               gameOverScene.setCurrentScore(score);
               GameView.getInstance(getContext()).replaceCurrentScene(50, gameOverScene);
           }
        }
    }

    public void checkScoreGame(){
        for (Pipe pipe : pipeList){
            if ( pipe.getTopPipe().getPosition().x  <= visibleSize.x / 3 && !pipe.isPassing()){
                pipe.setPassing(true);

                if ( soundPool != null && pointSoundId > 0){
                    soundPool.play(pointSoundId, 1, 1, 0, 0, 1);
                }
                score++;
                Log.i("Score", score + "");
            }
        }
    }


}
