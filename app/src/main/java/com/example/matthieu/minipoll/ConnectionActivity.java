package com.example.matthieu.minipoll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        Toast.makeText(ConnectionActivity.this,"Hello Matthieu",Toast.LENGTH_SHORT);
    }
}
