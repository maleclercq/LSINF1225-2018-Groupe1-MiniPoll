


//
//
//Encore brouillon car c'est moi (francois) qui ait fait
//
//




package com.example.matthieu.minipoll;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.matthieu.minipoll.ConnectionActivity;
import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

import java.io.IOException;

public class FriendActivity extends AppCompatActivity {

    Utilisateur u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Intent i = getIntent();
        u= (Utilisateur)i.getSerializableExtra("utilisateur");

        final DataBaseHelper myDbHelper = new DataBaseHelper(FriendActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(FriendActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(FriendActivity.this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }
        //A ouvert la dataBase et la stocke dans myDbHelper


        String [] whereArgs={u.pseudo};//les conditions de la requete sql
        Cursor c=myDbHelper.rawQuery("select AMIS from AMIS where u=? ",whereArgs);//on fait la requete

        String [][] tab=myDbHelper.createTabFromCursor(c,6);

    }

}
