package com.example.harbon.test;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FrameLayout relativeLayout = (FrameLayout) findViewById(R.id.relative);
        final WindViewTwo windViewTwo = (WindViewTwo) findViewById(R.id.surfaceView);
        windViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("aaaaaa", "startWind");
                windViewTwo.startWind();
            }
        });
    }

}
