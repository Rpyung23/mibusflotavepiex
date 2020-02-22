package com.vigitrackecuador.mibusflotavepiex.fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vigitrackecuador.mibusflotavepiex.POO.cFlotas;
import com.vigitrackecuador.mibusflotavepiex.POO.cIdBus;
import com.vigitrackecuador.mibusflotavepiex.R;
import com.vigitrackecuador.mibusflotavepiex.adapter.cAdapterRecyclerViewDespacho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


import static com.vigitrackecuador.mibusflotavepiex.LoginActivity.arrayStringIdBus;
import static com.vigitrackecuador.mibusflotavepiex.LoginActivity.url_despacho;

public class despachoVueltasFragment extends Fragment
{
    RecyclerView recyclerViewFlotas;
    JsonArrayRequest jsonArrayRequestDespacho;
    RequestQueue requestQueueDespacho;
    Spinner spinner_despacho;
    ArrayList<cIdBus> listaBusesIdJSON;
    ArrayList<String> listaBusesIdString;
    cIdBus oIdBuses;
    private static final String CERO = "0";
    private static final String BARRA = "-";
    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    TextView fecha_lista;
    ImageView img_fecha;
    Button btn_generar_lista;
    cFlotas oFlotasLista;
    JsonArrayRequest jsonArrayRequestlista;
    RequestQueue requestQueuelista;
    ProgressDialog progressDialog;
    ArrayList<cFlotas>oFlotasArray;
    String auxidBusLista;
    private boolean banFinalProgress=true;

    public despachoVueltasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_despacho_vueltas, container, false);
        recyclerViewFlotas=view.findViewById(R.id.recycler_view_flotas_despacho);
        btn_generar_lista=view.findViewById(R.id.btn_generar_listas);
        spinner_despacho=view.findViewById(R.id.id_spinner_flotas_despacho);
        fecha_lista=view.findViewById(R.id.txt_fecha);
        //ConsumirWebServiceIdBusDespacho();
        obtenerfechaActual();
        ArrayAdapter<String> oAdapterIdBusDespacho=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,arrayStringIdBus);
        spinner_despacho.setAdapter(oAdapterIdBusDespacho);
        spinner_despacho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                auxidBusLista=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        img_fecha=view.findViewById(R.id.img_Fecha);
        img_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final int mesActual = month + 1;
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        fecha_lista.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
                    }
                },anio, mes, dia);
                recogerFecha.show();
            }
        });
        btn_generar_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fecha_lista.getText().toString().equals("yyyy/mm/dd"))
                {
                    Toast.makeText(getContext(), "Seleccione una fecha valida", Toast.LENGTH_SHORT).show();
                }else{

                    ConsumirWebServiceListaDespacho();
                }
            }
        });

        return view;
    }

    private void obtenerfechaActual()
    {
        Calendar calendar= Calendar.getInstance(TimeZone.getDefault());
        int year=calendar.get(Calendar.YEAR);
        final int mesActual = calendar.get(Calendar.MONTH)+1;
        String diaFormateado = (calendar.get(Calendar.DAY_OF_MONTH) < 10)? CERO + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)):String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
        fecha_lista.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
    }

    private void ConsumirWebServiceListaDespacho()
    {
        progressDialog = new  ProgressDialog(getContext());
        progressDialog.setTitle("Generando reporte");
        progressDialog.setMessage("Generando por favor espere.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String val1="fechaDespacho="+fecha_lista.getText().toString();
        String val2="idBusDespacho="+auxidBusLista;
        final String newUrl=url_despacho+"?"+val1+"&"+val2;
        jsonArrayRequestlista = new JsonArrayRequest(newUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                oFlotasArray =new ArrayList<cFlotas>();
                for (int cont = 0;cont<response.length();cont++)
                {
                    try{
                        JSONObject jsonObject=response.getJSONObject(cont);
                        oFlotasLista=new cFlotas();
                        oFlotasLista.setId_bus(jsonObject.getString("CodiVehiSali_m"));
                        oFlotasLista.setId_ruta(jsonObject.getInt("idSali_m"));
                        oFlotasLista.setLetra_ruta(jsonObject.getString("LetraRutaSali_m"));
                        oFlotasLista.setDate_salida(jsonObject.getString("HoraSaliProgSali_m"));
                        oFlotasLista.setDate_llegada(jsonObject.getString("HoraLLegProgSali_m"));
                        oFlotasLista.setNum_vuelta(jsonObject.getInt("NumeVuelSali_m"));
                        oFlotasLista.setAdelanto(jsonObject.getInt("atrasoN"));
                        oFlotasLista.setAtraso(jsonObject.getInt("adelantoP"));
                        oFlotasArray.add(oFlotasLista);
                    }catch (JSONException error){
                        //Toast.makeText(getContext(), "JSONException : No existen datos", Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.hide();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
                recyclerViewFlotas.setLayoutManager(linearLayoutManager);
                cAdapterRecyclerViewDespacho oRD = new
                        cAdapterRecyclerViewDespacho(getActivity(),R.layout.flotas_despacho,oFlotasArray);
                recyclerViewFlotas.setAdapter(oRD);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                String norespodeservidor="null";
                if(norespodeservidor.equals(error.getMessage().toString()))
                {
                    Toast.makeText(getContext(), "Servidor no respode vuelta a intentarlo ", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "No existen reportes", Toast.LENGTH_SHORT).show();
                }

            }
        });
        requestQueuelista= Volley.newRequestQueue(getContext());
        requestQueuelista.add(jsonArrayRequestlista);

    }

   /* private void ConsumirWebServiceIdBusDespacho()
    {
        listaBusesIdJSON=new ArrayList<cIdBus>();
        String envio1="id_observador="+Codi_observador;
        String rutanueva=url_mapa+"?"+envio1;
        jsonArrayRequestDespacho = new JsonArrayRequest(rutanueva, new Response.Listener<JSONArray>() {
         @Override
         public void onResponse(JSONArray response)
         {
             for (int cont=0;cont<response.length();cont++)
             {
                 try
                 {
                     JSONObject  jsonObject=response.getJSONObject(cont);
                     oIdBuses = new cIdBus();
                     oIdBuses.setIdBus(jsonObject.getString("CodiVehiObseVehi"));
                     //Toast.makeText(getContext(),oB.getIdBus(),Toast.LENGTH_LONG).show();
                     listaBusesIdJSON.add(oIdBuses);
                     //Toast.makeText(getContext(), "CodiVehiObseVehi : "+jsonObject.getString("CodiVehiObseVehi"), Toast.LENGTH_SHORT).show();
                 }catch (JSONException e)
                 {Toast.makeText(getContext(), "JSONException : "+e, Toast.LENGTH_SHORT).show();}
             }
             llenarArraylistString();

         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             Toast.makeText(getContext(), "Error Response : "+error.getMessage(), Toast.LENGTH_SHORT).show();
         }
     });
        requestQueueDespacho=  Volley.newRequestQueue(getContext());
        requestQueueDespacho.add(jsonArrayRequestDespacho);
    }

    private void llenarArraylistString()
    {
        listaBusesIdString=new ArrayList<String>();
        for (int i=0;i<listaBusesIdJSON.size();i++)
        {
            listaBusesIdString.add(listaBusesIdJSON.get(i).getIdBus());
        }
        ArrayAdapter<String> oAdapterIdBusDespacho=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,listaBusesIdString);
        spinner_despacho.setAdapter(oAdapterIdBusDespacho);
    }*/
}
