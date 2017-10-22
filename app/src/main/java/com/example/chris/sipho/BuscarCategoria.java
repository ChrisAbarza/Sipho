package com.example.chris.sipho;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class BuscarCategoria extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner categoria;
    String categoriaOferta;
    ListView listaOferta;
    ArrayList<Oferta> Lista;
    Metodos met= new Metodos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_categoria);


        categoria = (Spinner) findViewById(R.id.spinnerOferta);
        listaOferta = (ListView) findViewById(R.id.lstOferta);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opcionesCategoria, android.R.layout.simple_spinner_item);
        categoria.setAdapter(adapter);
        categoria.setOnItemSelectedListener(this);

        setupActionBar();

        Lista = new ArrayList<Oferta>();

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
    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Búsqueda por categoría");
        }
    }

    private void buscarCategoria(String URL){
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
                Toast.makeText(BuscarCategoria.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myTextCategoria= (TextView) view;
        categoriaOferta = myTextCategoria.getText().toString();
        String URL = met.getBdUrl()+"buscarPorCategoria.php?cateOferta="+categoriaOferta;
        buscarCategoria(URL);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
