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
import android.util.Log;
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

public class NewQuestionActivity extends Activity {
    private ListView myList;
    private MyAdapter myAdapter;
    private DataBaseHelper myDbHelper;

    Utilisateur u;
    String titre;
    int count;
    String date;


    ArrayList<String> listeReponse= new ArrayList<String>();
    ArrayList<String> tabSTM;

    /** sources :https://vikaskanani.wordpress.com/2011/07/27/android-focusable-edittext-inside-listview/ */

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        Intent i = getIntent();
        this.count= (int)i.getSerializableExtra("count");
        this.titre= (String)i.getSerializableExtra("titre");
        this.u= (Utilisateur)i.getSerializableExtra("utilisateur");
        this.tabSTM= (ArrayList<String>)i.getSerializableExtra("tabSTM");
        this.date = (String)i.getSerializableExtra("date");

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
            for (int i = 0; i < 4; i++) {
                ListItem listItem = new ListItem();
                myItems.add(listItem);
            }
            notifyDataSetChanged();
        }
        @Override
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
                    if (!hasFocus){
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

    public void test(View v){
        ;
    }




    public void OK(View v) {

        String question = ((EditText) findViewById(R.id.Question)).getText().toString();
        int Rightanswer=-1;
        try {
            Rightanswer = Integer.parseInt(((EditText) findViewById(R.id.NumberRightAnswer)).getText().toString());
        } catch (NumberFormatException NFE) {
            Toast.makeText(this, "Fill all the blank please", Toast.LENGTH_LONG).show();
            return;
        }

        if (Rightanswer>4 || Rightanswer<1) {
            Toast.makeText(this, "Choose a number between 1 and 4", Toast.LENGTH_LONG).show();
            return;
        }

        for (int i = 0; i < 4; i++) {
            listeReponse.add(((ListItem) myAdapter.getItem(i)).caption);
        }

        if (listeReponse.contains("") || titre.compareTo("") == 0 || question.compareTo("") == 0 || listeReponse.contains(null)) { //si toute les cases n'ont pas ete remplies
            Toast.makeText(this, "Fill all the blank please", Toast.LENGTH_LONG).show();
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

        for (int i = 0; i < 4; i++) {
            int j;
            if (i == Rightanswer-1) {
                 j = 1;
            }
            else{
                j = 0;
            }

            String str = ("insert into QUESTIONNAIRE_PROPOSITION values('"
                    + titre + "','"
                    + date + "','"
                    + u.getPseudo() + "','"
                    + question.replace("'", " ") + "','"
                    + listeReponse.get(i).replace("'", " ") + "',"
                    + j
                    + ");");
            tabSTM.add(str); //OK
            Log.e("debug",str);
        }

        String str = ("insert into QUESTIONNAIRE values('"
                + titre + "','"
                + date + "','"
                + u.getPseudo()
                + "');");
        tabSTM.add(str);//OK
        Log.e("debug",str);

        String str2 = ("insert into QUESTION values('"
                + titre + "','"
                + date + "','"
                + u.getPseudo()  +"','"
                + question.replace("'", " " )
                + "');");//OK
        tabSTM.add(str2);//OK
        Log.e("debug",str2);

        count = count -1;

        if (count ==0) {
            Intent i = new Intent(this, ListeAmisPourPollActivity.class);
            i.putExtra("utilisateur", u);
            i.putExtra("sql", tabSTM);
            i.putExtra("typePoll", "Quizz");
            i.putExtra("titre", titre);
            i.putExtra("date", date);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(this, NewQuestionActivity.class);
            i.putExtra("utilisateur", u);
            i.putExtra("tabSTM", tabSTM);
            i.putExtra("titre", titre);
            i.putExtra("date", date);
            i.putExtra("count", count);
            startActivity(i);
            finish();
        }
    }

    public void retour(View v){
        finish();
    }
}