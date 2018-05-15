package com.example.matthieu.minipoll.Amis;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthieu.minipoll.ConnectionActivity;
import com.example.matthieu.minipoll.CreateAccountActivity;
import com.example.matthieu.minipoll.DataBaseHelper;
import com.example.matthieu.minipoll.New_poll.PopUpReponseFinaleActivity;
import com.example.matthieu.minipoll.PollListeActivity;
import com.example.matthieu.minipoll.Position;
import com.example.matthieu.minipoll.Profile.EditProfileActivity;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListeAmisActivity extends AppCompatActivity {

    DataBaseHelper myDbHelper;
    ArrayList<String> data=new ArrayList<String>();
    Utilisateur u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_amis);

        Intent i = getIntent();
        u = (Utilisateur) i.getSerializableExtra("utilisateur");

        generateListContent();

        ListView lv = (ListView) findViewById(R.id.listAmis);
        generateListContent();
        lv.setAdapter(new ListeAmisActivity.MyListAdaper(this, R.layout.list_choose_sondage, data));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i=new Intent(ListeAmisActivity.this,ViewProfileFriendActivity.class);
                i.putExtra("amis",data.get(position));
                i.putExtra("utilisateur",u);
                startActivity(i);
            }
        });
    }

    private void generateListContent() {
        this.myDbHelper = new DataBaseHelper(this);
        try {
            this.myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(this, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            this.myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(this, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }
        data=u.getFriends(myDbHelper);
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
            ListeAmisActivity.ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ListeAmisActivity.ViewHolder viewHolder = new ListeAmisActivity.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (ListeAmisActivity.ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position));
            return convertView;
        }
    }

    public class ViewHolder {
        TextView title;
    }

    public void find(View v){
        EditText et=findViewById(R.id.SearshingFriend);
        String ami=et.getText().toString();

        if(ami.matches("^.*[^a-zA-Z0-9 ].*$") || ami.length()<1){
            Toast.makeText(this,"Please, enter a valid pseudo",Toast.LENGTH_LONG).show(); //traduction
            return;
        }

        if(!u.addFriend(myDbHelper,ami)){
            Toast.makeText(this,"Sorry, not found",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i=new Intent(this,ListeAmisActivity.class);
        i.putExtra("utilisateur",u);
        startActivity(i);
        finish();
    }

    public void suggestion(View v){
        ArrayList<String> suggestion=u.getSuggestion(this.myDbHelper);
        if(suggestion.size()==0){
            Toast.makeText(this,"Sorry, no suggestion available",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i=new Intent(this,SuggestionActivity.class);
        i.putExtra("utilisateur",u);
        i.putExtra("suggestion",suggestion);
        i.putExtra("position",0);
        startActivity(i);
        finish();
    }

    public void retour(View v)
    {
        finish();
    }

}
