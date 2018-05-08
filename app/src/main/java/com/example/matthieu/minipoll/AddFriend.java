package com.example.matthieu.minipoll;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.io.IOException;

public class AddFriend extends AppCompatActivity {

    Utilisateur u;
    private ViewPager mPager ;
    private CardStackAdapter mAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Intent i = getIntent();
        u= (Utilisateur)i.getSerializableExtra("utilisateur");

        mPager = (ViewPager) findViewById(R.id.viewPager);

        mAdapter = new CardStackAdapter(getSupportFragmentManager());

        mPager.setPageTransformer(true, new CardStackTransformer());

        mPager.setOffscreenPageLimit(5);

        mPager.setAdapter(mAdapter);

        final DataBaseHelper myDbHelper = new DataBaseHelper(this);
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
        //A ouvert la dataBase et la stocke dans myDbHelper


        String [] whereArgs={u.getPseudo()};//les conditions de la requete sql
        Cursor c=myDbHelper.rawQuery("select AMIS from AMIS where u=? ",whereArgs);//on fait la requete

        String [][] tab=myDbHelper.createTabFromCursor(c,6);

    }

    private class CardStackTransformer implements ViewPager.PageTransformer
    {
        @Override
        public void transformPage(View page, float position)
        {
            if(position>=0)
            {
                page.setScaleX(0.8f - 0.02f * position);

                page.setScaleY(0.8f);

                page.setTranslationX(- page.getWidth()*position);

                page.setTranslationY(30 * position);
            }

        }
    }
}

