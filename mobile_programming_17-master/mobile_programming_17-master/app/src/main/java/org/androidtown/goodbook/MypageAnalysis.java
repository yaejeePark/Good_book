package org.androidtown.goodbook;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MypageAnalysis extends Fragment {
    /* 원형 그래프 */
    private ViewGroup layoutGraphView;
    List<CircleGraph> arrGraph = new ArrayList<CircleGraph>();
    ArrayList<String> strListColor = new ArrayList<String>(27);
    int nCnt = 0;

    /* 바 그래프 */
    TextView textView;
    ArrayList<String> strListGenre = new ArrayList<String>(27);
    ArrayList<Integer> nListCount = new ArrayList<Integer>(28);
    private ViewGroup layoutGraphView1;
    SQLiteDatabase db;
    BookListDBHelper helper;
    int iMax = 0, iSecond = 0;
    int nMax = 0, nSecond = 0;
    final int ANALYSIS_ERROR_MAX = 100;
    final int ANAYASIS_ERROR_SEC = 110;
    Button btnPersonal;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_graph, container, false);


        //DB 초기화
        helper = new BookListDBHelper(this.getContext(),
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호

        //genre list 초기화
        initGenreLists();
        initCntList();

        textView = (TextView) rootView.findViewById(R.id.textView);
        String str = "\"";
        textView.setText(str + getNicName() + str + "\n");

        //원형그래프 그리기
        CircleGraphVO vo = makeLineGraphAllSetting();
        layoutGraphView = (ViewGroup) rootView.findViewById(R.id.layoutGraphView);
        layoutGraphView.addView(new CircleGraphView(this.getContext(), vo));
        btnPersonal = (Button) rootView.findViewById(R.id.btnPersonal);
        setListColor();

        //바 그래프 그리기
//        LineGraphVO vo1 = makeLineGraph1AllSetting();
//        layoutGraphView1 = (ViewGroup) rootView.findViewById(R.id.layoutGraphView2);
//        layoutGraphView1.addView(new LineGraphView(this.getContext(), vo1));

        btnPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.naver.com/04_miffy/221024021387"));
                startActivity(i);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        arrGraph.clear();
        nListCount.clear();

        initCntList();

        nCnt = 0;
        readValuesFromDB();
        getNicName();
    }

    public String getNicName() {
        ArrayList<String> adjective = new ArrayList<String>(28);
        ArrayList<String> noun = new ArrayList<String>(27);

        adjective.add("상상력이 풍부한 ");
        adjective.add("감성적인 ");
        adjective.add("생각하는 ");
        adjective.add("다정한 ");
        adjective.add("건강한 ");
        adjective.add("자유를 즐기는 ");
        adjective.add("경제적인 ");
        adjective.add("행복을 주는 ");
        adjective.add("사회적인 ");
        adjective.add("역사적인 ");
        adjective.add("문화적인 ");
        adjective.add("삶의 의미를 아는 ");
        adjective.add("예술적인 ");
        adjective.add("시대를 따르는 ");
        adjective.add("열공하는 ");
        adjective.add("언어를 좋아하는 ");
        adjective.add("박학다식한 ");
        adjective.add("과학적인 ");
        adjective.add("잘 될 ");
        adjective.add("자유분방한 ");
        adjective.add("창조적인 ");
        adjective.add("세련된 ");
        adjective.add("청소년에게 관심이 많은 ");
        adjective.add("유아에게 관심이 많은 ");
        adjective.add("동심으로 돌아가고 싶은 ");
        adjective.add("그림을 좋아하는 ");
        adjective.add("외국어를 잘하는 ");
        adjective.add("");

        noun.add("소설 애호가");
        noun.add("감성왕");
        noun.add("최소 소크라테스");
        noun.add("사랑꾼");
        noun.add("트레이너");
        noun.add("자유인");
        noun.add("경제학도");
        noun.add("파워왕");
        noun.add("사회인");
        noun.add("역사인");
        noun.add("문화인");
        noun.add("초월인");
        noun.add("예술인");
        noun.add("신세대");
        noun.add("노력가");
        noun.add("외국인");
        noun.add("걸어다니는 사전");
        noun.add("공학도");
        noun.add("곰돌이");
        noun.add("자연인");
        noun.add("프로그래머");
        noun.add("도시인");
        noun.add("최소 중,고등학교 선생님");
        noun.add("최소 어린이집 선생님");
        noun.add("동심의 사람");
        noun.add("오타쿠");
        noun.add("good man!");

        Bundle bnd = getIdxOfBiggestValues();
        int idx1 = bnd.getInt("max");
        int idx2 = bnd.getInt("second");

        if (idx1 == ANALYSIS_ERROR_MAX) {
            return "책을 더 읽어주세요";

        } else if (idx1 != ANALYSIS_ERROR_MAX && idx2 == ANAYASIS_ERROR_SEC) {
            //max 책은 있으나
            //sec 책은 없다
            String no = noun.get(idx1); //1위
            String ad = adjective.get(idx1); //2위

            Log.i("MypageAnalysis>>", "(1-error) idx1: " + idx1 + ", idx2: " + idx2);
            Log.i("MypageAnalysis>>", "(1) idx1: " + idx1 + ", idx2: " + idx1);

            return ad + no;

        } else if (idx1 != ANALYSIS_ERROR_MAX && idx2 != ANAYASIS_ERROR_SEC) {
            //max, sec 둘 다 있다
            String no = noun.get(idx1); //1위
            String ad = adjective.get(idx2); //2위

            Log.i("MypageAnalysis>>", "(2-error) idx1: " + idx1 + ", idx2: " + idx2);
            Log.i("MypageAnalysis>>", "(2) idx1: " + idx1 + ", idx2: " + idx2);


            return ad + no;
        }
        return "";
    }

    public void initGenreLists() {
        //DB 얻어오기
        strListGenre.clear();

        db = helper.getReadableDatabase();
        Cursor c = db.query("genreCount", null, null, null, null, null, null);

        //strListGenre, nListCount 초기화
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            String str_genre = c.getString(c.getColumnIndex("genre"));
            strListGenre.add(str_genre);
        }

    }

    private void initCntList() {
        nListCount.clear();

        //DB 얻어오기
        db = helper.getReadableDatabase();
        Cursor c = db.query("genreCount", null, null, null, null, null, null);

        //strListGenre, nListCount 초기화
        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int int_count = c.getInt(c.getColumnIndex("count"));
            nListCount.add(int_count);
        }
        nListCount.add(0);
    }


    private Bundle getIdxOfBiggestValues() {
//        int iMax = 0, iSecond = 0;
//        int nMax = 0, nSecond = 0;
        Bundle bnd = new Bundle();
        bnd.putInt("max", ANALYSIS_ERROR_MAX);
        bnd.putInt("second", ANAYASIS_ERROR_SEC);

        int i;

        //max
        try {
            nMax = nListCount.get(0);

            iMax = 0;
            for (i = 1; i < 27; i++) {
                if (nListCount.get(i) >= nMax) {
                    nMax = nListCount.get(i);
                    iMax = i;
                }
            }
            Log.i("MypageAnalysis>>", "max idx: " + iMax);

            bnd.putInt("max", iMax);
            int iSec = getIdxOfSecondValues();

            if (iSec == ANAYASIS_ERROR_SEC)
                bnd.putInt("second", iMax);
            else
                bnd.putInt("second", iSec);

        } catch (IndexOutOfBoundsException e) {
            Log.i("MypageAnalysis>>", "BiggestValueError");
            e.printStackTrace();

        }
        return bnd;
    }

    private int getIdxOfSecondValues() {
        //second
        int i;
        iSecond = 0;
        nSecond = 0;

        try {
            for (i = 0; i < 28; i++) {
                if (i != iMax) {
                    if (nListCount.get(i) >= nSecond) {
                        nSecond = nListCount.get(i);
                        iSecond = i;
                    }
                }
            }
            Log.i("MypageAnalysis>>", "sec idx: " + iSecond + ", sec value: " + nListCount.get(iSecond));

        } catch (IndexOutOfBoundsException e) {
            iSecond = ANAYASIS_ERROR_SEC;
            e.printStackTrace();
            Log.i("MypageAnalysis>>", "SecondValueError");

        }
        return iSecond;
    }

    /**
     * make line graph using options
     *
     * @return
     */
    public CircleGraphVO makeLineGraphAllSetting() {
        //BASIC LAYOUT SETTING
        //padding
        int paddingBottom = CircleGraphVO.DEFAULT_PADDING;
        int paddingTop = CircleGraphVO.DEFAULT_PADDING;
        int paddingLeft = CircleGraphVO.DEFAULT_PADDING;
        int paddingRight = CircleGraphVO.DEFAULT_PADDING;

        //graph margin
        int marginTop = CircleGraphVO.DEFAULT_MARGIN_TOP;
        int marginRight = CircleGraphVO.DEFAULT_MARGIN_RIGHT;

        // radius setting
        int radius = 130;

        CircleGraphVO vo = new CircleGraphVO(paddingBottom, paddingTop, paddingLeft, paddingRight, marginTop, marginRight, radius, arrGraph);

        // circle Line
        vo.setLineColor(Color.WHITE);

        // set text setting
        vo.setTextColor(Color.WHITE);

        vo.setTextSize(50);

        // set circle center move X ,Y
        vo.setCenterX(0);
        vo.setCenterY(0);

        //set animation
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, 2000));
        //set graph name box

        GraphNameBox graphNameBox = new GraphNameBox();

        // nameBox
        graphNameBox.setNameboxMarginTop(25);
        graphNameBox.setNameboxMarginRight(25);

        vo.setGraphNameBox(graphNameBox);

        return vo;
    }

    private void readValuesFromDB() {

        Cursor c = db.query("genreCount", null, null, null, null, null, null, null);//6개가 있었음.

        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            int int_count = c.getInt(c.getColumnIndex("count"));
            String str_genre = c.getString(c.getColumnIndex("genre"));
            if (int_count > 0) {//걍 >이거였음.
                addValueToCircleGraph(str_genre, nCnt, int_count);
                nCnt = nCnt + 1;
            }
        }

    }

    private void addValueToCircleGraph(String strGenre, int nCntOrder, int nValue) {
        String strColor = strListColor.get(nCntOrder);
        arrGraph.add(new CircleGraph(strGenre, Color.parseColor(strColor), nValue));
    }


    //원형 차트 색깔 설정
    public void setListColor() {
        strListColor.add("#FF0066");
        strListColor.add("#EB6652");
        strListColor.add("#DEA645");
        strListColor.add("#D6CC3D");
        strListColor.add("#CCFF33");
        strListColor.add("#99FF4C");
        strListColor.add("#0AFF94");
        strListColor.add("#00C9BD");
        strListColor.add("#00A3D6");
        strListColor.add("#006EFA");
        strListColor.add("#6324DE");
        strListColor.add("#9900CC");
        strListColor.add("#800080");
        strListColor.add("#660033");
        strListColor.add("#942E1C");
        strListColor.add("#CC6600");
        strListColor.add("#8A2421");
        strListColor.add("#7A8570");
        strListColor.add("#669999");
        strListColor.add("#455799");
        strListColor.add("#333399");
        strListColor.add("#A317D1");
        strListColor.add("#FF00FF");
        strListColor.add("#FF5C85");
        strListColor.add("#FF9933");
        strListColor.add("#66B8AD");
        strListColor.add("#00CCFF");
    }
}