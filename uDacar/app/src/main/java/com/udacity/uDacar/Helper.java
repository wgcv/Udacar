package com.udacity.uDacar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

/**
 * Created by ik on 09-11-2016.
 */

public class Helper {

    public static void setImage(Context context, ImageView iv, int position, boolean isSelectedImg){
        if(isSelectedImg){
            if(position ==0){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.u_wordmark_slate));
            }else if(position ==2){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.functions_icon_selected));
            }else if(position == 3){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.locks_icon_selected));
            }else if(position == 1){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.route_icon_selected));
            }else if(position == 4){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.luxury_icon_selected));
            }
        }else{
            if(position ==0){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.u_wordmark_slate));
            }else if(position ==2){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.functions_icon));
            }else if(position == 3){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.locks_icon));
            }else if(position == 1){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.route_icon));
            }else if(position == 4){
                iv.setImageDrawable(ContextCompat.getDrawable(context, com.udacity.uDacar.R.drawable.luxury_icon));
            }
        }
    }

}
