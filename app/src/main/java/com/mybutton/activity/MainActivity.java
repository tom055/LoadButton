package com.mybutton.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mybutton.R;
import com.mybutton.view.LoadView;

public class MainActivity extends AppCompatActivity {

    private LoadView loadview;
    private Button play;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play=(Button)findViewById(R.id.play);
        loadview=(LoadView)findViewById(R.id.loadview);
        loadview.setOnClickListener(new BtnClickListener());
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadview.backAnimator();
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
