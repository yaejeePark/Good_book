package org.androidtown.goodbook;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class PhotoFragment extends Fragment {

    PhotoAdapter adapter;
    ListView listview;


    BookListDBHelper helper;
    SQLiteDatabase db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("PhotoFragment>>", "called");

//        return inflater.inflate(R.layout.photo_fragment, container, false);

        View rootView = inflater.inflate(R.layout.photo_fragment, container, false);

        // Adapter 생성
        adapter = new PhotoAdapter(getContext());

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) rootView.findViewById(R.id.photoList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new CustomOnClickListener());

        //DB 초기화
        helper = new BookListDBHelper(this.getContext(),
                "book.db",
                null,
                1);

        Resources res = getResources();

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        adapter.clear();
        adapter.notifyDataSetChanged();

        select();
    }

    //select
    public void select() {

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("report", null, null, null, null, null, null);


        while (c.moveToNext()) {

            int id = c.getInt(c.getColumnIndex("id_report"));
            String strTitle = c.getString(c.getColumnIndex("title"));
            String strContext = c.getString(c.getColumnIndex("context"));
            byte[] b_imageLink = c.getBlob(c.getColumnIndex("image_link"));

            if(strContext.length() > 10)
            {
                strContext = strContext.substring(0, 10);
                strContext = strContext + " ...";
            }

            if (b_imageLink.length == 1) {

            } else {
                //add
//            adapter.addItem(strTitle, strContext);
                adapter.addItem(strTitle, strContext, b_imageLink, id);
                adapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class CustomOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int id = ((PhotoItem) adapter.getItem(i)).getID();

            Bundle bnd = new Bundle();
            Intent intent=new Intent(getActivity(),PopUpPhotoReport.class);
            bnd.putInt("id", id);
            intent.putExtras(bnd);

            startActivity(intent);


//            showPopup(getView(), id);
//            PopUpPhotoReport dialog = PopUpPhotoReport.newInstance(id);
//            dialog.show(getActivity().getFragmentManager(), "MyDialogFragment");
        }

    }


}