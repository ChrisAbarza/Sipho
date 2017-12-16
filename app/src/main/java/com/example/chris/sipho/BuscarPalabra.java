package com.example.chris.sipho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by salv8 on 16/12/2017.
 */

public class BuscarPalabra extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Profile profile = Profile.getCurrentProfile();
    ListView listaOferta;
    ArrayList<Oferta> Lista;
    Metodos met= new Metodos();
    TextView texto;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_seguido);

        listaOferta = (ListView) findViewById(R.id.lstSeguido);
        texto = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();

        setupActionBar();

        Lista = new ArrayList<Oferta>();

        String buscar = intent.getStringExtra("buscar");

        URL= met.getBdUrl()+"buscador.php?buscar="+buscar;
        URL = URL.replaceAll(" ","%20");

        buscarOfertaSeguidos(URL);
        texto.setText("Busqueda por: "+buscar.toString());
        listaOferta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Oferta off = (Oferta) parent.getItemAtPosition(position);

                Intent ir = new Intent(getApplicationContext(), VerOferta.class);
                ir.putExtra("oferta", (Serializable) off);
                startActivity(ir);
            }
        });
    }
    private void buscarOfertaSeguidos(String url) {
        Log.i("url",""+URL);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.replace("][",",");
                if(response.length()>0){
                    try {
                        JSONArray mja = new JSONArray(response);
                        Log.i("sizejson",""+mja.length());
                        prepararList(mja);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Error "+e,Toast.LENGTH_LONG).show();
                    }catch (NullPointerException s){
                        s.printStackTrace();

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BuscarPalabra.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }
    private void prepararList(JSONArray mja){
        Lista.clear();
        ArrayList<String> lista = new ArrayList<>();
        for (int i=0;i<mja.length();i+=10){
            try {
                lista.add(mja.getString(i)+",æè"+mja.getString(i+1)+",æè"+mja.getString(i+2)+",æè"+mja.getString(i+3)+",æè"+mja.getString(i+4)+",æè"+mja.getString(i+5)+",æè"+mja.getString(i+6)+",æè"+mja.getString(i+7)+",æè"+mja.getString(i+8)+",æè"+mja.getString(i+9));
            }catch (JSONException e){

            }
        }
        for (int m=0;m<lista.size();m++) {
            String[] slista = lista.get(m).split(",æè");
            Lista.add(new Oferta(Integer.valueOf(slista[0]),Integer.valueOf(slista[1]), slista[2],slista[3],slista[4],slista[5],slista[6], slista[7],Double.valueOf(slista[8]),Double.valueOf(slista[9])));

        }
        AdaptadorCateOferta miadaptador = new AdaptadorCateOferta(getApplicationContext(),Lista);
        listaOferta.setAdapter(miadaptador);

    }
    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Buscar");
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
