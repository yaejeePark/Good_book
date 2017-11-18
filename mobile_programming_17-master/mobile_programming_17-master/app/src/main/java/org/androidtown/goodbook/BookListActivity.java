package org.androidtown.goodbook;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BookListActivity extends AppCompatActivity {

    //DB variable->
    static SQLiteDatabase db;
    static BookListDBHelper helper;
    static int int_tree_start = 0;
    //<-

    final static int NUM_PRODUCT_PER_SCREEN = 10;//한 화면에 출력될 제품수

    int nStartPosition = 1;//출력할 리스트의 시작 위치 지정

    ListView booklist;

    EditText et;

    BookListAdapter adapter;
    Button button;

    String bookName = null;
    String text;

    /* TabBooklist */
    TabBooklist tbl;

    /* TablReview */
    TabMypage tMyPage = new TabMypage();

    /* <DB> (genreCount) */
    //->
    //variable use in DB->
    SQLiteDatabase dbGenreCount;
    Cursor c;
    ArrayList<String> strListGenre;
    ArrayList<Integer> nListCount;
    final int GENRE = 27;
    ContentValues values = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);


        //TabBooklist
        tbl = new TabBooklist();

        //DB->
        helper = new BookListDBHelper(this, // 현재 화면의 context
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호
        //<-

        /* TabReview */
         /* DB(genreCount) init */
        helper = new BookListDBHelper(this,
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호

        dbGenreCount = helper.getReadableDatabase();

        strListGenre = new ArrayList<String>(GENRE);
        nListCount = new ArrayList<Integer>(GENRE);

        initLists();
        //<-



        Intent intent = getIntent();

        booklist = (ListView) findViewById(R.id.book_search_list);
        adapter = new BookListAdapter();
        booklist.setAdapter(adapter);
        button = (Button) findViewById(R.id.search);
        et = (EditText) findViewById(R.id.search_book);

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("booklist", null, null, null, null, null, null);
        int_tree_start = c.getCount();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //키보드 내려갓!!
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

                bookName = et.getText().toString();
                NaverBookRequest request = new NaverBookRequest(bookName);//검색 리퀘스트 값들 지정
                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<NaverBooks>() {//새로운 네트워크메니저 생성하여 생성한 리퀘스트 넣어줌
                    @Override
                    public void onSuccess(NetworkRequest<NaverBooks> request, NaverBooks result) {
                        //                            for (MovieItem item : result.items) {
//                                mAdapter.add(item);
//                            }
                        adapter.addAll(result.items);//성공시에 받아온 아이템을 리스트에 넣어준다

                    }

                    @Override
                    public void onFailure(NetworkRequest<NaverBooks> request, int errorCode, int responseCode, String message, Throwable exception) {
                        Toast.makeText(BookListActivity.this, "찾으시는 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                        Log.i("MainActivity", "responseCode : " + responseCode);

                    }
                });
            }
        });


        booklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View clickedView, int position, long id) {

                final DialogInterface dialogWindow;
                final long ID=id;
                final int pst=position;

                //팝업창 뜨게
                //책이름과 작가->

                //Dialog에서 보여줄 입력화면 View 객체 생성 작업
                //Layout xml 리소스 파일을 View 객체로 부불려 주는(inflate) LayoutInflater 객체 생성
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
                //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언
                final View dialogView = inflater.inflate(R.layout.pop_up_book_information, null);

                final AlertDialog.Builder builder = new AlertDialog.Builder(BookListActivity.this);     // 여기서 this는 Activity의 this
                builder.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                // 여기서 부터는 알림창의 속성 설정
                builder.setTitle("책 정보");       // 제목 설정
                final AlertDialog dialog = builder.create();    // 알림창 객체 생성



                ImageView bookImg = (ImageView) dialogView.findViewById(R.id.book_img);
                final EditText nameView = (EditText) dialogView.findViewById(R.id.book_name);
                final EditText pageView = (EditText) dialogView.findViewById(R.id.page);
                final Button btnOk=(Button)dialogView.findViewById(R.id.btnok);
                final Button btnNo=(Button)dialogView.findViewById(R.id.btnno);


                final Spinner spGenre = (Spinner) dialogView.findViewById(R.id.spinnerExample);
                ArrayAdapter spinnerAdapter=ArrayAdapter.createFromResource(BookListActivity.this,R.array.strarray_mobile_network_types,R.layout.spinner_style);
                spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
                spGenre.setAdapter(spinnerAdapter);


                Bitmap savebit;//저장할 비트맵 파일
                savebit = getImageList.arrBit[pst];
                String bookName = getImageList.name[pst];;

                nameView.setText(bookName);
                bookImg.setImageBitmap(savebit);

                btnOk.setOnClickListener(new View.OnClickListener(){

                    String str_bookName, str_author, str_imgLink,page;
                    @Override
                    public void onClick(View view) {
                        page = pageView.getText().toString();

                        //str_bookName = listView.getAdapter().getItem((int)id).toString();
                        str_bookName = nameView.getText().toString();
                        str_author = adapter.getAuthor((int) ID);
                        str_imgLink = adapter.getImage((int) ID);

                        str_bookName = str_bookName.replace("<b>", "");
                        str_bookName = str_bookName.replace("</b>", "");

                        str_author = str_author.replace("<b>", "");
                        str_author = str_author.replace("</b>", "");
                        //<-

                        /* Get data from pop_up window */


                        OutputStream outStream = null;
                        String extStroageDirectory =
                                Environment.getExternalStorageDirectory().toString();

                        Bitmap savebit;//저장할 비트맵 파일
                        savebit = getImageList.arrBit[pst];

                        //byte file로 저장한다
                        byte[] b_Img = getByteArrayFromDrawable(savebit);

                        if(str_bookName.isEmpty()||page.isEmpty()){
                            Toast.makeText(getApplicationContext(), "모든 사항을 입력해 주세요", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            insert(str_bookName, str_author, str_imgLink, Integer.parseInt(page), b_Img, spGenre.getSelectedItem().toString());
                            dialog.dismiss();

                        }

                    }
                });

                btnNo.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "책 저장이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });



//                //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
//             //   dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                dialog.show();

                //data 책 처음 추가할 때는 4개만 받는다 (str제목, str작가, str이미지링크, int전체페이지)
                // 1. 데이터 저장

                // 2. 수정하기
                //update("유저1", 58); // 나이만 수정하기
                //update("제목2", 100);

                // 3. 삭제하기
                //delete("유저2");
                //delete("제목1");

                // 4. 조회하기
                select();
            }
        });

    }

    public void setDeliver(String str) {
        text = str;
    }

    public String getDeliver() {
        return text;
    }

    //DB function->
    /*
    <Insert>
    work: 서재에 책 추가하기
    input: title, author, imagelink, total page
    output: none
     */
    public void insert(String str_title, String str_author, String str_imageLink, int int_pageTot, byte[] b_img, String str_genre) {
        Cursor cursor = null;
        Cursor cursor2 = null;
        select2();

        try {
            db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능
            ContentValues values = new ContentValues();

            cursor = db.rawQuery("SELECT title FROM booklist WHERE title=?", new String[]{str_title + ""});

            if (cursor.getCount() == 0) { //insert
                int_tree_start = int_tree_start + 1;

                values.put("read_times", 0);
                values.put("title", str_title);
                values.put("author", str_author);
                values.put("genre", str_genre);
                values.put("image_link", str_imageLink);
                values.put("page_total", int_pageTot);
                values.put("id_tree", int_tree_start);
                values.put("image_link", b_img);
                //date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = sdf.format(new Date());

                values.put("date", date);

                db.insert("booklist", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)

                Toast.makeText(getApplicationContext(), str_title + "책을 서재에 추가하였습니다.", Toast.LENGTH_SHORT).show();

                /* genreCount */
                //장르테이블에서 해당 장르 추가
                //장르로 count값 알아내고,

                db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능
                cursor2 = db.rawQuery("SELECT * FROM genreCount WHERE genre=?", new String[]{str_genre + ""});
                cursor2.moveToFirst();
                int n = cursor2.getInt(cursor2.getColumnIndex("count"));

                //거기에 +1하기
                n = n + 1;

                //DB에 업데이트
                ContentValues values2 = new ContentValues();
                values2.put("count", n);    //genre 값을 수정
                db.update("genreCount", values2, "genre=?", new String[]{str_genre});

                select2();
            }
            else //pass
                Toast.makeText(getApplicationContext(), "이미 서재에 있는 책입니다", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    <Update total page>
    work: Update total page
    input: title, new total pate
    output: none
    */
    public void update_tot_page(String str_title, int int_newPageTot) {
        db = helper.getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능

        ContentValues values = new ContentValues();
        values.put("page_total", int_newPageTot);    //page_total 값을 수정
        //error->
        //db.update("booklist", values, "page_total=?", new int[] {int_newPageTot});
        //<-

        Toast.makeText(getApplicationContext(),
                str_title+"의 전체 페이지 수는 "+Integer.toString(int_newPageTot)+" 페이지 입니다", Toast.LENGTH_SHORT).show();
    }

    /*
    <Update current page>
    work: Update current page
    input: title, new current page
    output: none
    */
    public void update_cur_page(String str_title, int int_newPageCur) {
        db = helper.getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능

        ContentValues values = new ContentValues();
        values.put("page_cur", int_newPageCur);    //page_total 값을 수정
        //db.update("booklist", values, "title=?", new String[]{str_title});

        Toast.makeText(getApplicationContext(),
                str_title+"을 "+Integer.toString(int_newPageCur)+" 페이지 만큼 읽었습니다", Toast.LENGTH_SHORT).show();
    }


    /*
    <Update genre>
    work: Update genre
    input: title, new genre
    output: none
    */
    public void update_genre(String str_title, String str_newGenre) {
        db = helper.getWritableDatabase(); //db 객체를 얻어온다. 쓰기가능

        ContentValues values = new ContentValues();
        values.put("genre", str_newGenre);    //page_total 값을 수정
        //db.update("booklist", values, "title=?", new String[]{str_title});
    }

    /*
    <Delete>
    work: Delete book
    input: title
    output: none
    */
    public void delete(String str_title) {
        db = helper.getWritableDatabase();
        db.delete("booklist", "str_title=?", new String[]{str_title});
        Log.i("db", str_title + "정상적으로 삭제 되었습니다.");
    }

    /*
    <Select>
    work: Show list of books (print)
    input: none
    output: none
    */
    public void select() {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("booklist", null, null, null, null, null, null);

        /*
         * 위 결과는 select * from student 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */

        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int int_idBook = c.getInt(c.getColumnIndex("id_book"));
            int int_idTree = c.getInt(c.getColumnIndex("id_tree"));
            String str_title = c.getString(c.getColumnIndex("title"));
            String str_author = c.getString(c.getColumnIndex("author"));
//            String str_imageLink = c.getString(c.getColumnIndex("image_link"));
            byte[] b_imageLink = c.getBlob(c.getColumnIndex("image_link"));
            int int_pageTotal = c.getInt(c.getColumnIndex("page_total"));
            String date = c.getString(c.getColumnIndex("date"));
            //String name = c.getString(c.getColumnIndex("name"));
            //int age = c.getInt(c.getColumnIndex("age"));
            //String address = c.getString(c.getColumnIndex("address"));
            //Log.i("db", "id: " + _id + ", name : " + name + ", age : " + age
            //        + ", address : " + address);
            Log.i("db_show", "id_book: " + int_idBook + "id_tree: " + int_idTree
                    + "title: " + str_title + "author: " + str_author + "image_link: " + b_imageLink + "page_total: " + int_pageTotal
                    +"date: "+date);
        }
    }

    public void select2() {

        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("genreCount", null, null, null, null, null, null);

        /*
         * 위 결과는 select * from student 가 된다. Cursor는 DB결과를 저장한다. public Cursor
         * query (String table, String[] columns, String selection, String[]
         * selectionArgs, String groupBy, String having, String orderBy)
         */

        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int nCount = c.getInt(c.getColumnIndex("count"));
            String genre = c.getString(c.getColumnIndex("genre"));

        }
    }


    public SQLiteDatabase getDb() {
        return db;
    }

    public BookListDBHelper getHelper() {
        return helper;
    }

    public BookListAdapter getAdapter() {
        return adapter;
    }

    public String getGenre(String strGenre) {
        return strGenre;
    }


    //Work: bitmap -> byte array (SQLite에 저장하기 위함)
    public byte[] getByteArrayFromDrawable( Bitmap bitmap) {
        //Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] data = stream.toByteArray();

        return data;
    }

    /////////////////////////////////MyPage//////////////////////////////////////////////

    public void initLists() {

        strListGenre.add("소설");
        strListGenre.add("시/에세이");
        strListGenre.add("인문");
        strListGenre.add("가정/생활/요리");
        strListGenre.add("건강");
        strListGenre.add("취미/레저");
        strListGenre.add("경제/경영");
        strListGenre.add("자기계발");
        strListGenre.add("사회");
        strListGenre.add("역사");
        strListGenre.add("문화");
        strListGenre.add("종교");
        strListGenre.add("예술");
        strListGenre.add("대중문화");
        strListGenre.add("학습/참고서");
        strListGenre.add("국어/외국어");
        strListGenre.add("사전");
        strListGenre.add("과학/공학");
        strListGenre.add("취업/수험서");
        strListGenre.add("여행/지도");
        strListGenre.add("컴퓨터/IT");
        strListGenre.add("잡지");
        strListGenre.add("청소년");
        strListGenre.add("유아");
        strListGenre.add("어린이");
        strListGenre.add("만화");
        strListGenre.add("해외도서");

        //db에다가 insert (db에 행이 이미 존재한다면 추가하지 않는다)
        c = dbGenreCount.rawQuery("SELECT genre FROM genreCount", null);

        if(c.getCount() == 0) {
            //db에 추가
            for(int i=0; i<GENRE; i++) {
                values = new ContentValues();
                values.put("genre", strListGenre.get(i));
                values.put("count", 0);
                try{
                    dbGenreCount.beginTransaction();
                    dbGenreCount.insert("genreCount", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
                    dbGenreCount.setTransactionSuccessful();
                } catch (SQLException e){
                } finally {
                    dbGenreCount.endTransaction();
                }
            }
        }


    }

}