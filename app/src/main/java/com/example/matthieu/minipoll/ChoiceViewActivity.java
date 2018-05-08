package com.example.matthieu.minipoll;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;

public class ChoiceViewActivity extends AppCompatActivity {

    Utilisateur u;
    DataBaseHelper myDbHelper;
    boolean participation;
    String titre;
    String date;
    String auteur;
    String question;
    String first;
    String second;
    //ImageView firstImg;
    //ImageView secondImg;
    ImageView gauche;
    ImageView droite;
    String answer;
    Choice choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_view);

        Intent i = getIntent();
        this.u = (Utilisateur) i.getSerializableExtra("utilisateur");
        this.titre = (String) i.getSerializableExtra("titre");
        this.date = (String) i.getSerializableExtra("date");
        this.auteur = (String) i.getSerializableExtra("auteur");
        //this.question = (String) i.getSerializableExtra("question");
        this.participation = (boolean) i.getSerializableExtra("participation");
        this.choice=new Choice(ChoiceViewActivity.this,this.titre,this.date,this.auteur);

        String [][] proposition = this.choice.getPropositions();

        gauche = findViewById(R.id.Gauche);
        droite = findViewById(R.id.Droite);

        first = (proposition[0][0]);
        second = (proposition[1][0]);
        question = (proposition[0][1]);

        TextView firstC = findViewById(R.id.Choix1);
        firstC.setText(first);

        TextView secondC = findViewById(R.id.Choix2);
        secondC.setText(second);

        TextView titreC=findViewById(R.id.TitleChoice);
        titreC.setText(titre);

        TextView questionC = findViewById(R.id.QuestionChoice);
        questionC.setText(question);

    }

    public void chooseFirstChoice(View v){
        gauche.setBackgroundResource(R.color.colorOui);
        droite.setBackgroundResource(R.color.colorNon);
        answer = first;
    }

    public void chooseSecondChoice(View v){
        gauche.setBackgroundResource(R.color.colorNon);
        droite.setBackgroundResource(R.color.colorOui);
        answer = second;
    }

    public void validation(View v){
        this.myDbHelper = new DataBaseHelper(ChoiceViewActivity.this);
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        if(participation){
            Toast.makeText(this,"You've already answer to it",Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            if (answer.compareTo(second)==0 || answer.compareTo(first)==0)
            {
                choice.insertAnswer(u.getPseudo(), answer, first, second);
                Toast.makeText(this,"Thank you for your answer",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this, "Please select one proposition", Toast.LENGTH_LONG).show();
                return;
            }
            SQLiteStatement stmt1=db.compileStatement("insert into CHOIX_PARTICIPANT values('"
                    + this.titre + "','"
                    + this.date + "','"
                    + this.auteur + "','"
                    + this.question + "','"
                    + this.u.getPseudo() + "',"
                    + 1 + ")");
            stmt1.execute();
            SQLiteStatement stmt2 = db.compileStatement("delete from CHOIX_PARTICIPANT where " +
                    "PARTICIPANT='"+ this.u.getPseudo() +"' AND "
                    + "TITRE='"+this.titre + "' AND "
                    + "DATE='"+this.date + "' AND "
                    + "AUTEUR='"+this.auteur +"' AND "
                    + "PARTICIPATION=0");
            stmt2.execute();
        }
        Intent i=new Intent(this,PollListeActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("type","CHOIX");
        startActivity(i);
        finish();
    }
    public void retour(View v) {
        finish();
    }
}
