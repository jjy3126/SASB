package com.example.wonsucklee.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by crown on 2018-02-08.
 */

public class Board_List_Adapter extends BaseAdapter {

    ArrayList<BoardData> arrayList;
    Context context;

    public Board_List_Adapter(Context context, ArrayList<BoardData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        BoardData data = arrayList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board_list_item, parent, false);
            holder = new ViewHolder();

            holder.textView_title = (TextView) convertView.findViewById(R.id.textView_title);
            holder.textView_name = (TextView) convertView.findViewById(R.id.textView_name);
            holder.textView_writetime = (TextView) convertView.findViewById(R.id.textView_writetime);
            holder.imageView_content = (ImageView) convertView.findViewById(R.id.imageView_content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView_title.setText(data.get_Title());
        holder.textView_name.setText(data.get_User_Name());
        holder.textView_writetime.setText(data.get_Writetime());

        String url = "http://121.187.77.1:25000/uploads/" + data.get_Image_Content();

        Glide.with(context)
                .load(url)
                .into(holder.imageView_content);

        return convertView;
    }

    class ViewHolder {
        TextView textView_title;
        TextView textView_name;
        TextView textView_writetime;
        ImageView imageView_content;
    }
}
