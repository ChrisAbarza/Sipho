package com.example.chris.sipho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerUsuario extends AppCompatActivity {

    CircleImageView fotoDeFace;
    TextView nombreCompleto,nombreUsuario,ofertasTotales,seguidores,seguidos;
    ListView listViewOfertas;
    String fotoPerfil,nombreFace,usrFace,idUsr;
    ArrayList<Oferta> Lista1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);

        Intent intent = getIntent();

        fotoDeFace = (CircleImageView) findViewById(R.id.circleImageViewVerUsr);
        nombreCompleto = (TextView) findViewById(R.id.textViewNomComVerUsr);
        nombreUsuario = (TextView) findViewById(R.id.textViewNomUsrVerUsr);
        ofertasTotales = (TextView) findViewById(R.id.textViewPublicacionesTotales);
        seguidores = (TextView) findViewById(R.id.textViewSeguidores);
        seguidos = (TextView) findViewById(R.id.textViewSeguidos);
        listViewOfertas = (ListView) findViewById(R.id.lstOfertasVerUsr);

        fotoPerfil = intent.getStringExtra("fotoPerfil");
        nombreFace = intent.getStringExtra("nombreCompleto");
        usrFace = intent.getStringExtra("usrName");
        idUsr = intent.getStringExtra("idUsuario");

        Metodos met = new Metodos();
        Lista1 = new ArrayList<Oferta>();
        String url=met.getBdUrl()+"buscarPorUsuario.php?usr="+idUsr;

        nombreCompleto.setText(nombreFace);
        nombreUsuario.setText(usrFace);

        Glide.with(getApplicationContext())
                .load(fotoPerfil)
                .into(fotoDeFace);

        buscarOferta(url);

        listViewOfertas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Oferta off = (Oferta) parent.getItemAtPosition(position);

                Intent ir = new Intent(getApplicationContext(), VerOferta.class);
                ir.putExtra("oferta", (Serializable) off);
                startActivity(ir);
            }
        });

    }
    private void buscarOferta(String URL){
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
                Toast.makeText(VerUsuario.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);

    }

    private void prepararList(JSONArray mja){
        Lista1.clear();
        ArrayList<String> lista = new ArrayList<>();
        for (int i=0;i<mja.length();i+=10){
            try {
                lista.add(mja.getString(i)+",æè"+mja.getString(i+1)+",æè"+mja.getString(i+2)+",æè"+mja.getString(i+3)+",æè"+mja.getString(i+4)+",æè"+mja.getString(i+5)+",æè"+mja.getString(i+6)+",æè"+mja.getString(i+7)+",æè"+mja.getString(i+8)+",æè"+mja.getString(i+9));
            }catch (JSONException e){

            }
        }
        for (int m=0;m<lista.size();m++) {
            String[] slista = lista.get(m).split(",æè");
            Lista1.add(new Oferta(Integer.valueOf(slista[0]),Integer.valueOf(slista[1]), slista[2],slista[3],slista[4],slista[5],slista[6], slista[7],Double.valueOf(slista[8]),Double.valueOf(slista[9])));

        }
        AdaptadorCateOferta miadaptador = new AdaptadorCateOferta(getApplicationContext(),Lista1);
        listViewOfertas.setAdapter(miadaptador);

    }
}
