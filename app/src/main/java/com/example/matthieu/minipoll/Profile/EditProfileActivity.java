package com.example.matthieu.minipoll.Profile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matthieu.minipoll.ChoiceOfPictureActivity;
import com.example.matthieu.minipoll.ChoiceViewActivity;
import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class EditProfileActivity extends AppCompatActivity {
    Utilisateur u;
    SQLiteDatabase sql;
    private DataBaseHelper myDbHelper;

    EditText editName, editFirstName, editPseudo, editMail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent i = getIntent();
        u = (Utilisateur) i.getSerializableExtra("utilisateur");

        editName = (EditText) findViewById(R.id.Name);
        editName.setText(u.nom);

        editFirstName = (EditText) findViewById(R.id.FirstName);
        editFirstName.setText(u.prenom);

        editPseudo = (EditText) findViewById(R.id.Pseudo);
        editPseudo.setText(u.getPseudo());

        editMail = (EditText) findViewById(R.id.Email);
        editMail.setText(u.email);

        editPassword = (EditText) findViewById(R.id.Password);
        editPassword.setText(u.mdp);
    }

    public void validationProfile(View v) throws SQLException {
        this.myDbHelper = new DataBaseHelper(EditProfileActivity.this);
        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        String nom = ((EditText) findViewById(R.id.Name)).getText().toString();
        String prenom = ((EditText) findViewById(R.id.Prenom)).getText().toString();
        String pseudo = ((EditText) findViewById(R.id.Pseudo)).getText().toString();
        String email = ((EditText) findViewById(R.id.AdresseEMail)).getText().toString();
        String mdp = ((EditText) findViewById(R.id.Password)).getText().toString();
        String photo = "basicimage.png";

        try {
            myDbHelper.createDataBase();
        }
        catch (IOException ioe) {
            Toast.makeText(EditProfileActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();

        SQLiteStatement stmt1=db.compileStatement("delete from UTILISATEUR where " +
                "ID='"+ this.u.getPseudo() +"'");
        stmt1.execute();

        SQLiteStatement stmt2=db.compileStatement("insert into UTILISATEUR values('"
                + pseudo + "','"
                + nom + "','"
                + prenom + "','"
                + mdp + "','"
                + email + "','"
                + photo + ")");
        stmt2.execute();

        Intent i=new Intent(this, ProfileActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("Nom", nom);
        i.putExtra("Prenom", prenom);
        i.putExtra("Pseudo", pseudo);
        i.putExtra("Email", email);
        i.putExtra("Mot de passe", mdp);
        //i.putExtra("Photo", img);
        startActivity(i);
    }

    public void retour(View v){
        finish();
    }

    public void chooseImage(View v){
        Intent i = new Intent(this, ChoiceOfPictureActivity.class);
        startActivity(i);
    }



}
