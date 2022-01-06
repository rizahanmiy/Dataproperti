package com.example.dataproperti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dataproperti.Cloud.DAOUserInfo;
import com.example.dataproperti.Cloud.UserInfo;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class UserConfirmation extends AppCompatActivity {

    EditText usernameinput, phone;
    TextInputLayout usernamecon, phonecon;
    String email;
    Button submit;

    DatabaseReference databaseReference;
    UserInfo uin_edit;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_confirmation);

        usernameinput = findViewById(R.id.uinput);
        usernamecon = findViewById(R.id.user_con);
        phone = findViewById(R.id.ph_input);
        phonecon = findViewById(R.id.ph_con);
        submit = findViewById(R.id.submit_conf);

        fAuth = FirebaseAuth.getInstance();

        email = fAuth.getCurrentUser().getEmail();
        int index = email.indexOf('@');
        String username = email.substring(0,index);

        DAOUserInfo dao = new DAOUserInfo();
        uin_edit = (UserInfo) getIntent().getSerializableExtra("EDIT");
        if (uin_edit !=null)
        {
            submit.setText("UPDATE");

            usernameinput.setText(uin_edit.getUsername());
            usernameinput.setTextColor(Color.GRAY);

            phone.setText(uin_edit.getPhoneNumber());

            Toast.makeText(this, "Email :"+uin_edit.getEmail(), Toast.LENGTH_SHORT).show();
        }
        else {
            usernameinput.setText(username);
            usernameinput.setTextColor(Color.GRAY);
            submit.setText("SUBMIT");
        }

        submit.setOnClickListener(a-> {

            String uname = usernameinput.getText().toString().trim();
            String pho = phone.getText().toString().trim();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest userupdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName(uname).build();

            user.updateProfile(userupdate).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("TAG", "User profile updated.");

                    UserInfo uin = new UserInfo(usernameinput.getText().toString().trim(), email.trim(), phone.getText().toString().trim(), "User");
                    if (uin_edit == null) {
                        dao.add(uin)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(UserConfirmation.this, "Submitted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(UserConfirmation.this, MainActivity.class));
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(UserConfirmation.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("Username", usernameinput.getText().toString());
                        hashMap.put("Email", email.trim());
                        hashMap.put("Phone", phone.getText().toString());
                        //hashMap.put("Role", "User");

                        dao.update(uin_edit.getKey(), hashMap).addOnSuccessListener(suc -> {
                            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }).addOnFailureListener(er -> {
                            Toast.makeText(this, "" + er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        });

    }
}