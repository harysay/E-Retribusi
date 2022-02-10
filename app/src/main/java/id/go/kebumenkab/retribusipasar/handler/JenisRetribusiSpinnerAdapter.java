package id.go.kebumenkab.retribusipasar.handler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import id.go.kebumenkab.retribusipasar.R;

public class JenisRetribusiSpinnerAdapter extends ArrayAdapter<JenisRetribusi> {
    private List<JenisRetribusi> stateList = new ArrayList<>();

    public JenisRetribusiSpinnerAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<JenisRetribusi> stateList) {
        super(context, resource, spinnerText, stateList);
        this.stateList = stateList;
    }

    @Override
    public JenisRetribusi getItem(int position) {
        return stateList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    /**
     * Gets the state object by calling getItem and
     * Sets the state name to the drop-down TextView.
     *
     * @param position the position of the item selected
     * @return returns the updated View
     */
    private View initView(int position) {
        JenisRetribusi namajenisret = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.data_jenis_retribusi_list, null);
        TextView textView =  v.findViewById(R.id.jenisretribusiSpinnerText);
        textView.setText(namajenisret.getNama());
        return v;

    }
}
