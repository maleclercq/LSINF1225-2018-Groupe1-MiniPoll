package com.example.matthieu.minipoll;

import java.io.Serializable;

/**
 * Created by matthieu on 26/03/18.
 */

public class Utilisateur implements Serializable {
    public String pseudo;
    public String nom;
    public String prenom;
    public String mdp;
    public String email;
    public String photo;

    public Utilisateur(String pseudo,String nom,String prenom,String mdp,String email,String photo){
        this.pseudo=pseudo;
        this.nom=nom;
        this.prenom=prenom;
        this.mdp=mdp;
        this.email=email;
        this.photo=photo;
    }
}
