package com.example.matthieu.minipoll;

import android.content.Context;
import android.database.Cursor;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by matthieu on 26/03/18.
 */

public class Utilisateur implements Serializable {
    private String pseudo;
    public String nom;
    public String prenom;
    public String mdp;
    public String email;
    public String photo;

    public Utilisateur(String pseudo,String nom,String prenom,String mdp,String email,String photo){
        this.setPseudo(pseudo);
        this.nom=nom;
        this.prenom=prenom;
        this.mdp=mdp;
        this.email=email;
        this.photo=photo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public ArrayList<String> getFriends(DataBaseHelper myDbHelper){
        ArrayList<String> friends=new ArrayList<String>();

        String [] whereArgs={this.getPseudo()};
        Cursor c=myDbHelper.rawQuery("select AMI from AMI where UTILISATEUR=?",whereArgs);

        String [][] value=myDbHelper.createTabFromCursor(c,1);

        for(int i=0;i<value.length;i++){
            friends.add(value[i][0]);
        }

        return friends;
    }

    public ArrayList<String> getSuggestion(DataBaseHelper myDbHelper){
        ArrayList<String> suggest=new ArrayList<String>();

        String [] whereArgs={this.getPseudo()};
        Cursor c=myDbHelper.rawQuery("select id from utilisateur where id!=?",whereArgs);

        String [][] allU=myDbHelper.createTabFromCursor(c,1);

        c=myDbHelper.rawQuery("select ami from ami where utilisateur=?",whereArgs);

        String [][] allF=myDbHelper.createTabFromCursor(c,1);

        boolean contains;
        for(int i=0;i<allU.length;i++){
            contains=false;

            for(int j=0;j<allF.length;j++){//regarde si cet ami est deja dans sa liste d'ami
                if(allU[i][0].compareTo(allF[j][0])==0){
                    contains=true;
                }
            }

            if(!contains){ //si il n'est pas dans sa liste d'ami alors il le rajoute a la liste de suggestion
                suggest.add(allU[i][0]);
            }
        }

        return suggest;
    }
}
