package id.go.kebumenkab.retribusipasar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import id.go.kebumenkab.retribusipasar.handler.BerdasarJenisSpinnerAdapter;
import id.go.kebumenkab.retribusipasar.handler.JenisRetribusi;
import id.go.kebumenkab.retribusipasar.handler.JumlahBerdasarJenis;
import id.go.kebumenkab.retribusipasar.handler.Server;
import id.go.kebumenkab.retribusipasar.handler.JenisRetribusiSpinnerAdapter;
import id.go.kebumenkab.retribusipasar.ui.DetailPembayaran;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.zxing.Result;
import com.gpfreetech.awesomescanner.ui.ScannerView;
import com.gpfreetech.awesomescanner.ui.GpCodeScanner;
import com.gpfreetech.awesomescanner.util.DecodeCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class ScanQRCode extends AppCompatActivity {

    private TextView txtScanTextNama,txtScanTextBlok,txtScanTextTotalHarian,txtScanTextTglTerakhirBayar;
    Button btnBayar;
    private GpCodeScanner mCodeScanner;
    public static final int PERMISSION_BLUETOOTH = 1;
    String npwrdPedagang,pedagangID,pedagangNama,pedagangBlok,pedagangRetPerhari,pedagangRetPerbulan,getIdJenisRetribusi,getNamaJenisRetribusi,pedagangNpwrdBaru,getIdJumPilih,getNameJumPilih,jumlahTagihan,totalBayar;
    ProgressBar mProgressAuth;
    String status;
    private ProgressDialog progressBar;
    Spinner spinnerJenisRetribusi,spinnerJumlahBerdasarJenis;
    String stat, tokenlog, pedagang,msgLogin;
    SimpleDateFormat myFormatTanggal = new SimpleDateFormat("yyyy-MM-dd");

//    private final Locale locale = new Locale("id", "ID");
//    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale);
//    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        SharedPreferences mSettings = this.getSharedPreferences("logSession", Context.MODE_PRIVATE);
        String usrIDSession = mSettings.getString("usrID","2");
        String usrTokenSession = mSettings.getString("returnTokenCode","2");
        txtScanTextNama = findViewById(R.id.textNama);
        txtScanTextBlok = findViewById(R.id.textBlok);
        txtScanTextTotalHarian  = findViewById(R.id.textTotalHarian);
        spinnerJenisRetribusi = (Spinner) findViewById(R.id.spinnerJenisRetribusi);
        spinnerJumlahBerdasarJenis = (Spinner) findViewById(R.id.spinnerJumlahBerdasarkanJenis);
        txtScanTextTglTerakhirBayar = findViewById(R.id.textTglTerakhirBayar);
        mProgressAuth = (ProgressBar) findViewById(R.id.scan_progress);
        btnBayar =  (Button) findViewById(R.id.btnBayar);
//        initializeUI();
        spinnerJenisRetribusi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                JenisRetribusi kategori = (JenisRetribusi) parent.getSelectedItem();
                getIdJenisRetribusi = kategori.getId();
                getNamaJenisRetribusi = kategori.getNama();
//                Toast.makeText(FormPendaftaran.this, "Country ID: "+kategori.getId()+",  Country Name : "+kategori.getNama(), Toast.LENGTH_SHORT).show();
                if(getNamaJenisRetribusi.equals("Harian")){
                    pilihRetribusi(usrIDSession, npwrdPedagang, getIdJenisRetribusi, usrTokenSession);
                }else if(getNamaJenisRetribusi.equals("Bulanan")){
                    pilihBulanan();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerJumlahBerdasarJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JumlahBerdasarJenis jumJenis = (JumlahBerdasarJenis) parent.getSelectedItem();
                getIdJumPilih = jumJenis.getJumlahid();
                getNameJumPilih = jumJenis.getJumlahname();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bayar(usrIDSession, npwrdPedagang, getIdJenisRetribusi, getIdJumPilih, usrTokenSession);
//                cetakNota(txtScanTextNama.getText().toString(),txtScanTextBlok.getText().toString(),getIdJenisRetribusi,getNamaJenisRetribusi);
            }
        });

        RequestCameraPermission requestCameraPermission=new RequestCameraPermission(this);

        if(requestCameraPermission.verifyCameraPermission()) {

            ScannerView scannerView = findViewById(R.id.scanner_view);
            mCodeScanner = new GpCodeScanner(this, scannerView);
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            npwrdPedagang = result.getText();
                            Toast.makeText(getApplicationContext(), npwrdPedagang, Toast.LENGTH_SHORT).show();
                            getTagihan(usrIDSession,npwrdPedagang,usrTokenSession);
                            pilihRetribusi(usrIDSession, npwrdPedagang, getIdJenisRetribusi, usrTokenSession);
                        }
                    });
                }
            });

            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCodeScanner.startPreview();
                }
            });

        }else{
            Toast.makeText(getApplicationContext(),"Camera Permission required", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeUI() {
        ArrayList<JenisRetribusi> contacts = new ArrayList<>();
        contacts.add(new JenisRetribusi("1", "Harian"));
        contacts.add(new JenisRetribusi("2", "Bulanan"));
        ArrayAdapter<JenisRetribusi> adapter =
                new ArrayAdapter<JenisRetribusi>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, contacts);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerJenisRetribusi.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCodeScanner!=null) {
            mCodeScanner.startPreview();
        }
    }

    @Override
    protected void onPause() {
        if(mCodeScanner!=null) {
            mCodeScanner.releaseResources();
        }
        super.onPause();
    }

//    public void cetakNota(String hasilScan,String blok,String idJenisRet,String namaJenisRet) {
//        try {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, ScanQRCode.PERMISSION_BLUETOOTH);
//            } else {
//                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
//                if (connection != null) {
//                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);
//                    final String text = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,
//                            this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logokbmretribusipasar,
//                                    DisplayMetrics.DENSITY_LOW, getTheme())) + "</img>\n" +
//                            "[L]\n" +
//                            "[L]" + df.format(new Date()) + "\n" +
//                            "[C]================================\n" +
//                            "[L]<b>"+hasilScan+"</b>\n" +
//                            "[L]    "+blok+"" + nf.format(25000) + "\n" +
//                            "[C]--------------------------------\n" +
//                            "[L]TOTAL[R]" + nf.format(25000) + "\n" +
//                            "[L]DENDA[R]" + nf.format(0) + "\n" +
//                            "[L]<b>GRAND TOTAL[R]" + nf.format(25000) + "</b>\n" +
//                            "[C]--------------------------------\n" +
//                            "[C]Terimakasih Atas Pembayarannya\n" +
//                            "[L]\n" +
//                            "[C]<qrcode>330510000101172</qrcode>\n";
//
//                    printer.printFormattedText(text);
//                } else {
//                    Toast.makeText(this, "No printer was connected!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        } catch (Exception e) {
//            Log.e("APP", "Can't print", e);
//        }
//    }

    public void getTagihan(String usrID, String noNpwrd, String token){
        AndroidNetworking.post(Server.URL+"absenpedagang")
                .setTag("test")
                .addBodyParameter("userid", usrID)
                .addBodyParameter("npwrd", noNpwrd)
                .addHeaders("Authorization", token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("tag-response",response.toString());
                        if (response == null) {
                            Toast.makeText(ScanQRCode.this, "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }else {
                        try {
                            JSONObject jsonPost = new JSONObject(response.toString());
                            status = jsonPost.getString("status");
                            String pesan = jsonPost.getString("message");
                            if(status.equals("success")){
                                JSONObject getDetailPedagang = jsonPost.getJSONObject("pedagang");
                                pedagangID = getDetailPedagang.getString("id");
                                pedagangNpwrdBaru = getDetailPedagang.getString("npwrd_baru");
                                pedagangNama = getDetailPedagang.getString("nama");
                                pedagangBlok = getDetailPedagang.getString("blok");
                                pedagangRetPerhari = getDetailPedagang.getString("retribusi_perhari");
                                pedagangRetPerbulan = getDetailPedagang.getString("retribusi_perbulan");

                                txtScanTextNama.setText(pedagangNama);
                                txtScanTextBlok.setText(pedagangBlok);
                                txtScanTextTotalHarian.setText(pedagangRetPerhari);


                                JSONObject getJenisRetribusi = jsonPost.getJSONObject("jenis_retribusi");
                                Iterator<String> iter = getJenisRetribusi.keys();
                                ArrayList<JenisRetribusi> jenisList = new ArrayList<>();
                                String[] array = new String[jenisList.size()];
                                int index = 0;
                                for (Object value : jenisList) {
                                    array[index] = (String) value;
                                    index++;
                                }
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    String value = getJenisRetribusi.optString(key);
                                    jenisList.add(new JenisRetribusi(key, value));
//                                    ArrayAdapter<JenisRetribusi> adapter = new ArrayAdapter<JenisRetribusi>(ScanQRCode.this, android.R.layout.simple_spinner_dropdown_item, jenisList);
//                                    spinnerJenisRetribusi.setAdapter(adapter);

                                    final JenisRetribusiSpinnerAdapter jenisRetAdapter = new JenisRetribusiSpinnerAdapter(ScanQRCode.this, R.layout.data_jenis_retribusi_list,R.id.jenisretribusiSpinnerText,jenisList);
                                    spinnerJenisRetribusi.setAdapter(jenisRetAdapter);
                                }

                            }else {
                                btnBayar.setEnabled(false);
                                Toast.makeText(ScanQRCode.this, pesan, Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("tag-error",anError.getResponse().toString());
                    }
                });
    }

    public void pilihRetribusi(String usrID, String noNpwrd, String jenisRetribusi, String token){
        AndroidNetworking.post(Server.URL+"pilihretribusi")
                .setTag("test")
                .addBodyParameter("user_id", usrID)
                .addBodyParameter("npwrd", noNpwrd)
                .addBodyParameter("jenis_retribusi", jenisRetribusi)
                .addHeaders("Authorization", token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("tag-response",response.toString());
                        if (response == null) {
                            Toast.makeText(ScanQRCode.this, "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            try {
                                JSONObject jsonPost = new JSONObject(response.toString());
                                String status = jsonPost.getString("status");
                                String pesan = jsonPost.getString("message");
                                if(status.equals("success")){
                                    JSONObject tglTerakhirBayar = jsonPost.getJSONObject("tanggal_terakahir_bayar");
                                    String tglAkhirBayar = tglTerakhirBayar.getString("date");
                                    String jumHari = jsonPost.getString("jumlah_hari");
                                    jumlahTagihan = jsonPost.getString("jumlah_tagihan");
                                    ArrayList<JumlahBerdasarJenis> jumlahHari = new ArrayList<>();

                                    for (int i = 0; i < Integer.parseInt(jumHari); i++) {
                                        int harinya = i+1;
                                        jumlahHari.add(new JumlahBerdasarJenis(harinya + " Hari" , String.valueOf(harinya)));
                                    }

                                    ArrayAdapter<JumlahBerdasarJenis> adapter2 = new ArrayAdapter<JumlahBerdasarJenis>(ScanQRCode.this,  android.R.layout.simple_spinner_dropdown_item, jumlahHari);
                                    adapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                                    spinnerJumlahBerdasarJenis.setAdapter(adapter2);

//                                    final BerdasarJenisSpinnerAdapter adapter2 = new BerdasarJenisSpinnerAdapter(ScanQRCode.this, R.layout.data_jenis_retribusi_list,R.id.jenisretribusiSpinnerText,jumlahHari);
//                                    spinnerJenisRetribusi.setAdapter(adapter2);
                                    try {
                                        String reformattedStr = myFormatTanggal.format(myFormatTanggal.parse(tglAkhirBayar));
                                        txtScanTextTglTerakhirBayar.setText(reformattedStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }else {
                                    Toast.makeText(ScanQRCode.this, pesan, Toast.LENGTH_LONG).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("tag-error",anError.getResponse().toString());
                    }
                });
    }

    public void bayar(String usrID, String noNpwrd, String jenisRetribusi, String jumlahBayar, String token){
        AndroidNetworking.post(Server.URL+"bayarretribusi")
                .setTag("test")
                .addBodyParameter("user_id", usrID)
                .addBodyParameter("npwrd", noNpwrd)
                .addBodyParameter("jenis_retribusi", jenisRetribusi)
                .addBodyParameter("jumlah_bayar", jumlahBayar)
                .addHeaders("Authorization", token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("tag-response",response.toString());
                        if (response == null) {
                            Toast.makeText(ScanQRCode.this, "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            try {
                                JSONObject jsonPost = new JSONObject(response.toString());
                                String status = jsonPost.getString("status");
                                String pesan = jsonPost.getString("message");
                                if(status.equals("success")){
                                    totalBayar = jsonPost.getString("total_bayar");
                                    String tglawal = jsonPost.getString("tanggal_awal");
                                    String tglakhir = jsonPost.getString("tanggal_akhir");
                    //                  totalDibayarkan.setText(totalBayar);
                                    Intent keDetail = new Intent(ScanQRCode.this, DetailPembayaran.class);
                    //                keDetail.putExtra("usrid", usrIDSession);
                                    keDetail.putExtra("blok", txtScanTextBlok.getText().toString());
                                    keDetail.putExtra("pemilik", txtScanTextNama.getText().toString());
                                    keDetail.putExtra("npwrdpedagang", npwrdPedagang);
                                    keDetail.putExtra("jumlahjenispilih", getIdJumPilih);
                                    keDetail.putExtra("jumlahtagihan", jumlahTagihan);
                                    keDetail.putExtra("jenisretribusinama", getNamaJenisRetribusi);
                                    keDetail.putExtra("tagihanharian", pedagangRetPerhari);
                                    keDetail.putExtra("tagihanbulanan", pedagangRetPerbulan);
                                    //keDetail.putExtra("tglterakhirbayar", txtScanTextTglTerakhirBayar.getText().toString());
                                    keDetail.putExtra("totalbayar", totalBayar);
                                    keDetail.putExtra("periodebayar", tglawal+" sampai "+tglakhir);
                                    startActivity(keDetail);
                                }else {
                                    Toast.makeText(ScanQRCode.this, pesan, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("tag-error",anError.getResponse().toString());
                    }
                });
    }

    public void pilihBulanan(){
        ArrayList<JumlahBerdasarJenis> jumlahHari = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            jumlahHari.add(new JumlahBerdasarJenis(i + " Bulan" , String.valueOf(i)));
        }

        ArrayAdapter<JumlahBerdasarJenis> adapter2 = new ArrayAdapter<JumlahBerdasarJenis>(ScanQRCode.this,  android.R.layout.simple_spinner_dropdown_item, jumlahHari);
        adapter2.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerJumlahBerdasarJenis.setAdapter(adapter2);
    }

}
