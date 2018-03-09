package com.letsrouting.com.letsrouting;

/**
 * Created by Raul George Dirlea on 8/03/18.
 */

public class UserInfo {

    private String nombre;
    private String email;
    private String urlPhoto;

    public UserInfo() {
    }

    public UserInfo(String nombre, String email, String urlPhoto) {
        this.nombre = nombre;
        this.email = email;
        this.urlPhoto = urlPhoto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
