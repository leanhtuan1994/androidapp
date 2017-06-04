package ttuananhle.android.simplegameengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ttuananhle.android.simplegameengine.SMLEngine.Scene;

/**
 * Created by leanh on 5/31/2017.
 */

public class GameView extends SurfaceView
        implements Runnable, SurfaceHolder.Callback {


    // FPS for Game
    public static  final int FPS = 60;


    private static GameView instance;

    // Create Thread
    private Thread gameThread;

    private Scene currentScene;

    // Screen Size
    private  Point visibleSize;


    // Volatile to check game running in thread
    private volatile boolean running;

    // Create Canvas and Paint to draw graphics
    private Canvas canvas;



    private GameView(Context context){
        super(context);
    }



    public static GameView getInstance(Context context){
        if (instance == null){
            instance =  new GameView(context);
        }
        return instance;
    }

    // Init
    public void init(Point screenSize){
        this.visibleSize = screenSize;

        this.setFocusable(true);
        gameThread = new Thread(this);
        getHolder().addCallback(this);

        // Init Splash Scene when launch
        currentScene = new SpashScene(getContext());

        Log.i("Size", "( " + screenSize.x + ", " + screenSize.y + ")");
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        running = true;
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        running = false;
        while (retry){
            try {
                gameThread.join();
            } catch (Exception ex){
                Log.i("Error", ex.toString() );
            }
        }
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTimeFrame;
        long sleepTime;


        while (running){
            canvas = null;
            startTimeFrame = System.currentTimeMillis();

            try {
                canvas = getHolder().lockCanvas();

                synchronized (getHolder()){
                    update(ticksPS);
                    draw(canvas);
                    controls();
                }

            } catch (Exception e){
              Log.i("Error", e.toString());
            } finally {
                if (canvas != null){
                    getHolder().unlockCanvasAndPost(canvas);
                }
            }

            sleepTime = ticksPS - (System.currentTimeMillis() - startTimeFrame);
            Log.i("FPS", "SleepTime: " + sleepTime);

            try {
                if( sleepTime > 0) gameThread.sleep(sleepTime);
                else gameThread.sleep(10);
            } catch (Exception e){}
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // Draw CrrScene
        currentScene.draw(canvas);

    }

    public void update(float delta) {
        // Update CrrScene
        currentScene.update(delta);
    }

    public void controls(){
        currentScene.controls();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentScene.setEventListener(event);
        return true;
    }


    /////// Getter and Setter

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void replaceCurrentScene( long transitionTime, Scene currentScene) {
       try {
           gameThread.sleep(transitionTime);
           this.currentScene = currentScene;
       } catch (Exception e){}
    }

    public Point getVisibleSize() {
        return visibleSize;
    }
}
