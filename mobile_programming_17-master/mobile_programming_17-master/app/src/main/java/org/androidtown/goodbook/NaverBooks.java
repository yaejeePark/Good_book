package org.androidtown.goodbook;

import com.begentgroup.xmlparser.SerializedName;

import java.util.ArrayList;


/**
 * Created by 지영 on 2017-05-04.
 */

public class NaverBooks {
    String title;
    String description;
    int total;
    int start;
    int display;
    String d_catg;
    @SerializedName("item")

    ArrayList<BookItem> items;
}
