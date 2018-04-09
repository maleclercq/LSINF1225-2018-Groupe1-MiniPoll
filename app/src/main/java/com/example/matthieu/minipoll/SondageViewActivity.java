package com.example.matthieu.minipoll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SondageViewActivity extends AppCompatActivity {
    Utilisateur u;
    String titre;
    String date;
    String auteur;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sondage_view);

        Intent i = getIntent();
        u= (Utilisateur)i.getSerializableExtra("utilisateur");
        titre= (String)i.getSerializableExtra("Titre");
        date=(String)i.getSerializableExtra("Date");
        auteur= (String)i.getSerializableExtra("Auteur");
        type= (String)i.getSerializableExtra("type");

        TextView tv=findViewById(R.id.tempo);
        tv.setText("You're "+u.pseudo+"\nAnd you're looking for: "+titre+"\nCreate on: "+date+"\nBy: "+auteur+"\nAnd that's a "+type);
    }
}
