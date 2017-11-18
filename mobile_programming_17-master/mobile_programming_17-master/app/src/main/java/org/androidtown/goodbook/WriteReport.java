package org.androidtown.goodbook;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

public class WriteReport extends AppCompatActivity {

    SQLiteDatabase db;
    SQLiteOpenHelper helper;

    //title
    Spinner spTitle;

    //context
    EditText etContext;

    //save
    Button btnSave;

    //picture
    Button btnPic;
    Button btnPicDelete;
    ImageView ivAddPic;
    int REQ_CODE_SELECT_IMAGE = 100;
    int resizedWidth = 300;
    int resizedHeight = 300;
    boolean isAddPic = false;
    byte[] bImgLink;


    ArrayList<String> strListTitle = new ArrayList<String>();

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    Uri photoUri;
    private static final int MULTIPLE_PERMISSIONS = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("WriteReport>>", "onCreate");
        bImgLink = new byte[1];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_report);

        Intent i = getIntent();

        //DB(report) reset
        helper = new BookListDBHelper(this, // 현재 화면의 context
                "book.db",
                null, // 커서 팩토리
                1);

        db = helper.getWritableDatabase();
        //booklist table에서 제목만 골라오기

        spTitle = (Spinner) findViewById(R.id.spTitle);
        etContext = (EditText) findViewById(R.id.etContext);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnPic = (Button) findViewById(R.id.btnPic);
        btnPicDelete = (Button) findViewById(R.id.btnPicDelete);
        ivAddPic = (ImageView) findViewById(R.id.ivAddPic);

        ivAddPic.setVisibility(View.GONE);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                db = helper.getWritableDatabase();

                values.put("title", spTitle.getSelectedItem().toString());
                values.put("context", etContext.getText().toString());
                values.put("image_link", bImgLink);

                db.insert("report", null, values);

                Toast.makeText(getApplicationContext(), "독서록이 추가되었습니다", Toast.LENGTH_SHORT).show();


                finish();
            }
        });

        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //사진을 추가하였다

                //그러면 tvIsPicExsist를 "사진이 추가되었습니다"
                //아니라면 "추가한 사진이 없습니다" 라고 함
//                Intent i = new Intent(Intent.ACTION_PICK);
//                i.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//                i.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, REQ_CODE_SELECT_IMAGE);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });

        btnPicDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bImgLink = new byte[1];
                Log.i("WriteReport>>", valueOf(bImgLink.length));
                isAddPic = false;
                ivAddPic.setImageResource(R.drawable.ic_empty);
                ivAddPic.setVisibility(View.GONE);

            }
        });
    }

    private boolean checkPermissions(){
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();

        //spinner와 연동
        initTitleSpinner();
    }


    //스피너 초기화
    //스피너에 책 제목이 들어갈 수 있게 한다
    public void initTitleSpinner() {
        getTitleFromBooklist();

        ArrayAdapter<String> adpTitle = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strListTitle);
        adpTitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTitle.setAdapter(adpTitle);

    }

    //booklist table에서 제목을 읽어와, arraylist(strListTitle)에 저장하는 것
    private void getTitleFromBooklist() {
        db = helper.getReadableDatabase();
        Cursor c = db.query("booklist", null, null, null, null, null, null);

        String strTitle;

        while (c.moveToNext()) {
            strTitle = c.getString(c.getColumnIndex("title"));
            strListTitle.add(strTitle);
        }
    }

    //db에 잘들어갔나 테스트
    private void reportTest() {
        db = helper.getReadableDatabase();
        Cursor c = db.query("report", null, null, null, null, null, null);

        String str1, str2, str3;

        while (c.moveToNext()) {
            str1 = c.getString(c.getColumnIndex("title"));
            str2 = c.getString(c.getColumnIndex("context"));
            str3 = c.getString(c.getColumnIndex("image_link"));

            //str3 toString하면 BLOB->String 바꿀 수 없어서 에러남
            Log.i("selectReport->", str1 + ", " + str2 + ", image_link>> " + str3);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SELECT_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {
                if(data==null){
                    return ;
                }

                try {
                    photoUri=data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),photoUri);
                    Bitmap resized = Bitmap.createScaledBitmap(bm, resizedWidth, resizedHeight, true);

                    //배치해놓은 ImageView에 set
                    ivAddPic.setImageBitmap(resized);

                    Toast.makeText(WriteReport.this, "사진을 추가하였습니다", Toast.LENGTH_SHORT).show();

                    bImgLink = getByteArrayFromBitmap(resized);

                    ivAddPic.setVisibility(View.VISIBLE);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


//    public String getImagePath(Uri data) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//
//        cursor.moveToFirst();
//
//        String imgPath = cursor.getString(column_index);
//
//        return imgPath;
//    }

    public byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        //Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        return data;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

}