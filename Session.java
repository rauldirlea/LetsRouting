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

    private String partidos_guardados;
    private String cadenaFotoDePerfil;
    private  String emailUser;
    private  String niknameUser;
    private String isVip;
    private String dataVipExpired;

    public String getPartidos_guardados() {
        return preferences.getString("partidos_guardados", partidos_guardados);
    }

    public void setPartidos_guardados(String partidos_guardados) {
        editor.putString("partidos_guardados", partidos_guardados);
        editor.commit();
    }


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

    public String getNikanmeUser() {
        return preferences.getString("niknameUser", niknameUser);
    }

    public void setNikanmeUser(String niknameUser) {
        editor.putString("niknameUser", niknameUser);
        editor.commit();
    }

    public String getIsVip() {
        return preferences.getString("isVip", isVip);
    }

    public void setIsVip(String isVip) {
        editor.putString("isVip", isVip);
        editor.commit();
    }

    public String getDataVipExpired() {
        return preferences.getString("dataVipExpired", dataVipExpired);
    }

    public void setDataVipExpired(String dataVipExpired) {
        editor.putString("dataVipExpired", dataVipExpired);
        editor.commit();
    }


}
