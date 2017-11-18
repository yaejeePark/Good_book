package org.androidtown.goodbook;

import android.graphics.Bitmap;

/**
 * Created by sang on 2017-06-07.
 */

public class ReadBookItem {

    String strDate;
    Bitmap bmp_img;
    String str_bookName;
    String str_author;
    int nTimes;


    public void setTimes(int newTimes) {
        nTimes = newTimes;
    }

    public void setImg(Bitmap img) {
        bmp_img = img;
    }

    public void setBookName(String strName) {
        str_bookName = strName;
    }

    public void setAuthor(String strAuthor) {
        str_author = strAuthor;
    }
    public void setDate(String strnewDate) {
        strDate = strnewDate;
    }

    /////////////////////////////////////////////////////
    public int getTimes() {
        return nTimes;
    }

    public Bitmap getImg() {
        return bmp_img;
    }

    public String getBookName() {
        return str_bookName;
    }

    public String getAuthor() {
        return str_author;
    }
    public String getDate() {
        return strDate;
    }


}
