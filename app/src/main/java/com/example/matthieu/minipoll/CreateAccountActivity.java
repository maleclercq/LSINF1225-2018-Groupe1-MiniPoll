package com.example.matthieu.minipoll;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matthieu.minipoll.Profile.CreateProfilActivity;

import java.io.IOException;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void validation(View v){
        String id=((EditText) findViewById(R.id.ID)).getText().toString();
        String password=((EditText) findViewById(R.id.Password)).getText().toString();
        String confirmPassword=((EditText) findViewById(R.id.ConfirmPassword)).getText().toString();

        if(        id.compareTo("")==0
                || confirmPassword.compareTo("")==0
                || password.compareTo("")==0
                ) {

            Toast.makeText(CreateAccountActivity.this, "Please, fill all the field", Toast.LENGTH_LONG).show(); //traduction
            return;
        }

        if(password.compareTo(confirmPassword)!=0){
            Toast.makeText(CreateAccountActivity.this,"password don't correspond",Toast.LENGTH_LONG).show(); //traduction
            return;
        }

        if(password.matches("^.*[^a-zA-Z0-9 ].*$") || password.length()<6){
            Toast.makeText(CreateAccountActivity.this,"Please, enter a valid password",Toast.LENGTH_LONG).show(); //traduction
            return;
        }

        final DataBaseHelper myDbHelper = new DataBaseHelper(CreateAccountActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(CreateAccountActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(CreateAccountActivity.this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        String [][] rep;
        String [] whereArgs={id};//les conditions de la requete sql
        Cursor c=myDbHelper.rawQuery("select ID from UTILISATEUR where ID=?",whereArgs);//on fait la requete

        rep=myDbHelper.createTabFromCursor(c,1);
        if(rep.length!=0){
            Toast.makeText(CreateAccountActivity.this,"Pseudo Already use",Toast.LENGTH_LONG).show(); //traduction
            return;
        }

        Intent i=new Intent(CreateAccountActivity.this, CreateProfilActivity.class);
        i.putExtra("id",id);
        i.putExtra("password",password);
        startActivity(i);
        finish();
    }
    public void retour(View v)
    {
        finish();
    }
}
