package com.example.dataproperti.Cloud;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dataproperti.R;
import com.example.dataproperti.add.Review;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Map;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Datprop dtp;
    private FirebaseFirestore fStore;
    private String username;
    private String Email;

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


    private final Context context;
    ArrayList<Datprop> list = new ArrayList<>();

    public RVAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<Datprop> dtp) {
        list.addAll(dtp);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new DatpropVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DatpropVH vh = (DatpropVH) holder;
        dtp = list.get(position);
        vh.pilihprop_in.setText(dtp.getJenisProp());
        vh.koordinat_in.setText(dtp.getKoordinat());
        vh.alamat_in.setText(dtp.getAlamat());


        vh.options_btn.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, vh.options_btn);
            popupMenu.inflate(R.menu.options_menu);
            popupMenu.setOnMenuItemClickListener(item ->
            {
                switch (item.getItemId())
                {
                    case R.id.menu_edit:
                        Intent intent = new Intent(context, Review.class);
                        intent.putExtra("EDIT", dtp);
                        context.startActivity(intent);
                        break;
                    case R.id.menu_remove:
                        DAODatprop dao = new DAODatprop();
                        deleteData();
                        dao.remove(dtp.getKey()).addOnSuccessListener(suc->{
                            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                        }).addOnFailureListener(er->{
                            Toast.makeText(context, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        break;
                    case R.id.menu_download:
                        download();
                        break;

                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    // TODO: Download to Excel File bug Fix for all SDK
    private void download(){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        fStore      = FirebaseFirestore.getInstance();
        FirebaseUser fUser = fAuth.getCurrentUser();

        //username    = fUser.getDisplayName();
        Email       = fUser.getEmail();
        String timestampkey   = String.valueOf(dtp.getTimestamp());

        DocumentReference userdata = fStore.collection(String.valueOf(Email)).document("userdata").collection("input").document(timestampkey);

        userdata.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()){
                        // Informasi Lokasi
                        String alamat       = documentSnapshot.getString(KEY_ALAMAT);
                        String koordinat    = documentSnapshot.getString(KEY_KOORDINAT);
                        String desa         = documentSnapshot.getString(KEY_DESA);
                        String kelurahan    = documentSnapshot.getString(KEY_KELURAHAN);
                        String kecamatan    = documentSnapshot.getString(KEY_KECAMATAN);
                        String kabupaten    = documentSnapshot.getString(KEY_KABUPATEN);
                        String kota         = documentSnapshot.getString(KEY_KOTA);
                        String provinsi     = documentSnapshot.getString(KEY_PROVINSI);
                        String tanggal      = documentSnapshot.getString(KEY_TANGGAL);
                        // Informasi Jual Sewa
                        String jenisproperti    = documentSnapshot.getString(KEY_JENISPRO);
                        String kondisipenawaran = documentSnapshot.getString(KEY_KONDPEN);
                        String harga            = documentSnapshot.getString(KEY_HARGAPEN);
                        String mulaidit         = documentSnapshot.getString(KEY_MULAIDIT);
                        String namasumberdata   = documentSnapshot.getString(KEY_NAMASUMBER);
                        String telepon          = documentSnapshot.getString(KEY_TELEPON);
                        // Informasi Properti
                        // A.
                        String jenistanah       = documentSnapshot.getString(KEY_JENISTAN);
                        String letaktanah       = documentSnapshot.getString(KEY_LEPOTAN);
                        String lebardepan       = documentSnapshot.getString(KEY_LEDEMUKA);
                        String topografi        = documentSnapshot.getString(KEY_TOPOGRAF);
                        String tinggipermukaan  = documentSnapshot.getString(KEY_ELEVASI);
                        String lebarjalandepan  = documentSnapshot.getString(KEY_LEBJADE);
                        String jenisperkerasan  = documentSnapshot.getString(KEY_JENPERJ);
                        String kondisijalan     = documentSnapshot.getString(KEY_KONDJALA);
                        String lingkungansekit  = documentSnapshot.getString(KEY_LINGSEKI);
                        String bentuktapaktana  = documentSnapshot.getString(KEY_BENTUKTAPA);
                        String luastanah        = documentSnapshot.getString(KEY_LUASTAN);
                        String legalitastanah   = documentSnapshot.getString(KEY_LEGALITAS);
                        //B.
                        String tipebang         = documentSnapshot.getString(KEY_TIPEBAN);
                        String jumlahlan        = documentSnapshot.getString(KEY_JUMLALA);
                        String jarakdin         = documentSnapshot.getString(KEY_JARDINTE);
                        String fungsibang       = documentSnapshot.getString(KEY_FUNGBANG);
                        String luasbang         = documentSnapshot.getString(KEY_LUASBANG);
                        String tahundibang      = documentSnapshot.getString(KEY_TAHUNDIBAN);
                        String imb              = documentSnapshot.getString(KEY_IMB);
                        String tahunrenov       = documentSnapshot.getString(KEY_TAHUNRENOV);
                        //C.
                        String pondasi          = documentSnapshot.getString(KEY_PONDASI);
                        String struktur         = documentSnapshot.getString(KEY_STRUKTUR);
                        String rangkaatap       = documentSnapshot.getString(KEY_RANGKAATAP);
                        String penutupatap      = documentSnapshot.getString(KEY_PENUTUPAT);
                        String plafon           = documentSnapshot.getString(KEY_PLAFON);
                        String dinding          = documentSnapshot.getString(KEY_DINDING);
                        String pintudanjendela  = documentSnapshot.getString(KEY_PINTUJEN);
                        String lantai           = documentSnapshot.getString(KEY_LANTAI);
                        String kapasitaslistrik = documentSnapshot.getString(KEY_KAPLISTRIK);
                        String sumberairbersih  = documentSnapshot.getString(KEY_SUMBERAIRB);
                        String telephone        = documentSnapshot.getString(KEY_TELEPHONE);
                        String airconditioning  = documentSnapshot.getString(KEY_AC);
                        //D.
                        String pagar            = documentSnapshot.getString(KEY_PAGAR);
                        String perkerasan       = documentSnapshot.getString(KEY_PERKERASAN);
                        String kanopi           = documentSnapshot.getString(KEY_KANOPI);
                        //E.
                        String keteranganlain   = documentSnapshot.getString(KEY_KETERLAINN);
                        //F. Foto
                        String fotodepan        = documentSnapshot.getString(KEY_FOTODEPAN);
                        String fotodalam        = documentSnapshot.getString(KEY_FOTODALAM);
                        String fotodijual       = documentSnapshot.getString(KEY_FOTODIJUAL);
                        String fotojalan        = documentSnapshot.getString(KEY_FOTOJALAN);


                        Map<String, Object> user = documentSnapshot.getData();

                        HSSFWorkbook Workbook = new HSSFWorkbook();
                        HSSFSheet Sheet = Workbook.createSheet();
                        //final FileInputStream stream = new FileInputStream (fotodepan);
                        //final int pictureIndex = Workbook.addPicture(IOUtils.toByteArray(stream), Workbook.PICTURE_TYPE_PNG);


                        HSSFRow row0    = Sheet.createRow(0);
                        HSSFCell c0r0  = row0.createCell(1);
                        HSSFCell c1r0  = row0.createCell(2);
                        HSSFCell c2r0  = row0.createCell(3);
                        c0r0.setCellValue("Tanggal");
                        c1r0.setCellValue(":");
                        c2r0.setCellValue(tanggal);

                        HSSFRow hssfRow = Sheet.createRow(1);
                        HSSFCell hssfCell = hssfRow.createCell(2);
                        hssfCell.setCellValue("Form Data Properti");

                        HSSFRow row2 = Sheet.createRow(2);
                        HSSFCell c1r2 = row2.createCell(1);
                        HSSFCell c2r2 = row2.createCell(2);
                        c1r2.setCellValue("Foto ...");
                        c2r2.setCellValue("Foto ...");
                        HSSFRow row3 = Sheet.createRow(3);
                        HSSFCell c1r3 = row3.createCell(1);
                        HSSFCell c2r3 = row3.createCell(2);
                        c1r3.setCellValue("Foto ...");
                        c2r3.setCellValue("Foto ...");

                        HSSFRow row4 = Sheet.createRow(4);
                        HSSFCell c2r4 = row4.createCell(2);
                        c2r4.setCellValue("Informasi Lokasi");

                        HSSFRow row5    = Sheet.createRow(5);
                        HSSFCell c1r5  = row5.createCell(1);
                        HSSFCell c2r5  = row5.createCell(2);
                        HSSFCell c3r5  = row5.createCell(3);
                        c1r5.setCellValue("Alamat");
                        c2r5.setCellValue(":");
                        c3r5.setCellValue(alamat);

                        HSSFRow row6    = Sheet.createRow(6);
                        HSSFCell c1r6  = row6.createCell(1);
                        HSSFCell c2r6  = row6.createCell(2);
                        HSSFCell c3r6  = row6.createCell(3);
                        c1r6.setCellValue("Koordinat");
                        c2r6.setCellValue(":");
                        c3r6.setCellValue(koordinat);

                        HSSFRow row7    = Sheet.createRow(7);
                        HSSFCell c1r7  = row7.createCell(1);
                        HSSFCell c2r7  = row7.createCell(2);
                        HSSFCell c3r7  = row7.createCell(3);
                        c1r7.setCellValue("Desa");
                        c2r7.setCellValue(":");
                        c3r7.setCellValue(desa);

                        HSSFRow row8    = Sheet.createRow(8);
                        HSSFCell c1r8  = row6.createCell(1);
                        HSSFCell c2r8  = row6.createCell(2);
                        HSSFCell c3r8  = row6.createCell(3);
                        c1r8.setCellValue("Kelurahan");
                        c2r8.setCellValue(":");
                        c3r8.setCellValue(kelurahan);

                        HSSFRow row9    = Sheet.createRow(9);
                        HSSFCell c1r9  = row9.createCell(1);
                        HSSFCell c2r9  = row9.createCell(2);
                        HSSFCell c3r9  = row9.createCell(3);
                        c1r9.setCellValue("kecamatan");
                        c2r9.setCellValue(":");
                        c3r9.setCellValue(kecamatan);

                        HSSFRow row10    = Sheet.createRow(10);
                        HSSFCell c1r10  = row10.createCell(1);
                        HSSFCell c2r10  = row10.createCell(2);
                        HSSFCell c3r10  = row10.createCell(3);
                        c1r10.setCellValue("kabupaten");
                        c2r10.setCellValue(":");
                        c3r10.setCellValue(kabupaten);

                        HSSFRow row11    = Sheet.createRow(11);
                        HSSFCell c1r11  = row11.createCell(1);
                        HSSFCell c2r11  = row11.createCell(2);
                        HSSFCell c3r11  = row11.createCell(3);
                        c1r11.setCellValue("kota");
                        c2r11.setCellValue(":");
                        c3r11.setCellValue(kota);

                        HSSFRow row12    = Sheet.createRow(12);
                        HSSFCell c1r12  = row12.createCell(1);
                        HSSFCell c2r12  = row12.createCell(2);
                        HSSFCell c3r12  = row12.createCell(3);
                        c1r12.setCellValue("provinsi");
                        c2r12.setCellValue(":");
                        c3r12.setCellValue(provinsi);

                        //Informasi Jual Sewa
                        HSSFRow row13 = Sheet.createRow(13);
                        HSSFCell c2r13 = row13.createCell(2);
                        c2r13.setCellValue("Informasi Jual Sewa");

                        HSSFRow row14    = Sheet.createRow(14);
                        HSSFCell c1r14  = row14.createCell(1);
                        HSSFCell c2r14  = row14.createCell(2);
                        HSSFCell c3r14  = row14.createCell(3);
                        c1r14.setCellValue("Jenis Properti");
                        c2r14.setCellValue(":");
                        c3r14.setCellValue(jenisproperti);

                        HSSFRow row15    = Sheet.createRow(15);
                        HSSFCell c1r15  = row15.createCell(1);
                        HSSFCell c2r15  = row15.createCell(2);
                        HSSFCell c3r15  = row15.createCell(3);
                        c1r15.setCellValue("Kondisi Penawaran");
                        c2r15.setCellValue(":");
                        c3r15.setCellValue(kondisipenawaran);

                        HSSFRow row16    = Sheet.createRow(16);
                        HSSFCell c1r16  = row16.createCell(1);
                        HSSFCell c2r16  = row16.createCell(2);
                        HSSFCell c3r16  = row16.createCell(3);
                        c1r16.setCellValue("Harga");
                        c2r16.setCellValue(":");
                        c3r16.setCellValue(harga);

                        HSSFRow row17    = Sheet.createRow(17);
                        HSSFCell c1r17  = row17.createCell(1);
                        HSSFCell c2r17  = row17.createCell(2);
                        HSSFCell c3r17  = row17.createCell(3);
                        c1r17.setCellValue("Mulai di Tawarkan");
                        c2r17.setCellValue(":");
                        c3r17.setCellValue(mulaidit);

                        HSSFRow row18    = Sheet.createRow(18);
                        HSSFCell c1r18  = row18.createCell(1);
                        HSSFCell c2r18  = row18.createCell(2);
                        HSSFCell c3r18  = row18.createCell(3);
                        c1r18.setCellValue("Nama Sumber Data");
                        c2r18.setCellValue(":");
                        c3r18.setCellValue(namasumberdata);

                        HSSFRow row19    = Sheet.createRow(19);
                        HSSFCell c1r19  = row19.createCell(1);
                        HSSFCell c2r19  = row19.createCell(2);
                        HSSFCell c3r19  = row19.createCell(3);
                        c1r19.setCellValue("Telepon");
                        c2r19.setCellValue(":");
                        c3r19.setCellValue(telepon);

                        //Informasi Properti
                        HSSFRow row20 = Sheet.createRow(20);
                        HSSFCell c2r20 = row20.createCell(2);
                        c2r20.setCellValue("Informasi Properti");
                        //A.Tanah
                        HSSFRow row21 = Sheet.createRow(21);
                        HSSFCell c2r21 = row21.createCell(1);
                        c2r21.setCellValue("A.Tanah");

                        HSSFRow row22    = Sheet.createRow(22);
                        HSSFCell c1r22  = row22.createCell(1);
                        HSSFCell c2r22  = row22.createCell(2);
                        HSSFCell c3r22  = row22.createCell(3);
                        c1r22.setCellValue("Jenis Tanah");
                        c2r22.setCellValue(":");
                        c3r22.setCellValue(jenistanah);

                        HSSFRow row23    = Sheet.createRow(23);
                        HSSFCell c1r23  = row23.createCell(1);
                        HSSFCell c2r23  = row23.createCell(2);
                        HSSFCell c3r23  = row23.createCell(3);
                        c1r23.setCellValue("Letak / Posisi Tanah");
                        c2r23.setCellValue(":");
                        c3r23.setCellValue(letaktanah);

                        HSSFRow row24    = Sheet.createRow(24);
                        HSSFCell c1r24  = row24.createCell(1);
                        HSSFCell c2r24  = row24.createCell(2);
                        HSSFCell c3r24  = row24.createCell(3);
                        c1r24.setCellValue("Lebar Depan / Muka");
                        c2r24.setCellValue(":");
                        c3r24.setCellValue(lebardepan);

                        HSSFRow row25    = Sheet.createRow(25);
                        HSSFCell c1r25  = row25.createCell(1);
                        HSSFCell c2r25  = row25.createCell(2);
                        HSSFCell c3r25  = row25.createCell(3);
                        c1r25.setCellValue("Topografi / Kontur Tanah");
                        c2r25.setCellValue(":");
                        c3r25.setCellValue(topografi);

                        HSSFRow row26    = Sheet.createRow(26);
                        HSSFCell c1r26  = row26.createCell(1);
                        HSSFCell c2r26  = row26.createCell(2);
                        HSSFCell c3r26  = row26.createCell(3);
                        c1r26.setCellValue("Tinggi Permukaan Tanah Terhadap Jalan / Elevasi");
                        c2r26.setCellValue(":");
                        c3r26.setCellValue(tinggipermukaan);

                        HSSFRow row27    = Sheet.createRow(27);
                        HSSFCell c1r27  = row27.createCell(1);
                        HSSFCell c2r27  = row27.createCell(2);
                        HSSFCell c3r27  = row27.createCell(3);
                        c1r27.setCellValue("Lebar Jalan Depan");
                        c2r27.setCellValue(":");
                        c3r27.setCellValue(lebarjalandepan);

                        HSSFRow row28    = Sheet.createRow(28);
                        HSSFCell c1r28  = row28.createCell(1);
                        HSSFCell c2r28  = row28.createCell(2);
                        HSSFCell c3r28  = row28.createCell(3);
                        c1r28.setCellValue("Jenis Perkerasan Jalan");
                        c2r28.setCellValue(":");
                        c3r28.setCellValue(jenisperkerasan);

                        HSSFRow row29    = Sheet.createRow(29);
                        HSSFCell c1r29  = row29.createCell(1);
                        HSSFCell c2r29  = row29.createCell(2);
                        HSSFCell c3r29  = row29.createCell(3);
                        c1r29.setCellValue("Kondisi Jalan");
                        c2r29.setCellValue(":");
                        c3r29.setCellValue(kondisijalan);

                        HSSFRow row30    = Sheet.createRow(30);
                        HSSFCell c1r30  = row30.createCell(1);
                        HSSFCell c2r30  = row30.createCell(2);
                        HSSFCell c3r30  = row30.createCell(3);
                        c1r30.setCellValue("Lingkungan Sekitar");
                        c2r30.setCellValue(":");
                        c3r30.setCellValue(lingkungansekit);

                        HSSFRow row31    = Sheet.createRow(31);
                        HSSFCell c1r31  = row31.createCell(1);
                        HSSFCell c2r31  = row31.createCell(2);
                        HSSFCell c3r31  = row31.createCell(3);
                        c1r31.setCellValue("Bentuk Tapak Tanah");
                        c2r31.setCellValue(":");
                        c3r31.setCellValue(bentuktapaktana);

                        HSSFRow row32    = Sheet.createRow(32);
                        HSSFCell c1r32  = row32.createCell(1);
                        HSSFCell c2r32  = row32.createCell(2);
                        HSSFCell c3r32  = row32.createCell(3);
                        c1r32.setCellValue("Luas Tanah");
                        c2r32.setCellValue(":");
                        c3r32.setCellValue(luastanah);

                        HSSFRow row33    = Sheet.createRow(33);
                        HSSFCell c1r33  = row33.createCell(1);
                        HSSFCell c2r33  = row33.createCell(2);
                        HSSFCell c3r33  = row33.createCell(3);
                        c1r33.setCellValue("Legalitas / Dokumen Tanah");
                        c2r33.setCellValue(":");
                        c3r33.setCellValue(legalitastanah);

                        //B.Bangunan
                        HSSFRow row34 = Sheet.createRow(34);
                        HSSFCell c2r34 = row34.createCell(1);
                        c2r34.setCellValue("B. Bangunan");

                        HSSFRow row35    = Sheet.createRow(35);
                        HSSFCell c1r35  = row35.createCell(1);
                        HSSFCell c2r35  = row35.createCell(2);
                        HSSFCell c3r35  = row35.createCell(3);
                        c1r35.setCellValue("Tipe Bangunan");
                        c2r35.setCellValue(":");
                        c3r35.setCellValue(tipebang);

                        HSSFRow row36    = Sheet.createRow(36);
                        HSSFCell c1r36  = row36.createCell(1);
                        HSSFCell c2r36  = row36.createCell(2);
                        HSSFCell c3r36  = row36.createCell(3);
                        c1r36.setCellValue("Jumlah Lantai");
                        c2r36.setCellValue(":");
                        c3r36.setCellValue(jumlahlan);

                        HSSFRow row37    = Sheet.createRow(37);
                        HSSFCell c1r37  = row37.createCell(1);
                        HSSFCell c2r37  = row37.createCell(2);
                        HSSFCell c3r37  = row37.createCell(3);
                        c1r37.setCellValue("Jarak Dinding Terluar ke Jalan");
                        c2r37.setCellValue(":");
                        c3r37.setCellValue(jarakdin);

                        HSSFRow row38    = Sheet.createRow(38);
                        HSSFCell c1r38  = row38.createCell(1);
                        HSSFCell c2r38  = row38.createCell(2);
                        HSSFCell c3r38  = row38.createCell(3);
                        c1r38.setCellValue("Fungsi Bangunan");
                        c2r38.setCellValue(":");
                        c3r38.setCellValue(fungsibang);

                        HSSFRow row39    = Sheet.createRow(39);
                        HSSFCell c1r39  = row39.createCell(1);
                        HSSFCell c2r39  = row39.createCell(2);
                        HSSFCell c3r39  = row39.createCell(3);
                        c1r39.setCellValue("Luas Bangunan");
                        c2r39.setCellValue(":");
                        c3r39.setCellValue(luasbang);

                        HSSFRow row40    = Sheet.createRow(40);
                        HSSFCell c1r40  = row40.createCell(1);
                        HSSFCell c2r40  = row40.createCell(2);
                        HSSFCell c3r40  = row40.createCell(3);
                        c1r40.setCellValue("Tahun Dibangun");
                        c2r40.setCellValue(":");
                        c3r40.setCellValue(tahundibang);

                        HSSFRow row41    = Sheet.createRow(41);
                        HSSFCell c1r41  = row41.createCell(1);
                        HSSFCell c2r41  = row41.createCell(2);
                        HSSFCell c3r41  = row41.createCell(3);
                        c1r41.setCellValue("Legalitas Bangunan / IMB");
                        c2r41.setCellValue(":");
                        c3r41.setCellValue(imb);

                        HSSFRow row42    = Sheet.createRow(42);
                        HSSFCell c1r42  = row42.createCell(1);
                        HSSFCell c2r42  = row42.createCell(2);
                        HSSFCell c3r42  = row42.createCell(3);
                        c1r42.setCellValue("Tahun Renovasi");
                        c2r42.setCellValue(":");
                        c3r42.setCellValue(tahunrenov);

                        //C.Spesifikasi Bangunan dan Utilitas
                        HSSFRow row43 = Sheet.createRow(43);
                        HSSFCell c2r43 = row43.createCell(1);
                        c2r43.setCellValue("C.Spesifikasi Bangunan dan Utilitas");

                        HSSFRow row44    = Sheet.createRow(44);
                        HSSFCell c1r44  = row44.createCell(1);
                        HSSFCell c2r44  = row44.createCell(2);
                        HSSFCell c3r44  = row44.createCell(3);
                        c1r44.setCellValue("Pondasi");
                        c2r44.setCellValue(":");
                        c3r44.setCellValue(pondasi);

                        HSSFRow row45    = Sheet.createRow(45);
                        HSSFCell c1r45  = row45.createCell(1);
                        HSSFCell c2r45  = row45.createCell(2);
                        HSSFCell c3r45  = row45.createCell(3);
                        c1r45.setCellValue("Struktur");
                        c2r45.setCellValue(":");
                        c3r45.setCellValue(struktur);

                        HSSFRow row46    = Sheet.createRow(46);
                        HSSFCell c1r46  = row46.createCell(1);
                        HSSFCell c2r46  = row46.createCell(2);
                        HSSFCell c3r46  = row46.createCell(3);
                        c1r46.setCellValue("Rangka Atap");
                        c2r46.setCellValue(":");
                        c3r46.setCellValue(rangkaatap);

                        HSSFRow row47    = Sheet.createRow(47);
                        HSSFCell c1r47  = row47.createCell(1);
                        HSSFCell c2r47  = row47.createCell(2);
                        HSSFCell c3r47  = row47.createCell(3);
                        c1r47.setCellValue("Penutup Atap");
                        c2r47.setCellValue(":");
                        c3r47.setCellValue(penutupatap);

                        HSSFRow row48    = Sheet.createRow(48);
                        HSSFCell c1r48  = row48.createCell(1);
                        HSSFCell c2r48  = row48.createCell(2);
                        HSSFCell c3r48  = row48.createCell(3);
                        c1r48.setCellValue("Plafon");
                        c2r48.setCellValue(":");
                        c3r48.setCellValue(plafon);

                        HSSFRow row49    = Sheet.createRow(49);
                        HSSFCell c1r49  = row49.createCell(1);
                        HSSFCell c2r49  = row49.createCell(2);
                        HSSFCell c3r49  = row49.createCell(3);
                        c1r49.setCellValue("Dinding");
                        c2r49.setCellValue(":");
                        c3r49.setCellValue(dinding);

                        HSSFRow row50    = Sheet.createRow(50);
                        HSSFCell c1r50  = row50.createCell(1);
                        HSSFCell c2r50  = row50.createCell(2);
                        HSSFCell c3r50  = row50.createCell(3);
                        c1r50.setCellValue("Pintu dan Jendela");
                        c2r50.setCellValue(":");
                        c3r50.setCellValue(pintudanjendela);

                        HSSFRow row51    = Sheet.createRow(51);
                        HSSFCell c1r51  = row51.createCell(1);
                        HSSFCell c2r51  = row51.createCell(2);
                        HSSFCell c3r51  = row51.createCell(3);
                        c1r51.setCellValue("Lantai");
                        c2r51.setCellValue(":");
                        c3r51.setCellValue(lantai);

                        HSSFRow row52    = Sheet.createRow(52);
                        HSSFCell c1r52  = row52.createCell(1);
                        HSSFCell c2r52  = row52.createCell(2);
                        HSSFCell c3r52  = row52.createCell(3);
                        c1r52.setCellValue("Kapasitas Listrik");
                        c2r52.setCellValue(":");
                        c3r52.setCellValue(kapasitaslistrik);

                        HSSFRow row53    = Sheet.createRow(53);
                        HSSFCell c1r53  = row53.createCell(1);
                        HSSFCell c2r53  = row53.createCell(2);
                        HSSFCell c3r53  = row53.createCell(3);
                        c1r53.setCellValue("Sumber Air Bersih");
                        c2r53.setCellValue(":");
                        c3r53.setCellValue(sumberairbersih);

                        HSSFRow row54    = Sheet.createRow(54);
                        HSSFCell c1r54  = row54.createCell(1);
                        HSSFCell c2r54  = row54.createCell(2);
                        HSSFCell c3r54  = row54.createCell(3);
                        c1r54.setCellValue("Telephone");
                        c2r54.setCellValue(":");
                        c3r54.setCellValue(telephone);

                        HSSFRow row55    = Sheet.createRow(55);
                        HSSFCell c1r55  = row55.createCell(1);
                        HSSFCell c2r55  = row55.createCell(2);
                        HSSFCell c3r55  = row55.createCell(3);
                        c1r55.setCellValue("Air Conditioning / AC");
                        c2r55.setCellValue(":");
                        c3r55.setCellValue(airconditioning);

                        //D.Sarana Pelengkap
                        HSSFRow row56 = Sheet.createRow(56);
                        HSSFCell c2r56 = row56.createCell(1);
                        c2r56.setCellValue("D.Sarana Pelengkap");

                        HSSFRow row57    = Sheet.createRow(57);
                        HSSFCell c1r57  = row57.createCell(1);
                        HSSFCell c2r57  = row57.createCell(2);
                        HSSFCell c3r57  = row57.createCell(3);
                        c1r57.setCellValue("Pagar");
                        c2r57.setCellValue(":");
                        c3r57.setCellValue(pagar);

                        HSSFRow row58    = Sheet.createRow(58);
                        HSSFCell c1r58  = row58.createCell(1);
                        HSSFCell c2r58  = row58.createCell(2);
                        HSSFCell c3r58  = row58.createCell(3);
                        c1r58.setCellValue("Perkerasan");
                        c2r58.setCellValue(":");
                        c3r58.setCellValue(perkerasan);

                        HSSFRow row59    = Sheet.createRow(59);
                        HSSFCell c1r59  = row59.createCell(1);
                        HSSFCell c2r59  = row59.createCell(2);
                        HSSFCell c3r59  = row59.createCell(3);
                        c1r59.setCellValue("Kanopi");
                        c2r59.setCellValue(":");
                        c3r59.setCellValue(kanopi);

                        //E.Keterangan Lainnya
                        HSSFRow row60 = Sheet.createRow(60);
                        HSSFCell c2r60 = row60.createCell(1);
                        c2r60.setCellValue("E.Keterangan Lainnya");

                        HSSFRow row61    = Sheet.createRow(61);
                        HSSFCell c1r61  = row61.createCell(1);
                        HSSFCell c2r61  = row61.createCell(2);
                        HSSFCell c3r61  = row61.createCell(3);
                        c1r61.setCellValue("Keterangan Lainnya");
                        c2r61.setCellValue(":");
                        c3r61.setCellValue(keteranganlain);


                        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
/*                        File myDir = new File(root + "/DataProperti");
                        myDir.mkdirs();*/

                        String fname    = alamat +".xls";
                        File file = new File(root, fname);
                        if (file.exists())
                            file.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            Workbook.write(file);
                            out.flush();
                            out.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "Document Created", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Document not exist !", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            Log.d("TAG", e.toString());
        });

    }

    public void deleteData(){

        //Firebase Database (Firestore)
        FirebaseAuth fAuth  = FirebaseAuth.getInstance();
        fStore              = FirebaseFirestore.getInstance();
        FirebaseUser fUser  = fAuth.getCurrentUser();
        //username            = fUser.getDisplayName();
        Email               = fUser.getEmail();
        String timestampkey   = String.valueOf(dtp.getTimestamp());

        DocumentReference userdata = fStore.collection(String.valueOf(Email)).document("userdata").collection("input").document(timestampkey);

        userdata.delete().addOnSuccessListener(sec->{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(fail->{
            Toast.makeText(context, ""+fail.getMessage(), Toast.LENGTH_SHORT).show();
        });


        //Firebase Storage (Photo)
        String alamat             = String.valueOf(dtp.getAlamat());
        StorageReference sStore   = FirebaseStorage.getInstance().getReference().child("Data Properti");
        final String fotodepan  = alamat + "_" + "_fotodepan" + timestampkey;
        final String fotojalan  = alamat + "_"+ "fotojalan" + timestampkey;
        final String fotodalam  = alamat + "_" + "fotodalam" + timestampkey;
        final String fotodijual  = alamat + "_" + "fotodijual" + timestampkey;
        final String fotolain  = alamat + "_" + "fotolain" + timestampkey;
        final String fotolain2  = alamat + "_" + "fotolain2" + timestampkey;

        StorageReference fotodepancl   = sStore.child(Email).child(alamat).child(fotodepan);
        StorageReference fotojalancl   = sStore.child(Email).child(alamat).child(fotojalan);
        StorageReference fotodalamcl   = sStore.child(Email).child(alamat).child(fotodalam);
        StorageReference fotodijualcl   = sStore.child(Email).child(alamat).child(fotodijual);
        StorageReference fotolaincl   = sStore.child(Email).child(alamat).child(fotolain);
        StorageReference fotolaincl2   = sStore.child(Email).child(alamat).child(fotolain2);

        fotodepancl.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

        fotojalancl.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

        fotodalamcl.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

        fotodijualcl.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

        fotolaincl.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

        fotolaincl2.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });

    }

}
