
package com.example.matthieu.minipoll;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class SondageListeActivity extends AppCompatActivity {

    //source:https://github.com/jonndavis1993/Android-Tutorials/tree/master/app

    Utilisateur u;
    private ArrayList<String> data = new ArrayList<String>();
    String typePoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sondage_liste);

        Intent i = getIntent();
        u = (Utilisateur) i.getSerializableExtra("utilisateur");
        this.typePoll = (String) i.getSerializableExtra("type");

        ListView lv = (ListView) findViewById(R.id.listview);
        generateListContent();
        if(typePoll.compareTo("QUESTIONNAIRE")==0) {
            lv.setAdapter(new MyListAdaper(this, R.layout.list_item_questionnaire, data));
        }else if(typePoll.compareTo("CHOIX")==0) {
            lv.setAdapter(new MyListAdaper(this, R.layout.list_item_choix, data));
        }else if(typePoll.compareTo("SONDAGE")==0) {
            lv.setAdapter(new MyListAdaper(this, R.layout.list_item_sondage, data));
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String [] tab=new String[3];
                String str=data.get(position);
                String [] tempo=str.split("\n");
                if(tempo.length==4){
                    tab[0]=tempo[1];
                    tab[1]=tempo[2];
                    tab[2]=tempo[3];
                }else{
                    tab=tempo;
                }
                Intent i=new Intent(SondageListeActivity.this,SondageViewActivity.class);
                i.putExtra("utilisateur",u);
                i.putExtra("Titre",tab[0]);
                i.putExtra("Date",tab[1].substring(9)); //substring pour eviter le 'Fait le: '
                i.putExtra("Auteur",tab[2].substring(5)); //substring pour eviter le 'Par: '
                i.putExtra("type",typePoll);
                startActivity(i);
            }
        });
    }

    private void generateListContent() {
        final DataBaseHelper myDbHelper = new DataBaseHelper(SondageListeActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(SondageListeActivity.this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(SondageListeActivity.this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        String [] whereArgs={u.pseudo};
        Cursor c=myDbHelper.rawQuery("select TITRE,DATE,AUTEUR from "+typePoll+"_PARTICIPANT where PARTICIPANT=? AND PARTICIPATION=0",whereArgs);

        String [][] value=myDbHelper.createTabFromCursor(c,3);
        for(int i=0;i<value.length;i++){
            data.add(value[i][0]+"\nFait le: "+value[i][1]+"\nPar: "+value[i][2]);
        }//Sondages non fait

        c=myDbHelper.rawQuery("select TITRE,DATE,AUTEUR from "+typePoll+"_PARTICIPANT where PARTICIPANT=? AND PARTICIPATION=1",whereArgs);
        //cherche mnt les sondages pas encore faits

        value=myDbHelper.createTabFromCursor(c,3);

        for(int i=0;i<value.length;i++){
            data.add("Vous y avez deja participe\n"+value[i][0]+"\nFait le: "+value[i][1]+"\nPar: "+value[i][2]);
        }//Sondages deja fait
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
            ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position));
            return convertView;
        }
    }

    public class ViewHolder {

        ImageView thumbnail;
        TextView title;
    }
}

