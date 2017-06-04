package ttuananhle.android.simplegameengine;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.MotionEvent;

import ttuananhle.android.simplegameengine.SMLEngine.Button;
import ttuananhle.android.simplegameengine.SMLEngine.Label;
import ttuananhle.android.simplegameengine.SMLEngine.Node;
import ttuananhle.android.simplegameengine.SMLEngine.Scene;
import ttuananhle.android.simplegameengine.SMLEngine.Sprite;

/**
 * Created by leanh on 6/4/2017.
 */

public class GameOverScene extends Scene {


    public static final String USER_DATA = "USER_DATA";
    public static final String HIGHT_SCORE = "HIGHT_SCORE";


    private Sprite bgSprite;
    private Point visibleSize;

    private Sprite newSprite;
    private Sprite groundSprite;

    private int currentScore = 0;
    private int hightScore = 0;

    private Label lblcrrScore;
    private Label lblHightScore;

    private Button btnReplay;
    private Button btnMenu;

    /**
     * Constructor
     * @param context
     */
    public GameOverScene(Context context, int score){
        super(context);

        visibleSize = GameView.getInstance(context).getVisibleSize();
        this.currentScore = score;

        // init bg
        Bitmap bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameoverbackground);
        bgSprite = new Sprite();
        bgSprite.setBtmSprite(bg, visibleSize.x, visibleSize.y);
        bgSprite.setPosition( new Point( visibleSize.x/2 - bgSprite.getWidth()/2, visibleSize.y/2 - bgSprite.getHeight()/2));
        this.addChild(bgSprite);

        // init label current score
        lblcrrScore = new Label(context);
        lblcrrScore.setText(String.valueOf(currentScore));
        lblcrrScore.setSize(60);
        lblcrrScore.setPosition( new Point( (int ) (visibleSize.x* 0.75), (int )(visibleSize.y * 0.445)));
        this.addChild(lblcrrScore);


        // Init new sprite
        Bitmap newBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.newscore);
        newSprite = new Sprite(newBit);
        newSprite.setScale(3);


        // get hightScore
        getHightScore();
        // Save hight Score
        if ( currentScore > hightScore){
            hightScore = currentScore;
            setHightScore(hightScore);

            // Show new Sprite
            newSprite.setPosition( new Point( (int)(visibleSize.x * 0.65 - newSprite.getWidth()/2),
                    (int) ( visibleSize.y * 0.52 - newSprite.getHeight()/2)));
            this.addChild(newSprite);

        }
        lblHightScore = new Label(context);
        lblHightScore.setSize(60);
        lblHightScore.setPosition( new Point( (int ) (visibleSize.x* 0.75), (int )(visibleSize.y * 0.542) ));
        lblHightScore.setText(String.valueOf(hightScore));
        this.addChild(lblHightScore);


        // init btn replay
        Bitmap normalReplay = BitmapFactory.decodeResource(context.getResources(), R.drawable.normalbuttonreplay );
        Bitmap selectedReplay = BitmapFactory.decodeResource(context.getResources(), R.drawable.selectedbuttonreplay);
        btnReplay = new Button(normalReplay, selectedReplay);
        btnReplay.setScale(3);
        btnReplay.setPosition( new Point( visibleSize.x/3 - btnReplay.getWidth()/2,
                visibleSize.y/3*2 - btnReplay.getHeight()/2));
        this.addChild(btnReplay);
        this.setTouchEventButtonReplay();

        // init btn share
        Bitmap normalShare = BitmapFactory.decodeResource(context.getResources(), R.drawable.normalmenubutton);
        Bitmap selectedShare = BitmapFactory.decodeResource(context.getResources(), R.drawable.selectedmenubutton);
        btnMenu = new Button(normalShare, selectedShare);
        btnMenu.setScale(3);
        btnMenu.setPosition( new Point( visibleSize.x/3*2 - btnMenu.getWidth()/2,
                visibleSize.y/3*2 - btnMenu.getHeight()/2));
        this.addChild(btnMenu);
        this.setTouchEventButtonMenu();

        // init ground
        Bitmap ground = BitmapFactory.decodeResource( context.getResources(), R.drawable.groundfani);
        groundSprite = new Sprite( ground);
        groundSprite.setScale(3);
        groundSprite.setPosition( new Point( 0, visibleSize.y  - groundSprite.getHeight()));
        this.addChild(groundSprite);
    }


    /**
     * go to game scene when replay button touched
     */
    public void setTouchEventButtonReplay(){
        btnReplay.setTouchEventListener(new Node.TouchEventListener() {
            @Override
            public void onTouchBegan(MotionEvent event) {

            }

            @Override
            public void onTouchEnded(MotionEvent event) {
                GameView.getInstance(getContext()).replaceCurrentScene(200, new GameScene(getContext()));
            }
        });
    }

    /**
     * Go to Main Menu Scene
     */
    public void setTouchEventButtonMenu(){
        btnMenu.setTouchEventListener(new Node.TouchEventListener() {
            @Override
            public void onTouchBegan(MotionEvent event) {

            }

            @Override
            public void onTouchEnded(MotionEvent event) {
                GameView.getInstance(getContext()).replaceCurrentScene(200, new MainMenuScene(getContext()));
            }
        });
    }


    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    /**
     *
     */
    public void getHightScore(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        hightScore = sharedPreferences.getInt(HIGHT_SCORE, 0);
    }

    /**
     * get HightScore from ShareReference
     * @param score
     */
    public void setHightScore( int score){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.putInt(HIGHT_SCORE, score);
        editor.commit();
    }

    /**
     *
     * @param delta
     */
    @Override
    public void update(float delta) {
        super.update(delta);
    }
}
