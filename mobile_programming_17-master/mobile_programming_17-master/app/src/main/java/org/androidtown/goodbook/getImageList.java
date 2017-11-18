package org.androidtown.goodbook;

import android.graphics.Bitmap;

/**
 * Created by 지영 on 2017-05-06.
 *
 * 비트맵 이미지와 그에 해당하는 책 이름을 어레이에 임시 저장하는 함수
 * 리스트뷰에서 선택했을 때 이 어레이에서 가져와서 db저장하기 위해서
 */

public class getImageList {

    static public Bitmap[] arrBit=new Bitmap[10];
    static public String[] name=new String[10];
    static public int position1=0,position2=0,count=0;


    static public void setBitmap(Bitmap image,int position){

        arrBit[position]=image;

    }

    static public void setName(BookItem item,int position){

        name[position]=item.title;
    }

}
