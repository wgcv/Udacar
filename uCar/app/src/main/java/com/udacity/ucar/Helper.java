package com.udacity.ucar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

/**
 * Created by ik on 09-11-2016.
 */

public class Helper {

    public static void setImage(Context context, ImageView iv, int position, boolean selectedImg){
        if(selectedImg){ //change selected images
            if(position ==0){//map
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.route_icon_selected));
            }else if(position ==1){//settings
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.adjustment_icon_selected));
            }else if(position == 2){//music
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.music_icon_selected));
            }else{//comfort
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.luxury_icon_selected));
            }
        }else{//change un-selected images
            if(position ==0){//map
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.route_icon));
            }else if(position ==1){//settings
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.adjustment_icon));
            }else if(position == 2){//music
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.music_icon));
            }else{//comfort
                iv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.luxury_icon));
            }
        }
    }

}
