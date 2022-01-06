package com.example.dataproperti.Cloud;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dataproperti.R;

public class DatpropVH extends RecyclerView.ViewHolder {
    public TextView alamat_in, koordinat_in, pilihprop_in, options_btn;
    public String time;

    public DatpropVH(@NonNull View itemView) {
        super(itemView);
        alamat_in = itemView.findViewById(R.id.alamat_item);
        koordinat_in = itemView.findViewById(R.id.koordinat_item);
        pilihprop_in = itemView.findViewById(R.id.jenisprop_item);
        options_btn = itemView.findViewById(R.id.options);

    }
}
