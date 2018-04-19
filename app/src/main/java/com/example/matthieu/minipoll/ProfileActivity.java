package com.example.matthieu.minipoll;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends AppCompatActivity {

    Utilisateur u;
    DataBaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        u = (Utilisateur) i.getSerializableExtra("utilisateur");
        TextView name=findViewById(R.id.name);
        name.setText(u.nom);

        TextView firstName = findViewById(R.id.firstName);
        firstName.setText(u.prenom);

        TextView pseudo = findViewById(R.id.pseudo);
        pseudo.setText(u.pseudo);

        TextView email = findViewById(R.id.email);
        email.setText(u.email);

        TextView password = findViewById(R.id.password);
        password.setText(u.mdp);


        //Ajouter la photo de profil
        //ImageView profilPicture = findViewById(R.id.profilPicture);
        //profilPicture.setImageAlpha(u.photo);

    }
}
