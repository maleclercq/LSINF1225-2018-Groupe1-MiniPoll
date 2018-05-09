package com.example.matthieu.minipoll;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthieu.minipoll.New_poll.ListeAmisPourPollActivity;
import com.example.matthieu.minipoll.New_poll.NewQuestionActivity;
import com.example.matthieu.minipoll.New_poll.NewQuizzActivity;
import com.example.matthieu.minipoll.New_poll.PopUpReponseFinaleActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuizzActivity extends AppCompatActivity {

    Utilisateur u;
    boolean participation;
    String titre;
    String date;
    String auteur;
    int count;
    Quizz quizz;
    ArrayList<String> answer;
    String finalAnswer;

    boolean confirmationCloture = false;
    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<Position> tabPosition = new ArrayList<Position>();
    public ArrayList<String> tabSTM = new ArrayList<String>();
    private DataBaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);

        Intent i = getIntent();
        this.u = (Utilisateur) i.getSerializableExtra("utilisateur");
        this.titre = (String) i.getSerializableExtra("titre");
        this.date = (String) i.getSerializableExtra("date");
        this.auteur = (String) i.getSerializableExtra("auteur");
        this.participation = (boolean) i.getSerializableExtra("participation");
        this.count = (int) i.getSerializableExtra("count");
        this.quizz = new Quizz(QuizzActivity.this, titre, date , auteur);
        this.answer=(ArrayList<String>) i.getSerializableExtra("answer");


        TextView titreTV = findViewById(R.id.Titre);
        SpannableString sousligne = new SpannableString(this.titre);
        sousligne.setSpan(new UnderlineSpan(), 0, sousligne.length(), 0);
        titreTV.setText(sousligne); //Titre souligne
        ListView lv = (ListView) findViewById(R.id.listview); //Affiche le titre


        TextView question = findViewById(R.id.Question);
        String[][] listeQuestion = quizz.getQuestion();
        String enonceQuestion = listeQuestion[count][0];
        question.setText(enonceQuestion);//Affiche la question

        generateListContent();
        lv.setAdapter(new QuizzActivity.MyListAdaper(this, R.layout.list_choose_sondage, data));

        if(!participation) {
            TextView ua = findViewById(R.id.UserAnswer);
            ua.setText("Your answer: ");
        }

        if(this.u.getPseudo().compareTo(this.auteur)!=0){//n'affiche pas le boutton pour cloturer si l'utilisateur n'est pas le createur
            Button but=findViewById(R.id.Cloturer);
            but.setVisibility(View.GONE);
        }

        if(participation){
            TextView ua = findViewById(R.id.UserAnswer);
            String [][] tab= quizz.getAnswer(quizz.getQuestion()[count][0]);
            for (int j=0;j<tab.length;j++){
                if (Integer.parseInt(tab[j][1])==1){
                    String aAfficher = "Your answer: "+ quizz.getUserAnswer(u.getPseudo())[count][0] + "\n"
                            + "The right answer was: "+ quizz.getProposition(quizz.getQuestion()[count][0])[j][0];
                    ua.setText(aAfficher);
                }
            }
        }
        else {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                // changer la reponse en bas avec data.get(position);
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    finalAnswer=data.get(position);
                    TextView ua = findViewById(R.id.UserAnswer);
                    ua.setText("Your answer: "+ finalAnswer);
                }
            });
        }
    }
    public void valider(View v) {

        count = count +1;
        answer.add(finalAnswer);
        TextView ua = findViewById(R.id.UserAnswer);
        if (ua.getText().equals("Your answer: ")) { //Si aucune reponse a ete selectionnee
            Toast.makeText(this, "Choose an answer please", Toast.LENGTH_LONG).show();
            return;
        }

        this.myDbHelper = new DataBaseHelper(this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        SQLiteDatabase db = myDbHelper.getWritableDatabase();


        if (quizz.getNumberOfQuestion()-count ==0) {//Si on a plus de question après
            quizz.deleteParticipant(u.getPseudo());
            quizz.insertParticipant(u.getPseudo());// On change la participation
            for(int i=0; i<quizz.getNumberOfQuestion(); i++){//On change les réponses utilisateurs
                quizz.insertResultat(u.getPseudo(), quizz.getQuestion()[i][0], answer.get(i));
            }
            Intent i = new Intent(this, ClassementActivity.class);
            i.putExtra("utilisateur", u);
            i.putExtra("titre", titre);
            i.putExtra("date", date);
            i.putExtra("auteur", auteur);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(this, QuizzActivity.class);
            i.putExtra("utilisateur", u);
            i.putExtra("titre", titre);
            i.putExtra("date", date);
            i.putExtra("auteur", auteur);
            i.putExtra("count", count);
            i.putExtra("participation", participation);
            i.putExtra("answer", answer);
            startActivity(i);
            finish();
        }
    }

    public void cloturer(View v){

    }

    private void generateListContent() {//ajoute les propositions de reponses

        String[][] value = quizz.getProposition(quizz.getQuestion()[count][0]);
        for (int i=0; i<value.length; i++) {
            data.add(value[i][0] + "\n");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyListAdaper extends ArrayAdapter<String> {
        private int layout;
        private List<String> mObjects;

        private MyListAdaper(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            QuizzActivity.ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                QuizzActivity.ViewHolder viewHolder = new QuizzActivity.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (QuizzActivity.ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position));
            return convertView;
        }
    }

    public class ViewHolder {
        TextView title;
    }


    public void retour(View v)
    {
        finish();
    }

}