package com.example.matthieu.minipoll.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.Profile.ChoiceOfProfilPictureActivity;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;


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

    public void chooseImage(View v){
        Intent i=new Intent(this, ChoiceOfProfilPictureActivity.class);
        startActivity(i);
    }
    public void retour(View v) {
        finish();
    }
}
