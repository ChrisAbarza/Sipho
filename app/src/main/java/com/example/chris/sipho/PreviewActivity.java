package com.example.chris.sipho;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.chris.sipho.R.id.imageViewCrear;
import static com.facebook.internal.FacebookRequestErrorClassification.KEY_NAME;

public class PreviewActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    String nombreCompleto,nombreOferta,descripcion,precio,idUsuario,imgUsuario,nombreUsuario,categoria,image;
    Double lat,lng;
    int id;
    Bitmap bitmap;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        TextView txtNombreOferta = (TextView) findViewById(R.id.textViewNombreOferta);
        final TextView txtDescripcion = (TextView) findViewById(R.id.textViewDescripcion);
        TextView txtPrecio = (TextView) findViewById(R.id.textViewPrecio);
        TextView nombreCompletoFacebook = (TextView) findViewById(R.id.textViewNombreCompleto);
        TextView txtNombreUsuario = (TextView) findViewById(R.id.textViewNombreUsuario);
        TextView txtCategoria = (TextView) findViewById(R.id.textViewCategoria);
        ImageView imageViewPreview = (CircleImageView) findViewById(R.id.imageViewUsuarioOferta);
        Button btnCancelar = (Button) findViewById(R.id.buttonCancelarPreview);
        Button btnPublicar = (Button) findViewById(R.id.buttonPublicarPreview);
        ImageView imageViewOfertaPreview = (ImageView) findViewById(R.id.imageViewOferta);
        CustomScrollView myScrollView = (CustomScrollView) findViewById(R.id.scrollViewPreview);


        Intent intent = getIntent();
        nombreOferta = intent.getStringExtra(NewOffActivity.NOMBREOFERTA);
        categoria = intent.getStringExtra(NewOffActivity.CATEGORIA);
        descripcion = intent.getStringExtra(NewOffActivity.DESCRIPCION);
        precio = intent.getStringExtra(NewOffActivity.PRECIO);
        nombreCompleto= intent.getStringExtra(NewOffActivity.NOMBRECOMPLETO2);
        idUsuario = intent.getStringExtra(NewOffActivity.IDUSUARIO2);
        imgUsuario = intent.getStringExtra(NewOffActivity.IMGUSUARIO2);
        nombreUsuario = intent.getStringExtra(NewOffActivity.NOMBREUSUARIO2);
        lat = intent.getDoubleExtra(NewOffActivity.LATITUD2,0.0);
        lng = intent.getDoubleExtra(NewOffActivity.LONGITUD2,0.0);
        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        txtNombreOferta.setText(nombreOferta);
        txtDescripcion.setText(descripcion);
        txtPrecio.setText("$"+precio);
        txtCategoria.setText(categoria);
        nombreCompletoFacebook.setText(nombreCompleto);
        txtNombreUsuario.setText(nombreUsuario);

        imageViewOfertaPreview.setImageBitmap(bitmap);

        setupActionBar();

        Glide.with(getApplicationContext())
                .load(imgUsuario)
                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()))))
                .into(imageViewPreview);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainScreen();
            }
        });
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Metodos met= new Metodos();
                generarIDAleatorio();
                //String url = met.getBdUrl()+"insertarofertaPost.php";
                String url="https://salv8.000webhostapp.com/InsertarofertaPost.php";
                //url = url.replaceAll(" ","%20");
                insertarDatos(url);

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPreview);
        mapFragment.getMapAsync(this);

    }
    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Previsualizar");
        }
    }
    private void generarIDAleatorio(){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        id = random.nextInt(2000000000);

    }
    public void insertarDatos(String url){
        final ProgressDialog loading = ProgressDialog.show(this,"Cargando...","Espere...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(PreviewActivity.this, "Correcto!" , Toast.LENGTH_LONG).show();
                        goMainScreen();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        //Toast.makeText(PreviewActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(PreviewActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("id", String.valueOf(id));
                params.put("usuario", idUsuario);
                params.put("nomOferta", nombreOferta);
                params.put("descOferta", descripcion);
                params.put("precioOferta", precio);
                params.put("cateOferta", categoria);
                params.put("lat", String.valueOf(lat));
                params.put("lng", String.valueOf(lng));
                if(bitmap != null){

                    image = getStringImage(bitmap);
                    params.put("imagen", image);

                }


                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng oferta = new LatLng(lat, lng);
        Marker off = mMap.addMarker(new MarkerOptions().position(oferta)
                .draggable(true));
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(oferta, 16);
        mMap.moveCamera(ubicacion);
        mMap.setOnMarkerDragListener(this);



    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(this, "¡Mueve a donde quieras!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        lat= marker.getPosition().latitude;
        lng= marker.getPosition().longitude;

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
