package com.example.wonsucklee.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class Frag_Attendace_Main extends Fragment {



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attendace_main, container, false);
        return view;
    }


}
