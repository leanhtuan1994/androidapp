package ttuananhle.android.simplegameengine.SMLEngine;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leanh on 6/2/2017.
 */

public abstract class Scene {

    // Set Context for get something in assets
    private Context context;

    // List All Node of Scene
    private List<Node> children;


    public Scene(Context context){
        this.context = context;
        children = new ArrayList<>();
    }

    /**
     * Draw list node of scene
     * @param canvas
     */
    public  void draw(Canvas canvas){

        // Draw all child
        for ( Node node : children){
            node.draw(canvas);
        }
    }

    /**
     * Update list node of scene
     * @param delta
     */
    public void update(float delta){
        for (Node node: children){
            node.update();
        }
    }
    public void controls(){}

    /**
     * listener list node of scene
     * @param event
     */
    public void setEventListener(MotionEvent event){
        for (Node node : children ){
            node.setEventListener(event);
        }
    }

    public Context getContext() {
        return context;
    }

    /**
     * Add Node into list children node of scene
     * @param node
     */
    public void addChild( Node node){
        children.add(node);
    }

    public List<Node> getChildren() {
        return children;
    }
}
