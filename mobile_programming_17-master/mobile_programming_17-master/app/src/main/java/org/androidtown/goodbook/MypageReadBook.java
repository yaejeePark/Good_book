package org.androidtown.goodbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MypageReadBook extends ListFragment {

    ReadBookAdapter adapter;
    ListView listview;

    BookListDBHelper helper;
    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_mypage_read_book, container, false);

        // Adapter 생성
        adapter = new ReadBookAdapter(getContext());

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) rootView.findViewById(android.R.id.list);
        listview.setAdapter(adapter);

        //DB 초기화
        helper = new BookListDBHelper(this.getContext(),
                "book.db",
                null,
                1);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ReadBookItem curItem = (ReadBookItem) adapter.getItem(position);
        int times = curItem.getTimes();

        Toast.makeText(getContext(), Integer.toString(times)+" 번 읽었습니다", Toast.LENGTH_SHORT).show();

//        showPopup(getView(), curItem.getID());
    }


    @Override
    public void onResume() {
        super.onResume();

        adapter.clear();
        adapter.notifyDataSetChanged();

        select();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //select
    public void select() {

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("booklist", null, null, null, null, null, null);

        adapter.clear();

        while (c.moveToNext()) {

            int nTimes = c.getInt(c.getColumnIndex("read_times"));


            if(nTimes > 0) {
                String strTitle = c.getString(c.getColumnIndex("title"));
                String strAuthor = c.getString(c.getColumnIndex("author"));
                String strDate = c.getString(c.getColumnIndex("date_finish"));
                byte[] b_imageLink = c.getBlob(c.getColumnIndex("image_link"));

                //insert
                adapter.addItem(nTimes, strTitle, strAuthor, strDate, b_imageLink);
                adapter.notifyDataSetChanged();

            }


        }
    }

    public void showPopup(View anchorView, int id) {
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.pop_up_txt_report, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, -100);

        /* 선언 */
        final Spinner spTitle;
        final EditText etContext;
        final EditText etDelete;
        Button btnSave;
        Button btnNo;
        ArrayList<String> strListTitle = new ArrayList<String>();
        final int idGet = id;

        final SQLiteDatabase db2_read;
        final SQLiteDatabase db2_write;
        final BookListDBHelper helper2;

        /* 대입 */
        spTitle = (Spinner) popupView.findViewById(R.id.spTitle);
        etContext = (EditText) popupView.findViewById(R.id.etContext);
        etDelete = (EditText) popupView.findViewById(R.id.etDelete);
        btnSave = (Button) popupView.findViewById(R.id.btnSave);
        btnNo=(Button)popupView.findViewById(R.id.btnNo);

        helper2 = new BookListDBHelper(this.getContext(),
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호

        db2_write = helper2.getWritableDatabase();
        db2_read = helper2.getReadableDatabase();


        /* spTitle 세팅 */
        Cursor c2 = db2_read.query("booklist", null, null, null, null, null, null);
        String strTitleRead;

        while (c2.moveToNext()) {
            strTitleRead = c2.getString(c2.getColumnIndex("title"));
            strListTitle.add(strTitleRead);
        }

        ArrayAdapter<String> adpTitle = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, strListTitle);
        adpTitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTitle.setAdapter(adpTitle);

        /* 내용 불러오기 */
        c2 = db2_write.rawQuery("SELECT * FROM report WHERE id_report=?", new String[]{idGet + ""});
        c2.moveToFirst();

        String strGetContext;
        String strGetTitle;
        strGetContext = c2.getString(c2.getColumnIndex("context"));
        strGetTitle = c2.getString(c2.getColumnIndex("title"));

        /* 창에 띄워주기 */
        etContext.setText(strGetContext);
//        spTitle.setSelection(getIndex(spTitle, strGetTitle));

        /* 변경 사항 저장 */
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDelete.getText().toString().equals("삭제")) {
                    /* 삭제하기 */
                    db2_write.delete("report", "id_report=?", new String[]{idGet+""});

                    Toast.makeText(getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    /* update 하기 */
                    ContentValues values2 = new ContentValues();

                    Cursor c2 = db2_write.rawQuery("SELECT * FROM report WHERE id_report=?", new String[]{idGet + ""});
                    c2.moveToFirst();

                    String strGetTitle, strGetContext;

                    strGetTitle = spTitle.getSelectedItem().toString();
                    strGetContext = etContext.getText().toString();
                    values2.put("title", strGetTitle);
                    values2.put("context", strGetContext);

                    db2_write.update("report", values2, "id_report=?", new String[]{idGet + ""});

                    Toast.makeText(getContext(), "내용이 변경되었습니다", Toast.LENGTH_SHORT).show();
                }
                listUpdate();

                popupWindow.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });

    }

    public void listUpdate() {
        adapter.clear();
        adapter.notifyDataSetChanged();
        select();
    }
}
