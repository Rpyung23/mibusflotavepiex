package com.vigitrackecuador.mibusflotavepiex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vigitrackecuador.mibusflotavepiex.POO.cContrles;
import com.vigitrackecuador.mibusflotavepiex.POO.cIdBus;
import com.vigitrackecuador.mibusflotavepiex.Views.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    JsonArrayRequest jsonArrayRequestIdBuses;
    RequestQueue requestQueueIdBuses;
    ArrayList<cIdBus> arrayIdBuses ;
    public static ArrayList<String> arrayStringIdBus ;
    cIdBus oIdBus;

    ProgressDialog progressDialog;
    ImageButton imageButtonConfig;
    Spinner oSpinner;
    Button btn_Ingresar;
    String user_name_xml;
    String pass_name_xml;
    TextView textViewUser;
    TextView textViewPass;
    TextView textViewEmpresa;
    RequestQueue requestQueue; //permite la conccion con el webService
    JsonArrayRequest jsonObjectRequest; //permite la conccion con el webService

    public static ArrayList<cContrles> oControlArrayList;
    cContrles oCtrl;
    JsonArrayRequest jsonArrayRequestControl;
    RequestQueue requestQueueControl;

    String empresa;
    String empresaLeer;
    public static String url_login;
    public static String url_mapa;
    public static String url_mapa_gps;
    public static String url_despacho_horas;
    public static String url_despacho;
    public static String url_despacho_pdf;
    public static String url_control_mapa;//los relojes del mata;
    public static String url_ConteoCliente_id;//obtengo las id de los conteo
    public static String url_ConteoCliente_valor;//obtengo la informacion despues de obtner las id con url_Conteo_id
    public static String Codi_observador;
    public static String UserLeer;
    public static String PassLeer;
    boolean banControl=false;
    boolean banIdBus=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        imageButtonConfig = findViewById(R.id.config_login);
        textViewUser = findViewById(R.id.txtusuarioLogin);
        textViewPass = findViewById(R.id.txtPassLogin);
        cargarsharedPreferendEMpre();

        url_login = "http://www.vigitrackecuador.com/webservice/vepiexsa/verificarLoginWebService.php";
        url_mapa = "http://www.vigitrackecuador.com/webservice/vepiexsa/mapaFlotaWebService.php";//obtiene las id de los buses
        url_mapa_gps = "http://www.vigitrackecuador.com/webservice/vepiexsa/rastreoflotaWebService.php";//obtiene las posiciones de los buses
        url_despacho = "http://www.vigitrackecuador.com/webservice/vepiexsa/despachoWebService.php";
        url_despacho_pdf = "http://www.vigitrackecuador.com/webservice/vepiexsa/despachoPdfwebservice.php";
        url_control_mapa = "http://www.vigitrackecuador.com/webservice/vepiexsa/controlesWebService.php";
        url_despacho_horas = "http://www.vigitrackecuador.com/webservice/vepiexsa/despachoHorasWebService.php";

        btn_Ingresar = findViewById(R.id.btn_Ingresar);
        btn_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ComprobarPermisos())
                {
                    user_name_xml = textViewUser.getText().toString();
                    pass_name_xml = textViewPass.getText().toString();
                    cargarWebService();
                }

            }
        });
    }

    private boolean ComprobarPermisos()
    {
        ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Permisos no Otorgados", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private void cargarsharedPreferendEMpre() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmpresaSeleccionadaUnion", Context.MODE_PRIVATE);
        UserLeer = sharedPreferences.getString("usuario", "ErrorU");
        PassLeer = sharedPreferences.getString("pass", "ErrorP");
        //Toast.makeText(this, "GSP"+empresaLeer+"-"+ UserLeer+"-"+PassLeer, Toast.LENGTH_LONG).show();
        if (UserLeer.equals("ErrorU") || PassLeer.equals("ErrorP"))
        {
            //Toast.makeText(LoginActivity.this,"Error SP",Toast.LENGTH_SHORT);
        } else {
            textViewUser.setText(UserLeer);
            textViewPass.setText(PassLeer);
        }
    }

    private void cargarControlWebservice()
    {
        oControlArrayList = new ArrayList<cContrles>();
        jsonArrayRequestControl = new JsonArrayRequest(url_control_mapa, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response) {
                for (int aux = 0; aux < response.length(); aux++) {
                    oCtrl = new cContrles();
                    try {
                        JSONObject jsonObject = response.getJSONObject(aux);
                        oCtrl.setLatitud1(jsonObject.getDouble("Lati1Ctrl"));
                        oCtrl.setLongitud1(jsonObject.getDouble("Long1Ctrl"));
                        oCtrl.setLatitud2(jsonObject.getDouble("Lati2Ctrl"));
                        oCtrl.setLongitud2(jsonObject.getDouble("Long2Ctrl"));
                        oCtrl.setRadioControl(jsonObject.getInt("RadiCtrl"));
                        oControlArrayList.add(oCtrl);
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, ":" + e, Toast.LENGTH_SHORT).show();
                    }
                }
                banControl=true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error Controles : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueueControl = Volley.newRequestQueue(getApplicationContext());
        requestQueueControl.add(jsonArrayRequestControl);
    }

    private void guardarPreferencia() {
        SharedPreferences sharedPreferences = getSharedPreferences("EmpresaSeleccionadaUnion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Toast.makeText(this, "GSP"+empresa+"-"+user_name_xml+"-"+pass_name_xml, Toast.LENGTH_LONG).show();
        editor.putString("usuario", user_name_xml);
        editor.putString("pass", pass_name_xml);
        editor.apply();
    }

    private void cargarWebService()
    {

        String envio1 = "username=" + user_name_xml;
        String envio2 = "clave=" + pass_name_xml;
        String url_login_App = url_login + "?" + envio1 + "&" + envio2;
        Toast.makeText(getApplicationContext(), "Ingresando Espere....", Toast.LENGTH_SHORT).show();
        jsonObjectRequest = new JsonArrayRequest(url_login_App, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = response.getJSONObject(0);
                    Codi_observador = jsonObject.getString("CodiObse");
                    guardarPreferencia();
                    cargarWebServiceIdBuses();
                    cargarControlWebservice();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    /*Toast.makeText(LoginActivity.this, "banControl : "+banControl+" *** banIdBus : "+banIdBus, Toast.LENGTH_LONG).show();
                    if (banControl && banIdBus){
                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }
                    else
                        {
                            Toast.makeText(LoginActivity.this, "Error de WebService vuelve a ingresar", Toast.LENGTH_SHORT).show();
                        }*/

                } catch (JSONException e)
                {

                    Toast.makeText(LoginActivity.this, "Datos no validos : "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errornull="null";
                if (errornull.equals(error.getMessage().toString()))
                {
                    Toast.makeText(LoginActivity.this, "Servidor no Responde", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Usuario Incorrecto / Contraseña Incorrecta ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void cargarWebServiceIdBuses() {
        arrayIdBuses=new ArrayList<cIdBus>();
        String envio1 = "id_observador=" + Codi_observador;
        String newUrl = url_mapa + "?" + envio1;
        jsonArrayRequestIdBuses = new JsonArrayRequest(newUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int cont = 0; cont < response.length(); cont++) {
                    oIdBus = new cIdBus();
                    try {
                        JSONObject jsonObject = response.getJSONObject(cont);
                        //Toast.makeText(LoginActivity.this, "id: " + jsonObject.getString("CodiVehiObseVehi"), Toast.LENGTH_SHORT).show();
                        oIdBus.setIdBus(jsonObject.getString("CodiVehiObseVehi"));
                        arrayIdBuses.add(oIdBus);
                    } catch (JSONException error) {
                        Toast.makeText(getApplicationContext(), "JSONException : " + error, Toast.LENGTH_SHORT).show();
                    }
                }
                llenarListaIdString();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "ErrorREsponse IdBusesLogin : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueueIdBuses = Volley.newRequestQueue(getApplicationContext());
        requestQueueIdBuses.add(jsonArrayRequestIdBuses);
    }
    private void llenarListaIdString() {
        arrayStringIdBus = new ArrayList<String>();
        for (int i = 0; i < arrayIdBuses.size(); i++)
        {
            arrayStringIdBus.add(arrayIdBuses.get(i).getIdBus());
        }
        banIdBus=true;
    }

}
