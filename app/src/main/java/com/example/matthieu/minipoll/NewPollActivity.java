package com.example.matthieu.minipoll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NewPollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll);
    }

    public void newChoice(View v){
        Intent i=new Intent(this, NewChoiceActivity.class);
        startActivity(i);
    }

}
