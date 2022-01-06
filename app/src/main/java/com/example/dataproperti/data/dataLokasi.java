package com.example.dataproperti.data;

import java.io.Serializable;

public class dataLokasi implements Serializable {

    private String timestamp, Koordinat, Alamat, Desa, Kelurahan, Kecamatan, Kabupaten, Kota, Provinsi;

    public dataLokasi() {
    }

    public dataLokasi(String timestamp, String Koordinat, String Alamat, String Desa, String Kelurahan, String Kecamatan, String Kabupaten, String Kota, String Provinsi) {
        this.timestamp = timestamp;
        this.Koordinat = Koordinat;
        this.Alamat = Alamat;
        this.Desa = Desa;
        this.Kelurahan = Kelurahan;
        this.Kecamatan = Kecamatan;
        this.Kabupaten = Kabupaten;
        this.Kota = Kota;
        this.Provinsi = Provinsi;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getKoordinat() {
        return Koordinat;
    }

    public void setKoordinat(String koordinat) {
        Koordinat = koordinat;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getDesa() {
        return Desa;
    }

    public void setDesa(String desa) {
        Desa = desa;
    }

    public String getKelurahan() {
        return Kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        Kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return Kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        Kecamatan = kecamatan;
    }

    public String getKabupaten() {
        return Kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        Kabupaten = kabupaten;
    }

    public String getKota() {
        return Kota;
    }

    public void setKota(String kota) {
        Kota = kota;
    }

    public String getProvinsi() {
        return Provinsi;
    }

    public void setProvinsi(String provinsi) {
        Provinsi = provinsi;
    }
}
