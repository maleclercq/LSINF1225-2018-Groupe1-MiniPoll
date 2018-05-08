package com.example.matthieu.minipoll.New_poll;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matthieu.minipoll.PollListeActivity;
import com.example.matthieu.minipoll.Position;
import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Sondage;
import com.example.matthieu.minipoll.SondageViewActivity;
import com.example.matthieu.minipoll.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class PrevisualisationSondage extends AppCompatActivity {
    boolean participation;
    String titre;
    String question;
    int nbrChoixMax;
    Sondage sondage;

    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previsualisation_sondage);

        Intent i = getIntent();
        this.titre = (String) i.getSerializableExtra("titre");
        this.question = (String) i.getSerializableExtra("question");
        this.data= (ArrayList) i.getSerializableExtra("tabProp");
        this.nbrChoixMax=(int) i.getSerializableExtra("nbrChoixMax");
        Log.e("debug:",data.get(0));

        TextView titreTV = findViewById(R.id.Titre);
        SpannableString sousligne = new SpannableString(this.titre);
        sousligne.setSpan(new UnderlineSpan(), 0, sousligne.length(), 0);
        titreTV.setText(sousligne); //Titre souligne

        TextView questionTV = findViewById(R.id.Question);
        questionTV.setText(this.question); //Titre souligne

        ListView lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(new PrevisualisationSondage.MyListAdaper(this, R.layout.list_choose_sondage, data));

        if (!participation) {
            TextView nbrMaxTV = findViewById(R.id.nbrMax);
            nbrMaxTV.setText("Number of choices: " + nbrChoixMax);
        }

        if (participation) { //affiche le score de la question
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    String[][] value = sondage.getPropositionAndQuestion();
                    int nbrMax = sondage.getNombreAChoisir();
                    String str = "" + sondage.getScore(value[position][0], nbrMax);

                    str = "Le score de cette reponse est : " + str;
                    Toast.makeText(PrevisualisationSondage.this, str, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                }
            });
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
            PrevisualisationSondage.ViewHolder mainViewholder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                PrevisualisationSondage.ViewHolder viewHolder = new PrevisualisationSondage.ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
                convertView.setTag(viewHolder);
            }
            mainViewholder = (PrevisualisationSondage.ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position));
            return convertView;
        }
    }

    public class ViewHolder {
        TextView title;
    }

    public void retour(View v) {
        finish();
    }
}

