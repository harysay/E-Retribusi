package id.go.kebumenkab.retribusipasar.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import id.go.kebumenkab.retribusipasar.CetakPembayaran;
import id.go.kebumenkab.retribusipasar.R;
import id.go.kebumenkab.retribusipasar.ScanQRCode;

public class HomeFragment extends Fragment {

    View rootHome;
    Button btnKeScan;
    TextView teksHome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootHome = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences mSettings = getActivity().getSharedPreferences("logSession", Context.MODE_PRIVATE);
        String usrRealSession = mSettings.getString("usrReal","Admin");
        teksHome = (TextView) rootHome.findViewById(R.id.text_home);
        teksHome.setText(usrRealSession);
        btnKeScan = (Button) rootHome.findViewById(R.id.btnScanQR);
        btnKeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ScanQRCode.class);
                startActivity(i);
            }
        });
        return rootHome;
    }
}