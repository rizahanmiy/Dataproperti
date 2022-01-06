package com.example.dataproperti.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dataproperti.MainActivity;
import com.example.dataproperti.R;
import com.example.dataproperti.Cloud.DAODatprop;
import com.example.dataproperti.Cloud.Datprop;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
import java.util.Locale;
import java.util.Map;

public class Review extends AppCompatActivity{

      //Firebase
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private StorageReference sStore;
    private FirebaseUser fUser;
    private String email;
    private DatabaseReference mbase;
    private String username;
    private FirebaseAnalytics Analytics;
    private FirebaseDatabase fRealData;
    Datprop dtp_edit;
    String timestamp, timestampkey;
    TextView timeid;

/*    *//* Location *//*
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;*/

    /* Tanggal */
    private final static Locale IDNA = new Locale("in", "ID");

    /* Layout */
    //Informasi Lokasi
    TextView Koordinat, Alamat, Desa, Kelurahan, Kecamatan, Kabupaten, Kota, Provinsi;

    //Informasi Jual Sewa
    TextView HargaPen, MulaiDit, NamaSumber, Telepon;

    //Informasi Properti
    // A.Tanah
    TextView LebarDepan, LebarJalanDepan,
            LuasTanah;

    // B.Bangunan
    TextView JarakDindingKejalan, FungsiBangunan, LuasBangunan,
            TahunDibangun, LegalitasBangunan, TahunRenovasi;

    // C. Spesifikasi Bangunan
    TextView KapasitasListrik, Telephone, AirConditioning;

    // D. Sarana Pelengkap
    TextView Pagar, Perkerasan, Kanopi;

    // E. Keterangan Lainnya
    TextView KeteranganLainnya;

    TextView PilihProp, KondisiPen, JenisTanah, LetakTanah, Topografi, TinggiPermukaan,
            JenisPerkerasanJalan, KondisiJalan, LingkunganSekitar, BentukTapakTanah,
            LegalitasDokumenTanah, TipeBangunan, JumlahLantai, Pondasi, Struktur,
            RangkaAtap, PenutupAtap, Plafon, Dinding, PintuJendela, Lantai, SumberAirBersih;

    Button submit, back;
    //Button tomboltampil;

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
        setContentView(R.layout.activity_review);
        View includelayout      = findViewById(R.id.input_scroll);

         //Firebase
        fAuth       = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();
        fUser       = fAuth.getCurrentUser();
        mbase       = FirebaseDatabase.getInstance().getReference(fAuth.getUid());
        sStore      = FirebaseStorage.getInstance().getReference().child("Data Properti");
        username    = fUser.getDisplayName();
        //username.toString().trim();
        email    = fUser.getEmail();

        /* Location */
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        timestamp    = Timestamp.now().toString();


        /*      Photo Intent        */
        fotodepan       = findViewById(R.id.foto_depan_review);
        fotojalan       = findViewById(R.id.foto_jalan_review);
        fotodalam       = findViewById(R.id.foto_dalam_review);
        fotodijual      = findViewById(R.id.foto_dijual_review);
        fotolain        = findViewById(R.id.foto_lain_review);
        fotolain2       = findViewById(R.id.foto_lain2_review);
        //fototambahan    = findViewById(R.id.foto_tambahan);

        //requestMultiplePermissions();
        // Task
/*        fotodepan.setOnClickListener(v -> {
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
        });*/

        //Informasi Lokasi
        Koordinat       =includelayout.findViewById(R.id.koordinat_review);
        Alamat          =includelayout.findViewById(R.id.alamat_review);
        Desa            =includelayout.findViewById(R.id.desa_review);
        Kelurahan       =includelayout.findViewById(R.id.kelurahan_review);
        Kecamatan       =includelayout.findViewById(R.id.kecamatan_review);
        Kabupaten       =includelayout.findViewById(R.id.kabupaten_review);
        Kota            =includelayout.findViewById(R.id.kota_review);
        Provinsi        =includelayout.findViewById(R.id.provinsi_review);

        //Informasi Jual & Sewa
        PilihProp       =includelayout.findViewById(R.id.properti_review);
        KondisiPen      =includelayout.findViewById(R.id.kondisi_penawaran_review);
        HargaPen        =includelayout.findViewById(R.id.harga_penawaran_review);
        MulaiDit        =includelayout.findViewById(R.id.mulai_ditawarkan_review);
        NamaSumber      =includelayout.findViewById(R.id.nama_sumber_review);
        Telepon         =includelayout.findViewById(R.id.telepon_review);

        //Informasi Properti
        // A . Tanah
        JenisTanah              =includelayout.findViewById(R.id.jenis_tanah_review);
        LetakTanah              =includelayout.findViewById(R.id.letak_tanah_review);
        LebarDepan              =includelayout.findViewById(R.id.lebar_depan_review);
        Topografi               =includelayout.findViewById(R.id.topografi_review);
        TinggiPermukaan         =includelayout.findViewById(R.id.tinggi_permukaan_review);
        LebarJalanDepan         =includelayout.findViewById(R.id.lebar_jalan_depan_review);
        JenisPerkerasanJalan    =includelayout.findViewById(R.id.jenis_perkerasan_jalan_review);
        KondisiJalan            =includelayout.findViewById(R.id.kondisi_jalan_review);
        LingkunganSekitar       =includelayout.findViewById(R.id.lingkungan_sekitar_review);
        BentukTapakTanah        =includelayout.findViewById(R.id.bentuk_tapak_tanah_review);
        LuasTanah               =includelayout.findViewById(R.id.luas_tanah_review);
        LegalitasDokumenTanah   =includelayout.findViewById(R.id.legalitas_dokumen_tanah_review);
        //B.Bangunan
        TipeBangunan        =includelayout.findViewById(R.id.tipe_bangunan_review);
        JumlahLantai        =includelayout.findViewById(R.id.jumlah_lantai_review);
        JarakDindingKejalan =includelayout.findViewById(R.id.jarak_dinding_kejalan_review);
        FungsiBangunan      =includelayout.findViewById(R.id.fungsi_bangunan_review);
        LuasBangunan        =includelayout.findViewById(R.id.luas_bangunan_review);
        TahunDibangun       =includelayout.findViewById(R.id.tahun_dibangun_review);
        LegalitasBangunan   =includelayout.findViewById(R.id.legalitas_bangunan_review);
        TahunRenovasi       =includelayout.findViewById(R.id.tahun_renovasi_review);

        //C.Spesifikasi Bangunan
        Pondasi                 = includelayout.findViewById(R.id.pondasi_review);
        Struktur                = includelayout.findViewById(R.id.struktur_review);
        RangkaAtap              = includelayout.findViewById(R.id.rangka_atap_review);
        PenutupAtap             = includelayout.findViewById(R.id.penutup_atap_review);
        Plafon                  = includelayout.findViewById(R.id.plafon_review);
        Dinding                 = includelayout.findViewById(R.id.dinding_review);
        PintuJendela            = includelayout.findViewById(R.id.pintu_jendela_review);
        Lantai                  = includelayout.findViewById(R.id.lantai_review);
        KapasitasListrik        = includelayout.findViewById(R.id.kapasitas_listrik_review);
        SumberAirBersih         = includelayout.findViewById(R.id.sumber_air_bersih_review);
        Telephone               = includelayout.findViewById(R.id.telephone_review);
        AirConditioning         = includelayout.findViewById(R.id.air_conditioning_review);

        //D.Sarana
        Pagar                   = includelayout.findViewById(R.id.pagar_review);
        Perkerasan              = includelayout.findViewById(R.id.perkerasan_review);
        Kanopi                  = includelayout.findViewById(R.id.kanopi_review);

        //E.Keterangan
        KeteranganLainnya       = includelayout.findViewById(R.id.keterangan_lainnya_review);


        //Submit Button
        back    = findViewById(R.id.Back_Review);
        back.setOnClickListener(v -> {
            startActivity(new Intent(Review.this, infoProperti2.class));
        });
        submit = findViewById(R.id.SubmitData);

        DAODatprop dao = new DAODatprop();
        dtp_edit    = (Datprop) getIntent().getSerializableExtra("EDIT");
        if (dtp_edit !=null)
        {
            submit.setText("UPDATE");
            back.setText("CANCEL");
            back.setOnClickListener(v -> {
                startActivity(new Intent(Review.this, MainActivity.class));
            });
            Alamat.setText(dtp_edit.getAlamat());
            Koordinat.setText(dtp_edit.getKoordinat());
            PilihProp.setText(dtp_edit.getJenisProp());
            //timeid.setText(dtp_edit.getTimestamp());
            try {
                getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            submit.setText("SUBMIT");
            //getLocation();
        }

        submit.setOnClickListener(v -> {
            savedata();
            Datprop dtp = new Datprop(Alamat.getText().toString(), PilihProp.getText().toString(), Koordinat.getText().toString(), timestamp.toString());
            if (dtp_edit == null){
                dao.add(dtp)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(Review.this, "Submitted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Review.this, MainActivity.class));
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(Review.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
            else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("alamat", Alamat.getText().toString());
                hashMap.put("koordinat", Koordinat.getText().toString());
                hashMap.put("jenisProp", PilihProp.getText().toString());
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
        final String alamat = Alamat.getText().toString().trim();
        final String koordinat = Koordinat.getText().toString().trim();

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
        final String jenisproperti = PilihProp.getText().toString().trim();
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
        final String alamat = Alamat.getText().toString().trim();

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
                        Toast.makeText(Review.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(Review.this, "", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Review.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(Review.this, "", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Review.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(Review.this, "", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Review.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(Review.this, "", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Review.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(Review.this, "", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Review.this, "Foto Berhasil di Upload", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(Review.this, "", Toast.LENGTH_SHORT).show();
                    });
        }
        finish();
    }

    private void updateData() {

        // Informasi Lokasi
        int priority = 1;
        //final String priority = String.valueOf(Prior);
        final String alamat = Alamat.getText().toString().trim();
        final String koordinat = Koordinat.getText().toString().trim();

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
        final String jenisproperti = PilihProp.getText().toString().trim();
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


                        Toast.makeText(Review.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Review.this, "Document not exist !", Toast.LENGTH_SHORT).show();
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

/*    private void getLocation() {
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
    }*/
}