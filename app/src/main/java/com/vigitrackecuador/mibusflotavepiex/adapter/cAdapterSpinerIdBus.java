package com.vigitrackecuador.mibusflotavepiex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.vigitrackecuador.mibusflotavepiex.POO.cIdBus;
import com.vigitrackecuador.mibusflotavepiex.R;

import java.util.ArrayList;

public class cAdapterSpinerIdBus extends ArrayAdapter<cIdBus>
{
 public cAdapterSpinerIdBus(Context context, ArrayList<cIdBus> lista){super(context,0,lista);}

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_id_buses, parent, false
            );
        }

        TextView textViewName = convertView.findViewById(R.id.txtx_id_buses);

        cIdBus oA= getItem(position);

        if ( oA != null) {
            textViewName.setText(oA.getIdBus());
        }

        return convertView;
    }
}
