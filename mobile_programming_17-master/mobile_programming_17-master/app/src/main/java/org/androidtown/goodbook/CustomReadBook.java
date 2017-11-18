package org.androidtown.goodbook;

import android.support.v4.app.ListFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;

/**
 * Created by sang on 2017-06-07.
 */

public class CustomReadBook extends ListFragment {

    ReadBookAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Adapter 생성 및 Adapter 지정.
        adapter = new ReadBookAdapter(getContext());
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // get TextView's Text.
        ReadBookItem item = (ReadBookItem) l.getItemAtPosition(position);

        String title = item.getBookName();
        String author = item.getAuthor();
        String date = item.getDate();
        Bitmap bmp = item.getImg();

        // TODO : use item data.
    }

    //    public void addItem(Bitmap bmp, String title, String context) {
//    public void addItem(String title, String context, int id) {
    public void addItem(int nTimes, String title, String author, String date, byte[] img) {
//        byte[] bImg = getByteArrayFromBitmap(bmp);
//        adapter.addItem(title, context, bImg);
        adapter.addItem(nTimes, title, author, date, img);
    }

    public byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        return data;
    }
}
