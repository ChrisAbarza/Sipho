package com.example.chris.sipho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by salv8 on 03/12/2017.
 */

public class VerOfertaDialog extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    private String idOferta;
    String valoracionOferta,URL,idUsuario,fotoPerfil,nomOferta,descOferta,precioOferta,cateOferta,nomUsr,nomCompleto,imgOferta;
    Metodos met = new Metodos();
    TextView nombreCompletoFacebook,txtNombreOferta,txtDescripcion,txtPrecio,txtNombreUsuario,txtCategoria;
    ImageView imageViewUsuario, imagenCate,imageViewOferta;
    private GoogleMap mMap;
    Double lat,lng;
    Spinner valoracion;
    EditText editTextComentario;
    Button btnComentar;
    Profile profile = Profile.getCurrentProfile();
    ListView listaComent;
    ArrayList<Comentario> Lista;
    ScrollView scroll;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_oferta);

        Intent llegada = getIntent();
        idOferta = llegada.getStringExtra("idOferta");

        URL=met.getBdUrl()+"verOfertaDialog.php?idOferta="+idOferta;

        buscarDatosOferta(URL);

        Lista = new ArrayList<Comentario>();

        valoracion= (Spinner) findViewById(R.id.spinnerComentario);
        editTextComentario=(EditText) findViewById(R.id.editTextComentario);
        btnComentar=(Button)findViewById(R.id.buttonComentar);
        txtNombreOferta = (TextView) findViewById(R.id.textViewNombreOfertaVer);
        txtDescripcion = (TextView) findViewById(R.id.textViewDescripcionVer);
        txtPrecio = (TextView) findViewById(R.id.textViewPrecioVer);
        nombreCompletoFacebook = (TextView) findViewById(R.id.textViewNombreCompletoVer);
        txtNombreUsuario = (TextView) findViewById(R.id.textViewNombreUsuarioVer);
        txtCategoria = (TextView) findViewById(R.id.textViewCategoriaVer);
        imageViewUsuario = (CircleImageView) findViewById(R.id.imageViewUsuarioOfertaVer);
        imageViewOferta = (ImageView) findViewById(R.id.imageViewOfertaVer);
        listaComent = (ListView) findViewById(R.id.lstComent);
        scroll= (ScrollView) findViewById(R.id.ScrollView01);
        imagenCate = (ImageView) findViewById(R.id.imageViewCateVer);





        String url2=met.getBdUrl()+"consultarComent.php?oferta="+idOferta;
        buscarComentario(url2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opcionesComentario, android.R.layout.simple_spinner_item);
        valoracion.setAdapter(adapter);
        valoracion.setOnItemSelectedListener(this);

        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String URL2=met.getBdUrl()+"insertarComentario.php";

                insertarDatos(URL2);

            }
        });

        scroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.lstComent).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        listaComent.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        txtNombreUsuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(VerOfertaDialog.this, VerUsuario.class);
                intent.putExtra("idUsuario",idUsuario);
                intent.putExtra("fotoPerfil",fotoPerfil);
                intent.putExtra("nombreCompleto",nombreCompletoFacebook.getText().toString());
                intent.putExtra("usrName",txtNombreUsuario.getText().toString());
                startActivity(intent);
            }
        });




    }

    private void insertarDatos(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(VerOfertaDialog.this, "Correcto!", Toast.LENGTH_LONG).show();
                        editTextComentario.setText("");
                        String url2=met.getBdUrl()+"consultarComent.php?oferta="+idOferta;
                        buscarComentario(url2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
                        Toast.makeText(VerOfertaDialog.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("oferta", String.valueOf(idOferta));
                params.put("usuario", profile.getId());
                params.put("coment", editTextComentario.getText().toString());
                params.put("valoracion", valoracionOferta);


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void buscarComentario(String URL) {
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
                Toast.makeText(VerOfertaDialog.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    private void prepararList(JSONArray mja) {
        Lista.clear();
        ArrayList<String> lista = new ArrayList<>();
        for (int i=0;i<mja.length();i+=6){
            try {
                lista.add(mja.getString(i)+",æè"+mja.getString(i+1)+",æè"+mja.getString(i+2)+",æè"+mja.getString(i+3)+",æè"+mja.getString(i+4)+",æè"+mja.getString(i+5));
            }catch (JSONException e){
                Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();

            }

        }
        for (int m=0;m<lista.size();m++) {
            String[] slista = lista.get(m).split(",æè");
            Lista.add(new Comentario(Long.valueOf(slista[0]),slista[1],slista[2],slista[3],slista[4],slista[5]));

        }
        AdaptadorComentarios miadaptador = new AdaptadorComentarios(getApplicationContext(),Lista);
        listaComent.setAdapter(miadaptador);
        scroll.scrollTo(0,0);

    }

    private void cargarImagenCate(String cateOferta) {
        switch (cateOferta){
            case "Comida":
                imagenCate.setImageResource(R.drawable.comida);
                break;
            case "Fiesta":
                imagenCate.setImageResource(R.drawable.copete);
                break;
            case "Actividades y eventos":
                imagenCate.setImageResource(R.drawable.eventos);
                break;
            case "Servicios":
                imagenCate.setImageResource(R.drawable.servicio);
                break;
            case "Moda":
                imagenCate.setImageResource(R.drawable.moda);
                break;
            case "Electrodomésticos":
                imagenCate.setImageResource(R.drawable.electrodomesticos);
                break;
            case "Autos y motos":
                imagenCate.setImageResource(R.drawable.rueda);
                break;
            case "Hogar":
                imagenCate.setImageResource(R.drawable.casa);
                break;
            case "Otros":
                imagenCate.setImageResource(R.drawable.otros);
                break;
        }
    }

    private void buscarDatosOferta(String URL) {
        Log.i("url",""+URL);
        final ProgressDialog loading = ProgressDialog.show(this,"Cargando...","Espere...",false,false);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.replace("][",",");
                if(response.length()>0){
                    try {
                        JSONArray mja = new JSONArray(response);
                        Log.i("sizejson",""+mja.length());
                        ArrayList<String> lista = new ArrayList<>();
                        lista.add(mja.getString(0)+",æè"+mja.getString(1)+",æè"+mja.getString(2)+",æè"+mja.getString(3)+",æè"+mja.getString(4)
                                +",æè"+mja.getString(5)+",æè"+mja.getString(6)+",æè"+mja.getString(7)+",æè"+mja.getString(8)+",æè"+mja.getString(9)+",æè"+mja.getString(10));

                        for (int m=0;m<lista.size();m++) {
                            String[] slista = lista.get(m).split(",æè");
                            nomOferta = slista[0].toString();
                            descOferta = slista[1].toString();
                            precioOferta = slista[2].toString();
                            cateOferta = slista[3].toString();
                            lat= Double.valueOf(slista[4].toString());
                            lng= Double.valueOf(slista[5].toString());
                            nomUsr=slista[6].toString();
                            nomCompleto=slista[7].toString();
                            fotoPerfil=slista[8].toString();
                            idUsuario=slista[9].toString();
                            imgOferta=slista[10].toString();


                        }
                        cargarImagenCate(cateOferta);

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                               .findFragmentById(R.id.mapVerOferta);
                          mapFragment.getMapAsync(VerOfertaDialog.this);

                        txtNombreOferta.setText(nomOferta);
                        txtDescripcion.setText(descOferta);
                        txtPrecio.setText(precioOferta);
                        txtNombreUsuario.setText(nomUsr);
                        nombreCompletoFacebook.setText(nomCompleto);
                        txtCategoria.setText(cateOferta);

                        Glide.with(getApplicationContext())
                                .load(imgOferta)
                                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()))))
                                .into(imageViewOferta);

                        Glide.with(getApplicationContext())
                                .load(fotoPerfil)
                                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()))))
                                .into(imageViewUsuario);

                        loading.dismiss();



                    } catch (JSONException e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),"Error "+e,Toast.LENGTH_LONG).show();
                    }catch (NullPointerException s){
                        s.printStackTrace();
                        loading.dismiss();

                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VerOfertaDialog.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myTextValoracion= (TextView) view;
        valoracionOferta = myTextValoracion.getText().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng oferta = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(oferta));
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(oferta, 16);
        mMap.moveCamera(ubicacion);

    }
}
