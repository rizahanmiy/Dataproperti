package com.example.dataproperti.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.dataproperti.R;

public class infoPenawaran extends AppCompatActivity {

    Button next, skip, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_penawaran);


        //Button
        next    = findViewById(R.id.NextPenawaran);
        next.setOnClickListener(v -> {
            startActivity(new Intent(infoPenawaran.this, infoProperti.class));
        });
        skip    = findViewById(R.id.Skip_Penawaran);
        skip.setOnClickListener(v -> {
            startActivity(new Intent(infoPenawaran.this, infoProperti.class));
        });
        back    = findViewById(R.id.Back_Penawaran);
        back.setOnClickListener(v -> {
            startActivity(new Intent(infoPenawaran.this, infoLokasi.class));
        });

    }
}