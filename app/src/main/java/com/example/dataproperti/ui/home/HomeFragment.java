package com.example.dataproperti.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dataproperti.R;
import com.example.dataproperti.SignIn;
import com.example.dataproperti.UserConfirmation;
import com.example.dataproperti.add.Photo;
import com.example.dataproperti.Cloud.DAODatprop;
import com.example.dataproperti.Cloud.Datprop;
import com.example.dataproperti.Cloud.RVAdapter;
import com.example.dataproperti.Cloud.UserInfo;
import com.example.dataproperti.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {

    /*/ Layout /*/
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    RVAdapter adapter;
    DAODatprop dao;
    boolean isLoading = false;
    String key = null;
    ImageView adddata;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String username = fAuth.getCurrentUser().getDisplayName();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(username).child("User Info");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo uinfo = snapshot.getValue(UserInfo.class);
                String Role = uinfo.getRole();
                if (Role.equals("null")){
                    startActivity(new Intent(HomeFragment.this.getContext(), UserConfirmation.class));
                }else if (!Role.isEmpty()){
                    onResume();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeFragment.this.getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeFragment.this.getContext(), SignIn.class));
            }
        });

        com.example.dataproperti.databinding.FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        swipeRefreshLayout = root.findViewById(R.id.swipe);
        recyclerView = root.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(HomeFragment.this.getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new RVAdapter(HomeFragment.this.getContext());
        recyclerView.setAdapter(adapter);

        dao = new DAODatprop();

        adddata = root.findViewById(R.id.fab_add);
        adddata.setOnClickListener(this);

        loaddata();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (!isLoading) {
                    isLoading = true;
                    loaddata();
                }
/*                if (totalItem < lastVisible+3) {
                    if (!isLoading) {
                        isLoading = true;
                        loaddata();
                    }
                }*/
            }
        });

        return root;
    }

    private void loaddata() {

        swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Datprop> dtps = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Datprop dtp = data.getValue(Datprop.class);
                    dtp.setKey(data.getKey());
                    dtps.add(dtp);
                    key = data.getKey();

                }
                adapter.setItems(dtps);
                adapter.notifyDataSetChanged();
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (adddata == v) {
            startActivity(new Intent(HomeFragment.this.getActivity(), Photo.class));
        }
    }
}