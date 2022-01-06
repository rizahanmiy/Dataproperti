package com.example.dataproperti.submit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.dataproperti.MainActivity;
import com.example.dataproperti.R;
import com.example.dataproperti.Cloud.DAODatprop;
import com.example.dataproperti.Cloud.Datprop;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainSubmit extends AppCompatActivity implements LocationListener{

    private FirebaseFirestore fStore;
    private StorageReference sStore;
    private String email;
    private String username;
    Datprop dtp_edit;
    TextView timeid;
    String timestamp, timestampkey;

    /* Excel */
    private File filepath;

    /* Location */
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;

    List<Address> addresses;
    String coordinates;

    /* Tanggal */
    private final static Locale IDNA = new Locale("in", "ID");

    /* Layout */
    //Informasi Lokasi
    EditText alamat_in, koor_in, Desa, Kelurahan, Kecamatan, Kabupaten, Kota, Provinsi;

    //Informasi Jual Sewa
    EditText HargaPen, MulaiDit, NamaSumber, Telepon;

    //Informasi Properti
    // A.Tanah
    EditText LebarDepan, LebarJalanDepan,
            LuasTanah;

    // B.Bangunan
    EditText JarakDindingKejalan, FungsiBangunan, LuasBangunan,
            TahunDibangun, LegalitasBangunan, TahunRenovasi;

    // C. Spesifikasi Bangunan
    EditText KapasitasListrik, Telephone, AirConditioning;

    // D. Sarana Pelengkap
    EditText Pagar, Perkerasan, Kanopi;

    // E. Keterangan Lainnya
    EditText KeteranganLainnya;

    AutoCompleteTextView jenprop_in, KondisiPen, JenisTanah, LetakTanah, Topografi, TinggiPermukaan,
            JenisPerkerasanJalan, KondisiJalan, LingkunganSekitar, BentukTapakTanah,
            LegalitasDokumenTanah, TipeBangunan, JumlahLantai, Pondasi, Struktur,
            RangkaAtap, PenutupAtap, Plafon, Dinding, PintuJendela, Lantai, SumberAirBersih;

    /*      Photo Intent        */
    ImageView fotodepan, fotojalan, fotodalam, fotodijual, fotolain, fotolain2;
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

    /*                                          < ----  ARRAY LIST ---- >           */
    // Pilih Properti
    String[] pprop = { "Tanah Kosong", "Tanah Kavling", "Pilih Properti",
            "Tanah Kosong", "Tanah Kavling", "Rumah Tinggal",
            "Tanah dan Bangunan", "Gudang", "Ruko", "Kios", "Apartemen", "Lainnya", "-"};

    // Kondisi Penawaran
    String[] konpen = {"Jual", "Sewa", "Jual atau Sewa", "Lainnya", "-"};

    // Jenis Tanah
    String[] jenta = {"Matang", "Darat", "Rawa", "Sawah", "Kebun", "Lainnya", "-"};

    // Letak Tanah
    String[] Leta = {"Badan", "Badan / Terkena Tusuk Sate", "Hoek", "Pojok", "Tengah", "Pinggir", "Kuldesak",
            "Lainnya", "-"};

    // Topografi
    String[] Topog = {"Datar", "Bergelombang", "Berbukit", "Menurun", "Tidak Rata", "Lainnya", "-"};

    // Tinggi Permukaan
    String[] Tiper = {"Tinggi Dari Jalan", "Rata dengan Jalan", "Lebih Rendah dari Jalan", "Lainnya", "-"};

    // Jenis Perkerasan Jalan
    String[] jenpeja = {"Aspal Hotmix", "Aspal", "Beton", "Paving Blok", "Sirtu", "Tanah", "Lainnya", "-"};

    // Kondisi Jalan
    String[] konjal = {"Baik", "Cukup", "Kurang", "Rusak", "Lainnya", "-"};

    // Lingkungan Sekitar
    String[] lingsek = {"Perumahan", "Perkampungan", "Pertokoan dan Perkantoran", "Pasar", "Pergudangan", "Perkebunan", "Campuran", "Lainnya", "-"};

    // Bentuk Tapak Tanah
    String[] bentatan = {"Persegi", "Persegi Panjang", "Trapesium", "Jajaran Genjang", "Segitiga", "Tidak Beraturan", "Letter L", "Ngantong", "Menyerupai Persegi Panjang", "Lainnya", "-"};

    // Legalitas Dokumen Tanah
    String[] ledota = {"SHM", "SHGB", "HGU", "Hak Pakai", "SKKT", "Segel", "Lainnya", "-"};

    // Tipe Bangunan
    String[] tiban = {"Rumah Tinggan Mewah", "Rumah Tinggal Menengah", "Rumah Tinggal Sederhana", "Rumah Tinggal Semi Permanen", "Ruko", "Kantor", "Gudang", "Kios", "Lainnya", "-"};

    // Jumlah Lantai
    String[] jumlan = {"1 Lantai", "2 Lantai", "3 Lantai", "4 Lantai", "Lainnya", "-"};

    // Pondasi
    String[] pond = {"Beton Bertulang", "Rollag Bata", "Kayu", "Lainnya", "-"};

    // Struktur
    String[] struk = {"Beton Bertulang", "Kayu", "Baja Ringan", "Lainnya", "-"};

    // Rangka Atap
    String[] rangtap = {"Kayu", "Baja Ringan", "Lainnya", "-"};

    // Penutup Atap
    String[] pentap = {"Genteng Metal", "Genteng Beton", "Asbes", "Seng", "Lainnya", "-"};
    // Plafon
    String[] plaf = {"Gypsum", "Triplek", "Asbes", "Lainnya", "-"};

    // Dinding
    String[] dind = {"Kayu Papan", "Batu Bata", "Bata Ringan", "Batako", "Lainnya", "-"};

    // Pintu & Jendela
    String[] pinjen = {"Pintu Kayu Panel dan Jendela Kayu Kaca", "Pintu Kayu Double Triplek dan Jendela Kayu Kaca", "Pintu Kayu Panel dan Jendela Alumunium", "Lainnya", "-"};

    // Lantai
    String[] lan = {"Keramik", "Granit", "Kayu Papan", "Lainnya", "-"};

    // Sumber Air Bersih
    String[] sumaber = {"PDAM", "Sumur Gali", "Sumur Artesis", "Sumur Pompa", "Lainnya", "-"};

    /* Database Key */
    //Informasi Lokasi
    private static final String KEY_KOORDINAT   = "koordinat";
    private static final String KEY_ALAMAT      = "alamat";
    private static final String KEY_DESA        = "desa";
    private static final String KEY_KELURAHAN   = "kelurahan";
    private static final String KEY_KECAMATAN   = "kecamatan";
    private static final String KEY_KABUPATEN   = "kabupaten";
    private static final String KEY_KOTA        = "kota";
    private static final String KEY_PROVINSI    = "provinsi";
    private static final String KEY_TANGGAL     = "Tanggal";
    //Informasi Jual Sewa
    private static final String KEY_JENISPRO   = "Jenis Properti";
    private static final String KEY_KONDPEN    = "Kondisi Penawaran";
    private static final String KEY_HARGAPEN   = "Harga";
    private static final String KEY_MULAIDIT   = "Mulai di Tawarkan";
    private static final String KEY_NAMASUMBER = "Nama Sumber Data";
    private static final String KEY_TELEPON    = "Telepon";
    //informasi Properti
    //A.
    private static final String KEY_JENISTAN   = "Jenis Tanah";
    private static final String KEY_LEPOTAN    = "Letak atau Posisi Tanah";
    private static final String KEY_LEDEMUKA   = "Lebar Depan atau Muka";
    private static final String KEY_TOPOGRAF   = "Topografi atau Kontur Tanah";
    private static final String KEY_ELEVASI    = "Tinggi Permukaan Tanah Terhadap Jalan atau Elevasi";
    private static final String KEY_LEBJADE    = "Lebar Jalan Depan";
    private static final String KEY_JENPERJ    = "Jenis Perkerasan Jalan";
    private static final String KEY_KONDJALA   = "Kondisi Jalan";
    private static final String KEY_LINGSEKI   = "Lingkungan Sekitar";
    private static final String KEY_BENTUKTAPA = "Bentuk Tapak Tanah";
    private static final String KEY_LUASTAN    = "Luas Tanah";
    private static final String KEY_LEGALITAS  = "Legalitas atau Dokumen Tanah";
    //B.
    private static final String KEY_TIPEBAN    = "Tipe Bangunan";
    private static final String KEY_JUMLALA    = "Jumlah Lantai";
    private static final String KEY_JARDINTE   = "Jarak Dinding Terluar ke Jalan";
    private static final String KEY_FUNGBANG   = "Fungsi Bangunan";
    private static final String KEY_LUASBANG   = "Luas Bangunan";
    private static final String KEY_TAHUNDIBAN = "Tahun Dibangun";
    private static final String KEY_IMB        = "IMB";
    private static final String KEY_TAHUNRENOV = "Tahun Renovasi";
    //C.
    private static final String KEY_PONDASI    = "Pondasi";
    private static final String KEY_STRUKTUR   = "Struktur";
    private static final String KEY_RANGKAATAP = "Rangka Atap";
    private static final String KEY_PENUTUPAT  = "Penutup Atap";
    private static final String KEY_PLAFON     = "Plafon";
    private static final String KEY_DINDING    = "Dinding";
    private static final String KEY_PINTUJEN   = "Pintu dan Jendela";
    private static final String KEY_LANTAI     = "Lantai";
    private static final String KEY_KAPLISTRIK = "Kapasitas Listrik";
    private static final String KEY_SUMBERAIRB = "Sumber Air Bersih";
    private static final String KEY_TELEPHONE  = "Telephone";
    private static final String KEY_AC         = "Air Conditioning atau AC";
    //D.
    private static final String KEY_PAGAR      = "Pagar";
    private static final String KEY_PERKERASAN = "Perkerasan";
    private static final String KEY_KANOPI     = "Kanopi";
    //E.
    private static final String KEY_KETERLAINN = "Keterangan Lainnya";
    //F. Foto
    private static final String KEY_FOTODEPAN   = "Foto Depan";
    private static final String KEY_FOTODALAM   = "Foto Dalam";
    private static final String KEY_FOTODIJUAL  = "Foto Dijual";
    private static final String KEY_FOTOJALAN   = "Foto Jalan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_submit);
        View includelayout      = findViewById(R.id.input_scroll);

        /*  Firebase  */
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();
        FirebaseUser fUser = fAuth.getCurrentUser();
        sStore      = FirebaseStorage.getInstance().getReference().child("Data Properti");
        username    = fUser.getDisplayName();
        //username.toString().trim();
        email    = fUser.getEmail();

        /* Location */
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Foto Intent
        fotodepan       = findViewById(R.id.foto_depan);
        fotojalan       = findViewById(R.id.foto_jalan);
        fotodalam       = findViewById(R.id.foto_dalam);
        fotodijual      = findViewById(R.id.foto_dijual);
        fotolain        = findViewById(R.id.foto_lain);
        fotolain2       = findViewById(R.id.foto_lain1);

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
            clickImage=5;
            takePhotoFromCamera();
        });
        fotolain2.setOnClickListener(v -> {
            clickImage=6;
            takePhotoFromCamera();
        });


        timestamp    = Timestamp.now().toString();
        timeid       = findViewById(R.id.timestamp_id);
        timeid.setText(timestamp);

        //alamat_in = includelayout.findViewById(R.id.alamat);
        //koor_in = includelayout.findViewById(R.id.koordinat);
        //final AutoCompleteTextView jenprop_in = includelayout.findViewById(R.id.jenisprop);

        //Informasi Lokasi
        Desa            =includelayout.findViewById(R.id.desa_input);
        Kelurahan       =includelayout.findViewById(R.id.kelurahan_input);
        Kecamatan       =includelayout.findViewById(R.id.kecamatan_input);
        Kabupaten       =includelayout.findViewById(R.id.kabupaten_input);
        Kota            =includelayout.findViewById(R.id.kota_input);
        Provinsi        =includelayout.findViewById(R.id.provinsi_input);

        //Informasi Jual & Sewa

        //jenprop_in       =includelayout.findViewById(R.id.jenisprop);
        //ARRAY
        ArrayAdapter<String> PilihPropAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pprop);
        jenprop_in.setAdapter(PilihPropAdapter);

        KondisiPen      =includelayout.findViewById(R.id.kondisi_penawaran);
        // ARRAY
        ArrayAdapter<String> KondisiPenAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, konpen);
        KondisiPen.setAdapter(KondisiPenAdapter);

        HargaPen        =includelayout.findViewById(R.id.harga_penawaran);
        MulaiDit        =includelayout.findViewById(R.id.mulai_ditawarkan);
        NamaSumber      =includelayout.findViewById(R.id.nama_sumber);
        Telepon         =includelayout.findViewById(R.id.telepon);


        //Informasi Properti
        // A . Tanah
        JenisTanah              =includelayout.findViewById(R.id.jenis_tanah);
        //ARRAY
        ArrayAdapter<String> JenisTanahAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, jenta);
        JenisTanah.setAdapter(JenisTanahAdapter);

        LetakTanah              =includelayout.findViewById(R.id.letak_tanah);
        //ARRAY
        ArrayAdapter<String> LetakTanahAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Leta);
        LetakTanah.setAdapter(LetakTanahAdapter);

        LebarDepan              =includelayout.findViewById(R.id.lebar_depan);
        Topografi               =includelayout.findViewById(R.id.topografi);
        //ARRAY
        ArrayAdapter<String> TopografiAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Topog);
        Topografi.setAdapter(TopografiAdapter);

        TinggiPermukaan         =includelayout.findViewById(R.id.tinggi_permukaan);
        //ARRAY
        ArrayAdapter<String> TinggiPermukaanAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, Tiper);
        TinggiPermukaan.setAdapter(TinggiPermukaanAdapter);

        LebarJalanDepan         =includelayout.findViewById(R.id.lebar_jalan_depan);
        JenisPerkerasanJalan    =includelayout.findViewById(R.id.jenis_perkerasan_jalan);
        //ARRAY
        ArrayAdapter<String> JenisPerkerasanAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, jenpeja);
        JenisPerkerasanJalan.setAdapter(JenisPerkerasanAdapter);

        KondisiJalan            =includelayout.findViewById(R.id.kondisi_jalan);
        //ARRAY
        ArrayAdapter<String> KondisiJalanAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, konjal);
        KondisiJalan.setAdapter(KondisiJalanAdapter);

        LingkunganSekitar       =includelayout.findViewById(R.id.lingkungan_sekitar);
        //ARRAY
        ArrayAdapter<String> LingkunganSekitarAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, lingsek);
        LingkunganSekitar.setAdapter(LingkunganSekitarAdapter);

        BentukTapakTanah        =includelayout.findViewById(R.id.bentuk_tapak_tanah);
        //ARRAY
        ArrayAdapter<String> BentukTapakTanahAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, bentatan);
        BentukTapakTanah.setAdapter(BentukTapakTanahAdapter);

        LuasTanah               =includelayout.findViewById(R.id.luas_tanah);
        LegalitasDokumenTanah   =includelayout.findViewById(R.id.legalitas_dokumen_tanah);
        //ARRAY
        ArrayAdapter<String> LegalitasDokumenTanahAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ledota);
        LegalitasDokumenTanah.setAdapter(LegalitasDokumenTanahAdapter);

        //B.Bangunan
        TipeBangunan        =includelayout.findViewById(R.id.tipe_bangunan);
        //ARRAY
        ArrayAdapter<String> TipeBangunanAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tiban);
        TipeBangunan.setAdapter(TipeBangunanAdapter);

        JumlahLantai        =includelayout.findViewById(R.id.jumlah_lantai);
        //ARRAY
        ArrayAdapter<String> JumlahLantaiAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, jumlan);
        JumlahLantai.setAdapter(JumlahLantaiAdapter);

        JarakDindingKejalan =includelayout.findViewById(R.id.jarak_dinding_kejalan);
        FungsiBangunan      =includelayout.findViewById(R.id.fungsi_bangunan);
        LuasBangunan        =includelayout.findViewById(R.id.luas_bangunan);
        TahunDibangun       =includelayout.findViewById(R.id.tahun_dibangun);
        LegalitasBangunan   =includelayout.findViewById(R.id.legalitas_bangunan);
        TahunRenovasi       =includelayout.findViewById(R.id.tahun_renovasi);
        //C.Spesifikasi Bangunan
        Pondasi                 = includelayout.findViewById(R.id.pondasi);
        //ARRAY
        ArrayAdapter<String> PondasiAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pond);
        Pondasi.setAdapter(PondasiAdapter);

        Struktur                = includelayout.findViewById(R.id.struktur);
        //ARRAY
        ArrayAdapter<String> StrukturAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, struk);
        Struktur.setAdapter(StrukturAdapter);

        RangkaAtap              = includelayout.findViewById(R.id.rangka_atap);
        //ARRAY
        ArrayAdapter<String> RangkaAtapAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, rangtap);
        RangkaAtap.setAdapter(RangkaAtapAdapter);

        PenutupAtap             = includelayout.findViewById(R.id.penutup_atap);
        //ARRAY
        ArrayAdapter<String> PenutupAtapAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pentap);
        PenutupAtap.setAdapter(PenutupAtapAdapter);

        Plafon                  = includelayout.findViewById(R.id.plafon);
        //ARRAY
        ArrayAdapter<String> PlafonAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, plaf);
        Plafon.setAdapter(PlafonAdapter);

        Dinding                 = includelayout.findViewById(R.id.dinding);
        //ARRAY
        ArrayAdapter<String> DindingAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, dind);
        Dinding.setAdapter(DindingAdapter);

        PintuJendela            = includelayout.findViewById(R.id.pintu_jendela);
        //ARRAY
        ArrayAdapter<String> PintuJendelaAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, pinjen);
        PintuJendela.setAdapter(PintuJendelaAdapter);

        Lantai                  = includelayout.findViewById(R.id.lantai);
        //ARRAY
        ArrayAdapter<String> LantaiAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, lan);
        Lantai.setAdapter(LantaiAdapter);

        KapasitasListrik        = includelayout.findViewById(R.id.kapasitas_listrik);
        SumberAirBersih         = includelayout.findViewById(R.id.sumber_air_bersih);
        //ARRAY
        ArrayAdapter<String> SumberAirBersihAdapter = new
                ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, sumaber);
        SumberAirBersih.setAdapter(SumberAirBersihAdapter);

        Telephone               = includelayout.findViewById(R.id.telephone);
        AirConditioning         = includelayout.findViewById(R.id.air_conditioning);
        //D.Sarana
        Pagar                   = includelayout.findViewById(R.id.pagar);
        Perkerasan              = includelayout.findViewById(R.id.perkerasan);
        Kanopi                  = includelayout.findViewById(R.id.kanopi);
        //E.Keterangan
        KeteranganLainnya       = includelayout.findViewById(R.id.keterangan_lainnya);


        /* SUBMIT TO REALTIME */

        Button submit = findViewById(R.id.submitdata);

        DAODatprop dao = new DAODatprop();
        dtp_edit    = (Datprop) getIntent().getSerializableExtra("EDIT");
        if (dtp_edit !=null)
        {
            submit.setText("UPDATE");
            alamat_in.setText(dtp_edit.getAlamat());
            koor_in.setText(dtp_edit.getKoordinat());
            jenprop_in.setText(dtp_edit.getJenisProp());
            timeid.setText(dtp_edit.getTimestamp());
            try {
                getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            submit.setText("SUBMIT");
            getLocation();
        }

        submit.setOnClickListener(v -> {
            savedata();
            Datprop dtp = new Datprop(alamat_in.getText().toString(), jenprop_in.getText().toString(), koor_in.getText().toString(), timestamp.toString());
            if (dtp_edit == null){
                dao.add(dtp)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(MainSubmit.this, "Submitted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainSubmit.this, MainActivity.class));
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(MainSubmit.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
            else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("alamat", alamat_in.getText().toString());
                hashMap.put("koordinat", koor_in.getText().toString());
                hashMap.put("jenisProp", jenprop_in.getText().toString());
                updateData();

                dao.update(dtp_edit.getKey(), hashMap).addOnSuccessListener(suc->{
                    Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(er->{
                    Toast.makeText(this, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                });

            }
        });

    }

    private void savedata() {

        // Informasi Lokasi
        int priority = 1;
        //final String priority = String.valueOf(Prior);
        final String alamat = alamat_in.getText().toString().trim();
        final String koordinat = koor_in.getText().toString().trim();

        //final String tanggal = Tataf.getText().toString().trim();
        final String desa = Desa.getText().toString().trim();
        final String kelurahan = Kelurahan.getText().toString().trim();
        final String kecamatan = Kecamatan.getText().toString().trim();
        final String kota = Kota.getText().toString().trim();
        final String kabupaten = Kabupaten.getText().toString().trim();
        final String provinsi = Provinsi.getText().toString().trim();

        //Tanggal
        final String Timestamp  = timeid.getText().toString();

        //informasi Kondisi Penawaran
        final String jenisproperti = jenprop_in.getText().toString().trim();
        final String kondisipenawaran = KondisiPen.getText().toString().trim();
        final String hargapenawaran = HargaPen.getText().toString().trim();
        final String mulaiditawarkan = MulaiDit.getText().toString().trim();
        final String namasumberdata = NamaSumber.getText().toString().trim();
        final String telepon = Telepon.getText().toString().trim();

        //informasi properti
        //A.Tanah
        final String jenistanah = JenisTanah.getText().toString().trim();
        final String letaktanah = LetakTanah.getText().toString().trim();
        final String lebardepan = LebarDepan.getText().toString().trim();
        final String topografi = Topografi.getText().toString().trim();
        final String tinggitanah = TinggiPermukaan.getText().toString().trim();
        final String lebarjalan = LebarJalanDepan.getText().toString().trim();
        final String jenisperkerasan = JenisPerkerasanJalan.getText().toString().trim();
        final String kondisijalan = KondisiJalan.getText().toString().trim();
        final String lingkungansekitar = LingkunganSekitar.getText().toString().trim();
        final String bentuktapaktanah = BentukTapakTanah.getText().toString().trim();
        final String luastanah = LuasTanah.getText().toString().trim();
        final String legalitas = LegalitasDokumenTanah.getText().toString().trim();
        //B.Bangunan
        final String tipebang = TipeBangunan.getText().toString().trim();
        final String jumlahlan = JumlahLantai.getText().toString().trim();
        final String jarakdin = JarakDindingKejalan.getText().toString().trim();
        final String fungsibang = FungsiBangunan.getText().toString().trim();
        final String luasbang = LuasBangunan.getText().toString().trim();
        final String tahundibang = TahunDibangun.getText().toString().trim();
        final String imb = LegalitasBangunan.getText().toString().trim();
        final String tahunrenov = TahunRenovasi.getText().toString().trim();
        //C.Spesifikasi
        final String pondasi = Pondasi.getText().toString().trim();
        final String struktur = Struktur.getText().toString().trim();
        final String rangkaatap = RangkaAtap.getText().toString().trim();
        final String penutupatap = PenutupAtap.getText().toString().trim();
        final String plafon = Plafon.getText().toString().trim();
        final String dinding = Dinding.getText().toString().trim();
        final String pintujendela = PintuJendela.getText().toString().trim();
        final String lantai = Lantai.getText().toString().trim();
        final String kapasitaslis = KapasitasListrik.getText().toString().trim();
        final String sumberair = SumberAirBersih.getText().toString().trim();
        final String telep = Telephone.getText().toString().trim();
        final String aircon = AirConditioning.getText().toString().trim();
        //D.Sarana Pelengkap
        final String pag = Pagar.getText().toString().trim();
        final String perk = Perkerasan.getText().toString().trim();
        final String kanop = Kanopi.getText().toString().trim();
        //E.Keterangan Lainnya
        final String ketlan = KeteranganLainnya.getText().toString().trim();
        /*//F. Foto
        final String fotodepan  = fotodepanuri.toString().trim();
        final String fotojalan  = fotojalanuri.toString().trim();
        final String fotodalam  = fotodalamuri.toString().trim();
        final String fotodijual   = fotodijualuri.toString().trim();*/


        //Informasi Lokasi Storing - - -
        DocumentReference informasilokasi = fStore.collection(String.valueOf(email)).document("userdata").collection("input").document(Timestamp);

        Map<String, Object> userdata = new HashMap<>();

        //userdata.put("priority", priority);
        userdata.put("alamat", alamat);
        userdata.put("koordinat", koordinat);
        userdata.put("Tanggal", Timestamp);
        userdata.put("desa", desa);
        userdata.put("kelurahan", kelurahan);
        userdata.put("kecamatan", kecamatan);
        userdata.put("kabupaten", kabupaten);
        userdata.put("kota", kota);
        userdata.put("provinsi", provinsi);

        //Informasi Jual Sewa
        userdata.put("Jenis Properti", jenisproperti);
        userdata.put("Kondisi Penawaran", kondisipenawaran);
        userdata.put("Harga", hargapenawaran);
        userdata.put("Mulai di Tawarkan", mulaiditawarkan);
        userdata.put("Nama Sumber Data", namasumberdata);
        userdata.put("Telepon", telepon);

        //Informasi Properti
        userdata.put("Jenis Tanah", jenistanah);
        userdata.put("Letak atau Posisi Tanah", letaktanah);
        userdata.put("Lebar Depan atau Muka", lebardepan);
        userdata.put("Topografi atau Kontur Tanah", topografi);
        userdata.put("Tinggi Permukaan Tanah Terhadap Jalan atau Elevasi", tinggitanah);
        userdata.put("Lebar Jalan Depan", lebarjalan);
        userdata.put("Jenis Perkerasan Jalan", jenisperkerasan);
        userdata.put("Kondisi Jalan", kondisijalan);
        userdata.put("Lingkungan Sekitar", lingkungansekitar);
        userdata.put("Bentuk Tapak Tanah", bentuktapaktanah);
        userdata.put("Luas Tanah", luastanah);
        userdata.put("Legalitas atau Dokumen Tanah", legalitas);
        //B.Bangunan
        userdata.put("Tipe Bangunan", tipebang);
        userdata.put("Jumlah Lantai", jumlahlan);
        userdata.put("Jarak Dinding Terluar ke Jalan", jarakdin);
        userdata.put("Fungsi Bangunan", fungsibang);
        userdata.put("Luas Bangunan", luasbang);
        userdata.put("Tahun Dibangun", tahundibang);
        userdata.put("IMB", imb);
        userdata.put("Tahun Renovasi", tahunrenov);
        //C.Spesifikasi Bangunan dan Utilitas
        userdata.put("Pondasi", pondasi);
        userdata.put("Struktur", struktur);
        userdata.put("Rangka Atap", rangkaatap);
        userdata.put("Penutup Atap", penutupatap);
        userdata.put("Plafon", plafon);
        userdata.put("Dinding", dinding);
        userdata.put("Pintu dan Jendela", pintujendela);
        userdata.put("Lantai", lantai);
        userdata.put("Kapasitas Listrik", kapasitaslis);
        userdata.put("Sumber Air Bersih", sumberair);
        userdata.put("Telephone", telep);
        userdata.put("Air Conditioning atau AC", aircon);
        //D.Sarana Pelengkap
        userdata.put("Pagar", pag);
        userdata.put("Perkerasan", perk);
        userdata.put("Kanopi", kanop);
        //E.Keterangan Lainnya
        userdata.put("Keterangan Lainnya", ketlan);
        /*//F. Foto
        userdata.put("Foto Depan", fotodepan);
        userdata.put("Foto Dalam", fotodalam);
        userdata.put("Foto Jalan", fotojalan);
        userdata.put("Foto Dijual", fotodijual);*/


        informasilokasi.set(userdata).addOnSuccessListener(aVoid -> Log.d("TAG", "User Data submitted"));
        uploadImage();
    }
    public void uploadImage(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        final String Timestamp  = timeid.getText().toString();
        final String alamat = alamat_in.getText().toString().trim();

        final String fotodepan  = alamat + "_" + "_fotodepan" + Timestamp;
        final String fotojalan  = alamat + "_" + "fotojalan" + Timestamp;
        final String fotodalam  = alamat + "_" + "fotodalam" + Timestamp;
        final String fotodijual  = alamat + "_" + "fotodijual" + Timestamp;
        final String fotolain  = alamat + "_" + "fotolain" + Timestamp;
        final String fotolain2  = alamat + "_" + "fotolain2" + Timestamp;

        StorageReference fotodepancl   = sStore.child(email).child(alamat).child(fotodepan);
        StorageReference fotojalancl   = sStore.child(email).child(alamat).child(fotojalan);
        StorageReference fotodalamcl   = sStore.child(email).child(alamat).child(fotodalam);
        StorageReference fotodijualcl   = sStore.child(email).child(alamat).child(fotodijual);
        StorageReference fotolaincl   = sStore.child(email).child(alamat).child(fotolain);
        StorageReference fotolaincl2   = sStore.child(email).child(alamat).child(fotolain2);


        ByteArrayOutputStream stream    = new ByteArrayOutputStream();
        if (fotodepanthm!=null){
            fotodepanthm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] b = stream.toByteArray();
            fotodepancl.putBytes(b)
                    .addOnSuccessListener(taskSnapshot -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> { Uri downloadUri = uri; });
                        fotodepanuri    = fotodepancl.getDownloadUrl();
                        Toast.makeText(MainSubmit.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(MainSubmit.this, "", Toast.LENGTH_SHORT).show();
                    });
        }
        if (fotojalanthm!=null){
            fotojalanthm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] b = stream.toByteArray();
            fotojalancl.putBytes(b)
                    .addOnSuccessListener(taskSnapshot -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> { Uri downloadUri = uri; });
                        fotojalanuri    = fotojalancl.getDownloadUrl();
                        Toast.makeText(MainSubmit.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(MainSubmit.this, "", Toast.LENGTH_SHORT).show();
                    });

        }
        if (fotodalamthm!=null){
            byte[] b = stream.toByteArray();
            fotodalamthm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            fotodalamcl.putBytes(b)
                    .addOnSuccessListener(taskSnapshot -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> { Uri downloadUri = uri; });
                        fotodalamuri    = fotodalamcl.getDownloadUrl();
                        Toast.makeText(MainSubmit.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(MainSubmit.this, "", Toast.LENGTH_SHORT).show();
                    });
        }
        if (fotodijualthm!=null){
            fotodijualthm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] b = stream.toByteArray();
            fotodijualcl.putBytes(b)
                    .addOnSuccessListener(taskSnapshot -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> { Uri downloadUri = uri; });
                        fotodijualuri    = fotodijualcl.getDownloadUrl();
                        Toast.makeText(MainSubmit.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(MainSubmit.this, "", Toast.LENGTH_SHORT).show();
                    });
        }
        if (fotolainthm!=null){
            fotolainthm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] b = stream.toByteArray();
            fotolaincl.putBytes(b)
                    .addOnSuccessListener(taskSnapshot -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> { Uri downloadUri = uri; });
                        fotolainuri    = fotolaincl.getDownloadUrl();
                        Toast.makeText(MainSubmit.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(MainSubmit.this, "", Toast.LENGTH_SHORT).show();
                    });
        }
        if (fotolainthm2!=null){
            fotolainthm2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] b = stream.toByteArray();
            fotolaincl2.putBytes(b)
                    .addOnSuccessListener(taskSnapshot -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> { Uri downloadUri = uri; });
                        fotolainuri2    = fotolaincl2.getDownloadUrl();
                        Toast.makeText(MainSubmit.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(MainSubmit.this, "", Toast.LENGTH_SHORT).show();
                    });
        }
        finish();
    }

    private void updateData() {

        // Informasi Lokasi
        int priority = 1;
        //final String priority = String.valueOf(Prior);
        final String alamat = alamat_in.getText().toString().trim();
        final String koordinat = koor_in.getText().toString().trim();

        //final String tanggal = Tataf.getText().toString().trim();
        final String desa = Desa.getText().toString().trim();
        final String kelurahan = Kelurahan.getText().toString().trim();
        final String kecamatan = Kecamatan.getText().toString().trim();
        final String kota = Kota.getText().toString().trim();
        final String kabupaten = Kabupaten.getText().toString().trim();
        final String provinsi = Provinsi.getText().toString().trim();

        //Tanggal
        timestampkey   = String.valueOf(dtp_edit.getTimestamp());

        //informasi Kondisi Penawaran
        final String jenisproperti = jenprop_in.getText().toString().trim();
        final String kondisipenawaran = KondisiPen.getText().toString().trim();
        final String hargapenawaran = HargaPen.getText().toString().trim();
        final String mulaiditawarkan = MulaiDit.getText().toString().trim();
        final String namasumberdata = NamaSumber.getText().toString().trim();
        final String telepon = Telepon.getText().toString().trim();

        //informasi properti
        //A.Tanah
        final String jenistanah = JenisTanah.getText().toString().trim();
        final String letaktanah = LetakTanah.getText().toString().trim();
        final String lebardepan = LebarDepan.getText().toString().trim();
        final String topografi = Topografi.getText().toString().trim();
        final String tinggitanah = TinggiPermukaan.getText().toString().trim();
        final String lebarjalan = LebarJalanDepan.getText().toString().trim();
        final String jenisperkerasan = JenisPerkerasanJalan.getText().toString().trim();
        final String kondisijalan = KondisiJalan.getText().toString().trim();
        final String lingkungansekitar = LingkunganSekitar.getText().toString().trim();
        final String bentuktapaktanah = BentukTapakTanah.getText().toString().trim();
        final String luastanah = LuasTanah.getText().toString().trim();
        final String legalitas = LegalitasDokumenTanah.getText().toString().trim();
        //B.Bangunan
        final String tipebang = TipeBangunan.getText().toString().trim();
        final String jumlahlan = JumlahLantai.getText().toString().trim();
        final String jarakdin = JarakDindingKejalan.getText().toString().trim();
        final String fungsibang = FungsiBangunan.getText().toString().trim();
        final String luasbang = LuasBangunan.getText().toString().trim();
        final String tahundibang = TahunDibangun.getText().toString().trim();
        final String imb = LegalitasBangunan.getText().toString().trim();
        final String tahunrenov = TahunRenovasi.getText().toString().trim();
        //C.Spesifikasi
        final String pondasi = Pondasi.getText().toString().trim();
        final String struktur = Struktur.getText().toString().trim();
        final String rangkaatap = RangkaAtap.getText().toString().trim();
        final String penutupatap = PenutupAtap.getText().toString().trim();
        final String plafon = Plafon.getText().toString().trim();
        final String dinding = Dinding.getText().toString().trim();
        final String pintujendela = PintuJendela.getText().toString().trim();
        final String lantai = Lantai.getText().toString().trim();
        final String kapasitaslis = KapasitasListrik.getText().toString().trim();
        final String sumberair = SumberAirBersih.getText().toString().trim();
        final String telep = Telephone.getText().toString().trim();
        final String aircon = AirConditioning.getText().toString().trim();
        //D.Sarana Pelengkap
        final String pag = Pagar.getText().toString().trim();
        final String perk = Perkerasan.getText().toString().trim();
        final String kanop = Kanopi.getText().toString().trim();
        //E.Keterangan Lainnya
        final String ketlan = KeteranganLainnya.getText().toString().trim();
        /*//F. Foto
        final String fotodepan  = fotodepanuri.toString().trim();
        final String fotojalan  = fotojalanuri.toString().trim();
        final String fotodalam  = fotodalamuri.toString().trim();
        final String fotodijual   = fotodijualuri.toString().trim();*/


        //Informasi Lokasi Storing - - -
        DocumentReference informasilokasi = fStore.collection(String.valueOf(email)).document("userdata").collection("input").document(timestampkey);

        Map<String, Object> userdata = new HashMap<>();

        //userdata.put("priority", priority);
        userdata.put("alamat", alamat);
        userdata.put("koordinat", koordinat);
        userdata.put("Tanggal", timestamp);
        userdata.put("desa", desa);
        userdata.put("kelurahan", kelurahan);
        userdata.put("kecamatan", kecamatan);
        userdata.put("kabupaten", kabupaten);
        userdata.put("kota", kota);
        userdata.put("provinsi", provinsi);

        //Informasi Jual Sewa
        userdata.put("Jenis Properti", jenisproperti);
        userdata.put("Kondisi Penawaran", kondisipenawaran);
        userdata.put("Harga", hargapenawaran);
        userdata.put("Mulai di Tawarkan", mulaiditawarkan);
        userdata.put("Nama Sumber Data", namasumberdata);
        userdata.put("Telepon", telepon);

        //Informasi Properti
        userdata.put("Jenis Tanah", jenistanah);
        userdata.put("Letak atau Posisi Tanah", letaktanah);
        userdata.put("Lebar Depan atau Muka", lebardepan);
        userdata.put("Topografi atau Kontur Tanah", topografi);
        userdata.put("Tinggi Permukaan Tanah Terhadap Jalan atau Elevasi", tinggitanah);
        userdata.put("Lebar Jalan Depan", lebarjalan);
        userdata.put("Jenis Perkerasan Jalan", jenisperkerasan);
        userdata.put("Kondisi Jalan", kondisijalan);
        userdata.put("Lingkungan Sekitar", lingkungansekitar);
        userdata.put("Bentuk Tapak Tanah", bentuktapaktanah);
        userdata.put("Luas Tanah", luastanah);
        userdata.put("Legalitas atau Dokumen Tanah", legalitas);
        //B.Bangunan
        userdata.put("Tipe Bangunan", tipebang);
        userdata.put("Jumlah Lantai", jumlahlan);
        userdata.put("Jarak Dinding Terluar ke Jalan", jarakdin);
        userdata.put("Fungsi Bangunan", fungsibang);
        userdata.put("Luas Bangunan", luasbang);
        userdata.put("Tahun Dibangun", tahundibang);
        userdata.put("IMB", imb);
        userdata.put("Tahun Renovasi", tahunrenov);
        //C.Spesifikasi Bangunan dan Utilitas
        userdata.put("Pondasi", pondasi);
        userdata.put("Struktur", struktur);
        userdata.put("Rangka Atap", rangkaatap);
        userdata.put("Penutup Atap", penutupatap);
        userdata.put("Plafon", plafon);
        userdata.put("Dinding", dinding);
        userdata.put("Pintu dan Jendela", pintujendela);
        userdata.put("Lantai", lantai);
        userdata.put("Kapasitas Listrik", kapasitaslis);
        userdata.put("Sumber Air Bersih", sumberair);
        userdata.put("Telephone", telep);
        userdata.put("Air Conditioning atau AC", aircon);
        //D.Sarana Pelengkap
        userdata.put("Pagar", pag);
        userdata.put("Perkerasan", perk);
        userdata.put("Kanopi", kanop);
        //E.Keterangan Lainnya
        userdata.put("Keterangan Lainnya", ketlan);
        /*//F. Foto
        userdata.put("Foto Depan", fotodepan);
        userdata.put("Foto Dalam", fotodalam);
        userdata.put("Foto Jalan", fotojalan);
        userdata.put("Foto Dijual", fotodijual);*/


        informasilokasi.update(userdata).addOnSuccessListener(aVoid -> Log.d("TAG", "User Data submitted"));
        uploadImage();
    }

    private void getData() throws IOException {
        timestampkey   = String.valueOf(dtp_edit.getTimestamp());
        DocumentReference userdata = fStore.collection(String.valueOf(email)).document("userdata").collection("input").document(timestampkey);

        userdata.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        // Informasi Lokasi
                        String desa         = documentSnapshot.getString(KEY_DESA);
                        Desa.setText(desa);
                        String kelurahan    = documentSnapshot.getString(KEY_KELURAHAN);
                        Kelurahan.setText(kelurahan);
                        String kecamatan    = documentSnapshot.getString(KEY_KECAMATAN);
                        Kecamatan.setText(kecamatan);
                        String kabupaten    = documentSnapshot.getString(KEY_KABUPATEN);
                        Kabupaten.setText(kabupaten);
                        String kota         = documentSnapshot.getString(KEY_KOTA);
                        Kota.setText(kota);
                        String provinsi     = documentSnapshot.getString(KEY_PROVINSI);
                        Provinsi.setText(provinsi);

                        // Informasi Jual Sewa
                        String kondisipenawaran = documentSnapshot.getString(KEY_KONDPEN);
                        KondisiPen.setText(kondisipenawaran);
                        String harga            = documentSnapshot.getString(KEY_HARGAPEN);
                        HargaPen.setText(harga);
                        String mulaidit         = documentSnapshot.getString(KEY_MULAIDIT);
                        MulaiDit.setText(mulaidit);
                        String namasumberdata   = documentSnapshot.getString(KEY_NAMASUMBER);
                        NamaSumber.setText(namasumberdata);
                        String telepon          = documentSnapshot.getString(KEY_TELEPON);
                        Telepon.setText(telepon);

                        // Informasi Properti
                        // A.
                        String jenistanah       = documentSnapshot.getString(KEY_JENISTAN);
                        JenisTanah.setText(jenistanah);
                        String letaktanah       = documentSnapshot.getString(KEY_LEPOTAN);
                        LetakTanah.setText(letaktanah);
                        String lebardepan       = documentSnapshot.getString(KEY_LEDEMUKA);
                        LebarDepan.setText(lebardepan);
                        String topografi        = documentSnapshot.getString(KEY_TOPOGRAF);
                        Topografi.setText(topografi);
                        String tinggipermukaan  = documentSnapshot.getString(KEY_ELEVASI);
                        TinggiPermukaan.setText(tinggipermukaan);
                        String lebarjalandepan  = documentSnapshot.getString(KEY_LEBJADE);
                        LebarJalanDepan.setText(lebarjalandepan);
                        String jenisperkerasan  = documentSnapshot.getString(KEY_JENPERJ);
                        JenisPerkerasanJalan.setText(jenisperkerasan);
                        String kondisijalan     = documentSnapshot.getString(KEY_KONDJALA);
                        KondisiJalan.setText(kondisijalan);
                        String lingkungansekit  = documentSnapshot.getString(KEY_LINGSEKI);
                        LingkunganSekitar.setText(lingkungansekit);
                        String bentuktapaktana  = documentSnapshot.getString(KEY_BENTUKTAPA);
                        BentukTapakTanah.setText(bentuktapaktana);
                        String luastanah        = documentSnapshot.getString(KEY_LUASTAN);
                        LuasTanah.setText(luastanah);
                        String legalitastanah   = documentSnapshot.getString(KEY_LEGALITAS);
                        LegalitasDokumenTanah.setText(legalitastanah);
                        //B.
                        String tipebang         = documentSnapshot.getString(KEY_TIPEBAN);
                        TipeBangunan.setText(tipebang);
                        String jumlahlan        = documentSnapshot.getString(KEY_JUMLALA);
                        JumlahLantai.setText(jumlahlan);
                        String jarakdin         = documentSnapshot.getString(KEY_JARDINTE);
                        JarakDindingKejalan.setText(jarakdin);
                        String fungsibang       = documentSnapshot.getString(KEY_FUNGBANG);
                        FungsiBangunan.setText(fungsibang);
                        String luasbang         = documentSnapshot.getString(KEY_LUASBANG);
                        LuasBangunan.setText(luasbang);
                        String tahundibang      = documentSnapshot.getString(KEY_TAHUNDIBAN);
                        TahunDibangun.setText(tahundibang);
                        String imb              = documentSnapshot.getString(KEY_IMB);
                        LegalitasBangunan.setText(imb);
                        String tahunrenov       = documentSnapshot.getString(KEY_TAHUNRENOV);
                        TahunRenovasi.setText(tahunrenov);
                        //C.
                        String pondasi          = documentSnapshot.getString(KEY_PONDASI);
                        Pondasi.setText(pondasi);
                        String struktur         = documentSnapshot.getString(KEY_STRUKTUR);
                        Struktur.setText(struktur);
                        String rangkaatap       = documentSnapshot.getString(KEY_RANGKAATAP);
                        RangkaAtap.setText(rangkaatap);
                        String penutupatap      = documentSnapshot.getString(KEY_PENUTUPAT);
                        PenutupAtap.setText(penutupatap);
                        String plafon           = documentSnapshot.getString(KEY_PLAFON);
                        Plafon.setText(plafon);
                        String dinding          = documentSnapshot.getString(KEY_DINDING);
                        Dinding.setText(dinding);
                        String pintudanjendela  = documentSnapshot.getString(KEY_PINTUJEN);
                        PintuJendela.setText(pintudanjendela);
                        String lantai           = documentSnapshot.getString(KEY_LANTAI);
                        Lantai.setText(lantai);
                        String kapasitaslistrik = documentSnapshot.getString(KEY_KAPLISTRIK);
                        KapasitasListrik.setText(kapasitaslistrik);
                        String sumberairbersih  = documentSnapshot.getString(KEY_SUMBERAIRB);
                        SumberAirBersih.setText(sumberairbersih);
                        String telephone        = documentSnapshot.getString(KEY_TELEPHONE);
                        Telephone.setText(telephone);
                        String airconditioning  = documentSnapshot.getString(KEY_AC);
                        AirConditioning.setText(airconditioning);
                        //D.
                        String pagar            = documentSnapshot.getString(KEY_PAGAR);
                        Pagar.setText(pagar);
                        String perkerasan       = documentSnapshot.getString(KEY_PERKERASAN);
                        Perkerasan.setText(perkerasan);
                        String kanopi           = documentSnapshot.getString(KEY_KANOPI);
                        Kanopi.setText(kanopi);
                        //E.
                        String keteranganlain   = documentSnapshot.getString(KEY_KETERLAINN);
                        KeteranganLainnya.setText(keteranganlain);
                        //F. Foto
                        String fotodepan        = documentSnapshot.getString(KEY_FOTODEPAN);
                        String fotodalam        = documentSnapshot.getString(KEY_FOTODALAM);
                        String fotodijual       = documentSnapshot.getString(KEY_FOTODIJUAL);
                        String fotojalan        = documentSnapshot.getString(KEY_FOTOJALAN);


                        Toast.makeText(MainSubmit.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainSubmit.this, "Document not exist !", Toast.LENGTH_SHORT).show();
                    }
                });
        //getImage();

    }
    public void getImage() throws IOException {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        final String Timestamp  = String.valueOf(dtp_edit.getTimestamp());
        final String alamat = String.valueOf(dtp_edit.getAlamat());

        final String fotodepannm  = alamat + "_" + "_fotodepan" + Timestamp;
        final String fotojalannm  = alamat + "_" + "fotojalan" + Timestamp;
        final String fotodalamnm  = alamat + "_" + "fotodalam" + Timestamp;
        final String fotodijualnm  = alamat + "_" + "fotodijual" + Timestamp;
        final String fotolainnm  = alamat + "_" + "fotolain" + Timestamp;
        final String fotolain2nm  = alamat + "_" + "fotolain2" + Timestamp;

        StorageReference fotodepancl   = sStore.child(email).child(alamat).child(fotodepannm);
        StorageReference fotojalancl   = sStore.child(email).child(alamat).child(fotojalannm);
        StorageReference fotodalamcl   = sStore.child(email).child(alamat).child(fotodalamnm);
        StorageReference fotodijualcl   = sStore.child(email).child(alamat).child(fotodijualnm);
        StorageReference fotolaincl   = sStore.child(email).child(alamat).child(fotolainnm);
        StorageReference fotolaincl2   = sStore.child(email).child(alamat).child(fotolain2nm);

        File localFotoDepan = File.createTempFile(fotodepannm, "jpg");
        fotodepancl.getFile(localFotoDepan)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap fotodepanbtmp    = BitmapFactory.decodeFile(localFotoDepan.getAbsolutePath());
                        fotodepan.setImageBitmap(fotodepanbtmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainSubmit.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        File localFotoJalan = File.createTempFile(fotojalannm, "jpg");
        fotojalancl.getFile(localFotoJalan)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap fotojalanbtmp    = BitmapFactory.decodeFile(localFotoJalan.getAbsolutePath());
                        fotojalan.setImageBitmap(fotojalanbtmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainSubmit.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        File localFotoDalam = File.createTempFile(fotodalamnm, "jpg");
        fotodalamcl.getFile(localFotoDalam)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap fotodalambtmp    = BitmapFactory.decodeFile(localFotoDalam.getAbsolutePath());
                        fotodalam.setImageBitmap(fotodalambtmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainSubmit.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        File localFotoDijual = File.createTempFile(fotodijualnm, "jpg");
        fotodijualcl.getFile(localFotoDijual)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap fotodijualbtmp    = BitmapFactory.decodeFile(localFotoDijual.getAbsolutePath());
                        fotodijual.setImageBitmap(fotodijualbtmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainSubmit.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        File localFotoLain = File.createTempFile(fotolainnm, "jpg");
        fotolaincl.getFile(localFotoLain)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap fotolainbtmp    = BitmapFactory.decodeFile(localFotoLain.getAbsolutePath());
                        fotolain.setImageBitmap(fotolainbtmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainSubmit.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
        File localFotoLain2 = File.createTempFile(fotolain2nm, "jpg");
        fotolaincl2.getFile(localFotoLain2)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap fotolainbtmp    = BitmapFactory.decodeFile(localFotoLain2.getAbsolutePath());
                        fotolain2.setImageBitmap(fotolainbtmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainSubmit.this, "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void takePhotoFromCamera() {
        Intent camera   = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    fotolain.setImageBitmap(fotolainthm);
                    saveImage(fotolainthm);
                }
                break;
            case 6:
                if (requestCode == CAMERA){
                    fotolainthm2  = (Bitmap) data.getExtras().get("data");
                    fotolain2.setImageBitmap(fotolainthm2);
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

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, (LocationListener)this);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5,(LocationListener) this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(MainSubmit.this,
                    Locale.getDefault());
            addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(),1);

            //set latitude & longitude
            String latitude = String.valueOf(Html.fromHtml("<font color='#000000'></font>"
                    + addresses.get(0).getLongitude()));
            latitude.trim();
            String longitude = String.valueOf(Html.fromHtml("<font color='#000000'><b>,</b></font>"
                    + addresses.get(0).getLatitude()));
            longitude.trim();

            coordinates = (latitude + "." + longitude);

            final String koor   = koor_in.getText().toString().trim();
            final String prov   = Provinsi.getText().toString().trim();
            final String alam   = alamat_in.getText().toString().trim();

            if (TextUtils.isEmpty(koor)){
                koor_in.setText(coordinates);
            }
            if (TextUtils.isEmpty(prov)){
                Provinsi.setText(addresses.get(0).getAdminArea());
            }
            if (TextUtils.isEmpty(alam)){
                alamat_in.setText(addresses.get(0).getAddressLine(0));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}