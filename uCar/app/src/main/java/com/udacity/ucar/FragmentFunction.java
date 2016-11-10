package com.udacity.ucar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentFunction extends Fragment {

    public FragmentFunction() {
        // Required empty public constructor
    }


    public static FragmentFunction newInstance() {
        FragmentFunction fragment = new FragmentFunction();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_function, container, false);

        return view;
    }

}
