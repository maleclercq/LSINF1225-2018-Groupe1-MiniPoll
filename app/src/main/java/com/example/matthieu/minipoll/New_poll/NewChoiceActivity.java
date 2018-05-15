package com.example.matthieu.minipoll.New_poll;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.matthieu.minipoll.Choice;
import com.example.matthieu.minipoll.ChoiceOfPictureActivity;
import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewChoiceActivity extends Activity {
    Utilisateur u;
    ArrayList<String> tabSTM = new ArrayList<String>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_choice);

        Intent i = getIntent();
        this.u= (Utilisateur)i.getSerializableExtra("utilisateur");

    }

    public String getDate(){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        return ( dateFormat.format( cal.getTime() ) );
    }

    public void validation(View v){
        String titre = ((EditText) findViewById(R.id.TitleChoice)).getText().toString();
        String question = ((EditText) findViewById(R.id.QuestionChoice)).getText().toString();
        String first = ((EditText) findViewById(R.id.TitleFirstChoice)).getText().toString();
        String second = ((EditText) findViewById(R.id.TitleSecondChoice)).getText().toString();
        //ImageView firstIg;
        //ImageView secondIg;

        //Enleve les caracteres indesirables
        titre=titre.replaceAll("[']+", " ");
        question=question.replaceAll("[']+", " ");
        first=first.replaceAll("[']+", " ");
        second=second.replaceAll("[']+", " ");


        if(titre.compareTo("")==0 || question.compareTo("")==0 || first.compareTo("")==0 || second.compareTo("")==0) { //si toute les cases n'ont pas ete remplies
            Toast.makeText(this,"Fill all the blanks please",Toast.LENGTH_LONG).show();
            return;
        }

        DataBaseHelper myDbHelper = new DataBaseHelper(this);

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        String date = getDate();

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        String stmt1 = ("insert into CHOIX values('"
                +titre     +"','"
                +date      + "','"
                +this.u.getPseudo() + "','"
                +first  + "','"
                +question  + "')");
        tabSTM.add(stmt1);

        String stmt2 = ("insert into CHOIX values('"
                +titre     +"','"
                +date      + "','"
                +this.u.getPseudo() + "','"
                +second +"','"
                +question  + "')");
        tabSTM.add(stmt2);

        Intent i=new Intent(this,ListeAmisPourPollActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("sql", tabSTM);
        i.putExtra("typePoll","Choice");
        i.putExtra("titre",titre);
        i.putExtra("question", question);
        i.putExtra("date",date);
        i.putExtra("first", first);
        i.putExtra("second", second);
        startActivity(i);
        finish();
    }

    public void retour(View v){
        finish();
    }

    public void chooseImage(View view) {
        Intent i = new Intent(this, ChoiceOfPictureActivity.class);
        i.putExtra("utilisateur",u);
        startActivity(i);
    }
}
