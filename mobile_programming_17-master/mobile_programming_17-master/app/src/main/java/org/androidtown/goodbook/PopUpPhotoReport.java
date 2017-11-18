package org.androidtown.goodbook;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by sang on 2017-06-04.
 */

public class PopUpPhotoReport extends AppCompatActivity {

    //선언
    Spinner spTitle;
    EditText etContext;
    EditText etDelete;
    ImageView ivAddPic;
    Button btnSave, btnPic, btnPicDelete;
    final int REQ_CODE_SELECT_IMAGE = 100;
    byte[] bImgLink = new byte[1];


    ArrayList<String> strListTitle = new ArrayList<String>();
    static int idGet;

    SQLiteDatabase db2_read;
    SQLiteDatabase db2_write;
    BookListDBHelper helper2;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    Uri photoUri;
    private static final int MULTIPLE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_photo_report);

        Intent intent = getIntent();
        Bundle bnd = intent.getExtras();
        idGet = bnd.getInt("id");

        //checkPermissions();

        spTitle = (Spinner) findViewById(R.id.spTitle);
        etContext = (EditText)findViewById(R.id.etContext);
        etDelete = (EditText) findViewById(R.id.etDelete);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnPic = (Button) findViewById(R.id.btnPic);
        ivAddPic = (ImageView) findViewById(R.id.ivAddPic);
        btnPicDelete = (Button)findViewById(R.id.btnPicDelete);


        helper2 = new BookListDBHelper(PopUpPhotoReport.this,"book.db",null, 1); // 버전 번호

        db2_write = helper2.getWritableDatabase();
        db2_read = helper2.getReadableDatabase();

        /* spTitle 세팅 */
        Cursor c2 = db2_read.query("booklist", null, null, null, null, null, null);
        String strTitleRead;

        while (c2.moveToNext()) {
            strTitleRead = c2.getString(c2.getColumnIndex("title"));
            strListTitle.add(strTitleRead);
        }

        ArrayAdapter<String> adpTitle = new ArrayAdapter<>(PopUpPhotoReport.this, android.R.layout.simple_spinner_item, strListTitle);
        adpTitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTitle.setAdapter(adpTitle);


        c2 = db2_write.rawQuery("SELECT * FROM report WHERE id_report=?", new String[]{idGet + ""});
        c2.moveToFirst();

        String strGetContext;
        String strGetTitle;
        strGetContext = c2.getString(c2.getColumnIndex("context"));
        strGetTitle = c2.getString(c2.getColumnIndex("title"));
        bImgLink = c2.getBlob(c2.getColumnIndex("image_link"));
        ivAddPic.setImageBitmap(byteArrayToBitmap(bImgLink));

        etContext.setText(strGetContext);
        spTitle.setSelection(getIndex(spTitle, strGetTitle));


        /* 사진 저장 */
        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(i, REQ_CODE_SELECT_IMAGE);
            }
        });

                /* 사진 지우기 */
        btnPicDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                byte[] imgLink = new byte[1];
//                Log.i("WriteReport>>", valueOf(imgLink.length));
//                boolean isAddPic = false;
//                ivAddPic.setImageResource(R.drawable.ic_empty);
                ivAddPic.setVisibility(View.GONE);
            }
        });



              /* 변경 사항 저장 */
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etDelete.getText().toString().isEmpty()){
                    /* update 하기 */
                    ContentValues values2 = new ContentValues();

                    Cursor c2 = db2_write.rawQuery("SELECT * FROM report WHERE id_report=?", new String[]{idGet + ""});
                    c2.moveToFirst();

                    String strGetTitle, strGetContext;

                    strGetTitle = spTitle.getSelectedItem().toString();
                    strGetContext = etContext.getText().toString();
                    values2.put("title", strGetTitle);
                    values2.put("context", strGetContext);

                    //사진이 보이지 않는다면 (사진을 지웠다면)
                    if (ivAddPic.getVisibility() == View.GONE) {
                        byte[] b = new byte[1];
                        values2.put("image_link", b);
                    }
                    //사진이 보인다면
                    else {
                        values2.put("image_link", bImgLink);
                    }

                    db2_write.update("report", values2, "id_report=?", new String[]{idGet + ""});

                    Toast.makeText(PopUpPhotoReport.this, "내용이 변경되었습니다", Toast.LENGTH_SHORT).show();
                    finish();

                }
                else {
                    if (etDelete.getText().toString().equals("삭제")) {
                    /* 삭제하기 */
                        db2_write.delete("report", "id_report=?", new String[]{idGet + ""});

                        Toast.makeText(PopUpPhotoReport.this, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(PopUpPhotoReport.this, "잘못입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
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



    public Bitmap byteArrayToBitmap(byte[] $byteArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray($byteArray, 0, $byteArray.length);
        return bitmap;
    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        int resizedWidth = 300;
        int resizedHeight = 300;
        final int REQ_CODE_SELECT_IMAGE = 100;

        int[] maxTextureSize = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, maxTextureSize, 0);


        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if(data==null){
                    return ;
                }

                try {
                    photoUri= data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),photoUri);
                    Bitmap resized = Bitmap.createScaledBitmap(bm, resizedWidth, resizedHeight, true);

                    //배치해놓은 ImageView에 set
                    ivAddPic.setImageBitmap(resized);

                    Toast.makeText(PopUpPhotoReport.this, "사진을 추가하였습니다", Toast.LENGTH_SHORT).show();

                    bImgLink = getByteArrayFromBitmap(resized);

                    ivAddPic.setVisibility(View.VISIBLE);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }




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