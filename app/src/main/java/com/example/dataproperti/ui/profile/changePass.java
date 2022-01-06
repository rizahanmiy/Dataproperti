package com.example.dataproperti.ui.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dataproperti.MainActivity;
import com.example.dataproperti.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class changePass extends AppCompatActivity implements View.OnClickListener {

    /*  Firebase  */
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private FirebaseUser fUser;
    AuthCredential credential;
    /*
    private FirebaseAnalytics Analytics;
    private FirebaseDatabase fRealData;
    */

    /* Layout */
    Button ChangeBtn;
    TextView Confirmbar;
    EditText OldPass, NewPass, ConfirmPass;

    /* Container */
    TextInputLayout oldpasscon, newpasscon, confirmpasscon;

    /* String */
    String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        /* Firebase */
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fUser = fAuth.getCurrentUser();
        Email = fUser.getEmail();

        /* Layout */
        ChangeBtn = findViewById(R.id.forgot_btn);
        Confirmbar = findViewById(R.id.confirmation_bar);
        OldPass = findViewById(R.id.oldpassword);
        NewPass = findViewById(R.id.newpassword);
        ConfirmPass = findViewById(R.id.confirmpassword);

        /* Container */
        oldpasscon = findViewById(R.id.oldpass_con);
        newpasscon = findViewById(R.id.newpass_con);
        confirmpasscon = findViewById(R.id.confirmpass_con);

        /* Task */
        ChangeBtn.setOnClickListener(this);
    }

    private void changepass() {
        String email = Email;
        String oldpass = OldPass.getText().toString().trim();
        String newpass = NewPass.getText().toString().trim();
        String confirmpass = ConfirmPass.getText().toString().trim();

        if (TextUtils.isEmpty(oldpass)) {
            oldpasscon.getBoxStrokeErrorColor();
            Toast.makeText(changePass.this, "Please input your current password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newpass)) {
            newpasscon.getBoxStrokeErrorColor();
            Toast.makeText(changePass.this, "Please input your new password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmpass)) {
            confirmpasscon.getBoxStrokeErrorColor();
            Toast.makeText(changePass.this, "Please confirm your new password", Toast.LENGTH_SHORT).show();
            return;
        }

        credential = EmailAuthProvider.getCredential(email, oldpass);

        fUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            fUser.updatePassword(newpass)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Confirmbar.setText("Password Changed !");
                                                Toast.makeText(changePass.this, "Password Changed !", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Confirmbar.setText("Password hasn't change !");
                                            Confirmbar.setTextColor(Color.RED);
                                            Toast.makeText(changePass.this, "Password hasn't change !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            //startActivity(new Intent(changePass.this, Dashboard.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Confirmbar.setText("Failed to update your password, credential not match");
                        Confirmbar.setTextColor(Color.RED);
                        Toast.makeText(changePass.this, "Failed to update your password, credential not match", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (ChangeBtn == v) {
            changepass();
            finish();
            startActivity(new Intent(changePass.this, MainActivity.class));
        }
    }
}
