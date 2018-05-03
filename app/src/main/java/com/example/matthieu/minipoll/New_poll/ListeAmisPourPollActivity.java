package com.example.matthieu.minipoll.New_poll;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListeAmisPourPollActivity extends AppCompatActivity {
    Utilisateur u;
    DataBaseHelper myDbHelper;
    ArrayList<String> tabSTM;
    String typePoll;

    String titre;
    String date;
    String auteur;

    private ArrayList<String> amisARajouter;
    private ArrayList<String> amisQuiOntEteRajoute;

    /**
     *
     * Elements a recevoir de i.getSeriazableExtra:
     *                              utilisateur: Recois un Utilisateur
     *                              sql: Recoit une liste de stmt sql pour créer le poll après avoir choisi les amis
     *                              isChoice: boolean qui regarde si c'est un choix (pour ne mettre que un ami)
     *                              titre: recoit le titre du poll
     *                              date: recoit la date du poll
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_amis_pour_poll);

        Intent i = getIntent();
        this.u = (Utilisateur) i.getSerializableExtra("utilisateur");
        this.tabSTM= (ArrayList<String>) i.getSerializableExtra("sql");
        this.typePoll=(String) i.getSerializableExtra("typePoll");

        this.titre=(String) i.getSerializableExtra("titre");
        this.date=(String) i.getSerializableExtra("date");

        amisARajouter= new ArrayList<String>();
        amisQuiOntEteRajoute = new ArrayList<String>();

        ListView amis1 = (ListView) findViewById(R.id.listview2);
        generateListContent();
        amis1.setAdapter(new ListeAmisPourPollActivity.MyListAdaper(this, R.layout.list_item_amis, amisQuiOntEteRajoute));

        /**  S'occupe du listener sur la liste des amis qui ont deja ete rajoute (premiere liste) */
        amis1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                amisARajouter.add(amisQuiOntEteRajoute.get(position));
                amisQuiOntEteRajoute.remove(position);

                //Actualise data
                ListView lv1 = (ListView) findViewById(R.id.listview3);
                lv1.setAdapter(new ListeAmisPourPollActivity.MyListAdaper(ListeAmisPourPollActivity.this, R.layout.list_item_amis, amisARajouter));

                ListView lv2 = (ListView) findViewById(R.id.listview2);
                lv2.setAdapter(new ListeAmisPourPollActivity.MyListAdaper(ListeAmisPourPollActivity.this, R.layout.list_item_amis, amisQuiOntEteRajoute));
            }
        });

        ListView amis2 = (ListView) findViewById(R.id.listview3);
        amis2.setAdapter(new ListeAmisPourPollActivity.MyListAdaper(this, R.layout.list_item_amis, amisARajouter));

        /**  S'occupe du listener sur la liste des amis qui peuvent etre rajoutes (deuxieme liste) */
        amis2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(typePoll.compareTo("Choice")==0 && amisQuiOntEteRajoute.size()==1){
                    amisARajouter.add(amisQuiOntEteRajoute.get(0));
                    amisQuiOntEteRajoute.remove(0);
                }
                amisQuiOntEteRajoute.add(amisARajouter.get(position));
                amisARajouter.remove(position);

                //Actualise data
                ListView lv1 = (ListView) findViewById(R.id.listview3);
                lv1.setAdapter(new ListeAmisPourPollActivity.MyListAdaper(ListeAmisPourPollActivity.this, R.layout.list_item_amis, amisARajouter));

                ListView lv2 = (ListView) findViewById(R.id.listview2);
                lv2.setAdapter(new ListeAmisPourPollActivity.MyListAdaper(ListeAmisPourPollActivity.this, R.layout.list_item_amis, amisQuiOntEteRajoute));
            }
        });

    }

    private void generateListContent() {
        this.myDbHelper = new DataBaseHelper(ListeAmisPourPollActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(ListeAmisPourPollActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(ListeAmisPourPollActivity.this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        String [] whereArgs={u.pseudo};//les conditions de la requete sql
        Cursor c=myDbHelper.rawQuery("select AMI from AMI where Utilisateur=?",whereArgs);//on fait la requete

        String [][] tab=myDbHelper.createTabFromCursor(c,1);

        for(int i=0;i<tab.length;i++){
            amisARajouter.add(tab[i][0]);
        }

    }

    public void OK(View v)
    {
        if(amisQuiOntEteRajoute.size()==0){
            Toast.makeText(this,"Choice, at least, one friend",Toast.LENGTH_LONG).show();
            return;
        }

        /** ouvre la db **/
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
        /** Fin d'ouverture **/

        for(int i=0;i<tabSTM.size();i++){
            Log.e("debug",tabSTM.get(i));
            SQLiteStatement stmt=db.compileStatement(tabSTM.get(i));
            stmt.execute();
        }

        String insert="";
        if(typePoll.compareTo("Survey")==0) {
            insert="SONDAGE_PARTICIPANT";
        }else if(typePoll.compareTo("Choice")==0){
            insert="CHOIX_PARTICIPANT";
        } else {
            insert="QUESTIONNAIRE_PARTICIPANT";
        }
        for(int i=0;i<amisQuiOntEteRajoute.size();i++){
            SQLiteStatement stmt = db.compileStatement("insert into "+insert+" values('"
                    +this.titre     +"','"
                    +this.date      + "','"
                    +this.u.pseudo    + "','"
                    +amisQuiOntEteRajoute.get(i)  +"',"
                    +0              +")");
            stmt.execute();
        }

        /**
         * rajoute le créateur a la liste des participants"
         **/
        if(typePoll.compareTo("Survey")==0) {
            Log.e("debug",insert);
            SQLiteStatement stmt = db.compileStatement("insert into " + insert + " values('"
                    + this.titre + "','"
                    + this.date + "','"
                    + this.u.pseudo + "','"
                    + this.u.pseudo + "',"
                    + 0 + ")");
            stmt.execute();
        }

        finish();
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
            ListeAmisPourPollActivity.ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ListeAmisPourPollActivity.ViewHolder viewHolder = new ListeAmisPourPollActivity.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_ami);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ListeAmisPourPollActivity.ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position));
            return convertView;
        }
    }

    public class ViewHolder {
        TextView title;
    }

}