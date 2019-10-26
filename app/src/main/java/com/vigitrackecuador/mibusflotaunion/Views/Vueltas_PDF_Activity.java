package com.vigitrackecuador.mibusflotaunion.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.vigitrackecuador.mibusflotaunion.POO.cDespacho;
import com.vigitrackecuador.mibusflotaunion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Vueltas_PDF_Activity extends AppCompatActivity {
    TableLayout tableLayout;
    ArrayList<cDespacho> arrayListDespacho= new ArrayList<cDespacho>();
    cDespacho oD;
    JsonArrayRequest jsonArrayRequestDespachoVueltasPdf;
    RequestQueue requestQueueDespachoVueltasPdf;
    String recuper_variable_url;
    String recupera_variable_unidad;
    String recuper_variable_salida;
    String recuperar_variable_ruta;
    TableRow filaDatos;
    TextView unidad_pdf;
    TextView salida_pdf;
    TextView ruta_pdf;
    TextView total_marca_pdf;
    TextView total_sub_pdf;
    TextView total_baja_pdf;
    int totalM=0;
    int totalS=0;
    int totalB=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vueltas__pdf);
        recuper_variable_url = getIntent().getStringExtra("rutapdfcargada");
        recupera_variable_unidad=getIntent().getStringExtra("unidad");
        recuper_variable_salida=getIntent().getStringExtra("prog");
        recuperar_variable_ruta=getIntent().getStringExtra("ruta");
        consumirWebServicedespachoVueltas();
        tableLayout = findViewById(R.id.tablelayoutPDF);
        unidad_pdf=findViewById(R.id.unidad_pdf);
        salida_pdf=findViewById(R.id.salida_pdf);
        ruta_pdf=findViewById(R.id.ruta_pdf);
        total_marca_pdf=findViewById(R.id.total_marca_pdf);
        total_sub_pdf=findViewById(R.id.total_sub_pdf);
        total_baja_pdf=findViewById(R.id.total_baja_pdf);
    }

    private void consumirWebServicedespachoVueltas()
    {
        final ProgressDialog progressDialog = new ProgressDialog(Vueltas_PDF_Activity.this);
        progressDialog.setTitle("Generando Tarjetas");
        progressDialog.setMessage("Espere ....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        jsonArrayRequestDespachoVueltasPdf = new JsonArrayRequest(recuper_variable_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                for (int i=0;i<response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        oD=new cDespacho();
                        oD.setContadorIndice(i);
                        oD.setTotalSubPasa(jsonObject.getInt("PasaContSubiSali_d"));
                        oD.setTotalBajaPasa(jsonObject.getInt("PasaContBajaSali_d"));
                        oD.setProgamada(jsonObject.getString("HoraProgSali_d"));
                        oD.setMarcpro(jsonObject.getString("HoraMarcSali_d"));
                        oD.setControl(jsonObject.getString("CodiCtrlSali_d"));
                        oD.setCalifi(jsonObject.getInt("FaltSali_d"));
                        if (oD.getCalifi()>=0)
                        {
                            totalM=totalM+oD.getCalifi();
                        }
                        totalB=totalB+oD.getTotalBajaPasa();
                        totalS=totalS+oD.getTotalSubPasa();
                        arrayListDespacho.add(oD);
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                if (arrayListDespacho.size()>0)
                {
                    Toast.makeText(Vueltas_PDF_Activity.this, "Tam : "+arrayListDespacho.size(), Toast.LENGTH_SHORT).show();
                }
                else{Toast.makeText(Vueltas_PDF_Activity.this, "Error catch : VUELTA NO VALIDA ", Toast.LENGTH_SHORT).show();}

                for (int fila=0;fila<arrayListDespacho.size();fila++)
                {
                    boolean bancolor=false;
                    filaDatos = new TableRow(getApplicationContext());
                    filaDatos.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

                    if(arrayListDespacho.get(fila).getCalifi()==0 && arrayListDespacho.get(fila).getMarcpro().equals("00:00:00"))
                    {
                        bancolor=true;
                        filaDatos.setBackgroundColor(getResources().getColor(R.color.vueltas_no_hechas));
                    }
                    tableLayout.addView(filaDatos);
                    TextView columnaTextView = new TextView(getApplicationContext());
                    columnaTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columnaTextView.setText(String.valueOf(arrayListDespacho.get(fila).getContadorIndice()));
                    columnaTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                    if(bancolor==true){columnaTextView.setBackgroundColor(getResources().getColor(R.color.vueltas_no_hechas));}
                    columnaTextView.setTextColor(getResources().getColor(R.color.secondText));
                    filaDatos.addView(columnaTextView);

                    TextView columnaTextView2 = new TextView(getApplicationContext());
                    columnaTextView2.setGravity(Gravity.CENTER_HORIZONTAL);
                    columnaTextView2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columnaTextView2.setText(arrayListDespacho.get(fila).getControl());
                    if(bancolor==true){columnaTextView2.setBackgroundColor(getResources().getColor(R.color.vueltas_no_hechas));}
                    columnaTextView2.setTextColor(getResources().getColor(R.color.secondText));
                    filaDatos.addView(columnaTextView2);

                    TextView columnaTextView3 = new TextView(getApplicationContext());
                    columnaTextView3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columnaTextView3.setText(arrayListDespacho.get(fila).getProgamada());
                    columnaTextView3.setGravity(Gravity.CENTER_HORIZONTAL);
                    if(bancolor==true){columnaTextView3.setBackgroundColor(getResources().getColor(R.color.vueltas_no_hechas));}
                    columnaTextView3.setTextColor(getResources().getColor(R.color.secondText));
                    filaDatos.addView(columnaTextView3);

                    TextView columnaTextView4 = new TextView(getApplicationContext());
                    columnaTextView4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columnaTextView4.setText(arrayListDespacho.get(fila).getMarcpro());
                    columnaTextView4.setGravity(Gravity.CENTER_HORIZONTAL);
                    if(bancolor==true){columnaTextView4.setBackgroundColor(getResources().getColor(R.color.vueltas_no_hechas));}
                    columnaTextView4.setTextColor(getResources().getColor(R.color.secondText));
                    filaDatos.addView(columnaTextView4);

                    TextView columnaTextView5 = new TextView(getApplicationContext());
                    columnaTextView5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columnaTextView5.setText(String.valueOf(arrayListDespacho.get(fila).getCalifi()));
                    columnaTextView5.setGravity(Gravity.CENTER_HORIZONTAL);
                    if(bancolor==true){columnaTextView5.setBackgroundColor(getResources().getColor(R.color.vueltas_no_hechas));}
                    columnaTextView5.setTextColor(getResources().getColor(R.color.secondText));
                    filaDatos.addView(columnaTextView5);

                    TextView columnaTextView6 = new TextView(getApplicationContext());
                    columnaTextView6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columnaTextView6.setText(String.valueOf(arrayListDespacho.get(fila).getTotalSubPasa()));
                    columnaTextView6.setGravity(Gravity.CENTER_HORIZONTAL);
                    if(bancolor==true){columnaTextView6.setBackgroundColor(getResources().getColor(R.color.vueltas_no_hechas));}
                    columnaTextView6.setTextColor(getResources().getColor(R.color.secondText));
                    filaDatos.addView(columnaTextView6);

                    TextView columnaTextView7 = new TextView(getApplicationContext());
                    columnaTextView7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    columnaTextView7.setText(String.valueOf(arrayListDespacho.get(fila).getTotalBajaPasa()));
                    columnaTextView7.setGravity(Gravity.CENTER_HORIZONTAL);
                    if(bancolor==true){columnaTextView7.setBackgroundColor(getResources().getColor(R.color.vueltas_no_hechas));}
                    columnaTextView7.setTextColor(getResources().getColor(R.color.secondText));
                    filaDatos.addView(columnaTextView7);
                    bancolor=false;
                }
                unidad_pdf.setText("Unidad "+recupera_variable_unidad);
                salida_pdf.setText("Salida Prog. "+recuper_variable_salida);
                ruta_pdf.setText("Ruta : "+recuperar_variable_ruta);
                total_marca_pdf.setText("Total Marcad : "+String.valueOf(totalM));
                total_sub_pdf.setText("Total Subidas : "+String.valueOf(totalS));
                total_baja_pdf.setText("Total Bajadas : "+String.valueOf(totalB));
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(Vueltas_PDF_Activity.this, "ErrorListener : Servidor no responde", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueueDespachoVueltasPdf= Volley.newRequestQueue(getApplicationContext());
        requestQueueDespachoVueltasPdf.add(jsonArrayRequestDespachoVueltasPdf);

    }

}
