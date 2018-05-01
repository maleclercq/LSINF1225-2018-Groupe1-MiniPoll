package com.example.matthieu.minipoll;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class NewSondageActivity extends Activity {
    private ListView myList;
    private MyAdapter myAdapter;

    Utilisateur u;
    int nbrChoix;
    int nbrProp;

    ArrayList tabProp;

    /** sources :https://vikaskanani.wordpress.com/2011/07/27/android-focusable-edittext-inside-listview/ */

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sondage);

        Intent i = getIntent();
        this.nbrProp= (int)i.getSerializableExtra("nbrProp");
        this.nbrChoix= (int)i.getSerializableExtra("nbrChoix");
        this.u= (Utilisateur)i.getSerializableExtra("utilisateur");

        this.tabProp=new ArrayList<>();

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
            return position;
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

    public void OK(View v){
        Intent i=new Intent(this,ListeAmisPourPollActivity.class);
        i.putExtra("utilisateur",u);
//        i.putExtra("sql",sqlListe);
        startActivity(i);
        finish();
    }
}