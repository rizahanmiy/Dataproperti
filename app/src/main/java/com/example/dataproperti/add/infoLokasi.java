package com.example.dataproperti.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dataproperti.R;
import com.google.android.material.textfield.TextInputLayout;

public class infoLokasi extends AppCompatActivity implements View.OnClickListener {

    /* Layout */
    //Informasi Lokasi
    EditText Koordinat, Alamat, Desa, Kelurahan, Kecamatan, Kabupaten, Kota, Provinsi;
    TextInputLayout desacon, kelurahancon, kabupatencon, kotacon;

    Button next, skip, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_lokasi);
        View include    = findViewById(R.id.scroll_infolok);

        //Informasi Lokasi
        Koordinat       =findViewById(R.id.koordinat_input);
        Alamat          =findViewById(R.id.alamat_input);
        Desa            =findViewById(R.id.desa_input);
        desacon         =findViewById(R.id.desa_con);
        Kelurahan       =findViewById(R.id.kelurahan_input);
        kelurahancon    =findViewById(R.id.kelurahan_con);
        Kecamatan       =findViewById(R.id.kecamatan_input);
        Kabupaten       =findViewById(R.id.kabupaten_input);
        kabupatencon    =findViewById(R.id.kabupaten_con);
        Kota            =findViewById(R.id.kota_input);
        kotacon         =findViewById(R.id.kota_con);
        Provinsi        =findViewById(R.id.provinsi_input);

        next    = findViewById(R.id.NextLokasi);
        next.setOnClickListener(v -> {
            String koordinat = Koordinat.getText().toString();
            String alamat    = Alamat.getText().toString();
            String desa      = Desa.getText().toString();
            String kelurahan = Kelurahan.getText().toString();
            String kecamatan = Kecamatan.getText().toString();
            String kabupaten = Kabupaten.getText().toString();
            String kota      = Kota.getText().toString();
            String provinsi  = Provinsi.getText().toString();

            // Create the Intent object of this class Context() to Second_activity class
            Intent infolok = new Intent(getApplicationContext(), infoPenawaran.class);

            // now by putExtra method put the value in key, value pair
            // key is message_key by this key we will receive the value, and put the string

            infolok.putExtra("koordinat", koordinat);
            infolok.putExtra("alamat", alamat);
            infolok.putExtra("desa", desa);
            infolok.putExtra("kelurahan", kelurahan);
            infolok.putExtra("kecamatan", kecamatan);
            infolok.putExtra("kabupaten", kabupaten);
            infolok.putExtra("kota", kota);
            infolok.putExtra("provinsi", provinsi);

            // start the Intent
            startActivity(infolok);
        });
        skip    = findViewById(R.id.Skip_Lokasi);
        skip.setOnClickListener(v -> {
            startActivity(new Intent(infoLokasi.this, infoPenawaran.class));
        });
        back    = findViewById(R.id.Back_Lokasi);
        back.setOnClickListener(v -> {
            startActivity(new Intent(infoLokasi.this, Photo.class));
        });


    }

    @Override
    public void onClick(View v) {
    }
}