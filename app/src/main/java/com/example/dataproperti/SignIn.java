package com.example.dataproperti;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.dataproperti.Cloud.DAOUserInfo;
import com.example.dataproperti.Cloud.UserInfo;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    //App Permissions
    String[] appPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final int PERMISSIONS_REQUEST_CODE = 1;

    //Firebase
    FirebaseAnalytics analytics;
    FirebaseAuth fAuth;
    DatabaseReference mDatabase;

    //Layout
    EditText emailIn, passwordIn;
    TextInputLayout emailCon, passwordCon;
    TextView forgotPass;
    Button loginBtn;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        //Checking and Requesting Permissions
        checkandRequestPermissions();
/*        if (checkandRequestPermissions()) {
            onResume();
        }*/

        //Firebase
        analytics = FirebaseAnalytics.getInstance(this);
        fAuth = FirebaseAuth.getInstance();

        //Layout
        emailCon = findViewById(R.id.email_con);
        emailIn = findViewById(R.id.email_input);
        passwordCon = findViewById(R.id.password_con);
        passwordIn = findViewById(R.id.password_input);
        forgotPass = findViewById(R.id.forgot_pass);
        loginBtn = findViewById(R.id.login_btn);
        progressDialog = new ProgressDialog(this);

        //forgotPass.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    // Sign In Authentication
    private void userSignIn() {
        String email = emailIn.getText().toString().trim();
        String password = passwordIn.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailCon.getErrorContentDescription();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordCon.getErrorContentDescription();
            return;
        }

        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){
                        UserCheck();
                    }else{
                        startActivity(new Intent(SignIn.this, MainActivity.class));
                    }
                    //FirebaseUser user = fAuth.getCurrentUser();

/*                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usernamein.getText().toString().trim()).build();*/
/*                    user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                            //finish();
                            //startActivity(new Intent(SignIn.this, MainActivity.class));
                        }
                    });*/
                }).addOnFailureListener(this, e->{
                    progressDialog.setMessage("You're not registered, please contact administrator to sign you up");
                    progressDialog.show();
                    startActivity(new Intent(SignIn.this, SignUp.class));
                    progressDialog.dismiss();
                    finish();
        });
    }

    // Permissions Check and Request code below
    public boolean checkandRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]),
                    PERMISSIONS_REQUEST_CODE
            );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;

            //Gather permissions grans results
            for (int i = 0; i < grantResults.length; i++) {
                // Add only permissions which are denied
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                onResume();
                // Continue app
            } else {
                for (Map.Entry<String, Integer> entry : permissionResults.entrySet()) {
                    String permName = entry.getKey();
                    //int permResult = entry.getValue();

                    // permissions is denied (this is the first time, when "never ask again" is not checked)
                    // so ask again explaining the usage of permission
                    // shouldShowRequestPermissionRationale will return true
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        // show dialog of explaination
                        showDialog("", "This App needs Location and Storage permissions to work without problems.",
                                "Yes, Grant Permissions",
                                (dialog, i) ->
                                {
                                    dialog.dismiss();
                                    checkandRequestPermissions();
                                }, "No, Exit App", (dialog, i) -> {
                                    dialog.dismiss();
                                    finish();
                                }, false);
                    }
                    //permissions is denied (and never ask again is checked)
                    //shouldShowRequestPermissionRationale will return false
                    else {
                        //ask user to go to settings and manually allow permissions
                        showDialog("",
                                "You have denied some permissions, please allow all permissions at [Setting] > [Permissions]",
                                "Grant All Permissions",
                                (dialog, i) -> {
                                    dialog.dismiss();
                                    // Go to app settings
                                    openSettings();
                                },
                                "No, Exit App", (dialog, i) -> {
                                    dialog.dismiss();
                                    finish();
                                }, false);
                        break;
                    }
                }
            }
        }
    }


    public void showDialog(String title, String msg, String positiveLabel,
                           DialogInterface.OnClickListener positiveOnClick,
                           String negativeLabel, DialogInterface.OnClickListener negativeOnClick,
                           boolean isCancelAble) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setCancelable(isCancelAble);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnClick);
        builder.setNegativeButton(negativeLabel, negativeOnClick);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    public void UserCheck(){
        String email = fAuth.getCurrentUser().getEmail();
        String username = fAuth.getCurrentUser().getDisplayName();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(username).child("User Info");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    DAOUserInfo daoin = new DAOUserInfo();
                    UserInfo uin = new UserInfo(username, email,"null", "User");
                    daoin.add(uin);
                    startActivity(new Intent(SignIn.this, MainActivity.class));
                    return;
                } else if (snapshot.exists()) {
                    UserInfo uinfo = snapshot.getValue(UserInfo.class);
                    if (uinfo==null){
                        DAOUserInfo daoin = new DAOUserInfo();
                        UserInfo uin = new UserInfo(username, email,"null", "User");
                        daoin.add(uin);
                        startActivity(new Intent(SignIn.this, MainActivity.class));
                        return;
                    }
                    else if (uinfo!=null){
                        String Role = uinfo.getRole();
                        if (Role==null){
                            DAOUserInfo daoin = new DAOUserInfo();
                            UserInfo uin = new UserInfo(username, email,"null", "User");
                            daoin.add(uin);
                            startActivity(new Intent(SignIn.this, MainActivity.class));
                            return;
                        }
                        if (Role!=null){
                            if (Role.equals("User")){
                                startActivity(new Intent(SignIn.this, MainActivity.class));
                                return;
                            }else {
                                if (Role.equals("Admin")){
                                    startActivity(new Intent(SignIn.this, UserConfirmation.class));
                                }
                                return;
                            }
                        }
                    }
                }
                return;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                startActivity(new Intent(SignIn.this, MainActivity.class));
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (loginBtn == v) {
            userSignIn();
        }
    }

}