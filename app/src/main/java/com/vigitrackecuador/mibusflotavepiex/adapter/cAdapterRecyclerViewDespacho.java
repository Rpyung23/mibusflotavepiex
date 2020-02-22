package com.vigitrackecuador.mibusflotavepiex.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vigitrackecuador.mibusflotavepiex.POO.cFlotas;
import com.vigitrackecuador.mibusflotavepiex.R;
import com.vigitrackecuador.mibusflotavepiex.Views.Vueltas_PDF_Activity;

import java.util.ArrayList;

import static com.vigitrackecuador.mibusflotavepiex.LoginActivity.url_despacho_pdf;

public class cAdapterRecyclerViewDespacho extends RecyclerView.Adapter<cAdapterRecyclerViewDespacho.cViewHolderDespacho>
{
    private Activity activity;
    private int resource;
    private ArrayList<cFlotas>oFlotas;

    public cAdapterRecyclerViewDespacho(Activity activity, int resource, ArrayList<cFlotas> oFlotas) {
        this.activity = activity;
        this.resource = resource;
        this.oFlotas = oFlotas;
    }

    @NonNull
    @Override
    public cViewHolderDespacho onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view=LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new cViewHolderDespacho(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cViewHolderDespacho holder, final int position)
    {
        cFlotas oF = oFlotas.get(position);
        final String idBus="[ "+oF.getId_bus()+" ]";
        String idSalida="# " +oF.getId_ruta();
        final String LetraRu="Ruta "+oF.getLetra_ruta();
        final String Salida="Salida : "+oF.getDate_salida().toString();
        String Llegada="Llegada : "+oF.getDate_llegada().toString();
        String adelantos = "Adelantos "+oF.getAdelanto();
        String atrosos = "Atrasos "+oF.getAtraso();
        holder.txtUnidadDespacho.setText(idBus);
        holder.txtIdRutaDespacho.setText(idSalida);
        holder.txtLetraRutaDespacho.setText(LetraRu);
        holder.txtSalidaDespacho.setText(Salida);
        holder.txtLlegadaDespacho.setText(Llegada);
        holder.txtAtrasos.setText(atrosos);
        holder.txtAdelanto.setText(adelantos);
        holder.btn_generar_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val1="idvuelta="+oFlotas.get(position).getId_ruta();
                String url=url_despacho_pdf+"?"+val1;
                Intent intent = new Intent(activity, Vueltas_PDF_Activity.class);
                intent.putExtra("rutapdfcargada",url);
                intent.putExtra("unidad",idBus);
                intent.putExtra("prog",Salida);
                intent.putExtra("ruta",LetraRu);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return oFlotas.size();
    }

    public class cViewHolderDespacho extends RecyclerView.ViewHolder
    {
        TextView txtUnidadDespacho;
        TextView txtIdRutaDespacho;
        TextView txtLetraRutaDespacho;
        TextView txtSalidaDespacho;
        TextView txtLlegadaDespacho;
        Button btn_generar_pdf;
        TextView txtAtrasos;
        TextView txtAdelanto;
        public cViewHolderDespacho(@NonNull View itemView)
        {
            super(itemView);
            txtUnidadDespacho=itemView.findViewById(R.id.txtUnidad);
            txtIdRutaDespacho=itemView.findViewById(R.id.txtidRuta);
            txtLetraRutaDespacho=itemView.findViewById(R.id.txtLetraRuta);
            txtSalidaDespacho=itemView.findViewById(R.id.txtSalida);
            txtLlegadaDespacho=itemView.findViewById(R.id.txtLlegada);
            btn_generar_pdf=itemView.findViewById(R.id.btn_pdf_despacho);
            txtAtrasos=itemView.findViewById(R.id.txtAtrasos);
            txtAdelanto=itemView.findViewById(R.id.txtAdelantos);
        }
    }
}
