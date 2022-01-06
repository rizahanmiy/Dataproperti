package com.example.dataproperti.Cloud;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Objects;

public class DAODatprop {
    private final DatabaseReference databaseReference;
    FirebaseAuth fAuth;

    public DAODatprop() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        //String email = Objects.requireNonNull(fAuth.getCurrentUser()).getEmail();
        String username = Objects.requireNonNull(fAuth.getCurrentUser()).getDisplayName();
        databaseReference = db.getReference(username).child("User Data");
    }

    public Task<Void> add(Datprop dtp) {
        return databaseReference.push().setValue(dtp);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> remove(String key) {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key){
        if (key == null) {
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.startAfter(key);
    }
    
/*    public Query get(String key) {

        //return databaseReference.orderByKey();
        if (key == null) {
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.startAfter(key).limitToFirst(8);
    }*/
}
