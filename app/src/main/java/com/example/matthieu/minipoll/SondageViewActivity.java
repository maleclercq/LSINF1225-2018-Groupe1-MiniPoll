package com.example.matthieu.minipoll;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SondageViewActivity extends AppCompatActivity {
    Utilisateur u;
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
        u = (Utilisateur) i.getSerializableExtra("utilisateur");
        titre = (String) i.getSerializableExtra("Titre");
        date = (String) i.getSerializableExtra("Date");
        auteur = (String) i.getSerializableExtra("Auteur");
        nbrDuChoix=1;

        TextView titreTV=findViewById(R.id.Titre);
        titreTV.setText(this.titre);

        ListView lv = (ListView) findViewById(R.id.listview);
        generateListContent();
        lv.setAdapter(new SondageViewActivity.MyListAdaper(this, R.layout.list_choose_sondage, data));

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
                        data.set(index, data.get(index).substring(0, data.get(index).length() - 5));
                        tabPosition.remove(i);
                        i--;
                    }
                    nbrDuChoix=1;
                }else if (!dejaSelectionne) {
                    data.set(position, data.get(position) + "      " + nbrDuChoix);
                    Position p = new Position(position, nbrDuChoix);
                    tabPosition.add(p);
                    nbrDuChoix++;
                }else{
                    int indexDansTab=0;
                    boolean etaitLeDernier=false;

                    for(int i=0;i<tabPosition.size();i++){
                        if(tabPosition.get(i).position==position){
                            indexDansTab=i;
                        }
                    }//compte ou se trouve l'element qui a ete clique dans tabPosition

                    for(int i=indexDansTab;i<tabPosition.size();i++){

                        int index=tabPosition.get(i).position;
                        String newValue=data.get(index);
                        int val=tabPosition.get(i).value;
                        val--;

                        if(val==0){//enleve le dernier element (si il etais avant a 0)
                            data.set(index,data.get(index).substring(0,data.get(index).length()-5));
                            tabPosition.remove(i);
                            etaitLeDernier=true;
                            i--;

                        } else{
                            data.set(index,data.get(index).substring(0,data.get(index).length()-1)+val);
                            tabPosition.get(i).value=tabPosition.get(i).value-1;
                        }
                    }
                    if(!etaitLeDernier) {
                        data.set(position, data.get(position).substring(0, data.get(position).length() - 5));
                        tabPosition.remove(indexDansTab);
                    }

                    if(nbrDuChoix!=1) { //evite de tomber a 0
                        nbrDuChoix--;
                    }
                }
                ListView lv = (ListView) findViewById(R.id.listview);
                lv.setAdapter(new SondageViewActivity.MyListAdaper(SondageViewActivity.this, R.layout.list_choose_sondage, data));
            }
        });
    }

    private void generateListContent() {
        final DataBaseHelper myDbHelper = new DataBaseHelper(SondageViewActivity.this);
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
        Cursor c = myDbHelper.rawQuery("select PROPOSITION from SONDAGE where titre=? and auteur=? and date=?", whereArgs);

        String[][] value = myDbHelper.createTabFromCursor(c, 1);
        for (int i = 0; i < value.length; i++) {
            data.add("Proposition " + i + ": " + value[i][0] + "\n");
        }

        c=myDbHelper.rawQuery("select NOMBRE_A_CHOISIR from SONDAGE_TYPE where titre=? and auteur=? and date=?", whereArgs);
        nbrChoixMax=Integer.parseInt(myDbHelper.createTabFromCursor(c,1)[0][0]);
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
}