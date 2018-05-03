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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListeAmisPourPollActivity extends AppCompatActivity {
    Utilisateur u;
    DataBaseHelper myDbHelper;

    private ArrayList<String> amisARajouter;
    private ArrayList<String> amisQuiOntEteRajoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_amis_pour_poll);

        Intent i = getIntent();
        this.u = (Utilisateur) i.getSerializableExtra("utilisateur");

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

        /**  S'occupe du listener sur la liste des amis qui peuvent etre rajoute (deuxieme liste) */
        amis2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
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
        amisARajouter.add("ami 1");
        amisARajouter.add("ami 2");

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
    public void retour(View v)
    {
        finish();
    }
}