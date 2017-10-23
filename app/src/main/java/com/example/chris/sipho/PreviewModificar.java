package com.example.chris.sipho;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.bitmap;

/**
 * Created by salv8 on 22/10/2017.
 */

public class PreviewModificar extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    String nombreCompleto,nombreOferta,descripcion,precio,idUsuario,imgUsuario,nombreUsuario,categoria,image,idOferta, imagen;
    Double lat,lng;
    int id, bandera;
    Profile profile = Profile.getCurrentProfile();
    Bitmap bitmap;

    private GoogleMap mMap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        TextView txtNombreOferta = (TextView) findViewById(R.id.textViewNombreOferta);
        TextView txtDescripcion = (TextView) findViewById(R.id.textViewDescripcion);
        TextView txtPrecio = (TextView) findViewById(R.id.textViewPrecio);
        TextView nombreCompletoFacebook = (TextView) findViewById(R.id.textViewNombreCompleto);
        TextView txtNombreUsuario = (TextView) findViewById(R.id.textViewNombreUsuario);
        TextView txtCategoria = (TextView) findViewById(R.id.textViewCategoria);
        ImageView imageViewPreview = (CircleImageView) findViewById(R.id.imageViewUsuarioOferta);
        final Button btnCancelar = (Button) findViewById(R.id.buttonCancelarPreview);
        Button btnPublicar = (Button) findViewById(R.id.buttonPublicarPreview);
        ImageView imageViewOfertaPreview = (ImageView) findViewById(R.id.imageViewOferta);
        CustomScrollView myScrollView = (CustomScrollView) findViewById(R.id.scrollViewPreview);

        Intent intent = getIntent();
        nombreOferta = intent.getStringExtra(ModificarOffActivity.NOMBREOFERTA);
        categoria = intent.getStringExtra(ModificarOffActivity.CATEGORIA);
        descripcion = intent.getStringExtra(ModificarOffActivity.DESCRIPCION);
        precio = intent.getStringExtra(ModificarOffActivity.PRECIO);
        idOferta= intent.getStringExtra(ModificarOffActivity.IDOFERTA);
        nombreUsuario = intent.getStringExtra(ModificarOffActivity.NOMBREUSUARIO2);
        lat = intent.getDoubleExtra(ModificarOffActivity.LATITUD2,0.0);
        lng = intent.getDoubleExtra(ModificarOffActivity.LONGITUD2,0.0);
        bandera = intent.getIntExtra("bandera",0);

        switch (bandera){
            case 1:
                byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imageViewOfertaPreview.setImageBitmap(bitmap);
                break;
            case 2:
                image = intent.getStringExtra("image");
                Glide.with(getApplicationContext())
                        .load(image)
                        .into(imageViewOfertaPreview);

                break;
        }
        String nombreCompleto = profile.getName();
        nombreCompletoFacebook.setText(nombreCompleto);
        txtNombreOferta.setText(nombreOferta);
        txtDescripcion.setText(descripcion);
        txtPrecio.setText("$"+precio);
        txtCategoria.setText(categoria);
        nombreCompletoFacebook.setText(nombreCompleto);
        txtNombreUsuario.setText(nombreUsuario);


        Glide.with(getApplicationContext())
                .load(profile.getProfilePictureUri(100,100).toString())
                .into(imageViewPreview);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPreview);
        mapFragment.getMapAsync(this);

        btnCancelar.setText("Eliminar oferta");

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PreviewModificar.this);
                builder.setCancelable(true);
                builder.setTitle("¡Eliminar Publicacion!");
                builder.setMessage("¿Está seguro que desea eliminar permanentemente esta oferta?");
                builder.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Metodos met= new Metodos();
                                String url2 = met.getBdUrl()+"eliminarOferta.php?idOferta="+idOferta;
                                eliminarOferta(url2);
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                //goMainScreen();
            }
        });
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Metodos met= new Metodos();
                //String url = met.getBdUrl()+"insertarofertaPost.php";
                String url="https://salv8.000webhostapp.com/updateOferta.php";
                //url = url.replaceAll(" ","%20");
                insertarDatos(url);

            }
        });

    }
    public void eliminarOferta(String URL){
        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(PreviewModificar.this, "Correcto!", Toast.LENGTH_SHORT).show();
                goMainScreen();

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

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
                        Toast.makeText(PreviewModificar.this, "Correcto!"+s , Toast.LENGTH_LONG).show();
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
                        Toast.makeText(PreviewModificar.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("idOferta", String.valueOf(idOferta));
                params.put("nomOferta", nombreOferta);
                params.put("descOferta", descripcion);
                params.put("precioOferta", precio);
                params.put("cateOferta", categoria);
                params.put("lat", String.valueOf(lat));
                params.put("lng", String.valueOf(lng));
                switch (bandera){
                    case 1:
                        imagen = getStringImage(bitmap);
                        params.put("imagen", imagen);
                        break;
                    case 2:
                        params.put("imagen", image);
                        break;
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
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
