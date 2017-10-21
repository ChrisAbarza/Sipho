package com.example.chris.sipho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class VerUsuario extends AppCompatActivity {

    CircleImageView fotoDeFace;
    TextView nombreCompleto,nombreUsuario,ofertasTotales,seguidores,seguidos;
    ListView listViewOfertas;
    String fotoPerfil,nombreFace,usrFace,idUsr;

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

        nombreCompleto.setText(nombreFace);
        nombreUsuario.setText(usrFace);

        Glide.with(getApplicationContext())
                .load(fotoPerfil)
                .into(fotoDeFace);

    }
}
