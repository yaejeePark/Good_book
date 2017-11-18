package org.androidtown.goodbook;

/**
 * Created by 지영 on 2017-05-04.
 */

public class BookItem {
    String title;
    String link;
    String image;
    String author;
    String d_catg;

    @Override
    public String toString(){return title;}

    public String getTitle() {return title;}
    public String getImage() {return image;}
    public String getAuthor() {return author;}
}
