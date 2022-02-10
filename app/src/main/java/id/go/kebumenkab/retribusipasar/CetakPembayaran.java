package id.go.kebumenkab.retribusipasar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CetakPembayaran extends AppCompatActivity {
    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_pembayaran);
    }

    public void doPrint(View view) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, CetakPembayaran.PERMISSION_BLUETOOTH);
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
                            "[L]<b>Retribusi Pasar Kebumen</b>\n" +
                            "[L]    1 kios[R]" + nf.format(25000) + "\n" +
                            "[C]--------------------------------\n" +
                            "[L]TOTAL[R]" + nf.format(25000) + "\n" +
                            "[L]DENDA[R]" + nf.format(0) + "\n" +
                            "[L]<b>GRAND TOTAL[R]" + nf.format(25000) + "</b>\n" +
                            "[C]--------------------------------\n" +
                            "[C]<barcode type='ean13' height='10'>202109280005</barcode>\n" +
                            "[C]--------------------------------\n" +
                            "[C]Terimakasih Atas Pembayarannya\n" +
                            "[L]\n" +
                            "[L]<qrcode>https://skrdpasar.kebumenkab.go.id/</qrcode>\n";

                    printer.printFormattedText(text);
                } else {
                    Toast.makeText(this, "No printer was connected!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }
}