package com.mybutton.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lc.loadbutton.LoadButton;
import com.lc.loadbutton.LoadRectView;
import com.lc.loadbutton.LoadView;
import com.mybutton.R;

public class MainActivity extends AppCompatActivity {

    private LoadView loadview;
    private LoadButton loadbtn;
    private LoadRectView loadRectView;
    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play=(Button)findViewById(R.id.play);
        loadbtn=(LoadButton)findViewById(R.id.loadbtn);
        loadview=(LoadView)findViewById(R.id.loadview);
        loadRectView=(LoadRectView)findViewById(R.id.loadrectview);
        loadview.setOnClickListener(new BtnClickListener());
        ((Button)findViewById(R.id.end)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRectView.showPaintTxtAnima();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadbtn.stopAnimator();
                loadRectView.startLoading();
                //loadRectView.showPaintTxtAnima();
                //loadRectView.showStartLoadAnimation();
            }
        });
        loadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadbtn.startAnimator();
            }
        });
    }
    class BtnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Log.e("Mytext","BtnClickListener-->"+view.getClass().getName());
           if(view instanceof LoadView){
               Log.e("Mytext","LoadView");
               loadview.startAnimator();
               //loadview.start();
           }
        }
    }
}
