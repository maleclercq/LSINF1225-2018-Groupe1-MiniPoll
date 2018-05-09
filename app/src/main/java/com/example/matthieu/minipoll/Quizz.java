package com.example.matthieu.minipoll;



import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class Quizz {//ok en principe
    private String titre;
    private String date;
    private String auteur;
    private DataBaseHelper myDbHelper;
    private SQLiteDatabase db;

    public Quizz(Context context, String titre, String date, String auteur){
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

    public String [][] getQuestion(){//ok ?
        String[] whereArgs = {titre, auteur, date};
        Cursor c = myDbHelper.rawQuery("select QUESTION from QUESTION where titre=? and auteur=? and date=?", whereArgs);
        String [][] value = myDbHelper.createTabFromCursor(c,1);
        return value;
    }

    public String [][] getProposition(String question){//ok ?
        String[] whereArgs = {titre, auteur, date, question};
        Cursor c = myDbHelper.rawQuery("select PROPOSITION from QUESTIONNAIRE_PROPOSITION where titre=? and auteur=? and date=? and question=?", whereArgs);
        String [][] value = myDbHelper.createTabFromCursor(c,1);
        return value;
    }

    public String [][] getAnswer(String question){//ok ?
        String[] whereArgs = {titre, auteur, date, question};
        Cursor c = myDbHelper.rawQuery("select PROPOSITION, PROPOSITION_CORRECTE from QUESTIONNAIRE_PROPOSITION where titre=? and auteur=? and date=? and question=? ", whereArgs);
        String [][] value = myDbHelper.createTabFromCursor(c,2);
        return value;
    }


    public int getNumberOfQuestion(){//ok ?

        String[] whereArgs = {titre, auteur, date};

        Cursor c=myDbHelper.rawQuery("select QUESTION from QUESTION where titre=? and auteur=? and date=?", whereArgs);

        String value [][] = (myDbHelper.createTabFromCursor(c,1));
        return value.length;

    }

    public String [][] getUserAnswer(String participant){
        String[] whereArgs = {titre, auteur, date, participant};
        Cursor c = myDbHelper.rawQuery("select REPONSE_PARTICIPANT from QUESTIONNAIRE_REPONSE where titre=? and auteur=? and date=? and participant=?", whereArgs);
        String [][] value = myDbHelper.createTabFromCursor(c,1);
        return value;
    }

    public String[][] getParticipants(){
        String[] whereArgs={titre,date,auteur};
        Cursor c=myDbHelper.rawQuery("select PARTICIPANT, PARTICIPATION from QUESTIONNAIRE_PARTICIPANT where titre=? and date=? and auteur=?", whereArgs);
        String[][] value = myDbHelper.createTabFromCursor(c, 2);
        return value;
    }

    public void insertResultat(String pseudo,String question,String reponse){

        SQLiteStatement stmt = db.compileStatement("insert into QUESTIONNAIRE_REPONSE values('"
                + this.titre + "','"
                + this.date + "','"
                + this.auteur + "','"
                + pseudo + "','"
                + question +"','"
                + reponse +"')");
        stmt.execute();
    }

    public void deleteParticipant(String pseudo){
        SQLiteStatement stmt = db.compileStatement("delete from QUESTIONNAIRE_PARTICIPANT where " +
                "PARTICIPANT='"+ pseudo +"' AND "
                + "TITRE='"+this.titre + "' AND "
                + "DATE='"+this.date + "' AND "
                + "AUTEUR='"+this.auteur+"'");
        stmt.execute();
    }

    public void insertParticipant(String pseudo){
        SQLiteStatement stmt=db.compileStatement("insert into QUESTIONNAIRE_PARTICIPANT values('"
                + this.titre + "','"
                + this.date + "','"
                + this.auteur + "','"
                + pseudo + "',1)");
        stmt.execute();
    }


    public void deleteQuizz(){

    }


}
