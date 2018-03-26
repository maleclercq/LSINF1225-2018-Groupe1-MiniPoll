package com.example.matthieu.minipoll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Utilisateur u;
    TextView pseudo;
    TextView nom;
    TextView prenom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        u= (Utilisateur)i.getSerializableExtra("sampleObject");

        pseudo=findViewById(R.id.Pseudo);
        pseudo.setText(u.id);

        nom=findViewById(R.id.Nom);
        nom.setText(u.nom);

        prenom=findViewById(R.id.Prenom);
        prenom.setText(u.prenom);
    }

    public void logOff(View v){
        Intent i = new Intent(MainActivity.this, ConnectionActivity.class);
        startActivity(i);
    }
}
