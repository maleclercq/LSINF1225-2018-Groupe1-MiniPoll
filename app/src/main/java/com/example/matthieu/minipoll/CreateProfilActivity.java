package com.example.matthieu.minipoll;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class CreateProfilActivity extends AppCompatActivity {

    String id;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profil);


        Intent i = getIntent();
        this.id = (String) i.getSerializableExtra("id");
        this.password = (String) i.getSerializableExtra("password");
    }

    public void Validation(View v) {

        String prenom = ((EditText) findViewById(R.id.Prenom)).getText().toString();
        String nom = ((EditText) findViewById(R.id.NomDeFamille)).getText().toString();
        String mail = ((EditText) findViewById(R.id.AdresseEMail)).getText().toString();
        String image = "basicimage.png";

        if (prenom.compareTo("") == 0
                || nom.compareTo("") == 0
                || mail.compareTo("") == 0
                ) {

            Toast.makeText(CreateProfilActivity.this, "Please, fill all the field", Toast.LENGTH_LONG).show(); //traduction
            return;
        }

        if (!mail.contains("@") || !mail.contains(".")) {
            Toast.makeText(CreateProfilActivity.this, "Please, enter a valid mail", Toast.LENGTH_LONG).show(); //traduction
            return;
        }

        final DataBaseHelper myDbHelper = new DataBaseHelper(CreateProfilActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(CreateProfilActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(CreateProfilActivity.this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement("insert into UTILISATEUR values('" + id + "','" + nom + "','" + prenom + "','" + password + "','" + mail + "','" + image + "')");
        stmt.execute();

        Utilisateur u = new Utilisateur(id, nom, prenom, password, mail, "basicimage.png");
        Intent i = new Intent(CreateProfilActivity.this, MainActivity.class);

        i.putExtra("utilisateur", u);
        startActivity(i);
        finish();
    }

    public void retour(View v)
    {
        finish();
    }
}
