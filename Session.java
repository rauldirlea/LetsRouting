package com.letsrouting.com.letsrouting;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by raul on 8/03/18.
 */

public class Session {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Boolean accesoDirectoCreado;


    public Session(Context context){

        this.context = context;
        preferences = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = preferences.edit();

    }


    /****ALL DATA SAVE OF USER****/

    private String cadenaFotoDePerfil;
    private String emailUser;
    private String nameUser;
    private Boolean filtroPeli;
    private Boolean filtroSerie;
    private Boolean filtroLibro;
    private String filtroCategoria;
    private int numPelisFin;
    private int numSeriesFin;
    private int numLibrosFin;



    public Boolean getAccesoDirectoCreado() {
        return preferences.getBoolean("accesoDirectoCreado", false);
    }

    public void setAccesoDirectoCreado(Boolean accesoDirectoCreado) {
        editor.putBoolean("accesoDirectoCreado",accesoDirectoCreado);
        editor.commit();
    }

    public String getCadenaFotoDePerfil() {
        return preferences.getString("cadenaFotoDePerfil", cadenaFotoDePerfil);
    }

    public void setCadenaFotoDePerfil(String cadenaFotoDePerfil) {
        editor.putString("cadenaFotoDePerfil", cadenaFotoDePerfil);
        editor.commit();
    }

    public String getEmailUser() {
        return preferences.getString("emailUser", emailUser);
    }

    public void setEmailUser(String emailUser) {
        editor.putString("emailUser", emailUser);
        editor.commit();
    }

    public String getNameUser() {
        return preferences.getString("nameUser", nameUser);
    }

    public void setNameUser(String nameUser) {
        editor.putString("nameUser", nameUser);
        editor.commit();
    }

    public Boolean getFiltroPeli() {
        return preferences.getBoolean("filtroPeli", false);
    }

    public void setFiltroPeli(Boolean filtroPeli) {
        editor.putBoolean("filtroPeli",filtroPeli);
        editor.commit();
    }

    public Boolean getFiltroSerie() {
        return preferences.getBoolean("filtroSerie", false);
    }

    public void setFiltroSerie(Boolean filtroSerie) {
        editor.putBoolean("filtroSerie",filtroSerie);
        editor.commit();
    }

    public Boolean getFiltroLibro() {
        return preferences.getBoolean("filtroLibro", false);
    }

    public void setFiltroLibro(Boolean filtroLibro) {
        editor.putBoolean("filtroLibro",filtroLibro);
        editor.commit();
    }

    public String getFiltroCategoria() {
        return preferences.getString("filtroCategoria", filtroCategoria);
    }

    public void setFiltroCategoria(String filtroCategoria) {
        editor.putString("filtroCategoria", filtroCategoria);
        editor.commit();
    }

    public void setNumPelisFin(int numPelisFin) {
        editor.putInt("numPelisFin", numPelisFin);
        editor.commit();
    }

    public int getNumPelisFin() {
        return preferences.getInt("numPelisFin", 0);
    }

    public void setNumSeriesFin(int numSeriesFin) {
        editor.putInt("numSeriesFin", numSeriesFin);
        editor.commit();
    }

    public int getNumSeriesFin() {
        return preferences.getInt("numSeriesFin", 0);
    }

    public void setNumLibrosFin(int numLibrosFin) {
        editor.putInt("numLibrosFin", numLibrosFin);
        editor.commit();
    }

    public int getNumLibrosFin() {
        return preferences.getInt("numLibrosFin", 0);
    }
}
