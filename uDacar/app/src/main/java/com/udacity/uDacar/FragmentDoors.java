package com.udacity.uDacar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentDoors extends Fragment {

    public FragmentDoors() {
    }


    public static FragmentDoors newInstance() {
        FragmentDoors fragment = new FragmentDoors();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(com.udacity.uDacar.R.layout.fragment_doors, container, false);

        return view;
    }

}
