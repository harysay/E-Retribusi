package id.go.kebumenkab.retribusipasar.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import id.go.kebumenkab.retribusipasar.R;
import id.go.kebumenkab.retribusipasar.handler.Server;

public class HistoryFragment extends Fragment {

View rootHistory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootHistory = inflater.inflate(R.layout.fragment_history, container, false);
        final TextView textView = rootHistory.findViewById(R.id.text_notifications);
                    textView.setText(Server.version);
        return rootHistory;
    }
}