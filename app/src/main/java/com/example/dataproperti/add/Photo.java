package com.example.dataproperti.add;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dataproperti.MainActivity;
import com.example.dataproperti.R;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Photo extends AppCompatActivity implements View.OnClickListener{

    ImageView fotodepan, fotojalan, fotodalam, fotodijual, fotolain, fotolain2;
    ImageView fototambahan;
    Button next, cancel;

    // Camera
    private final int CAMERA = 1;
    private int clickImage;
    // Bitmap
    Bitmap fotodepanthm, fotojalanthm, fotodalamthm, fotodijualthm, fotolainthm, fotolainthm2;
    Task<Uri> fotodepanuri;
    Task<Uri> fotojalanuri;
    Task<Uri> fotodalamuri;
    Task<Uri> fotodijualuri;
    Task<Uri> fotolainuri;
    Task<Uri> fotolainuri2;

    private final static Locale IDNA = new Locale("in", "ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        View include    = findViewById(R.id.scroll_photo);

        // Button
        next    = findViewById(R.id.NextPhoto);
        next.setOnClickListener(v -> {
            startActivity(new Intent(Photo.this, infoLokasi.class));
        });
        cancel = findViewById(R.id.cancel_submit);
        cancel.setOnClickListener(v -> {
            startActivity(new Intent(Photo.this, MainActivity.class));
        });

        /*      Photo Intent        */
        fotodepan       = findViewById(R.id.foto_depan);
        fotojalan       = findViewById(R.id.foto_jalan);
        fotodalam       = findViewById(R.id.foto_dalam);
        fotodijual      = findViewById(R.id.foto_dijual);
        fotolain        = findViewById(R.id.foto_lain);
        fotolain2       = findViewById(R.id.foto_lain1);
        //fototambahan    = findViewById(R.id.foto_tambahan);

        //requestMultiplePermissions();
        // Task
        fotodepan.setOnClickListener(v -> {
            clickImage=1;
            takePhotoFromCamera();
        });
        fotojalan.setOnClickListener(v -> {
            clickImage=2;
            takePhotoFromCamera();
        });
        fotodalam.setOnClickListener(v -> {
            clickImage=3;
            takePhotoFromCamera();
        });
        fotodijual.setOnClickListener(v -> {
            clickImage=4;
            takePhotoFromCamera();
        });
        fotolain.setOnClickListener(v -> {
            clickImage=4;
            takePhotoFromCamera();
        });
        fotolain2.setOnClickListener(v -> {
            clickImage=4;
            takePhotoFromCamera();
        });
    }

    private void takePhotoFromCamera() {
        Intent camera   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            return;
        }
        switch (clickImage){
            case 1:
                if (requestCode == CAMERA){
                    fotodepanthm  = (Bitmap) data.getExtras().get("data");
                    fotodepan.setImageBitmap(fotodepanthm);
                    saveImage(fotodepanthm);
                }
                break;
            case 2:
                if (requestCode == CAMERA){
                    fotojalanthm  = (Bitmap) data.getExtras().get("data");
                    fotojalan.setImageBitmap(fotojalanthm);
                    saveImage(fotojalanthm);
                }
                break;
            case 3:
                if (requestCode == CAMERA){
                    fotodalamthm  = (Bitmap) data.getExtras().get("data");
                    fotodalam.setImageBitmap(fotodalamthm);
                    saveImage(fotodalamthm);
                }
                break;
            case 4:
                if (requestCode == CAMERA){
                    fotodijualthm  = (Bitmap) data.getExtras().get("data");
                    fotodijual.setImageBitmap(fotodijualthm);
                    saveImage(fotodijualthm);
                }
                break;
            case 5:
                if (requestCode == CAMERA){
                    fotolainthm  = (Bitmap) data.getExtras().get("data");
                    fotodijual.setImageBitmap(fotolainthm);
                    saveImage(fotolainthm);
                }
                break;
            case 6:
                if (requestCode == CAMERA){
                    fotolainthm2  = (Bitmap) data.getExtras().get("data");
                    fotodijual.setImageBitmap(fotolainthm2);
                    saveImage(fotolainthm2);
                }
                break;
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + Environment.DIRECTORY_DCIM);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {

    }
}