package com.example.matthieu.minipoll.Amis;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

import java.io.IOException;

public class ViewProfileFriendActivity extends AppCompatActivity {

    String amiPseudo;
    DataBaseHelper myDbHelper;
    Utilisateur u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_friend);

        Intent i = getIntent();
        amiPseudo = (String) i.getSerializableExtra("amis");
        u = (Utilisateur) i.getSerializableExtra("utilisateur");

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

        String [] whereArgs={this.amiPseudo};
        Cursor c=myDbHelper.rawQuery("select nom,prenom,email from utilisateur where id=?",whereArgs);

        String [][] value=myDbHelper.createTabFromCursor(c,3);

        TextView nom=findViewById(R.id.Name);
        nom.setText(value[0][0]);

        TextView prenom=findViewById(R.id.FirstName);
        prenom.setText(value[0][1]);

        TextView email=findViewById(R.id.Email);
        email.setText(value[0][2]);

        TextView pseudo=findViewById(R.id.Pseudo);
        pseudo.setText(this.amiPseudo);
    }

    public void delete(View v){
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("delete from AMI where utilisateur='"+this.u.getPseudo()+"' and ami='"+this.amiPseudo+"'");
        stmt.execute();
        Intent i = new Intent(this, ListeAmisActivity.class);
        i.putExtra("utilisateur", u);
        startActivity(i);
        finish();
    }

    public void retour(View v) {
        Intent i = new Intent(this, ListeAmisActivity.class);
        i.putExtra("utilisateur", u);
        startActivity(i);
        finish();
    }
}