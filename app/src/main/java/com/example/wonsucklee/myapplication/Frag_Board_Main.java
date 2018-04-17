package com.example.wonsucklee.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO__FRAG_F_BOARD_MAIN;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO__FRAG_G_BOARD_MAIN;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO__FRAG_LA_BOARD_MAIN;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO__FRAG_SH_BOARD_MAIN;

/**
 * Created by crown on 2018-02-08.
 */


public class Frag_Board_Main extends Fragment {

    GetBoardList getBoardList;
    JSONArray jsonArray;

    FloatingActionButton btn_insert;

    Board_List_Adapter board_list_adapter;
    ArrayList<BoardData> board_list_arrayList;

    ListView listView_board;


    int what;
    int grade;
    String user_id;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        what = getArguments().getInt("what");
        user_id = getArguments().getString("user_id");
        View view = inflater.inflate(R.layout.board_main, container, false);

        btn_insert = (FloatingActionButton) view.findViewById(R.id.btn_insert);
        listView_board = (ListView) view.findViewById(R.id.listView_board);

        board_list_arrayList = new ArrayList<>();
        board_list_adapter = new Board_List_Adapter(getActivity(), board_list_arrayList);
        listView_board.setAdapter(board_list_adapter);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Frag_MainActivity) getActivity()).change_Activity(Frag_MainActivity.REQUEST_CODE_TO_BOARD_INSERT, what);
            }
        });

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int temp_position = position;

                BoardData data = board_list_arrayList.get(position);
                String user_name = data.get_User_Name();
                String user_id = data.get_User_Id();
                String user_profile = data.get_User_Profile();
                int board_number = data.get_B_Number();
                String board_title = data.get_Title();
                String board_text_content = data.get_Text_Content();
                String board_image_content = data.get_Image_Content();
                String board_writetime = data.get_Writetime();

                ((Frag_MainActivity) getActivity()).change_Activity(Frag_MainActivity.REQUEST_CODE_TO_BOARD_VIEW, what, user_id,  user_name, user_profile, board_number, board_title, board_text_content, board_image_content, board_writetime, temp_position);
            }
        };
        listView_board.setOnItemClickListener(onItemClickListener);

        switch(what) {
            case REQUEST_CODE_TO__FRAG_F_BOARD_MAIN: //자유 게시판
                break;
            case REQUEST_CODE_TO__FRAG_G_BOARD_MAIN: //학년별 게시판
                grade = 1;
                break;
            case REQUEST_CODE_TO__FRAG_LA_BOARD_MAIN: //자취방 게시판
                break;
            case REQUEST_CODE_TO__FRAG_SH_BOARD_MAIN: //중고 게시판
                break;
        }

        getBoardList = new GetBoardList();
        getBoardList.execute();
        //위로 드래그 시, refresh 하는 코드 필요
        return view;
    }

    public void addItem(int b_number, String user_id, String user_name, String user_profile, String title, String text_content, String image_content, String writetime) {
        BoardData item = new BoardData();

        item.set_B_Number(b_number);
        item.set_User_Id(user_id);
        item.set_User_Name(user_name);
        item.set_User_Profile(user_profile);
        item.set_Title(title);
        item.set_Image_Content(image_content);
        item.set_Text_Content(text_content);
        item.set_Writetime(writetime);

        board_list_arrayList.add(item);
    }

    //게시판 목록을 불러오는 쓰레드
    class GetBoardList extends AsyncTask<Void, Void, String> {

        String host = "http://121.187.77.1:25000/board_list.php";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        @Override
        protected void onPreExecute() {
            Log.i("GetBoardList", "onPreExecute()");
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder builder = new StringBuilder();

            HttpURLConnection conn = null;
            try {
                URL connectUrl = new URL(host);
                conn = (HttpURLConnection) connectUrl.openConnection();

                String what_temp = URLEncoder.encode(String.valueOf(what), "utf-8");
                String user_id_temp = URLEncoder.encode(user_id, "utf-8");

                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.connect();

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                //텍스트전송
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"what\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(what_temp);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(user_id_temp);
                dos.writeBytes(lineEnd);

                if(what == 2) { //학년별 게시판일 때 무슨 학년게시판인지 grade 변수를 보낸다
                    String grade_temp = URLEncoder.encode(String.valueOf(grade), "utf-8");

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"grade\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(grade_temp);
                    dos.writeBytes(lineEnd);
                }


                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                builder = new StringBuilder();
                String str;
                while((str = reader.readLine()) != null) {
                    builder.append(str + "\n");
                }

                reader.close();
                isr.close();
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("GetBoardList", "fail");
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String str) {
            Log.i("GetBoardList", str);

            try {
                jsonArray = new JSONArray(str);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int number = jsonObject.getInt("board_num");
                    String user_id = jsonObject.getString("board_user_id");
                    String user_name = jsonObject.getString("user_name");
                    String user_profile = jsonObject.getString("user_profile");
                    String title = jsonObject.getString("board_title");
                    String image_content = jsonObject.getString("board_image_content");
                    String text_content = jsonObject.getString("board_text_content");
                    String date = jsonObject.getString("board_date");

                    addItem(number, user_id, user_name, user_profile, title, text_content, image_content, date);
                }

                board_list_adapter.notifyDataSetChanged();
            } catch(JSONException e) {
                e.printStackTrace();
                Log.i("GetBoardList", "jsonerr");
            }
        }
    }


}
