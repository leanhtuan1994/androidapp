package ttuananhle.android.chatlearningapp.model;

import android.graphics.Bitmap;

/**
 * Created by leanh on 5/7/2017.
 */

public class Setting {
    private int id;
    private String name;
    private Bitmap imgBitmap;

    public Setting(int id, String name, Bitmap imgBitmap) {
        this.id = id;
        this.name = name;
        this.imgBitmap = imgBitmap;
    }

    public Setting(String name, Bitmap imgBitmap) {
        this.name = name;
        this.imgBitmap = imgBitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImageUrl() {
        return imgBitmap ;
    }

    public void setImageUrl(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }
}
