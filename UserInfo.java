package com.letsrouting.com.letsrouting;

/**
 * Created by Raul George Dirlea on 8/03/18.
 */

public class UserInfo {

    private String nombre;
    private String email;
    private String urlPhoto;
    private int numPelisFin;
    private int numSeriesFin;
    private int numLibrosFin;

    public UserInfo() {
    }

    public UserInfo(String nombre, String email, String urlPhoto, int numPelisFin, int numSeriesFin, int numLibrosFin) {
        this.nombre = nombre;
        this.email = email;
        this.urlPhoto = urlPhoto;
        this.numPelisFin = numPelisFin;
        this.numSeriesFin = numSeriesFin;
        this.numLibrosFin = numLibrosFin;
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

    public int getNumPelisFin() {
        return numPelisFin;
    }

    public void setNumPelisFin(int numPelisFin) {
        this.numPelisFin = numPelisFin;
    }

    public int getNumSeriesFin() {
        return numSeriesFin;
    }

    public void setNumSeriesFin(int numSeriesFin) {
        this.numSeriesFin = numSeriesFin;
    }

    public int getNumLibrosFin() {
        return numLibrosFin;
    }

    public void setNumLibrosFin(int numLibrosFin) {
        this.numLibrosFin = numLibrosFin;
    }
}
