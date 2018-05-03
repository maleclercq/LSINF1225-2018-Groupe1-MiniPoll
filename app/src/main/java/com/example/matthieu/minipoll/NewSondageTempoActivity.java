package com.example.matthieu.minipoll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewSondageTempoActivity extends AppCompatActivity {

    Utilisateur u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sondage_tempo);


        Intent i = getIntent();
        this.u= (Utilisateur)i.getSerializableExtra("utilisateur");
    }

    public void OK(View v){

        int nbrChoix=0;
        int nbrProposition=0;

        try {
            nbrChoix = Integer.parseInt(((EditText) findViewById(R.id.NbrChoix)).getText().toString());
            nbrProposition = Integer.parseInt(((EditText) findViewById(R.id.NbrProposition)).getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(this,"Please, field coorectly the field",Toast.LENGTH_LONG).show();
            return;
        }

        if(nbrChoix<1 || nbrChoix>nbrProposition){
            Toast.makeText(this,"Please, eenter a correct value for the choises",Toast.LENGTH_LONG).show();
            return;
        }

        if(nbrProposition<2 || nbrProposition>6){
            Toast.makeText(this,"Please, eenter a correct value for the Proposition (between 2 and 6)",Toast.LENGTH_LONG).show();
            return;
        }

        Intent i=new Intent(this,NewSondageActivity.class);
        i.putExtra("nbrChoix",nbrChoix);
        i.putExtra("nbrProp",nbrProposition);
        i.putExtra("utilisateur",u);
        startActivity(i);
        finish();
    }
}
