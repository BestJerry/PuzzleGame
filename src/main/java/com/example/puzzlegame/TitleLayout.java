package com.example.puzzlegame;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by jerry on 16-8-7.
 */
public class TitleLayout extends LinearLayout {

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
        Button titleback = (Button) findViewById(R.id.title_back);
        Button titleedit = (Button) findViewById(R.id.title_finish);
        titleback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });

        titleedit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "You clicked Edit button", Toast.LENGTH_SHORT).show();
                ActivityCollector.finishAll();
            }
        });


    }
}
