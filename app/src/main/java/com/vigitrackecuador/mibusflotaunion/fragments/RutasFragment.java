package com.vigitrackecuador.mibusflotaunion.fragments;


import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vigitrackecuador.mibusflotaunion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import static com.vigitrackecuador.mibusflotaunion.LoginActivity.arrayStringIdBus;
import static com.vigitrackecuador.mibusflotaunion.LoginActivity.oControlArrayList;
import static com.vigitrackecuador.mibusflotaunion.LoginActivity.url_mapa_gps;

/**
 * A simple {@link Fragment} subclass.
 */
public class RutasFragment extends Fragment implements OnMapReadyCallback, LocationSource.OnLocationChangedListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private Handler handler = new Handler();
    int angulo=0;
    String aux;
    GoogleMap mMap;
    MapView mapView;
    Marker oMarker;
    double latiBus = 0.0;
    double longiBus = 0.0;
    Spinner oSpinner, oSppinnerIdBus;
    String envio2;
    RequestQueue requestQueuePosBuses;
    JsonArrayRequest jsonArrayRequestPosBuses;
    LatLng oLaLg1;
    LatLng oLaLg2;
    boolean centrar=false;
    float v1 = (float) 0.5;
    float v2 = (float) 0.5;
    boolean banControles=false;
    Marker mYo;

    public RutasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rutas, container, false);
        oSpinner = view.findViewById(R.id.spinner_tipo_maps);
        oSppinnerIdBus = view.findViewById(R.id.spinner_id_flotas_rutas);
        //ConsumirWebServiceConsultaIdBuses();
        ArrayAdapter<CharSequence> oAdapterMapas = ArrayAdapter.createFromResource(view.getContext(), R.array.tipo_mapas_array, R.layout.support_simple_spinner_dropdown_item);
        oAdapterMapas.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        oSpinner.setAdapter(oAdapterMapas);
        oSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        //Normal
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        //Satelital
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        //Hibrido
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case 3:
                        //Terrestre
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<String> oAdapterIdBus = new ArrayAdapter<String>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, arrayStringIdBus);
        oSppinnerIdBus.setAdapter(oAdapterIdBus);
        oSppinnerIdBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aux = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Rastreando Unidad : " + aux, Toast.LENGTH_SHORT).show();
                ConsumirWebServicePosicionesBuses();
                handler.postDelayed(mPosiciones, 9000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    public void llenarControles()
    {
        for (int i=0;i<oControlArrayList.size();i++)
        {
            oLaLg1= new LatLng(oControlArrayList.get(i).getLatitud1(),oControlArrayList.get(i).getLongitud1());
            oLaLg2 = new LatLng(oControlArrayList.get(i).getLatitud2(),oControlArrayList.get(i).getLongitud2());
            mMap.addPolyline(new PolylineOptions()
                    .add(oLaLg1,oLaLg2)
                    .width(oControlArrayList.get(i)
                    .getRadioControl())
                    .color(R.color.background_controles));
        }
    }
    private void ConsumirWebServicePosicionesBuses()
    {
        if (oMarker!=null)
        {
            oMarker.remove();
        }
        if (mYo!=null)
        {
            mYo.remove();
        }

        if (banControles==false)
        {
            llenarControles();
        }
        envio2 = aux;
        String rutanuevapos = "idbusObse=" + aux;
        String uri_pos = url_mapa_gps + "?" + rutanuevapos;
        jsonArrayRequestPosBuses = new JsonArrayRequest(uri_pos, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    latiBus = jsonObject.getDouble("UltiLatiMoni");
                    longiBus = jsonObject.getDouble("UltiLongMoni");
                    int velocidad=jsonObject.getInt("UltiVeloMoni");
                    angulo=jsonObject.getInt("UltiRumbMoni");
                    LatLng oL = new LatLng(latiBus, longiBus);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String strDate = sdf.format(c.getTime());
                    if (centrar==false)
                    {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(oL, 18));
                        centrar=true;
                    }
                    if (angulo>0 && angulo<=180)
                    {
                        oMarker = mMap.addMarker(new MarkerOptions().position(oL).title("Unidad : " + aux).snippet("HORA : " + strDate +" "+velocidad+ " Km"+" Posición : " + latiBus + " - " + longiBus)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.b2)).anchor(v1,v2)
                                .rotation(angulo).flat(true));

                    }
                    else
                    {
                        oMarker = mMap.addMarker(new MarkerOptions().position(oL).title("Unidad : " + aux).snippet("HORA : " + strDate +" "+velocidad+ " Km"+" Posición : " + latiBus + " - " + longiBus)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.b)).anchor(v1,v2)
                                .rotation(angulo).flat(true));
                    }
                    banControles=true;
                } catch (JSONException j) {
                    Toast.makeText(getContext(), "JSONException : " + j, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "ErrorResponse : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueuePosBuses = Volley.newRequestQueue(getContext());
        requestQueuePosBuses.add(jsonArrayRequestPosBuses);
    }



    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapview);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapsInitializer.initialize(getContext());
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setTrafficEnabled(true);
      /*  for (int i=0;i<oControlArrayList.size();i++)
        {
            oLaLg1= new LatLng(oControlArrayList.get(i).getLatitud1(),oControlArrayList.get(i).getLongitud1());
            oLaLg2 = new LatLng(oControlArrayList.get(i).getLatitud2(),oControlArrayList.get(i).getLongitud2());
            polylineOptions = new PolylineOptions()
                    .add(oLaLg1,oLaLg2)
                    .width(oControlArrayList.get(i).getRadioControl())
                    .color(0xffF57F17);
            mMap.addPolyline(polylineOptions);
        }*/
    }

    /*   private void ConsumirWebServiceConsultaIdBuses()
       {
           IdBuses=new ArrayList<cIdBus>();
           envio1="id_observador="+Codi_observador;
           String rutanueva=url_mapa+"?"+envio1;
           jsonArrayRequest= new JsonArrayRequest(rutanueva, new Response.Listener<JSONArray>() {
               @Override
               public void onResponse(JSONArray response)
               {
                   cIdBus oB ;
                       for (int cont=0;cont<response.length();cont++)
                       {
                           try
                           {
                               JSONObject  jsonObject=response.getJSONObject(cont);
                               oB = new cIdBus();
                               oB.setIdBus(jsonObject.getString("CodiVehiObseVehi"));
                               IdBuses.add(oB);
                           }catch (JSONException e)
                           {Toast.makeText(getContext(), "JSONException : "+e, Toast.LENGTH_SHORT).show();}
                       }
                   llenarArraylist();
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Toast.makeText(getContext(), "ErrorResponse : "+error.getMessage(), Toast.LENGTH_LONG).show();
               }
           });
           requestQueue= Volley.newRequestQueue(getContext());
           requestQueue.add(jsonArrayRequest);
       }
       private void llenarArraylist()
       {
           lista=new ArrayList<String>();
           for (int i=0;i<IdBuses.size();i++)
           {
               lista.add(IdBuses.get(i).getIdBus());
           }
           ArrayAdapter<String>oAdapterIdBus=new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,lista);
           oSppinnerIdBus.setAdapter(oAdapterIdBus);
           oSppinnerIdBus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   aux=parent.getItemAtPosition(position).toString();
                   Toast.makeText(getContext(), "Id : "+aux, Toast.LENGTH_SHORT).show();
                   ConsumirWebServicePosicionesBuses();
                   handler.postDelayed(mPosiciones,9000);
               }
               @Override
               public void onNothingSelected(AdapterView<?> parent) {
               }
           });
       }*/
    private Runnable mPosiciones = new Runnable() {
        @Override
        public void run() {
            ConsumirWebServicePosicionesBuses();
            handler.postDelayed(this, 9000);
        }
    };

    public void stopRepeating(View view)
    {
        handler.removeCallbacks(mPosiciones);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopRepeating(getView());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onMyLocationButtonClick()
    {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location)
    {

    }


    @Override
    public void onLocationChanged(Location location) {

    }
}
