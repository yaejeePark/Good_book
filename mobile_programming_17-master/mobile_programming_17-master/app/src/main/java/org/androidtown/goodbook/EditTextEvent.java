package org.androidtown.goodbook;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class EditTextEvent extends Activity implements OnEditorActionListener {
    final static int NUM_PRODUCT_PER_SCREEN = 10;//한 화면에 출력될 제품수

    int nStartPosition = 1;//출력할 리스트의 시작 위치 지정

    ListView booklist;
    EditText editInput = null;
    TextView textResult = null;
    String bookName = null;
    EditText et;

    BookListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        Intent intent = getIntent();

        adapter = new BookListAdapter();
        et = (EditText) findViewById(R.id.search_book);
        editInput = (EditText) findViewById(R.id.search_book);
        editInput.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        bookName = et.getText().toString();
        NaverBookRequest request = new NaverBookRequest(bookName);//검색 리퀘스트 값들 지정
        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<NaverBooks>() {//새로운 네트워크메니저 생성하여 생성한 리퀘스트 넣어줌
            @Override
            public void onSuccess(NetworkRequest<NaverBooks> request, NaverBooks result) {
                adapter.addAll(result.items);//성공시에 받아온 아이템을 리스트에 넣어준다
            }

            @Override
            public void onFailure(NetworkRequest<NaverBooks> request, int errorCode, int responseCode, String message, Throwable exception) {
                Toast.makeText(EditTextEvent.this, "fail", Toast.LENGTH_SHORT).show();
                Log.i("MainActivity", "responseCode : " + responseCode);

            }
        });
        return true;

    }

}
