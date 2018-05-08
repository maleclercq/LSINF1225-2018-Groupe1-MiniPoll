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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewQuizzActivity extends AppCompatActivity {

    Utilisateur u;
    ArrayList<String> tabSTM= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_quizz);


        Intent i = getIntent();
        this.u= (Utilisateur)i.getSerializableExtra("utilisateur");
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        return ( dateFormat.format( cal.getTime() ) );
    }

    public void OKQuizz(View v){

        String titre=" ";
        int nbrQuestion=-1;

        try {
            titre = ((EditText) findViewById(R.id.Titre)).getText().toString();
            nbrQuestion = Integer.parseInt(((EditText) findViewById(R.id.NbrQuestion)).getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(this,"Please, field correctly the field",Toast.LENGTH_LONG).show();
            return;
        }

        if(nbrQuestion<1 || nbrQuestion>5){
            Toast.makeText(this,"Please, enter a correct value for the number of questions (between 1 and 5)",Toast.LENGTH_LONG).show();
            return;
        }


        Intent i=new Intent(this,NewQuestionActivity.class);
        i.putExtra("titre",titre);
        i.putExtra("utilisateur",u);
        i.putExtra("count", nbrQuestion);
        i.putExtra("tabSTM", tabSTM);
        i.putExtra("date", getDate());
        startActivity(i);
        finish();
    }
}
