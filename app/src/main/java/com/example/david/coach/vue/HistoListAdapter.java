package com.example.david.coach.vue;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.david.coach.R;
import com.example.david.coach.controleur.Controle;
import com.example.david.coach.modele.Profil;
import com.example.david.coach.outils.MesOutils;

import java.util.ArrayList;

public class HistoListAdapter extends BaseAdapter{

    private ArrayList<Profil> lesProfils;
    private LayoutInflater inflater;
    private Controle controle;
    private Context contexte;

    public HistoListAdapter(Context contexte, ArrayList<Profil> lesProfils){
        this.lesProfils = lesProfils;
        this.contexte = contexte;
        this.inflater = LayoutInflater.from(contexte);
        this.controle = Controle.getInstance(null);
    }
    /**
     * retourne le nombre de lignes
     * @return
     */
    @Override
    public int getCount() {
        return lesProfils.size();
    }

    /**
     * retourne l'item de la ligne actuelle
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return lesProfils.get(position);
    }

    /**
     * retourne un indice par rapport à la ligne actuelle
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * retourne la ligne (view) formatée avec gestion des evenements
     * @param position
     * @param convertView
     * @param parent
     * @return
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // déclaration d'un holder
        ViewHolder holder;
        // si la ligne n'existe pas encore
        if(convertView == null){
            holder = new ViewHolder();
        // la ligne est construite avec un formatage (inflater) relié à layout_list_histo
            convertView = inflater.inflate(R.layout.layout_liste_histo,null);
            // chaque propriété du holder est relié à une propriété graphique
            holder.txtListDate = (TextView) convertView.findViewById(R.id.txtListDate);
            holder.txtListIMG = (TextView) convertView.findViewById(R.id.txtListIMG);
            holder.btnListSuppr = (ImageButton) convertView.findViewById(R.id.btnListSuppr);
            // affecter le holder à la vue
            convertView.setTag(holder);
        }else{
            // récupération du holder dans la ligne existante
            holder = (ViewHolder) convertView.getTag();
        }
        // valorisation du contenu du holder (donc la ligne)
        holder.txtListDate.setText(MesOutils.convertDateToString(lesProfils.get(position).getDateMesure()));
        holder.txtListIMG.setText(MesOutils.format2Decimal(lesProfils.get(position).getImg()));
        holder.btnListSuppr.setTag(position);
        // clic sur la croix pour supprimer le profil enregistré
        holder.btnListSuppr.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                int position = (int)v.getTag();
                // demande la suppression au controleur
                controle.delProfil(lesProfils.get(position));
                // rafaraichir la liste
                notifyDataSetChanged();
            }
        });
        holder.txtListDate.setTag(position);
        // clic sur le reste de la ligne pour afficher le profil dans calculActivity
        holder.txtListDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                int position = (int)v.getTag();
                // demande l'affichage du profil dans CalculActivity
                ((HistoActivity)contexte).afficheProfil(lesProfils.get(position));
            }
        });
        holder.txtListIMG.setTag(position);
        // clic sur le reste de la ligne pour afficher le profil dans calculActivity
        holder.txtListIMG.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                int position = (int)v.getTag();
                // demande l'affichage du profil dans CalculActivity
                ((HistoActivity)contexte).afficheProfil(lesProfils.get(position));
            }
        });
        return convertView;
    }

    private class ViewHolder{
        ImageButton btnListSuppr;
        TextView txtListDate;
        TextView txtListIMG;
    }
}
