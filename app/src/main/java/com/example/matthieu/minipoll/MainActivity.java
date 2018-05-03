package com.example.matthieu.minipoll;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


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
        Intent i=new Intent(MainActivity.this,PollListeActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("type","CHOIX");
        startActivity(i);
    }

    public void questionnaire(View v){
        Intent i=new Intent(MainActivity.this,PollListeActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("type","QUESTIONNAIRE");
        startActivity(i);
    }
    public void sondage(View v){
        Intent i=new Intent(this,PollListeActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("type","SONDAGE");
        startActivity(i);
    }

    public void profile(View v){
        Intent i=new Intent(this,ProfileActivity.class);
        i.putExtra("utilisateur",u);
        startActivity(i);
    }

    public void newPoll(View v){
        //a completer
        Intent i=new Intent(this, NewPollActivity.class); // NewPollActivity à creer
        i.putExtra("utilisateur",u);
        startActivity(i);
    }

}
