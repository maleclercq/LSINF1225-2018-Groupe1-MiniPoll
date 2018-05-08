package com.example.matthieu.minipoll;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by matthieu on 7/05/18.
 */

public class Sondage {
    private String titre;
    private String date;
    private String auteur;
    private DataBaseHelper myDbHelper;
    private SQLiteDatabase db;

    public Sondage(Context context,String titre, String date, String auteur){
        this.myDbHelper = new DataBaseHelper(context);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            Toast.makeText(context, "Unable to create database", Toast.LENGTH_LONG).show();
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            Toast.makeText(context, "Unable to open database", Toast.LENGTH_LONG).show();
            throw sqle;
        }

        this.titre=titre;
        this.date=date;
        this.auteur=auteur;

        this.db= myDbHelper.getWritableDatabase();
    }

    public String [][] getPropositionAndQuestion(){
        String[] whereArgs = {titre, auteur, date};
        Cursor c = myDbHelper.rawQuery("select PROPOSITION,QUESTION from SONDAGE where titre=? and auteur=? and date=?", whereArgs);

        String[][] value = myDbHelper.createTabFromCursor(c, 2);

        return value;
    }

    public int getNombreAChoisir(){

        String[] whereArgs = {titre, auteur, date};

        Cursor c=myDbHelper.rawQuery("select NOMBRE_A_CHOISIR from SONDAGE_TYPE where titre=? and auteur=? and date=?", whereArgs);

        return Integer.parseInt(myDbHelper.createTabFromCursor(c,1)[0][0]);

    }

    public String [][] getPropositionAndOrdrePref(String pseudo){
        String [] whereArgs = {titre, auteur, date, pseudo};
        Cursor c= myDbHelper.rawQuery("select PROPOSITION, ORDRE_PREF from SONDAGE_RESULTAT where titre=? and auteur=? and date=? and PARTICIPANT=?", whereArgs);
        String [][] tab=myDbHelper.createTabFromCursor(c,2);
        return tab;
    }

    public String[][] getParticipants(){
        String[] whereArgs={titre,date,auteur,"0"};
        Cursor c=myDbHelper.rawQuery("select participant from sondage_participant where titre=? and date=? and auteur=? and participation=? ", whereArgs);
        String[][] value = myDbHelper.createTabFromCursor(c, 1);
        return value;
    }

    public void insertResultat(String pseudo,String answer,int ordrePref){

        SQLiteStatement stmt = db.compileStatement("insert into SONDAGE_RESULTAT values('"
                + this.titre + "','"
                + this.date + "','"
                + this.auteur + "','"
                + pseudo + "','"
                + answer +"',"
                + ordrePref +")");
        stmt.execute();
    }

    public void deleteParticipant(String pseudo){
        SQLiteStatement stmt = db.compileStatement("delete from SONDAGE_PARTICIPANT where " +
                "PARTICIPANT='"+ pseudo +"' AND "
                + "TITRE='"+this.titre + "' AND "
                + "DATE='"+this.date + "' AND "
                + "AUTEUR='"+this.auteur+"'");
        stmt.execute();
    }

    public void insertParticipant(String pseudo){
        SQLiteStatement stmt=db.compileStatement("insert into SONDAGE_PARTICIPANT values('"
                + this.titre + "','"
                + this.date + "','"
                + this.auteur + "','"
                + pseudo + "',1)");
        stmt.execute();
    }

    public int getScore(String proposition,int nbrMax){
        int m=nbrMax + 1;
        int score=0;
        String [] whereArgs = {titre, auteur, date, proposition};
        Cursor c= myDbHelper.rawQuery("select ordre_pref from SONDAGE_RESULTAT where titre=? and auteur=? and date=? and proposition=?", whereArgs);

        String[][] tabScore=myDbHelper.createTabFromCursor(c,1);

        for(int i=0;i<tabScore.length;i++){
            Log.e("debug",tabScore[i][0]);
            score+=(m- Integer.parseInt(tabScore[i][0]));
        }

        return score;
    }

    public String deleteSondage(String pseudo){
        if(pseudo.compareTo(this.auteur)!=0){
            return null;
        }
        String test="";
        String [] whereArgs = {titre, auteur, date};
        Cursor c= myDbHelper.rawQuery("select PROPOSITION from SONDAGE where titre=? and auteur=? and date=?", whereArgs);
        String[][] value=myDbHelper.createTabFromCursor(c,1);
        int maxScore=-1;
        int tempo=-1;
        int position=-1;
        int nbrChoisir=getNombreAChoisir();

        for(int i=0;i<value.length;i++){
            Log.e("debug",value[i][0]);
            tempo=getScore(value[i][0],nbrChoisir);
            if(tempo>maxScore) {
                maxScore=tempo;
                position=i;
            }
            if(tempo==maxScore){
                String choixA=choixAuteur(value[position][0],value[i][0]);
                if(value[position][0].compareTo(choixA)==0){
                    position=i;
                }
            }
        }

        removeSondage();

        return "La reponse finale a ce sondage est: "+value[position][0];
    }

    private String choixAuteur(String prop1,String prop2){
        String [] whereArgs = {titre, auteur, date,auteur,prop1};
        Cursor c= myDbHelper.rawQuery("select ordre_pref from SONDAGE_RESULTAT where titre=? and auteur=? and date=? and participant=? and proposition=?", whereArgs);
        int i=Integer.parseInt(myDbHelper.createTabFromCursor(c,1)[0][0]);

        String [] whereArgs2 = {titre, auteur, date,auteur,prop2};
        c= myDbHelper.rawQuery("select ordre_pref from SONDAGE_RESULTAT where titre=? and auteur=? and date=? and participant=? and proposition=?", whereArgs);
        int j=Integer.parseInt(myDbHelper.createTabFromCursor(c,1)[0][0]);

        if(i>j){
            return prop1;
        } else{
            return prop2;
        }
    }

    private void removeSondage(){
        SQLiteStatement stmt = db.compileStatement("delete from SONDAGE where " +
                  "TITRE='"+this.titre + "' AND "
                + "DATE='"+this.date + "' AND "
                + "AUTEUR='"+this.auteur+"'");
        stmt.execute();

        stmt = db.compileStatement("delete from SONDAGE_PARTICIPANT where " +
                "TITRE='"+this.titre + "' AND "
                + "DATE='"+this.date + "' AND "
                + "AUTEUR='"+this.auteur+"'");
        stmt.execute();

        stmt = db.compileStatement("delete from SONDAGE_RESULTAT where " +
                "TITRE='"+this.titre + "' AND "
                + "DATE='"+this.date + "' AND "
                + "AUTEUR='"+this.auteur+"'");
        stmt.execute();

        stmt = db.compileStatement("delete from SONDAGE_TYPE where " +
                "TITRE='"+this.titre + "' AND "
                + "DATE='"+this.date + "' AND "
                + "AUTEUR='"+this.auteur+"'");
        stmt.execute();
    }

    public boolean aRepondu(String pseudo){
        boolean reponse=false;
        String [][] values=getParticipants();

        for(int i=0;i<values.length;i++){
            if(values[i][0].compareTo(pseudo)==0){
                reponse=true;
            }
        }

        return reponse;
    }

}
