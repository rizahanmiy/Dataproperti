package com.example.dataproperti.Cloud;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class DAOUserInfo {
    private final DatabaseReference databaseReference;
    FirebaseAuth fAuth;

    public DAOUserInfo() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        String username = Objects.requireNonNull(fAuth.getCurrentUser()).getDisplayName();

        databaseReference = db.getReference().child(username).child("User Info");
    }

    public Task<Void> add(UserInfo uin) {

        return databaseReference.setValue(uin);
        //return databaseReference.child(username).child("User Info").setValue(uin);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key) {
        return databaseReference.child(key).removeValue();
    }
}
