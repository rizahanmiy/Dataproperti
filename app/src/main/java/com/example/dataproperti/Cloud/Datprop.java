package com.example.dataproperti.Cloud;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Datprop implements Serializable {

    @Exclude
    private String key;
    private String Alamat;
    private String JenisProp;
    private String Koordinat;
    private String timestamp;

    public Datprop() {
    }

    public Datprop(String Alamat, String JenisProp, String Koordinat, String timestamp) {
        this.Alamat = Alamat;
        this.JenisProp = JenisProp;
        this.Koordinat = Koordinat;
        this.timestamp = timestamp;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getJenisProp() {
        return JenisProp;
    }

    public void setJenisProp(String jenisProp) {
        JenisProp = jenisProp;
    }

    public String getKoordinat() {
        return Koordinat;
    }

    public void setKoordinat(String koordinat) {
        Koordinat = koordinat;
    }

    public String getKey() { return key; }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
