package com.example.david.coach.modele;

import android.util.Log;

import com.example.david.coach.controleur.Controle;
import com.example.david.coach.outils.AccesHTTP;
import com.example.david.coach.outils.AsyncResponse;
import com.example.david.coach.outils.MesOutils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class AccesDistant implements AsyncResponse {

    // constante
    private static final String SERVERADDR = "http://192.168.0.31/coach/serveurcoach.php";
    private Controle controle;

    public AccesDistant() {
        controle = Controle.getInstance(null);
    }
    /**
     * retour du serveur distant
     * @param output
     */
    @Override
    public void processFinish(String output){
        Log.d("serveur", "************"+output);
        // découpage du message reçu
        String[] message = output.split("%");
        // dans message[0] : "enreg", "dernier", "Erreur !"
        // dans message[1] : reste du message

        // s'il y a 2 cases
        if(message.length>1){
            if(message[0].equals("enreg")) {
                Log.d("enreg","***********"+message[1]);
            }else{
                if(message[0].equals("dernier")){
                    Log.d("dernier","***********"+message[1]);
                    try {
                        JSONObject info = new JSONObject(message[1]);
                        Integer poids = info.getInt("poids");
                        Integer taille = info.getInt("taille");
                        Integer age = info.getInt("age");
                        Integer sexe = info.getInt("sexe");
                        String datemesure = info.getString("datemesure");
                        Date date = MesOutils.convertStringToDate(datemesure,"yyyy-MM-dd hh:mm:ss");
                        Log.d("sate mysql","*************"+date);
                        Profil profil = new Profil(date,poids,taille,age,sexe);
                        controle.setProfil(profil);
                    } catch (JSONException e) {
                        Log.d("erreur","conversion JSON impossible"+e.toString());
                    }
                }else{
                    if(message[0].equals("tous")) {
                        Log.d("tous", "***********" + message[1]);
                        try {
                            JSONArray jSonInfo = new JSONArray(message[1]);
                            ArrayList<Profil> lesProfils = new ArrayList<Profil>();
                            for(int k=0; k<jSonInfo.length();k++){
                                JSONObject info = new JSONObject(jSonInfo.get(k).toString());
                                Integer poids = info.getInt("poids");
                                Integer taille = info.getInt("taille");
                                Integer age = info.getInt("age");
                                Integer sexe = info.getInt("sexe");
                                String datemesure = info.getString("datemesure");
                                Date date = MesOutils.convertStringToDate(datemesure,"yyyy-MM-dd hh:mm:ss");
                                Profil profil = new Profil(date,poids,taille,age,sexe);
                                lesProfils.add(profil);
                            }
                            controle.setLesProfils(lesProfils);
                        } catch (JSONException e) {
                            Log.d("erreur","conversion JSON impossible"+e.toString());
                        }
                    }else {
                        if (message[0].equals("Erreur !")) {
                            Log.d("Erreur !", "***********" + message[1]);
                        }
                    }
                }
            }

        }
    }

    public void envoi(String operation, JSONArray lesDonneesJSON){
        AccesHTTP accesDonnees = new AccesHTTP();
        // lien de délégation
        accesDonnees.delegate = this;
        // ajout de parametres
        accesDonnees.addParam("operation", operation);
        accesDonnees.addParam("lesdonnees", lesDonneesJSON.toString());
        // appel au serveur
        accesDonnees.execute(SERVERADDR);
    }
}
