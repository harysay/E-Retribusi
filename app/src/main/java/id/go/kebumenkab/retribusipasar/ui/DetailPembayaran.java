package id.go.kebumenkab.retribusipasar.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import id.go.kebumenkab.retribusipasar.R;
import id.go.kebumenkab.retribusipasar.ScanQRCode;
import id.go.kebumenkab.retribusipasar.handler.JenisRetribusi;
import id.go.kebumenkab.retribusipasar.handler.Server;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.gpfreetech.awesomescanner.ui.GpCodeScanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class DetailPembayaran extends AppCompatActivity {
//    private GpCodeScanner mCodeScanner;
    public static final int PERMISSION_BLUETOOTH = 1;
    Button btnCetak;
    private TextView tv_blok,tv_pemilik,tv_jenisRetribusi,tv_tagihanPerJenis, tv_totalTagihan,totalDibayarkan,tv_periodeBayar,tv_jumlahDipilih;
    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
    String userId, getblok,getpemilik,getnpwrd,getjenisretribusi,getjumlahjenispilih,getjenisretribusinama,gettahihanharian,gettagihanbulanan,getperiodebayar,getjumlahtagihan,totalBayar;
    Bundle extrasDariScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran);
        extrasDariScan = getIntent().getExtras();
//        userId = extrasDariScan.getString("usrid");
        getblok = extrasDariScan.getString("blok");
        getpemilik = extrasDariScan.getString("pemilik");
        getnpwrd = extrasDariScan.getString("npwrdpedagang");
        getjumlahjenispilih = extrasDariScan.getString("jumlahjenispilih");
        getjumlahtagihan = extrasDariScan.getString("jumlahtagihan");
        getjenisretribusinama = extrasDariScan.getString("jenisretribusinama");
        gettahihanharian = extrasDariScan.getString("tagihanharian");
        gettagihanbulanan = extrasDariScan.getString("tagihanbulanan");
        getperiodebayar = extrasDariScan.getString("periodebayar");
        totalBayar = extrasDariScan.getString("totalbayar");

        tv_blok = (TextView) findViewById(R.id.blokPasar);
        tv_pemilik = (TextView) findViewById(R.id.pemilikBlok);
        tv_jenisRetribusi = (TextView) findViewById(R.id.jenisRetribusi);
        tv_tagihanPerJenis = (TextView) findViewById(R.id.tagihanBerdasarJenis);
        tv_totalTagihan = (TextView) findViewById(R.id.totalTagihan);
        totalDibayarkan = (TextView) findViewById(R.id.totalDibayar);
        tv_jumlahDipilih = (TextView) findViewById(R.id.jumlahDipilih);
        tv_periodeBayar = (TextView) findViewById(R.id.periodeBayar);
        btnCetak = (Button) findViewById(R.id.btnCetak);


        tv_blok.setText(getblok);
        tv_pemilik.setText(getpemilik);
        tv_jenisRetribusi.setText(getjenisretribusinama);
        tv_jumlahDipilih.setText(getjumlahjenispilih);
        tv_periodeBayar.setText(getperiodebayar);
        if(getjenisretribusinama.equals("Harian")){
            tv_tagihanPerJenis.setText(gettahihanharian);
        }else if(getjenisretribusinama.equals("Bulanan")){
            tv_tagihanPerJenis.setText(gettagihanbulanan);
        }

        tv_totalTagihan.setText(getjumlahtagihan);
        totalDibayarkan.setText(totalBayar);


        btnCetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cetakNota(getnpwrd,getpemilik,getblok,getjenisretribusinama,totalBayar,getjumlahjenispilih,tv_tagihanPerJenis.getText().toString(),getperiodebayar);
            }
        });

    }

    public void cetakNota(String npwrdku,String namaPemilik,String blok,String namaJenisRet,String jumlahBayar,String jumlahJanisPilihan,String tagihanPerJenis,String periodeBayar) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, DetailPembayaran.PERMISSION_BLUETOOTH);
            } else {
                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
                if (connection != null) {
                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);
                    final String text = "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer,
                            this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.logokbmretribusipasar,
                                    DisplayMetrics.DENSITY_LOW, getTheme())) + "</img>\n" +
                            "[L]\n" +
                            "[L]" + df.format(new Date()) + "\n" +
                            "[C]================================\n" +
                            "[L]<b>"+namaPemilik+"</b>\n" +
                            "[L]Blok: " +blok+ "\n" +
                            "[L]Retribusi: "+ namaJenisRet + "\n" +
                            "[L]"+jumlahJanisPilihan+" x "+tagihanPerJenis+"[R]"+ nf.format(Integer.parseInt(jumlahBayar)) + "\n" +
                            "[C]--------------------------------\n" +
                            "[L]<b>TOTAL[R]" + nf.format(Integer.parseInt(jumlahBayar)) + "</b>\n" +
                            "[C]--------------------------------\n" +
                            "[C].:Periode Bayar:.\n" +
                            "[C]"+ periodeBayar + "\n" +
                            "[C]Terimakasih Atas Pembayarannya\n" +
                            "[L]\n" +
                            "[C]<qrcode>"+npwrdku+"</qrcode>\n";

                    printer.printFormattedText(text);
                } else {
                    Toast.makeText(this, "No printer was connected!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }
//    public void bayar(String usrID, String noNpwrd, String jenisRetribusi, String jumlahBayar, String token){
//        AndroidNetworking.post(Server.URL+"bayarretribusi")
//                .setTag("test")
//                .addBodyParameter("user_id", usrID)
//                .addBodyParameter("npwrd", noNpwrd)
//                .addBodyParameter("jenis_retribusi", jenisRetribusi)
//                .addBodyParameter("jumlah_bayar", jumlahBayar)
//                .addHeaders("Authorization", token)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i("tag-response",response.toString());
//                        if (response == null) {
//                            Toast.makeText(DetailPembayaran.this, "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
//                            return;
//                        }else {
//                            try {
//                                JSONObject jsonPost = new JSONObject(response.toString());
//                                String status = jsonPost.getString("status");
//                                String pesan = jsonPost.getString("message");
//                                if(status.equals("success")){
//                                    totalBayar = jsonPost.getString("total_bayar");
//                                    totalDibayarkan.setText(totalBayar);
//                                }else {
//                                    Toast.makeText(DetailPembayaran.this, pesan, Toast.LENGTH_LONG).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Log.i("tag-error",anError.getResponse().toString());
//                    }
//                });
//    }
}