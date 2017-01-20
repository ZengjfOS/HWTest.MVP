package com.aplex.aplextest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.aplex.aplextest.R;
import com.aplex.aplextest.flatui.FlatUI;
import com.aplex.aplextest.flatui.view.FlatToggleButton;
import com.aplex.aplextest.jninative.AplexBuzz;


/**
 * Created by APLEX on 2016/6/21.
 */
public class BuzzFragment extends Fragment implements View.OnClickListener {
    private FlatToggleButton mFlatToggleButton;
    private RadioGroup mRadioGroup;
    private LinearLayout RootView;
    private RadioButton mBuzzBtn1;
    private RadioButton mBuzzBtn2;
    private RadioButton mBuzzBtn3;
    private RadioButton mBuzzBtn4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //FlatUI.setDefaultTheme(R.array.my_custom_theme);
        FlatUI.initDefaultValues(getActivity());
        initView(inflater);
        initListener();

        // Setting default theme to avoid to add the attribute "theme" to XML
        // and to be able to change the whole theme at once
        FlatUI.setDefaultTheme(FlatUI.SKY);
//        FlatUI.setDefaultTheme(R.array.custom_theme);
        RootView.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));

        return RootView;
    }

    private void initListener() {
        mBuzzBtn1.setOnClickListener(this);
        mBuzzBtn2.setOnClickListener(this);
        mBuzzBtn3.setOnClickListener(this);
        mBuzzBtn4.setOnClickListener(this);
        mFlatToggleButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String text = "";
        switch (mRadioGroup.getCheckedRadioButtonId()) {
            case R.id.buzz_hz_500:
                text = "500";
                break;
            case R.id.buzz_hz_1000:
                text = "1000";
                break;
            case R.id.buzz_hz_1500:
                text = "1500";
                break;
            case R.id.buzz_hz_2000:
                text = "2000";
                break;

            default:
                text = "500";
                break;
        }
        if (mFlatToggleButton.isChecked()) {
            AplexBuzz.setFrequency(Integer.valueOf(text));    // set buzzer frequency
            AplexBuzz.enable();
        } else {
            AplexBuzz.disable();
        }
    }
    private void initView(LayoutInflater inflater) {
        RootView = (LinearLayout) inflater.inflate(R.layout.ui_buzz, null);
        mFlatToggleButton = (FlatToggleButton) RootView.findViewById(R.id.buzz_toggle);
        mRadioGroup = (RadioGroup) RootView.findViewById(R.id.buzz_hz);
        mBuzzBtn1 = (RadioButton) RootView.findViewById(R.id.buzz_hz_500);
        mBuzzBtn2 = (RadioButton) RootView.findViewById(R.id.buzz_hz_1000);
        mBuzzBtn3 = (RadioButton) RootView.findViewById(R.id.buzz_hz_1500);
        mBuzzBtn4 = (RadioButton) RootView.findViewById(R.id.buzz_hz_2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        AplexBuzz.disable();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }


}
