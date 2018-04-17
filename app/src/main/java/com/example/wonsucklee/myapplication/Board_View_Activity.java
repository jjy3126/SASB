package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_BOARD_MODIFY;

/**
 * Created by crown on 2018-02-08.
 */


public class Board_View_Activity extends Activity {

    ImageButton button_back;
    TextView textView_back;
    ImageView imageView_modify;

    TextView textView_name;
    TextView textView_title;
    TextView textView_writetime;
    TextView textView_text_content;
    ImageView imageView_image_content;

    int what = 0;
    String user_id;

    String board_user_id;
    String board_user_name;
    String board_user_profile;
    int board_number;
    String board_title;
    String board_text_content;
    String board_image_content;
    String board_writetime;

    int arraylist_position;

    BoardDelete boardDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_view);

        button_back = (ImageButton) findViewById(R.id.button_back);
        textView_back = (TextView) findViewById(R.id.textView_back);
        textView_name = (TextView) findViewById(R.id.textView_name);
        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_writetime = (TextView) findViewById(R.id.textView_writetime);
        textView_text_content = (TextView) findViewById(R.id.textView_text_content);
        imageView_image_content = (ImageView) findViewById(R.id.imageView_image_content);

        imageView_modify = (ImageView) findViewById(R.id.imageView_modify);

        arraylist_position= getIntent().getExtras().getInt("arraylist_position");
        what = getIntent().getExtras().getInt("what");
        user_id = getIntent().getExtras().getString("user_id");

        board_user_id = getIntent().getExtras().getString("board_user_id");
        board_user_name = getIntent().getExtras().getString("board_user_name");
        board_user_profile = getIntent().getExtras().getString("board_user_profile");
        board_number = getIntent().getExtras().getInt("board_number");
        board_title = getIntent().getExtras().getString("board_title");
        board_text_content = getIntent().getExtras().getString("board_text_content");
        board_image_content = getIntent().getExtras().getString("board_image_content");
        board_writetime = getIntent().getExtras().getString("board_writetime");

        textView_name.setText(board_user_name);
        textView_title.setText(board_title);
        textView_text_content.setText(board_text_content);
        textView_writetime.setText(board_writetime);

        String image_url = "http://121.187.77.1:25000/uploads/" + board_image_content;

        Glide.with(getApplicationContext())
                .load(image_url)
                .into(imageView_image_content);

        if(user_id.equals(board_user_id)) {
            imageView_modify.setVisibility(View.VISIBLE);
        } else {
            imageView_modify.setVisibility(View.GONE);
        }


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        imageView_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.menu_modify, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.menu_modify:
                                Intent intent = new Intent(getApplicationContext(), Board_Modify_Activity.class);
                                intent.putExtra("what", what);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("board_number", board_number);
                                intent.putExtra("board_title", board_title);
                                intent.putExtra("board_text_content", board_text_content);
                                intent.putExtra("board_image_content", board_image_content);

                                startActivityForResult(intent, REQUEST_CODE_TO_BOARD_MODIFY);
                                break;
                            case R.id.menu_delete:

                                AlertDialog.Builder dialog = new AlertDialog.Builder(Board_View_Activity.this);

                                dialog.setTitle("삭제하시겠습니까?");

                                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        boardDelete = new BoardDelete();
                                        boardDelete.execute();
                                        dialog.dismiss();
                                    }
                                }); //수락을 눌렀을 때

                                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }); //거절을 눌렀을 때
                                dialog.show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    class BoardDelete extends AsyncTask<Void, Void, String> {

        String host = "http://121.187.77.1:25000/board_delete.php";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        @Override
        protected void onPreExecute() {
            Log.i("BoardDelete", "1");
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder builder = new StringBuilder();

            try {
                URL connectUrl = new URL(host);
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

                String what_temp = URLEncoder.encode(String.valueOf(what), "utf-8");
                String board_number_temp = URLEncoder.encode(String.valueOf(board_number), "utf-8");
                String user_id_temp = URLEncoder.encode(user_id, "utf-8");

                Log.i("BoardDelete", "2");
                if(conn != null) {
                    Log.i("BoardDelete", "3");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    conn.connect();

                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"what\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(what_temp);
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"board_number\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(board_number_temp);
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"userID\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(user_id_temp);
                    dos.writeBytes(lineEnd);

                    dos.flush();
                    dos.close();

                    InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(isr);
                    builder = new StringBuilder();
                    String str;
                    while((str = reader.readLine()) != null) {
                        builder.append(str + "\n");
                    }

                    Log.i("Board_Delete", builder.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Board_delete", "fail");
            }
            Log.i("Board_delete", "success");

            return builder.toString();
        }

        @Override
        protected void onPostExecute(String str) {
            Log.i("Board_delete", str);

            if(str.equals("1\n")) {
                Toast.makeText(getApplicationContext(), "삭제 완료", Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.putExtra("is_deleted", "Y");
                intent.putExtra("arraylist_position", arraylist_position);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "삭제에러, 다시 시도하여 주세요", Toast.LENGTH_LONG).show();
            }
        }
    }

}
