package com.example.matthieu.minipoll;

import java.io.Serializable;

/**
 * Created by matthieu on 26/03/18.
 */

public class Utilisateur implements Serializable {
    public String id;
    public String nom;
    public String prenom;
    public String mdp;
    public String email;
    public String photo;

    public Utilisateur(String id,String nom,String prenom,String mdp,String email,String photo){
        this.id=id;
        this.nom=nom;
        this.prenom=prenom;
        this.mdp=mdp;
        this.email=email;
        this.photo=photo;
    }
}
