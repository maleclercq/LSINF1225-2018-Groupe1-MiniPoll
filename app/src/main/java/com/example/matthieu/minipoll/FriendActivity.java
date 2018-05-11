package com.example.matthieu.minipoll;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FriendActivity extends ListActivity {
    Utilisateur u;
    protected DataBaseHelper myDbHelper;
    protected SQLiteDatabase db;
    public static final String CHOSEN_TEXT = "texteChoisi";
    List<String> friendList = new List<String>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<String> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] ts) {
            return null;
        }

        @Override
        public boolean add(String s) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends String> collection) {
            return false;
        }

        @Override
        public boolean addAll(int i, @NonNull Collection<? extends String> collection) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> collection) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public boolean equals(Object o) {
            return false;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public String get(int i) {
            return null;
        }

        @Override
        public String set(int i, String s) {
            return null;
        }

        @Override
        public void add(int i, String s) {

        }

        @Override
        public String remove(int i) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<String> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<String> listIterator(int i) {
            return null;
        }

        @NonNull
        @Override
        public List<String> subList(int i, int i1) {
            return null;
        }
    };
    String chosenFriend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Intent in = getIntent();
        this.u = (Utilisateur) in.getSerializableExtra("utilisateur");

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


        String[] whereArgs = {this.u.getPseudo()};
        Cursor c = myDbHelper.rawQuery("select AMI from AMI where Utilisateur=?", whereArgs);
        String[][] values = myDbHelper.createTabFromCursor(c, 1);


        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                friendList.add(values[i][j]);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenFriend = friendList.get(position);
                Toast.makeText(com.example.matthieu.minipoll.FriendActivity.this, "Vous avez choisi : " + chosenFriend, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void finish() {
        if (chosenFriend != null) {
            Intent data = new Intent();
            data.putExtra(CHOSEN_TEXT, chosenFriend);
            setResult(RESULT_OK, data);
        }
        super.finish();
    }
}
