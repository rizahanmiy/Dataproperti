package com.example.dataproperti.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.dataproperti.R;

public class infoProperti2 extends AppCompatActivity {

    Button next, skip, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_properti2);

        //Button
        next    = findViewById(R.id.NextProp2);
        next.setOnClickListener(v -> {
            startActivity(new Intent(infoProperti2.this, Review.class));
        });
        skip    = findViewById(R.id.Skip_Prop2);
        skip.setOnClickListener(v -> {
            startActivity(new Intent(infoProperti2.this, Review.class));
        });
        back    = findViewById(R.id.Back_Prop2);
        back.setOnClickListener(v -> {
            startActivity(new Intent(infoProperti2.this, infoProperti.class));
        });
    }
}