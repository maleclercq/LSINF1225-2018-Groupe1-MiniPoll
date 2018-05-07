package com.example.matthieu.minipoll;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SondageViewActivity extends AppCompatActivity {
    Utilisateur u;
    DataBaseHelper myDbHelper;
    boolean participation;
    String titre;
    String date;
    String auteur;
    int nbrDuChoix;
    int nbrChoixMax;

    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<Position> tabPosition=new ArrayList<Position>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sondage_view);

        Intent i = getIntent();
        this.u = (Utilisateur) i.getSerializableExtra("utilisateur");
        this.titre = (String) i.getSerializableExtra("titre");
        this.date = (String) i.getSerializableExtra("date");
        this.auteur = (String) i.getSerializableExtra("auteur");
        this.participation=(boolean) i.getSerializableExtra("participation");
        this.nbrDuChoix=1;

        TextView titreTV=findViewById(R.id.Titre);
        SpannableString sousligne = new SpannableString(this.titre);
        sousligne.setSpan(new UnderlineSpan(), 0, sousligne.length(), 0);
        titreTV.setText(sousligne); //Titre souligne

        ListView lv = (ListView) findViewById(R.id.listview);
        generateListContent();
        lv.setAdapter(new SondageViewActivity.MyListAdaper(this, R.layout.list_choose_sondage, data));

        if(!participation) {
            TextView nbrMaxTV = findViewById(R.id.nbrMax);
            nbrMaxTV.setText("Number of choices: " + nbrChoixMax);
        }

        if(participation){
            return; //pas de listener vu qu'il a deja participe
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                boolean dejaSelectionne = false;
                for (int i = 0; i < tabPosition.size(); i++) {
                    if (tabPosition.get(i).position == position) dejaSelectionne = true;
                }

                if (nbrChoixMax+1==nbrDuChoix && !dejaSelectionne) {
                    for (int i = 0; i < tabPosition.size(); i++) {
                        int index = tabPosition.get(i).position;
                        data.set(index, data.get(index).substring(0, data.get(index).length() - 7));
                        tabPosition.remove(i);
                        i--;
                    }
                    nbrDuChoix=1;
                } else if (!dejaSelectionne) {
                    data.set(position, data.get(position) + "      " + nbrDuChoix);
                    Position p = new Position(position, nbrDuChoix);
                    tabPosition.add(p);
                    nbrDuChoix++;
                } else{
                    decalerData(position);
                }

                //Actualise data
                ListView lv = (ListView) findViewById(R.id.listview);
                lv.setAdapter(new SondageViewActivity.MyListAdaper(SondageViewActivity.this, R.layout.list_choose_sondage, data));
            }


            /*
            *trouve l'endroid ou se trouve l'element selectionne dans tabPosition
             */
            private int trouverIndex(int position){
                int indexDansTab=0;
                for(int i=0;i<tabPosition.size();i++){
                    if(tabPosition.get(i).position==position){
                        indexDansTab=i;
                    }
                }
                return  indexDansTab;
            }

            /*
            *supprime l'element selectionne et decale tout ce
            * qu'il y avait apres
             */
            private void decalerData(int position){
                int indexDansTab=trouverIndex(position);
                boolean etaitLeDernier=false;

                for(int i=indexDansTab;i<tabPosition.size();i++){
                    int index=tabPosition.get(i).position;
                    String newValue=data.get(index);
                    int val=tabPosition.get(i).value;
                    val--;

                    if(val==0){//enleve le dernier element (si il etais avant a 0)
                        data.set(index,data.get(index).substring(0,data.get(index).length()-7));
                        tabPosition.remove(i);
                        etaitLeDernier=true;
                        i--;

                    } else{
                        data.set(index,data.get(index).substring(0,data.get(index).length()-1)+val);
                        tabPosition.get(i).value=tabPosition.get(i).value-1;
                    }
                }
                if(!etaitLeDernier) {
                    data.set(position, data.get(position).substring(0, data.get(position).length() - 7));
                    tabPosition.remove(indexDansTab);
                }

                if(nbrDuChoix!=1) { //evite de tomber a 0
                    nbrDuChoix--;
                }
            }


        });
    }

    public void saveChoises(View v){
        if(participation){
            Toast.makeText(this,"You've already answer to it",Toast.LENGTH_LONG).show();
            return;
        } else if (nbrDuChoix!=nbrChoixMax+1){
            Toast.makeText(this,"Please, select "+(nbrChoixMax-nbrDuChoix+1) +" more answer",Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        for(int i=0;i<tabPosition.size();i++) {

            String answer=this.data.get(this.tabPosition.get(i).position);
            answer=answer.substring(15,answer.length()-8); //enleve 'proposition 1: ' et la valeur de la proposition

            SQLiteStatement stmt = db.compileStatement("insert into SONDAGE_RESULTAT values('"
                    + this.titre + "','"
                    + this.date + "','"
                    + this.auteur + "','"
                    + this.u.getPseudo() + "','"
                    + answer +"','"
                    + this.tabPosition.get(i).value+"')");
            stmt.execute();
        }

        SQLiteStatement stmt = db.compileStatement("delete from SONDAGE_PARTICIPANT where " +
                "PARTICIPANT='"+ this.u.getPseudo() +"' AND "
                + "TITRE='"+this.titre + "' AND "
                + "DATE='"+this.date + "' AND "
                + "AUTEUR='"+this.auteur+"'");
        stmt.execute();

        stmt=db.compileStatement("insert into SONDAGE_PARTICIPANT values('"
                + this.titre + "','"
                + this.date + "','"
                + this.auteur + "','"
                + this.u.getPseudo() + "','1')");
        stmt.execute();

        Intent i=new Intent(this,PollListeActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("type","SONDAGE");
        startActivity(i);
        finish();
    }

    private void generateListContent() {
        this.myDbHelper = new DataBaseHelper(SondageViewActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(SondageViewActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(SondageViewActivity.this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        String[] whereArgs = {this.titre, this.auteur, this.date};
        Cursor c = myDbHelper.rawQuery("select PROPOSITION,QUESTION from SONDAGE where titre=? and auteur=? and date=?", whereArgs);

        String[][] value = myDbHelper.createTabFromCursor(c, 2);
        for (int i = 0; i < value.length; i++) {
            data.add("Proposition " + (i+1) + ": " + value[i][0] + "\n");
        }

        TextView question=findViewById(R.id.Question);
        question.setText(value[0][1]);

        if(participation){
            ajoutReponse();
        }

        c=myDbHelper.rawQuery("select NOMBRE_A_CHOISIR from SONDAGE_TYPE where titre=? and auteur=? and date=?", whereArgs);
        nbrChoixMax=Integer.parseInt(myDbHelper.createTabFromCursor(c,1)[0][0]);

        if(participation){
            TextView reponses=findViewById(R.id.Reponses);
            TextView amis=findViewById(R.id.pasRepondu);
            String strReponses="";

            String[] whereArgs2={this.titre,this.date,this.auteur,"0"};
            c=myDbHelper.rawQuery("select participant from questionnaire_participant where titre=? and date=? and auteur=? and participation=? ", whereArgs);
            String[][] value2 = myDbHelper.createTabFromCursor(c, 1);

            for(int i=0;i<value2.length;i++){
                strReponses+=value2[i][0]+"\n";
            }

            reponses.setText(strReponses);
        }

    }

    private void ajoutReponse() {
        String [] whereArgs = {this.titre, this.auteur, this.date, this.u.getPseudo()};
        Cursor c= myDbHelper.rawQuery("select PROPOSITION, ORDRE_PREF from SONDAGE_RESULTAT where titre=? and auteur=? and date=? and PARTICIPANT=?", whereArgs);

        String [][] tab=myDbHelper.createTabFromCursor(c,2);

        for(int i=0;i<data.size();i++){
            for(int j=0;j<tab.length;j++){
                if(data.get(i).substring(15).compareTo(tab[j][0]+"\n")==0){ //rajoute les propositions quand data.get(i)==une des reponses de l'utilisateur
                    data.set(i,data.get(i)+"      " + tab[j][1]);
                }
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
            SondageViewActivity.ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                SondageViewActivity.ViewHolder viewHolder = new SondageViewActivity.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (SondageViewActivity.ViewHolder) convertView.getTag();
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