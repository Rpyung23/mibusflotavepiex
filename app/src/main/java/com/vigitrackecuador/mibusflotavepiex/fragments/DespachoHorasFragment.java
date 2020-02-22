package com.vigitrackecuador.mibusflotavepiex.fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vigitrackecuador.mibusflotavepiex.POO.cConteoGHoras;
import com.vigitrackecuador.mibusflotavepiex.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;

import static com.vigitrackecuador.mibusflotavepiex.LoginActivity.arrayStringIdBus;
import static com.vigitrackecuador.mibusflotavepiex.LoginActivity.url_despacho_horas;

/**
 * A simple {@link Fragment} subclass.
 */
public class DespachoHorasFragment extends Fragment
{
    private static final int MY_SOCKET_TIMEOUT_MS = 30000 ;
    ImageButton imageButtonFechaDespacho;
    ImageButton imageButtonHoraInicial;
    ImageButton imageButtonHoraFinal;
    TextView textViewHoraInicial;
    TextView textViewHoraFinal;
    TextView textViewFechaDespacho;
    Spinner spinnerIdDespachoHoras;
    Button btn_generar;

    TextView txtUnidad;
    TextView TotalS;
    TextView TotalB;
    TextView SubidaP1;
    TextView SubidaP2;
    TextView SubidaP3;
    TextView BajadaP1;
    TextView BajadaP2;
    TextView BajadaP3;

    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    
    String stringFecha;
    String stringHoraInicial="05:00";
    String stringHoraFinal="23:00";
    String idBus;
    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "-";
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueuejsonConteoGHoras;
    cConteoGHoras oCGH = new cConteoGHoras();
    ProgressDialog progressDialog;
    public DespachoHorasFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view= inflater.inflate(R.layout.fragment_despacho_horas, container, false);
        txtUnidad=view.findViewById(R.id.txtUnidad);
        TotalS=view.findViewById(R.id.txtTotalSubidas);
        TotalB=view.findViewById(R.id.txtTotalBajadas);
        SubidaP1=view.findViewById(R.id.textView3);
        SubidaP2=view.findViewById(R.id.textView4);
        SubidaP3=view.findViewById(R.id.textView6);
        BajadaP1=view.findViewById(R.id.textView2);
        BajadaP2=view.findViewById(R.id.textView5);
        BajadaP3=view.findViewById(R.id.textView7);
        textViewFechaDespacho=view.findViewById(R.id.textfecha_despacho_horas);
        textViewHoraFinal=view.findViewById(R.id.horafinal_despacho);
        textViewHoraInicial=view.findViewById(R.id.txthorainicial_despacho);
        imageButtonHoraInicial=view.findViewById(R.id.timer_despacho_horainicial);
        imageButtonHoraFinal=view.findViewById(R.id.btn_timer_horafinal);
        imageButtonFechaDespacho=view.findViewById(R.id.btn_calendario_despacho_hora);
        spinnerIdDespachoHoras=view.findViewById(R.id.Spinner_id_flotas_despacho_horas);
        btn_generar=view.findViewById(R.id.btn_generar_despacho_horas);
        obtenerfechaActual();
        ArrayAdapter<String> oAdap = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,arrayStringIdBus);
        spinnerIdDespachoHoras.setAdapter(oAdap);
        imageButtonHoraInicial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                obtenerHoraInicial();
            }
        });
        imageButtonHoraFinal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                obtenerHoraFinal();
            }
        });
        imageButtonFechaDespacho.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                obtenerFecha();
            }
        });
        spinnerIdDespachoHoras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) 
            {
                idBus=parent.getItemAtPosition(position).toString();
                //Toast.makeText(getContext(), "id : "+idBus, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        btn_generar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(textViewFechaDespacho.getText().toString().equals("yyyy-mm-dd"))
                {
                    Toast.makeText(getContext(), "Fecha no valida", Toast.LENGTH_SHORT).show();
                }else
                {
                 consumirWebServiceConteoGHoras();
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
        textViewFechaDespacho.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
        stringFecha=year + BARRA + mesFormateado + BARRA + diaFormateado;
    }
    private void consumirWebServiceConteoGHoras()
    {
        progressDialog = new  ProgressDialog(getContext());
        progressDialog.setTitle("Generando conteo");
        progressDialog.setMessage("Generando por favor espere.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String envio1="unidadhora="+idBus;
        String envio2="fechadespacho="+stringFecha;
        String envio3="horainicial="+stringHoraInicial;
        String envio4="horafinal="+stringHoraFinal;
        String newUrlConteoGHoras=url_despacho_horas+"?"+envio1+"&"+envio2+"&"+envio3+"&"+envio4;

        jsonArrayRequest = new JsonArrayRequest(newUrlConteoGHoras, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response)
            {
                try {
                    JSONObject jsonObject=response.getJSONObject(0);
                    oCGH.setSubida1(jsonObject.getInt("subida1"));
                    oCGH.setSubida2(jsonObject.getInt("subida2"));
                    oCGH.setSubida3(jsonObject.getInt("subida3"));
                    oCGH.setBajada1(jsonObject.getInt("bajada1"));
                    oCGH.setBajada2(jsonObject.getInt("bajada2"));
                    oCGH.setBajada3(jsonObject.getInt("bajada3"));
                    int auxSubidaT=oCGH.getSubida1()+oCGH.getSubida2()+oCGH.getSubida3();
                    int auxBajadaT=oCGH.getBajada1()+oCGH.getBajada2()+oCGH.getBajada3();
                    oCGH.setTotalSubida(auxSubidaT);
                    oCGH.setTotalBajada(auxBajadaT);
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "try : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                txtUnidad.setText("Unidad "+idBus);
                BajadaP1.setText(String.valueOf(oCGH.getBajada1()));
                BajadaP2.setText(String.valueOf(oCGH.getBajada2()));
                BajadaP3.setText(String.valueOf(oCGH.getBajada3()));
                SubidaP1.setText(String.valueOf(oCGH.getSubida1()));
                SubidaP2.setText(String.valueOf(oCGH.getSubida2()));
                SubidaP3.setText(String.valueOf(oCGH.getSubida3()));
                TotalS.setText(String.valueOf(oCGH.getTotalSubida()));
                TotalB.setText(String.valueOf(oCGH.getTotalBajada()));
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String servidornorespode="null";
                Toast.makeText(getContext(),"Error "+error.getMessage(),Toast.LENGTH_LONG).show();
               /* if(servidornorespode.equals(error.getMessage()))
                {
                    Toast.makeText(getContext()," "+error.getMessage(),Toast.LENGTH_LONG).show();
                    //Toast.makeText(getContext(), "Servidor no responde vuelva a intentar", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        Toast.makeText(getContext(), "No existe información", Toast.LENGTH_SHORT).show();
                    }*/
                BajadaP1.setText("0000");
                BajadaP2.setText("0000");
                BajadaP3.setText("0000");
                SubidaP1.setText("0000");
                SubidaP2.setText("0000");
                SubidaP3.setText("0000");
                TotalS.setText("0000");
                TotalB.setText("0000");
                progressDialog.hide();
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueuejsonConteoGHoras = Volley.newRequestQueue(getContext());
        requestQueuejsonConteoGHoras.add(jsonArrayRequest);
    }

    private void obtenerFecha()
    {
        DatePickerDialog recogerFecha = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                textViewFechaDespacho.setText(year + BARRA + mesFormateado + BARRA + diaFormateado);
                stringFecha=year + BARRA + mesFormateado + BARRA + diaFormateado;
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }
    private void obtenerHoraInicial()
    {
        TimePickerDialog recogerHora = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                stringHoraInicial=horaFormateada+":"+minutoFormateado;
                //Muestro la hora con el formato deseado
                textViewHoraInicial.setText("Hora Inicial : "+horaFormateada + DOS_PUNTOS + minutoFormateado );
            }
        }, hora, minuto, true);
        recogerHora.show();
    }
    private void obtenerHoraFinal()
    {
        TimePickerDialog recogerHoraF = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                stringHoraFinal=horaFormateada+":"+minutoFormateado;
                //Muestro la hora con el formato deseado
                textViewHoraFinal.setText("Hora Final : "+horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
        }, hora, minuto, true);
        recogerHoraF.show();
    }

}
