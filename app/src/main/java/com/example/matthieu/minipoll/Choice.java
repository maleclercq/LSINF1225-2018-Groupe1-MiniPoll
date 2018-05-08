package com.example.matthieu.minipoll;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.io.IOException;

public class Choice {
    private String titre;
    private String date;
    private String auteur;
    private String question;
    private DataBaseHelper myDbHelper;
    private SQLiteDatabase db;

    public Choice(Context context, String titre, String date, String auteur) {
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

        this.titre = titre;
        this.date = date;
        this.auteur = auteur;
        this.question = question;

        this.db = myDbHelper.getWritableDatabase();
    }

    public void insertAnswer(String pseudo, String answer, String first, String second) {
        if (answer.compareTo(first) == 0) {
            SQLiteStatement stmt = db.compileStatement("insert into CHOIX_REPONSE values('"
                    + this.titre + "','"
                    + this.date + "','"
                    + this.auteur + "','"
                    + this.question + "','"
                    + first + "','"
                    + pseudo + "')");
            stmt.execute();
        }
        if (answer.compareTo(second) == 0) {
            SQLiteStatement stmt = db.compileStatement("insert into CHOIX_REPONSE values('"
                    + this.titre + "','"
                    + this.date + "','"
                    + this.auteur + "','"
                    + this.question + "','"
                    + second + "','"
                    + pseudo + "')");
            stmt.execute();
        }

    }

    public void chooseParticipant(String pseudo) {
        SQLiteStatement stmt = db.compileStatement("insert into CHOIX_PARTICIPANT values('"
                + this.titre + "','"
                + this.date + "','"
                + this.auteur + "','"
                + this.question + "','"
                + pseudo + "','"
                + 0 + "')");
        stmt.execute();

    }

    public String[][] getPropositions() {
        String[] whereArgs = {titre, date, auteur};
        Cursor c = myDbHelper.rawQuery("select PROPOSITION,QUESTION from CHOIX where titre=? and date=? and auteur=?", whereArgs);
        String[][] proposition =  myDbHelper.createTabFromCursor(c, 2);
        return proposition;
    }
}
