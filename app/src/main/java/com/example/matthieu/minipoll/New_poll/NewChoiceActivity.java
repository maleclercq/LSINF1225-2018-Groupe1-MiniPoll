package com.example.matthieu.minipoll.New_poll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.matthieu.minipoll.R;

public class NewChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_choice);
    }

    public void retour(View v){
        finish();
    }
}
