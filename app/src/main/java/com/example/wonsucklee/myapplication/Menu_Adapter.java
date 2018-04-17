package com.example.wonsucklee.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by crown on 2018-02-08.
 */

public class Menu_Adapter extends BaseAdapter {

    ArrayList<MenuData> arrayList;
    Context context;

    public Menu_Adapter(Context context, ArrayList<MenuData> arrayList) {
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

       MenuData data = arrayList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_item, parent, false);
            holder = new ViewHolder();

            holder.textView_title = (TextView) convertView.findViewById(R.id.textView_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(data.get_Type() == 1) {
            holder.textView_title.setText(data.get_Title());
        } else {
            holder.textView_title.setText("   - " + data.get_Title());
        }
        //imageView는 glide 이용해서 하는 코드 필요

        return convertView;
    }

    class ViewHolder {
        TextView textView_title;
    }
}
