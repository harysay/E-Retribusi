package id.go.kebumenkab.retribusipasar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class Prasyarat extends AppCompatActivity {
    WebView web;
    Button btnIzinkan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prasyarat);
        web = (WebView) findViewById(R.id.prasyarat);
        String fromHtml = getString(R.string.html_setuju);
        web.loadDataWithBaseURL(null, fromHtml, "text/html", "utf-8", null);
        btnIzinkan = (Button) findViewById(R.id.btn_izinkan);
        btnIzinkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}