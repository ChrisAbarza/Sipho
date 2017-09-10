package com.example.chris.sipho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.chris.sipho.R.id.imageViewCrear;

public class PreviewActivity extends AppCompatActivity {
    String nombreCompleto,nombreOferta,descripcion,precio,idUsuario,imgUsuario,nombreUsuario,categoria;
    Double lat,lng;

    @Override
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

        Toast.makeText(this, "asd"+nombreCompleto, Toast.LENGTH_SHORT).show();

        Glide.with(getApplicationContext())
                .load(imgUsuario)
                .into(imageViewPreview);
    }
}
