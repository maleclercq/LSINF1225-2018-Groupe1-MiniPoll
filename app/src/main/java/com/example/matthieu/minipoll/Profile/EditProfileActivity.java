package com.example.matthieu.minipoll.Profile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class EditProfileActivity extends AppCompatActivity {
    EditText editFirstName, editName, editId, editEmail, editPassword;
    String firstname, name, id, email, password;
    Utilisateur u;
    DataBaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent i = getIntent();
        u = (Utilisateur) i.getSerializableExtra("utilisateur");

        editFirstName = findViewById(R.id.FirstName);
        //editFirstName.setText(u.prenom);

        editName = (EditText) findViewById(R.id.Name);
        //editName.setText(u.nom);

        editId = (EditText) findViewById(R.id.Pseudo);
        //editId.setText(u.pseudo);

        editEmail = (EditText)  findViewById(R.id.Email);
        //editId.setText(u.email);

        editPassword = (EditText) findViewById(R.id.Password);
        //editPassword.setText(u.mdp);

    }

    public void validationProfile(View v) throws SQLException {
        final DataBaseHelper myDbHelper = new DataBaseHelper(EditProfileActivity.this);
        try {
            myDbHelper.createDataBase();
        }
        catch (IOException ioe) {
            Toast.makeText(EditProfileActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();
        if(editEmail.toString().compareTo("")==0
                || password.compareTo("")==0 ){
            Toast.makeText(EditProfileActivity.this, "Please, fill all the fields", Toast.LENGTH_LONG).show(); //traduction
            return;
        }
        //if(password.compareTo(confirmPassword)!=0){
        //  Toast.makeText(EditProfileActivity.this,"Password don't correspond",Toast.LENGTH_LONG).show(); //traduction
        //return;
        // }

        if(editPassword.toString().matches("^.*[^a-zA-Z0-9 ].*$") || password.length()<6){
            Toast.makeText(EditProfileActivity.this,"Please, enter a valid password",Toast.LENGTH_LONG).show(); //traduction
        }
        Cursor c=myDbHelper.rawQuery("SELECT * FROM Utilisateur WHERE ID='"+ editId.getText().toString()+"'", null);
        if(c.moveToFirst()) {
            myDbHelper.rawQuery("UPDATE Utilisateur SET NOM ='"+ editName.getText()+"', FIRSTNAME='"+ editFirstName.getText()+"',ID='"+ editId.getText()+"', MDP='" + editPassword.getText()+"', EMAIL'" + editEmail.getText(), new String[]{"' WHERE ID ='" + editId.getText() + "'"});
            Toast.makeText(this, "Record Modified", Toast.LENGTH_LONG).show();
        }
    }

    public void retour(View v){
        finish();
    }

    public void chooseImage(View v){
        Intent i = new Intent(this, ChoiceOfProfilePictureActivity.class);
        startActivity(i);
    }



}
