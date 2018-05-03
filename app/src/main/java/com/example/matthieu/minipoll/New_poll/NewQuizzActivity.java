package com.example.matthieu.minipoll.New_poll;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

public class NewQuizzActivity extends AppCompatActivity {

    Utilisateur u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quizz2);


        Intent i = getIntent();
        this.u= (Utilisateur)i.getSerializableExtra("utilisateur");
    }

    public void OK(View v){

        String titre="";
        int nbrQuestion=0;

        try {
            titre = ((EditText) findViewById(R.id.NbrChoix)).getText().toString();
            nbrQuestion = Integer.parseInt(((EditText) findViewById(R.id.NbrProposition)).getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(this,"Please, field correctly the field",Toast.LENGTH_LONG).show();
            return;
        }

        if(nbrQuestion<1 || nbrQuestion>5){
            Toast.makeText(this,"Please, enter a correct value for the number of questions (between 2 and 5)",Toast.LENGTH_LONG).show();
            return;
        }

        Intent i=new Intent(this,NewSondageActivity.class);
        i.putExtra("titre",titre);
        i.putExtra("nbrQuestion",nbrQuestion);
        i.putExtra("utilisateur",u);
        startActivity(i);
        finish();
    }
}