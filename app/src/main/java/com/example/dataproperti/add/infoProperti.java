package com.example.dataproperti.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.dataproperti.R;

public class infoProperti extends AppCompatActivity {

    Button next, skip, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_properti);

        //Button
        next    = findViewById(R.id.NextProp);
        next.setOnClickListener(v -> {
            startActivity(new Intent(infoProperti.this, infoProperti2.class));
        });
        skip    = findViewById(R.id.Skip_Prop);
        skip.setOnClickListener(v -> {
            startActivity(new Intent(infoProperti.this, infoProperti2.class));
        });
        back    = findViewById(R.id.Back_Prop);
        back.setOnClickListener(v -> {
            startActivity(new Intent(infoProperti.this, infoPenawaran.class));
        });
    }
}