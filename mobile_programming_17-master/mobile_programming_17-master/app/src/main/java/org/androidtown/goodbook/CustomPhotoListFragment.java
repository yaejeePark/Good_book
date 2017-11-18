package org.androidtown.goodbook;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;

/**
 * Created by sang on 2017-05-29.
 */

public class CustomPhotoListFragment extends ListFragment {

    PhotoAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Adapter 생성 및 Adapter 지정.
        adapter = new PhotoAdapter(getContext());
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // get TextView's Text.
        MemoItem item = (MemoItem) l.getItemAtPosition(position);

        String titleStr = item.getTitle();
        String descStr = item.getContext();
        Bitmap bmp = item.getImg();

        // TODO : use item data.
    }

    public void addItem(Bitmap bmp, String title, String context, int id) {
//    public void addItem(String title, String context) {
        byte[] bImg = getByteArrayFromBitmap(bmp);
        adapter.addItem(title, context, bImg, id);
//        adapter.addItem(title, context);
    }

    public byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        return data;
    }

}
