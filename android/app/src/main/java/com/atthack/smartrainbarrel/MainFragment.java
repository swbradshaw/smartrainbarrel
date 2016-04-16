package com.atthack.smartrainbarrel;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by scott on 4/16/2016.
 */
public class MainFragment extends Fragment {


    private TextView weightTextView;
    private ImageView rainBarrelImage;
    SharedPreferences sharedPreferences;
    private Button btnOpen;

    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {

    }


    public void setupInitialWeight() {

        int weight = sharedPreferences.getInt("weight", 0);
        setWeight(weight);
    }


    public void setWeight(int weight) {
        weightTextView.setText(new Integer(weight).toString() + "%");
        int yend = 1780;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#529AD3"));


        Bitmap myBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.rainbarrel2);

        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas tempCanvas = new Canvas(tempBitmap);

//Draw the image bitmap into the cavas
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        int xstart = 320;
        int xend = 1400;

        //int y1 = yend - (weight * 1500;
        int y1 = (yend - (weight * 14));
//Draw everything else you want into the canvas, in this example a rectangle with rounded edges
        tempCanvas.drawRoundRect(new RectF(xstart,y1,xend,yend), 2, 2, paint);

//Attach the canvas to the ImageView
        rainBarrelImage.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        weightTextView = (TextView) rootView.findViewById(R.id.weight);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());


        btnOpen=(Button)rootView.findViewById(R.id.openbutton);
        if (btnOpen != null) {
            btnOpen.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Helper.sendM2Xrequest(MainFragment.this.getContext());
                }
            });
        }
        rainBarrelImage=(ImageView)rootView.findViewById(R.id.imageView);
        //Drawable myDrawable = getResources().getDrawable(R.drawable.rainbarrel);
        //rainBarrelImage.setImageDrawable(myDrawable);

        setupInitialWeight();
        return rootView;
    }
}