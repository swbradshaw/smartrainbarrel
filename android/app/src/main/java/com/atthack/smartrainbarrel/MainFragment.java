package com.atthack.smartrainbarrel;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by scott on 4/16/2016.
 */
public class MainFragment extends Fragment {


    private TextView weightTextView;

    SharedPreferences sharedPreferences;

    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {

    }


    private void setupInitialWeight() {

        int weight = sharedPreferences.getInt("weight", 0);
        setWeight(weight);
    }


    public void setWeight(int weight) {
        weightTextView.setText(new Integer(weight).toString() + "%");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        weightTextView = (TextView) rootView.findViewById(R.id.weight);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        setupInitialWeight();


        ImageView img=(ImageView)rootView.findViewById(R.id.imageView);
        Drawable myDrawable = getResources().getDrawable(R.drawable.rainbarrel);
        img.setImageDrawable(myDrawable);

        return rootView;
    }
}