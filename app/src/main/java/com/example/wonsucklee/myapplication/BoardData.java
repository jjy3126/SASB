package com.example.wonsucklee.myapplication;

/**
 * Created by crown on 2018-02-08.
 */

public class BoardData {
    int b_number;
    String user_id;
    String user_name;
    String user_profile;
    //올리 사람 정보

    String title;
    String image_content;
    String text_content;
    String writetime;

    public int get_B_Number() {return b_number;}
    public String get_User_Id() {return user_id;}
    public String get_User_Name() {return user_name;}
    public String get_User_Profile() {return user_profile;}
    public String get_Title() {return title;}
    public String get_Image_Content() {return image_content;}
    public String get_Text_Content() {return text_content;}
    public String get_Writetime() {return writetime;}

    public void set_B_Number(int b_number) {this.b_number = b_number;}
    public void set_User_Id(String user_id) {this.user_id = user_id;}
    public void set_User_Name(String user_name) {this.user_name = user_name;}
    public void set_User_Profile(String user_profile) {this.user_profile = user_profile;}
    public void set_Title(String title) {this.title = title;}
    public void set_Image_Content(String image_content) {this.image_content = image_content;}
    public void set_Text_Content(String text_content) {this.text_content = text_content;}
    public void set_Writetime(String writetime) {this.writetime = writetime;}

}

