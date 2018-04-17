package com.example.wonsucklee.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by crown on 2018-02-08.
 */


/**
 안드로이드의 Activity의 상단을 보면 ActionBar라는 것이 있었다.
 그런데 안드로이드 API21 부터 ActionBar는 deprecated되고 ToolBar라는 것이 추가 되었다.
 ToolBar란 기존의 ActionBar를 대체하는 View의 일종이다.
 ToolBar란 View이기 때문에 기존의 ActionBar에서는 할수 없던 것, 또는 하기 어려웠던 것들을 쉽게 코드로 제어 할 수 있다. (위치제어 등…)

 ActionBar : View가 아니다. 따라서 위치나 내부 아이템을 제어하기 힘들다.
 ToolBar : View다. 따라서 기타 View처럼 제어하기가 슆다.



 */

public class Frag_MainActivity extends AppCompatActivity{
    public static final int REQUEST_CODE_TO__FRAG_ATTENDACE_MAIN = 0;
    public static final int REQUEST_CODE_TO__FRAG_F_BOARD_MAIN = 1;
    public static final int REQUEST_CODE_TO__FRAG_G_BOARD_MAIN = 2;
    public static final int REQUEST_CODE_TO__FRAG_LA_BOARD_MAIN = 3;
    public static final int REQUEST_CODE_TO__FRAG_SH_BOARD_MAIN = 4;

    public static final int REQUEST_CODE_TO_ATTENDACE_MAIN = 1000;
    public static final int REQUEST_CODE_TO_BOARD_MAIN = 1001;
    public static final int REQUEST_CODE_TO_BOARD_INSERT = 1002;
    public static final int REQUEST_CODE_TO_BOARD_MODIFY = 1003;
    public static final int REQUEST_CODE_TO_BOARD_VIEW = 1004;

    Toolbar toolbar;
    DrawerLayout drawerLayout;

    ActionBar actionBar;
    ImageView button_search;
    ImageView button_filter;

    Button btn_logout;

    ListView listView_menu;
    ArrayList<MenuData> arrayList_menu;
    Menu_Adapter menuAdapter;

    private static final int MENU_SIZE = 9;
    private static final String [] MENU_ARRAY = {"출석현황", "게시판"};
    private static final String [] BOARD_MENU_ARRAY = {"자유게시판", "학년별 게시판", "자취방게시판", "중고게시판"};

    boolean menu_change_on;

    private int user_number;
    private String user_id;
    private String user_name;
    private String user_major;
    private String user_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_main);

        user_id = getIntent().getExtras().getString("user_id");
        user_name = getIntent().getExtras().getString("user_name");
        user_major = getIntent().getExtras().getString("user_major");
        user_profile = getIntent().getExtras().getString("user_profile");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        button_search = (ImageView) findViewById(R.id.button_search);
        button_filter = (ImageView) findViewById(R.id.button_filter);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        listView_menu = (ListView) findViewById(R.id.listView_menu);
        arrayList_menu = new ArrayList<>();
        menuAdapter = new Menu_Adapter(getApplicationContext(), arrayList_menu);
        listView_menu.setAdapter(menuAdapter);

        // 툴바 생성 및 세팅하는 부분
        //Toolbar의 왼쪽에 버튼을 추가하고 버튼의 아이콘을 바꾼다.
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그아웃 코드
            }
        });

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                int temp_position = position;
                MenuData data = arrayList_menu.get(temp_position);

                int type_temp = data.get_Type();
                String title_temp = data.get_Title();

                if(type_temp == 1) { //상위메뉴
                    switch(temp_position) {
                        case 0:
                            //출석현황
                            change_fragment(REQUEST_CODE_TO_ATTENDACE_MAIN, REQUEST_CODE_TO__FRAG_ATTENDACE_MAIN);
                            break;
                        case 1: //하위 게시판 펼치기 또는 접기\
                            menu_change_on = change_menu(temp_position, 1, menu_change_on);
                            break;
                    }
                } else if(type_temp == 2) { //하위메뉴
                    if(data.get_Title().equals(BOARD_MENU_ARRAY[0])) {
                        change_fragment(REQUEST_CODE_TO_BOARD_MAIN, REQUEST_CODE_TO__FRAG_F_BOARD_MAIN);
                    } else if(data.get_Title().equals(BOARD_MENU_ARRAY[1])) {
                        change_fragment(REQUEST_CODE_TO_BOARD_MAIN, REQUEST_CODE_TO__FRAG_G_BOARD_MAIN);
                    } else if(data.get_Title().equals(BOARD_MENU_ARRAY[2])) {
                        change_fragment(REQUEST_CODE_TO_BOARD_MAIN, REQUEST_CODE_TO__FRAG_LA_BOARD_MAIN);
                    } else if(data.get_Title().equals(BOARD_MENU_ARRAY[3])) {
                        change_fragment(REQUEST_CODE_TO_BOARD_MAIN, REQUEST_CODE_TO__FRAG_SH_BOARD_MAIN);
                    }
                }

            }
        };
        listView_menu.setOnItemClickListener(onItemClickListener); //리스트뷰 아이템 클릭시

        menu_Init();
        change_fragment(REQUEST_CODE_TO_ATTENDACE_MAIN, 0);

    }

    public void menu_Init() {
        arrayList_menu.clear();

        MenuData data;

        for(int i = 0; i < MENU_ARRAY.length; i++) {
            data = new MenuData();
            data.set_Title(MENU_ARRAY[i]);
            data.set_Type(1);
            arrayList_menu.add(data);
        }

        menuAdapter.notifyDataSetChanged();
    }
    //뒤로가기 버튼을 눌렀을 때

    public void add_menu_item(String title, int type) {
        MenuData data = new MenuData();
        data.set_Title(title);
        data.set_Type(type);

        arrayList_menu.add(data);
    }

    public boolean change_menu(int position, int type, boolean menu_change_on) {
        arrayList_menu.clear();

        if(menu_change_on) { //하위메뉴 펼쳐져 있으면 닫는다.
            for(int i = 0; i < MENU_ARRAY.length; i++) {
                add_menu_item(MENU_ARRAY[i], 1);
            }
            menu_change_on = false;
        } else {
            switch(type) {
                case 1:
                    if(position == 0) {

                    } else if(position == 1) { //하위 게시판 보여주기
                        for(int i = 0; i < MENU_ARRAY.length; i++) {
                            if(i < position) {
                                add_menu_item(MENU_ARRAY[i], 1);
                            } else if(i == position) {
                                add_menu_item(MENU_ARRAY[i], 1);

                                for(int j = 0; j < BOARD_MENU_ARRAY.length; j++) {
                                    add_menu_item(BOARD_MENU_ARRAY[j], 2);
                                }
                            }
                            else {
                                add_menu_item(MENU_ARRAY[i], 1);
                            }
                        }
                    }
                    break;
                case 2:
                    break;
            }
            menu_change_on = true;
        }

        menuAdapter.notifyDataSetChanged();
        return menu_change_on;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) { //이미 열려져있었다면 닫는다
            drawer.closeDrawer(GravityCompat.START);
        } else { //아니라면 뒤로가기
            super.onBackPressed();
        }
    }

    // 햄버거 버튼 클릭 시 드로어 열리도록 하는 곳
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void change_fragment(int frag_num, int what) {
        switch(frag_num) {
            case REQUEST_CODE_TO_ATTENDACE_MAIN:
                button_search.setVisibility(View.GONE);
                button_filter.setVisibility(View.GONE);
                set_Title(what);

                Fragment fragment_attendance = new Frag_Attendace_Main();
                Bundle bundle_attendance = new Bundle(3); // 파라미터는 전달할 데이터 개수
                bundle_attendance.putString("user_id", user_id); // key , value
                bundle_attendance.putString("user_name", user_name);
                bundle_attendance.putString("user_major", user_major);
                bundle_attendance.putString("user_profile", user_profile);
                fragment_attendance.setArguments(bundle_attendance);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, fragment_attendance)
                        .commit();
                break;
            case REQUEST_CODE_TO_BOARD_MAIN:
                button_search.setVisibility(View.VISIBLE);
                button_filter.setVisibility(View.VISIBLE);
                set_Title(what);

                Fragment fragment_board = new Frag_Board_Main();
                Bundle bundle_board = new Bundle(4); // 파라미터는 전달할 데이터 개수
                bundle_board.putInt("what", what); // key , value\
                bundle_board.putString("user_id", user_id); // key , value
                bundle_board.putString("user_name", user_name);
                bundle_board.putString("user_major", user_major);
                bundle_board.putString("user_profile", user_profile);
                fragment_board.setArguments(bundle_board);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, fragment_board)
                        .commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void set_Title(int what) {
        switch(what) {
            case 0:
                getSupportActionBar().setTitle("출석 현황");
                break;
            case 1:
                getSupportActionBar().setTitle("자유 게시판");
                break;
            case 2:
                getSupportActionBar().setTitle("학년별 게시판");
                break;
            case 3:
                getSupportActionBar().setTitle("자취방 게시판");
                break;
            case 4:
                getSupportActionBar().setTitle("중고 게시판");
                break;
        }
    }

    public void change_Activity(int activity_num, int what) {
        Intent intent;

        switch(activity_num) {
            case REQUEST_CODE_TO_ATTENDACE_MAIN:
                break;
            case REQUEST_CODE_TO_BOARD_MAIN:
                break;
            case REQUEST_CODE_TO_BOARD_INSERT:
                intent = new Intent(getApplicationContext(), Board_Insert_Activity.class);
                intent.putExtra("what", what);
                intent.putExtra("user_id", user_id);
                startActivityForResult(intent, REQUEST_CODE_TO_BOARD_INSERT);
                break;
            case REQUEST_CODE_TO_BOARD_VIEW:
                intent = new Intent(getApplicationContext(), Board_View_Activity.class);
                intent.putExtra("what", what);
                intent.putExtra("user_id", user_id);
                startActivityForResult(intent, REQUEST_CODE_TO_BOARD_VIEW);
                break;
            case REQUEST_CODE_TO_BOARD_MODIFY:
                break;
        }
    }

    public void change_Activity(int activity_num, int what, String board_user_id, String board_user_name, String board_user_profile, int board_number, String board_title, String board_text_content, String board_image_content, String board_writetime, int arraylist_position) {
        Intent intent;

        switch(activity_num) {
            case REQUEST_CODE_TO_BOARD_VIEW:
                intent = new Intent(getApplicationContext(), Board_View_Activity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("what", what);
                intent.putExtra("board_user_id", board_user_id);
                intent.putExtra("board_user_name", board_user_name);
                intent.putExtra("board_user_profile", board_user_profile);
                intent.putExtra("board_number", board_number);
                intent.putExtra("board_title", board_title);
                intent.putExtra("board_text_content", board_text_content);
                intent.putExtra("board_image_content", board_image_content);
                intent.putExtra("board_writetime", board_writetime);
                intent.putExtra("arraylist_position", arraylist_position);

                startActivityForResult(intent, REQUEST_CODE_TO_BOARD_VIEW);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(requestCode == REQUEST_CODE_TO_BOARD_INSERT) {
            if(resultCode == RESULT_OK) {
                //게시판 쓰기 화면에서 돌아오기
            }
        } else if(requestCode == REQUEST_CODE_TO_BOARD_VIEW) {
            if(resultCode == RESULT_OK) {
                //게시글 보기 화면에서 돌아오기
            }
        } else if(requestCode == REQUEST_CODE_TO_BOARD_MODIFY) {
            if(resultCode == RESULT_OK) {
                String is_delete = intent.getExtras().getString("is_deleted");
                if(is_delete.equals("Y")) {
                    //(Frag_Board_Main) getSupportFragmentManager().findFragmentById(R.id.fragment_board);
                    //tf.testFunction();

                }

            }
        }
    }
}
