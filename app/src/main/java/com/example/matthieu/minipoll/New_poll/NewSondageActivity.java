package com.example.matthieu.minipoll.New_poll;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.New_poll.ListeAmisPourPollActivity;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

public class NewSondageActivity extends Activity {
    private ListView myList;
    private MyAdapter myAdapter;
    private DataBaseHelper myDbHelper;

    Utilisateur u;
    int nbrChoix;
    int nbrProp;

    ArrayList<String> tabProp;
    ArrayList<String> tabSTM;

    /** sources :https://vikaskanani.wordpress.com/2011/07/27/android-focusable-edittext-inside-listview/ */

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sondage);

        Intent i = getIntent();
        this.nbrProp = (int) i.getSerializableExtra("nbrProp");
        this.nbrChoix = (int) i.getSerializableExtra("nbrChoix");
        this.u = (Utilisateur) i.getSerializableExtra("utilisateur");

        this.tabProp = new ArrayList<String>();

        myList = (ListView) findViewById(R.id.MyList);
        myList.setItemsCanFocus(true);
        myAdapter = new MyAdapter();
        myList.setAdapter(myAdapter);

    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public ArrayList<ListItem> myItems = new ArrayList<ListItem>();

        public MyAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < nbrProp; i++) {
                ListItem listItem = new ListItem();
                myItems.add(listItem);
            }
            notifyDataSetChanged();
        }

        public int getCount() {
            return myItems.size();
        }

        public Object getItem(int position) {
            return myItems.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_pour_sondage, null);
                holder.caption = (EditText) convertView.findViewById(R.id.ItemCaption);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Fill EditText with the value you have in data source
            holder.caption.setText(myItems.get(position).caption);
            holder.caption.setId(position);

            //we need to update adapter once we finish with editing
            holder.caption.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        final int position = v.getId();
                        final EditText Caption = (EditText) v;
                        myItems.get(position).caption = Caption.getText().toString();
                    }
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        EditText caption;
    }

    class ListItem {
        String caption;
    }

    public void test(View v) {

    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        return (dateFormat.format(cal.getTime()));
    }

    public void OK(View v) {
        String titre = ((EditText) findViewById(R.id.Titre)).getText().toString();

        String question = ((EditText) findViewById(R.id.Question)).getText().toString();

        for (int i = 0; i < nbrProp; i++) {
            tabProp.add(((ListItem) myAdapter.getItem(i)).caption);
        }

        if (tabProp.contains("") || titre.compareTo("") == 0 || question.compareTo("") == 0) { //si toute les cases n'ont pas ete remplies
            Toast.makeText(this, "Fill all the blank please", Toast.LENGTH_LONG).show();
            return;
        }

        titre = titre.replace("'", " ");
        question = question.replace("'", " ");

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

        tabSTM = new ArrayList<String>();

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        String date = getDate();

        for (int i = 0; i < nbrProp; i++) {
            String str = ("insert into SONDAGE values('"
                    + titre + "','"
                    + date + "','"
                    + u.pseudo + "','"
                    + question + "','"
                    + tabProp.get(i).replace("'", " ") + "');");
            tabSTM.add(str);
        }

        String str = ("insert into SONDAGE_TYPE values('"
                + titre + "','"
                + date + "','"
                + u.pseudo + "',"
                + nbrChoix + ","
                + nbrProp + ");");
        tabSTM.add(str);

        Intent i = new Intent(this, ListeAmisPourPollActivity.class);
        i.putExtra("utilisateur", u);
        i.putExtra("sql", tabSTM);
        i.putExtra("typePoll", "Survey");
        i.putExtra("titre", titre);
        i.putExtra("date", date);
        startActivity(i);
        finish();
    }

    public void retour(View v) {
        finish();
    }
}