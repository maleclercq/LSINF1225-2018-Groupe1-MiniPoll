package com.example.matthieu.minipoll.New_poll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

public class NewPollActivity extends AppCompatActivity {

    Utilisateur u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll);
        Intent i = getIntent();
        this.u= (Utilisateur)i.getSerializableExtra("utilisateur");
    }

    public void questionnaire(View v){
        Intent i=new Intent(this,NewQuizzActivity.class);
        i.putExtra("utilisateur",this.u);
        startActivity(i);
    }

    public void newSondage(View v){
        Intent i=new Intent(this,NewSondageTempoActivity.class);
        i.putExtra("utilisateur",this.u);
        startActivity(i);
    }

    public void newChoice(View v){
        Intent i=new Intent(this, NewChoiceActivity.class);
        i.putExtra("utilisateur",this.u);
        startActivity(i);
    }

}
