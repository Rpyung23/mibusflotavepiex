package com.vigitrackecuador.mibusflotaunion.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vigitrackecuador.mibusflotaunion.POO.cFlotas;
import com.vigitrackecuador.mibusflotaunion.R;

import java.util.ArrayList;

public class cAdapter_flotas extends RecyclerView.Adapter<cAdapter_flotas.cViewHolderFlotas>
{
    private int vista;
    private Activity activity;
    private ArrayList<cFlotas> oFlotas;

    public cAdapter_flotas(int vista, Activity activity, ArrayList<cFlotas> oFlotas)
    {
        this.vista = vista;
        this.activity = activity;
        this.oFlotas = oFlotas;
    }

    @NonNull
    @Override
    public cViewHolderFlotas onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(vista,parent,false);
        return new cViewHolderFlotas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cViewHolderFlotas holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return oFlotas.size();
    }

    class cViewHolderFlotas extends RecyclerView.ViewHolder
    {
        private TextView txtunidad;
        private TextView txtruta;
        private TextView txtLetraRuta;
        private TextView txtsalida;
        private TextView txtllegada;
        private Button btn_pdf_floats_depacho;
        public cViewHolderFlotas(@NonNull View itemView)
        {
            super(itemView);
            txtunidad=itemView.findViewById(R.id.txtUnidad);
            txtruta=itemView.findViewById(R.id.txtidRuta);
            txtLetraRuta=itemView.findViewById(R.id.txtLetraRuta);
            txtsalida=itemView.findViewById(R.id.txtSalida);
            txtllegada=itemView.findViewById(R.id.txtLlegada);
        }
    }
}
