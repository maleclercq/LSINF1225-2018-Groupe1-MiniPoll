package com.example.matthieu.minipoll;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class ConnectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
    }

    public void login(View v){
        final DataBaseHelper myDbHelper = new DataBaseHelper(ConnectionActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(ConnectionActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(ConnectionActivity.this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }
        //A ouvert la dataBase et la stocke dans myDbHelper

        EditText ID=findViewById(R.id.ID);//Recupere ID
        EditText MDP=findViewById(R.id.Password);//Recuper MDP

        String [] whereArgs={ID.getText().toString(),MDP.getText().toString()};//les conditions de la requete sql
        Cursor c=myDbHelper.rawQuery("select ID,NOM,PRENOM,MDP,EMAIL,PHOTO from UTILISATEUR where ID=? AND MDP=?",whereArgs);//on fait la requete

        String [][] tab=myDbHelper.createTabFromCursor(c,6);

        if(tab.length==0){  //cela veut dire qu'on a pas trouvé de couple ID/MDP dans la requete
            Toast.makeText(ConnectionActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
        } else {
            Utilisateur u=new Utilisateur(tab[0][0],tab[0][1],tab[0][2],tab[0][3],tab[0][4],tab[0][5]);
            Intent i = new Intent(ConnectionActivity.this, MainActivity.class);

            //permet de changer de fenetre
            i.putExtra("utilisateur",u);
            ConnectionActivity.this.finish();
            startActivity(i);
        }
    }

    public void createAccount(View v){
        Intent i=new Intent(ConnectionActivity.this, CreateAccountActivity.class);
        startActivity(i);
    }
}
