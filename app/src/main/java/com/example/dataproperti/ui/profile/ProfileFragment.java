package com.example.dataproperti.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.dataproperti.R;
import com.example.dataproperti.SignIn;
import com.example.dataproperti.Cloud.UserInfo;
import com.example.dataproperti.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private FragmentProfileBinding binding;

    FirebaseAuth fAuth;

    TextView Hello, Mail, Role;
    Button Change, Download, UserManagement, Logout;
    String email, user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fAuth = FirebaseAuth.getInstance();

        /* Layout */
        Hello = root.findViewById(R.id.hello_user);
        Mail = root.findViewById(R.id.user_mail);
        Role = root.findViewById(R.id.user_role);

        Change = root.findViewById(R.id.change_pass);
        Download = root.findViewById(R.id.download_data);
        UserManagement = root.findViewById(R.id.user_management);
        Logout = root.findViewById(R.id.btn_logout);

        /*/  Task  /*/
        String username = Objects.requireNonNull(fAuth.getCurrentUser()).getDisplayName();
        Hello.setText("Hello, "+username);

        //-- Role
        email = fAuth.getCurrentUser().getEmail();
        Mail.setText(email);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(username).child("User Info");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo uinfo = snapshot.getValue(UserInfo.class);
                String getRole = uinfo.getRole();
                Role.setText(getRole);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileFragment.this.getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileFragment.this.getContext(), SignIn.class));
            }
        });

        Change.setOnClickListener(this);
        Logout.setOnClickListener(this);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (Change == v) {
            startActivity(new Intent(ProfileFragment.this.getActivity(), changePass.class));
        }
        if (Logout == v) {
            fAuth.signOut();
            getParentFragment().getActivity().finish();
            startActivity(new Intent(ProfileFragment.this.getActivity(), SignIn.class));
        }
    }
}