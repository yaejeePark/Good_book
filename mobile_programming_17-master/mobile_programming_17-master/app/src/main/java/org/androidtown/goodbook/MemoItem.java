package org.androidtown.goodbook;

import android.graphics.Bitmap;

/**
 * Created by sang on 2017-05-27.
 */

public class MemoItem {

    int id;
    Bitmap bmpImg;
    String strTitle;
    String strContext;

    public void setID(int newID) {id = newID;}

    public void setImg(Bitmap img) {
        bmpImg = img;
    }

    public void setTitle(String strName) {
        strTitle = strName;
    }

    public void setContext(String strAuthor) {
        strContext = strAuthor;
    }


    /////////////////////////////////////////////////////
    public int getID() {return id;}

    public Bitmap getImg() {
        return bmpImg;
    }

    public String getTitle() {
        return strTitle;
    }

    public String getContext() {
        return strContext;
    }

}
