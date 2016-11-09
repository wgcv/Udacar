package com.udacity.ucar;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu, parent, false);
            mHolder = new MyViewHolder(convertView);
            convertView.setTag(mHolder);
        }else{
            mHolder = (MyViewHolder) convertView.getTag();
        }
        mHolder.mTvMenuName.setText(mItems.get(position));
        Helper.setImage(parent.getContext(), mHolder.mIvMenu, position, true);

        return convertView;
    }

    class MyViewHolder {
        TextView mTvMenuName;
        ImageView mIvMenu;

        public MyViewHolder(View view){
            mTvMenuName = (TextView) view.findViewById(R.id.tv_menu_name);
            mIvMenu = (ImageView) view.findViewById(R.id.iv_menu);
        }
    }
}
