package com.example.matthieu.minipoll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    Utilisateur u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        u= (Utilisateur)i.getSerializableExtra("utilisateur");
    }

    public void choix(View v){
        Toast.makeText(MainActivity.this,"Not Yet Implemented",Toast.LENGTH_LONG).show();
//        Intent i=new Intent(MainActivity.this,ChoixListeActivity.class);
//        startActivity(i);
    }

    public void questionnaire(View v){
        Toast.makeText(MainActivity.this,"Not Yet Implemented",Toast.LENGTH_LONG).show();
//        Intent i=new Intent(MainActivity.this,QuestionnaireListeActivity.class);
//        startActivity(i);

    }
    public void sondage(View v){
        Intent i=new Intent(MainActivity.this,SondageListeActivity.class);
        i.putExtra("utilisateur",u);
        startActivity(i);
    }

}
