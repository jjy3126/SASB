package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by crown on 2018-02-08.
 */


public class Board_Modify_Activity extends Activity {

    ImageButton button_back;
    TextView textView_back;

    EditText editText_title;
    EditText editText_content;
    ImageView imageView_content;
    Button button_add_image;
    Button button_modify;

    int what;

    String user_id;
    int board_number;
    String board_title;
    String board_text_content;
    String board_image_content;

    String fileName = null;
    String absolutePath = null;

    public static final int PICK_FROM_CAMERA = 1;
    public static final int PICK_FROM_ALBUM = 2;

    private Uri photoUri;

    AlertDialog.Builder builder;

    BoardModifyThread boardModifyThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_modify);

        button_back = (ImageButton) findViewById(R.id.button_back);
        textView_back = (TextView) findViewById(R.id.textView_back);
        editText_title = (EditText) findViewById(R.id.editText_title);
        editText_content = (EditText) findViewById(R.id.editText_content);
        imageView_content = (ImageView) findViewById(R.id.imageView_content);
        button_add_image = (Button) findViewById(R.id.button_add_image);
        button_modify = (Button) findViewById(R.id.button_modify);

        what = getIntent().getExtras().getInt("what");
        user_id = getIntent().getExtras().getString("user_id");

        board_number = getIntent().getExtras().getInt("board_number");
        board_title = getIntent().getExtras().getString("board_title");
        board_text_content = getIntent().getExtras().getString("board_text_content");
        board_image_content = getIntent().getExtras().getString("board_image_content");

        editText_title.setText(board_title);
        editText_content.setText(board_text_content);

        String image_url = "http://121.187.77.1:25000/uploads/" + board_image_content;
        Glide.with(getApplicationContext())
                .load(image_url)
                .into(imageView_content);

        builder = new AlertDialog.Builder(this);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        button_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto();
                    }
                };
                DialogInterface.OnClickListener galleryListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAlbum();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };
                builder.setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", galleryListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });

        button_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                board_title = editText_title.getText().toString();
                board_text_content = editText_content.getText().toString();

                boardModifyThread = new BoardModifyThread();
                boardModifyThread.execute();
            }
        });

        set_Title(what);
    }

    public void set_Title(int what) {
        switch(what) {
            case 1:
                textView_back.setText("글 쓰기(자유 게시판)");
                break;
            case 2:
                textView_back.setText("글 쓰기(학년별 게시판)");
                break;
            case 3:
                textView_back.setText("글 쓰기(자취방 게시판)");
                break;
            case 4:
                textView_back.setText("글 쓰기(중고 게시판)");
                break;
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;

        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "이미지 처리 오류", Toast.LENGTH_LONG).show();
        }

        if (photoFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
            } else {
                photoUri = Uri.fromFile(photoFile);
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "jseaf_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/NOSTest/");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(imageFileName, ".png", storageDir);
        absolutePath = image.getAbsolutePath();
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;

    }


    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            photoUri = data.getData();

            absolutePath = getPathFromUri(photoUri);
            Bitmap bm = BitmapFactory.decodeFile(absolutePath);
            imageView_content.setImageBitmap(bm);
            create_FileName();

        } else if (requestCode == PICK_FROM_CAMERA) {
            // 갤러리에 나타나게
            MediaScannerConnection.scanFile(getApplicationContext(),//앨범에 사진을 보여주기 위해 스캔
                    new String[]{photoUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });

            Bitmap bm = BitmapFactory.decodeFile(absolutePath);
            imageView_content.setImageBitmap(bm);
            create_FileName();
        }
    }

    public void create_FileName() {
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "jseaf_" + timeStamp + "_";

        fileName = imageFileName + ".png";
    }

    public String getPathFromUri(Uri uri){

        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();

        return path;
    }


    class BoardModifyThread extends AsyncTask<Void, Void, String> {
        private String myResult;

        String host = "http://121.187.77.1:25000/board_modify.php";

        String what_temp;
        String board_number_temp;
        String user_id_temp;
        String title_temp;
        String text_content_temp;

        @Override
        protected void onPreExecute() {
            Log.i("BoardModifyThread", "onPreExecute");
        }
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";


                FileInputStream mFileInputStream = null;

                what_temp = URLEncoder.encode(String.valueOf(what), "utf-8");
                board_number_temp = URLEncoder.encode(String.valueOf(board_number), "utf-8");

                user_id_temp = URLEncoder.encode(user_id, "utf-8");
                title_temp = URLEncoder.encode(board_title, "utf-8");
                text_content_temp = URLEncoder.encode(board_text_content, "utf-8");

                URL connectUrl = new URL(host); // url 설정
                HttpURLConnection conn = (HttpURLConnection) connectUrl
                        .openConnection(); //접속
                //설정=
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                conn.connect();


                // write data
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                //텍스트전송
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"what\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(what_temp);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"userID\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(user_id_temp);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"board_number\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(board_number_temp);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"TITLE\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(title_temp);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"TEXT_CONTENT\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(text_content_temp);
                dos.writeBytes(lineEnd);

                if(absolutePath != null) {
                    mFileInputStream = new FileInputStream(absolutePath);
                    //이미지전송
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + fileName + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.flush(); // 찌꺼기 전송

                    int bytesAvailable = mFileInputStream.available();
                    int maxBufferSize = 8 * 1024 * 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

                    Log.d("BoardModifyThread", "image byte is " + bytesRead);
                    // read image
                    while (bytesRead > 0) {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = mFileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
                    }

                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    Log.e("BoardModifyThread", "File is written");
                    mFileInputStream.close();
                }
                // close streams
                dos.flush(); // finish upload...
                dos.close();

                // get response
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                }
                myResult = builder.toString();
                conn.disconnect();
            } catch (Exception e) {
                Log.d("BoardModifyThread", "exception " + e.getMessage());
                Log.i("BoardModifyThread", "fail");
                // TODO: handle exception
            }
            return myResult;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("BoardModifyThraed", "result : " + result);

            if(result.equals("1\n")) {
                Log.i("BoardMoidfyThread", "upload 완료");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "수정 에러..", Toast.LENGTH_LONG).show();
            }

        }
    }
}
