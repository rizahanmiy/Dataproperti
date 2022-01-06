package com.example.dataproperti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dataproperti.Cloud.DAOUserInfo;
import com.example.dataproperti.Cloud.UserInfo;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    /*/  Firebase  /*/
    private FirebaseAuth fAuth;
    //DatabaseReference databaseReference;
    FirebaseDatabase db;

    /*/  Layout  /*/
    Button btnSignUp;
    EditText Email, Password;
    TextInputLayout Emailcon, Passwordcon;
    TextView SignIn;
    ProgressDialog progressDialog;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*/  Firebase  /*/
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        /*/  Layout  /*/
        btnSignUp = findViewById(R.id.btn_signup);
        Email = findViewById(R.id.emailin);
        Emailcon = findViewById(R.id.emaicon);
        Password = findViewById(R.id.passin);
        Passwordcon = findViewById(R.id.passwordcon);
        SignIn = findViewById(R.id.sign_in);
        SignIn.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(SignUp.this, com.example.dataproperti.SignIn.class));
        });
        progressDialog = new ProgressDialog(this);

        /*/  Task  /*/
        btnSignUp.setOnClickListener(this);
    }


    private void userSignUp() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        DAOUserInfo daoin = new DAOUserInfo();

        if (TextUtils.isEmpty(email)) {
            Emailcon.getErrorContentDescription();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Passwordcon.getErrorContentDescription();
            return;
        }
        //DAOUserInfo daoin = new DAOUserInfo();

        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {

                    String UserMail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    int index = UserMail.indexOf('@');
                    String username = UserMail.substring(0,index);

                    FirebaseUser user = fAuth.getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username).build();

                    assert user != null;
                    user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");

                            UserInfo uin = new UserInfo(username, email,"null", "User");
                            daoin.add(uin);
                        }
                    });

                    Toast.makeText(SignUp.this,
                            "Sign Up Success",
                            Toast.LENGTH_SHORT).show();
                    fAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    finish();
                                    startActivity(new Intent(SignUp.this, MainActivity.class));
                                } else {
                                    progressDialog.setMessage("User Already Registered, please Sign In");
                                    progressDialog.show();
                                    finish();
                                    startActivity(new Intent(SignUp.this, SignIn.class));
                                }
                            });
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SignUp.this,
                                "Sign Up Failed"+e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onClick(View v) {
        if (btnSignUp == v) {
            userSignUp();
        }
    }
}