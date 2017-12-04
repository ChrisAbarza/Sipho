package com.example.chris.sipho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class VerInformacion extends AppCompatActivity {
    String id,tipo,URL;
    TextView textoSuperior;
    ListView listaUsuarios;
    ArrayList<Usuario> Lista1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_informacion);

        Intent intent = getIntent();
        id=intent.getStringExtra("idUsuario");
        tipo= intent.getStringExtra("tipo");

        textoSuperior = (TextView) findViewById(R.id.textViewInfoSeguidos);
        listaUsuarios = (ListView) findViewById(R.id.lstInfo);
        Lista1 = new ArrayList<Usuario>();

        switch (tipo){
            case "seguidor":
                URL="https://salv8.000webhostapp.com/seguidores.php?id="+id;
                textoSuperior.setText("Seguidores");
                break;
            case "seguidos":
                URL="https://salv8.000webhostapp.com/seguidos.php?id="+id;
                textoSuperior.setText("Seguidos");
                break;
        }
        buscarUsuarios(URL);
    }

    private void buscarUsuarios(String url) {
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
                Toast.makeText(VerInformacion.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    private void prepararList(JSONArray mja){
        Lista1.clear();
        ArrayList<String> lista = new ArrayList<>();
        for (int i=0;i<mja.length();i+=8){
            try {
                lista.add(mja.getString(i)+",æè"+mja.getString(i+1)+",æè"+mja.getString(i+2)+",æè"+mja.getString(i+3)+",æè"+mja.getString(i+4)+",æè"+mja.getString(i+5)+",æè"+mja.getString(i+6)+",æè"+mja.getString(i+7));
            }catch (JSONException e){

            }
        }
        for (int m=0;m<lista.size();m++) {
            String[] slista = lista.get(m).split(",æè");
            Lista1.add(new Usuario(slista[0],slista[1],slista[2],slista[3]));

        }
        AdaptadorUsr miadaptador = new AdaptadorUsr(getApplicationContext(),Lista1);
        listaUsuarios.setAdapter(miadaptador);
    }
}
