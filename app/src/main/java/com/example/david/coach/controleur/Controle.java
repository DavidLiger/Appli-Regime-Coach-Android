package com.example.david.coach.controleur;

import android.content.Context;
import android.util.Log;

import com.example.david.coach.modele.AccesDistant;
import com.example.david.coach.modele.Profil;
import com.example.david.coach.outils.Serializer;
import com.example.david.coach.vue.CalculActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

public final class Controle {

    private static Controle instance = null;
    private static Profil profil;
    private static String nomFic = "saveprofil";
//    private static AccesLocal accesLocal;
    private static AccesDistant accesDistant;
    private static Context contexte;
    private ArrayList<Profil> lesProfils = new ArrayList<Profil>();


    /**
     * Constructeur privé
     */
    private Controle(){
        super();
    }

    /**
     * Création de l'instance
     * @return instance
     */
    public static final Controle getInstance(Context contexte){
        if(contexte != null){
            Controle.contexte = contexte;
        }
        if(Controle.instance == null){
            Controle.instance = new Controle();
//            accesLocal = new AccesLocal(contexte);
            accesDistant = new AccesDistant();
//            profil = accesLocal.recupDernier();
//            accesDistant.envoi("dernier",new JSONArray());
            accesDistant.envoi("tous",new JSONArray());
//            recupSerialize(contexte);
        }
        return Controle.instance;
    }

    public ArrayList<Profil> getLesProfils() {
        return lesProfils;
    }

    public void setLesProfils(ArrayList<Profil> lesProfils) {
        this.lesProfils = lesProfils;
    }

    /**
     * Création du profil
     * @param poids
     * @param taille en cm
     * @param age
     * @param sexe 1 pour homme et 0 pour femme
     */
    public void creerProfil(Integer poids, Integer taille, Integer age, Integer sexe, Context contexte){
        Profil unProfil = new Profil(new Date(), poids, taille, age, sexe);
        lesProfils.add(unProfil);
        Log.d("date","**************"+(new Date()));
//        accesLocal.ajout(profil);
        accesDistant.envoi("enreg",unProfil.convertToJSONArray());
//        Serializer.serialize(nomFic, profil, contexte);
    }

    /**
     * permet de supprimer un profil dans la base distante et dans la collection
     * @param profil
     */
    public void delProfil(Profil profil){
        accesDistant.envoi("del",profil.convertToJSONArray());
        lesProfils.remove(profil);
    }

    public void setProfil(Profil profil){
        Controle.profil = profil;
 //       ((CalculActivity)contexte).recupProfil();
    }

    /**
     * Récupération img de profil
     * @return img
     */
    public float getImg(){

//        return profil.getImg();
        return  lesProfils.get(lesProfils.size()-1).getImg();
    }

    /**
     * recupération message de profil
     * @return message
     */
    public String getMessage(){

        return lesProfils.get(lesProfils.size()-1).getMessage();
    }

    /**
     * récupération de l'objet sérialisé (le profil)
     * @param contexte
     */
    private static void recupSerialize(Context contexte){
        profil = (Profil) Serializer.deSerialize(nomFic, contexte);
    }

    public Integer getPoids(){
        if(profil == null){
            return  null;
        }else {
            return profil.getPoids();
        }
    }

    public Integer getTaille(){
        if(profil == null){
            return  null;
        }else {
            return profil.getTaille();
        }
    }

    public Integer getAge(){
        if(profil == null){
            return  null;
        }else {
            return profil.getAge();
        }
    }

    public Integer getSexe(){
        if(profil == null){
            return  null;
        }else {
            return profil.getSexe();
        }
    }
}
