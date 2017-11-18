package org.androidtown.goodbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sang on 2017-05-06.
 */

public class BookListDBHelper extends SQLiteOpenHelper{
    public BookListDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // TODO Auto-generated method stub

        String sql = "create table booklist (" +
                "id_book integer primary key autoincrement, " +
                "id_tree integer, " +
                "title text, " +
                "author text, " +
                "genre text, " +
                "image_link BLOB, " +
                "page_total integer, " +
                "page_cur integer, " +
                "read_times integer, " +
                "rate integer," +
                "date_finish text, " +
                "date text" +
                ");";

        String sqlReport = "create table report (" +
                "id_report integer primary key autoincrement, " +
                "title text ," +
                "context text," +
                "image_link BLOB" +
                ");";

        String sqlGenreCount = "create table genreCount(" +
                "genre text," +
                "count integer" +
                ");";

        String sqlReadAmount = "create table readAmount(" +
                "title text," +
                "genre text," +
                "date text," +
                "page_amount integer" +
                ");";

        String sqlGoal = "create table goal(" +
                "date text," +
                "page_amount int," +
                "tree_type text" +
                ");";

        String sqlTutorial = "create table tutorial(" +
                "pass text" +
                ");";



        sqLiteDatabase.execSQL(sqlReport);
        sqLiteDatabase.execSQL(sqlGenreCount);
        sqLiteDatabase.execSQL(sqlReadAmount);
        sqLiteDatabase.execSQL(sqlGoal);
        sqLiteDatabase.execSQL(sqlTutorial);
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // db = 적용할 db, old/new 구 버전/신버전
        // TODO Auto-generated method stub
        /*
         * db 버전이 업그레이드 되었을 때 실행되는 메소드
         * 이 부분은 사용에 조심해야 하는 일이 많이 있다. 버전이 1인 사용자가 2로 바뀌면
         * 한번의 수정만 하면 되지만 버전이 3으로 되면 1인 사용자가 2, 3을 거쳐야 하고
         * 2인 사용자는 3 까지만 거치면 된다. 이렇게 증가할 수록 수정할 일이 많아지므로
         * 적절히 사용해야 하며 가능하면 최초 설계 시에 완벽을 기하는 것이 가장 좋을 것이다.
         * 테스트에서는 기존의 데이터를 모두 지우고 다시 만드는 형태로 하겠다.
         */

        String sql = "drop table if exists booklist";
        sqLiteDatabase.execSQL(sql);
        String sqlReport = "drop table if exists report";
        sqLiteDatabase.execSQL(sqlReport);
        String sqlGenreCount = "drop table if exists genreCount";
        sqLiteDatabase.execSQL(sqlGenreCount);
        String sqlReadAmount = "drop table if exists readAmount";
        sqLiteDatabase.execSQL(sqlReadAmount);
        String sqlGoal = "drop table if exists goal";
        sqLiteDatabase.execSQL(sqlGoal);
        String sqlTutorial = "drop table if exists tutorial";
        sqLiteDatabase.execSQL(sqlTutorial);
        onCreate(sqLiteDatabase); // 테이블을 지웠으므로 다시 테이블을 만들어주는 과정
    }
}
