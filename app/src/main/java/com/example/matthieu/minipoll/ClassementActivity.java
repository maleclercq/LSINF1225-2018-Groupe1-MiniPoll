package com.example.matthieu.minipoll;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ClassementActivity extends AppCompatActivity {

    Utilisateur u;
    String titre;
    String date;
    String auteur;
    Quizz quizz;
    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classement);

        Intent i = getIntent();
        this.u = (Utilisateur) i.getSerializableExtra("utilisateur");
        this.titre = (String) i.getSerializableExtra("titre");
        this.date = (String) i.getSerializableExtra("date");
        this.auteur = (String) i.getSerializableExtra("auteur");
        this.quizz = new Quizz(ClassementActivity.this, titre, date , auteur);

        TextView titreTV = findViewById(R.id.Titre);
        SpannableString sousligne = new SpannableString(this.titre);
        sousligne.setSpan(new UnderlineSpan(), 0, sousligne.length(), 0);
        titreTV.setText(sousligne); //Titre souligne
        ListView lv = (ListView) findViewById(R.id.listview); //Affiche le titre

        generateListContent();
        lv.setAdapter(new ClassementActivity.MyListAdaper(this, R.layout.list_choose_sondage, data));
    }
    private void generateListContent() {//ajoute les scores
        String[][] value = quizz.getParticipants();
        String question="";
        String userAnswer="";
        String bonneReponse="";
        int size = quizz.getNumberOfQuestion();
        for (int i=0; i<value.length; i++) {
            if(Integer.parseInt(value[i][1])==1){
                int score = 0;
                String participant = value[i][0];
                for (int j=0; j<size ;j++){
                    question = quizz.getQuestion()[j][0];//Question numero j
                    userAnswer = quizz.getUserAnswer(participant)[j][0];//Reponse de l'utilisateur a cette question
                    String [][] answerIsTrue = quizz.getAnswer(question);//Tableau avec les proposition et si oui ou non elles sont correctes
                    for (int k=0; k< answerIsTrue.length; k++){//Pour chaque proposition, on vérifie si elle est bonne et si c'est ce que l'utilisateur a repondu
                        bonneReponse = answerIsTrue[k][0];
                        bonneReponse=bonneReponse.replaceAll("\n","");//evite les characteres indésirables
                        userAnswer=userAnswer.replaceAll("\n","");//evite les characteres indésirables
                        if(Integer.parseInt(answerIsTrue[k][1])== 1 && userAnswer.compareTo(bonneReponse)==0){// COMPARE TA MERE !!!!
                            score++;
                            Log.i("Erreur: ", "boucle3");
                        }
                    }
                }
                data.add(participant+ ": " + score + "/" + size + "\n");
            }
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
            ClassementActivity.ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ClassementActivity.ViewHolder viewHolder = new ClassementActivity.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ClassementActivity.ViewHolder) convertView.getTag();
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
