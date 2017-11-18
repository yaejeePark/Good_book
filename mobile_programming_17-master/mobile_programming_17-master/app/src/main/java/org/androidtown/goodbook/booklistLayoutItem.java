package org.androidtown.goodbook;

import android.graphics.Bitmap;

/**
 * Created by sang on 2017-05-12.
 */

//booklistlayout item의 각 컴포넌트에 값(텍스트, 이미지, 수치)을 넣어줌

public class booklistLayoutItem {

    Bitmap bmp_img;
    String str_bookName;
    String str_author;
    int n_prog;

    public void setImg(Bitmap img) {
        bmp_img = img;
    }

    public void setBookName(String strName) {
        str_bookName = strName;
    }

    public void setAuthor(String strAuthor) {
        str_author = strAuthor;
    }

    public void setProg(int nCur, int nTot) {
        n_prog = nCur*100 / nTot;
    }

    /////////////////////////////////////////////////////

    public Bitmap getImg() {
        return bmp_img;
    }

    public String getBookName() {
        return str_bookName;
    }

    public String getAuthor() {
        return str_author;
    }

    public int getProg() {
        return n_prog;
    }
}