package com.udacity.ucar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class FragmentComfort extends Fragment implements View.OnClickListener{

    ImageView mIvAcCold, mIvAcWarm,
            mIvLight1, mIvLight2, mIvLight3,
            mIvPilotSeatL, mIvPilotSeatR, mIvCopilotSeatL, mIvCopilotSeatR;

    TextView mTvACValue;

    int mACTempValue;

    public FragmentComfort() {
    }

    public static FragmentComfort newInstance() {
        FragmentComfort fragment = new FragmentComfort();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_comfort, container, false);
        mACTempValue = 23;
        initializeViews(view);
        return view;
    }

    public void setAcTempValue(int acTempValue){
        mACTempValue = acTempValue;
    }

    private void initializeViews(View view){
        mTvACValue = (TextView) view.findViewById(R.id.tv_temp);

        mIvAcCold = (ImageView) view.findViewById(R.id.iv_btn_down);
        mIvAcWarm = (ImageView) view.findViewById(R.id.iv_btn_up);

        mIvLight1 = (ImageView) view.findViewById(R.id.iv_light_1);
        mIvLight2 = (ImageView) view.findViewById(R.id.iv_light_2);
        mIvLight3 = (ImageView) view.findViewById(R.id.iv_light_3);

        mIvPilotSeatL = (ImageView) view.findViewById(R.id.iv_btn_left_pilot);
        mIvPilotSeatR = (ImageView) view.findViewById(R.id.iv_btn_right_pilot);
        mIvCopilotSeatL = (ImageView) view.findViewById(R.id.iv_btn_left_copilot);
        mIvCopilotSeatR = (ImageView) view.findViewById(R.id.iv_btn_right_copilot);

        mIvAcCold.setOnClickListener(this);
        mIvAcWarm.setOnClickListener(this);

        mIvLight1.setOnClickListener(this);
        mIvLight2.setOnClickListener(this);
        mIvLight3.setOnClickListener(this);

        mIvPilotSeatL.setOnClickListener(this);
        mIvPilotSeatR.setOnClickListener(this);
        mIvCopilotSeatL.setOnClickListener(this);
        mIvCopilotSeatR.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_btn_down:
                decreaseAcTemp();
                break;
            case R.id.iv_btn_up:
                increaseAcTemp();
                break;

            case R.id.iv_light_1:
                changeLightToPos1();
                break;
            case R.id.iv_light_2:
                changeLightToPos2();
                break;
            case R.id.iv_light_3:
                changeLightToPos3();
                break;

            case R.id.iv_btn_left_pilot:
                adjustPilotSeatToLeft();
                break;
            case R.id.iv_btn_right_pilot:
                adjustPilotSeatToRight();
                break;
            case R.id.iv_btn_left_copilot:
                adjustCopilotSeatToLeft();
                break;
            case R.id.iv_btn_right_copilot:
                adjustCopilotSeatToRight();
                break;
        }
    }

    /*
        AC Temperature
     */
    private void decreaseAcTemp(){
        mACTempValue--;
        mTvACValue.setText(mACTempValue + " C");
    }

    private void increaseAcTemp(){
        mACTempValue++;
        mTvACValue.setText(mACTempValue + " C");
    }

    /*
        Light Direction
     */
    private void changeLightToPos1(){ //forward

    }

    private void changeLightToPos2(){ //center

    }

    private void changeLightToPos3(){ //backward

    }

    /*
        Seat Adjustment
     */
    private void adjustPilotSeatToLeft(){

    }

    private void adjustPilotSeatToRight(){

    }

    private void adjustCopilotSeatToLeft(){

    }

    private void adjustCopilotSeatToRight(){

    }

}
