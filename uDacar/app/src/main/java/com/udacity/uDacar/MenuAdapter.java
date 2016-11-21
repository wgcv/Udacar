package com.udacity.uDacar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Ismail.Khan2 on 11/8/2016.
 */

public class MenuAdapter extends BaseAdapter {

    List<String> mItems;
    MyViewHolder mHolder;

    public MenuAdapter(List<String> items){
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(com.udacity.uDacar.R.layout.row_menu, parent, false);
            mHolder = new MyViewHolder(convertView);
            convertView.setTag(mHolder);
        }else{
            mHolder = (MyViewHolder) convertView.getTag();
        }
        Helper.setImage(parent.getContext(), mHolder.mIvMenu, position, false);

        return convertView;
    }

    class MyViewHolder {
        ImageView mIvMenu;

        public MyViewHolder(View view){
            mIvMenu = (ImageView) view.findViewById(com.udacity.uDacar.R.id.iv_menu);
        }
    }
}
