package com.vigitrackecuador.mibusflotaunion.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.vigitrackecuador.mibusflotaunion.R;
import com.vigitrackecuador.mibusflotaunion.R;
import static com.vigitrackecuador.mibusflotaunion.LoginActivity.arrayStringIdBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProduccFragment extends Fragment {
    Spinner oSpinerIdProduc;
    public ProduccFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_producc, container, false);
        oSpinerIdProduc=view.findViewById(R.id.spinner_id_Prod);
        ArrayAdapter<String>oAdap=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,arrayStringIdBus);
        oSpinerIdProduc.setAdapter(oAdap);
        return view;
    }

}
