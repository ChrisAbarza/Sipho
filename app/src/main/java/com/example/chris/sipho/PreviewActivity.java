package com.example.chris.sipho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.chris.sipho.R.id.imageViewCrear;

public class PreviewActivity extends AppCompatActivity {
    String nombreCompleto,nombreOferta,descripcion,precio,idUsuario,imgUsuario,nombreUsuario,categoria;
    Double lat,lng;
    int id;

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

        txtNombreOferta.setText(nombreOferta);
        txtDescripcion.setText(descripcion);
        txtPrecio.setText("$"+precio);
        txtCategoria.setText(categoria);
        nombreCompletoFacebook.setText(nombreCompleto);
        txtNombreUsuario.setText(nombreUsuario);

        Glide.with(getApplicationContext())
                .load(imgUsuario)
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
                Toast.makeText(PreviewActivity.this, ""+descripcion, Toast.LENGTH_SHORT).show();
                String url = met.getBdUrl()+"insertarOferta.php?id="+id+"&usuario="+idUsuario+"&nomOferta="+nombreOferta+"&descOferta="+descripcion+"&precioOferta="+precio+"&cateOferta="+categoria+"&lat="+lat+"&lng="+lng;
                insertarDatos(url);

            }
        });

    }
    private void generarIDAleatorio(){
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        id = random.nextInt(2000000000);

    }
    public void insertarDatos(String URL){
        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(PreviewActivity.this, "Correcto!", Toast.LENGTH_SHORT).show();
                goMainScreen();

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PreviewActivity.this, ""+error, Toast.LENGTH_LONG).show();

            }
        });

        queue.add(stringRequest);

    }
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
