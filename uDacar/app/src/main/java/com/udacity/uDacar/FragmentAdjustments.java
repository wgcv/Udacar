package com.udacity.uDacar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Saul on 11/20/2016.
 */

public class FragmentAdjustments extends Fragment {

    public FragmentAdjustments() {
    }


    public static FragmentAdjustments newInstance() {
        FragmentAdjustments fragment = new FragmentAdjustments();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(com.udacity.uDacar.R.layout.fragment_adjustments, container, false);
        return view;
    }

}
