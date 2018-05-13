package com.example.matthieu.minipoll.Amis;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

import java.io.IOException;
import java.util.ArrayList;

public class SuggestionActivity extends AppCompatActivity {

    ArrayList<String> suggestion;
    int position;
    Utilisateur u;
    DataBaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        Intent i=getIntent();
        this.suggestion=(ArrayList<String>) i.getSerializableExtra("suggestion");
        this.u=(Utilisateur) i.getSerializableExtra("utilisateur");
        this.position=(int) i.getSerializableExtra("position");

        this.myDbHelper = new DataBaseHelper(this);
        try {
            this.myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            this.myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        String [] whereArgs={suggestion.get(position)};
        Cursor c=myDbHelper.rawQuery("select nom,prenom,email from utilisateur where id=?",whereArgs);

        String [][] value=myDbHelper.createTabFromCursor(c,3);

        TextView nom=findViewById(R.id.Name);
        nom.setText(value[0][0]);

        TextView prenom=findViewById(R.id.FirstName);
        prenom.setText(value[0][1]);

        TextView email=findViewById(R.id.Email);
        email.setText(value[0][2]);

        TextView pseudo=findViewById(R.id.Pseudo);
        pseudo.setText(suggestion.get(position));
    }

    public void left(View v){
        int p=position-1;
        if(p<0){
            p=suggestion.size()-1;
        }
        position=p;

        String [] whereArgs={suggestion.get(position)};
        Cursor c=myDbHelper.rawQuery("select nom,prenom,email from utilisateur where id=?",whereArgs);

        String [][] value=myDbHelper.createTabFromCursor(c,3);

        TextView nom=findViewById(R.id.Name);
        nom.setText(value[0][0]);

        TextView prenom=findViewById(R.id.FirstName);
        prenom.setText(value[0][1]);

        TextView email=findViewById(R.id.Email);
        email.setText(value[0][2]);

        TextView pseudo=findViewById(R.id.Pseudo);
        pseudo.setText(suggestion.get(position));
    }

    public void right(View v){
        int p=position+1;
        if(p>=suggestion.size()){
            p=0;
        }
        position=p;
        Intent i=new Intent(this,SuggestionActivity.class);

        String [] whereArgs={suggestion.get(position)};
        Cursor c=myDbHelper.rawQuery("select nom,prenom,email from utilisateur where id=?",whereArgs);

        String [][] value=myDbHelper.createTabFromCursor(c,3);

        TextView nom=findViewById(R.id.Name);
        nom.setText(value[0][0]);

        TextView prenom=findViewById(R.id.FirstName);
        prenom.setText(value[0][1]);

        TextView email=findViewById(R.id.Email);
        email.setText(value[0][2]);

        TextView pseudo=findViewById(R.id.Pseudo);
        pseudo.setText(suggestion.get(position));
    }

    public void addFriend(View v){
        u.addFriend(myDbHelper,this.suggestion.get(position));

        ArrayList<String> newSuggestion=u.getSuggestion(this.myDbHelper);
        if(newSuggestion.size()==0){
            Intent i = new Intent(this, ListeAmisActivity.class);
            i.putExtra("utilisateur", u);
            startActivity(i);
            finish();
            return;
        }

        Intent i=new Intent(this,SuggestionActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("suggestion",newSuggestion);
        i.putExtra("position",0);
        startActivity(i);
        finish();
    }

    public void retour(View v){
        Intent i = new Intent(this, ListeAmisActivity.class);
        i.putExtra("utilisateur", u);
        startActivity(i);
        finish();
    }
}
