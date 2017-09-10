package com.example.chris.sipho;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.N;
import static com.example.chris.sipho.MainActivity.IDUSUARIO;
import static com.example.chris.sipho.MainActivity.IMGUSUARIO;
import static com.example.chris.sipho.MainActivity.LATITUD;
import static com.example.chris.sipho.MainActivity.LONGITUD;
import static com.example.chris.sipho.MainActivity.NOMBRECOMPLETO;
import static com.example.chris.sipho.MainActivity.NOMBREUSUARIO;
import static com.example.chris.sipho.R.id.idTextView;

public class NewOffActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String NOMBREOFERTA = "nombreOferta" ;
    public static final String DESCRIPCION = "descripcion";
    public static final String PRECIO = "precio";
    public static final String NOMBRECOMPLETO2 = "nombreCompleto";
    public static final String IDUSUARIO2 = "idUsuario";
    public static final String IMGUSUARIO2 = "imgUsuario" ;
    public static final String NOMBREUSUARIO2 ="nombreUsuario" ;
    public static final String LATITUD2 ="latitud" ;
    public static final String LONGITUD2 = "longitud";
    public static final String CATEGORIA = "categoria";
    Spinner categoria;
    EditText nombre,descripcion,precio;
    Button btnCancelar,btnprevisualizar;

    public String nombreCompleto, idUsuario,imgUsuario,nombreUsuario,categoriaOferta;
    Double lat,lng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_off);

        categoria = (Spinner) findViewById(R.id.spCategoria);
        nombre = (EditText) findViewById(R.id.editTextNombre);
        descripcion= (EditText) findViewById(R.id.editTextDescripcion);
        precio = (EditText) findViewById(R.id.editTextPrecio);
        btnCancelar = (Button) findViewById(R.id.buttonCancelarOferta);
        btnprevisualizar = (Button) findViewById(R.id.buttonPrevisualizar);

        Intent llegada = getIntent();
        nombreCompleto = llegada.getStringExtra(MainActivity.NOMBRECOMPLETO);
        idUsuario = llegada.getStringExtra(MainActivity.IDUSUARIO);
        imgUsuario = llegada.getStringExtra(MainActivity.IMGUSUARIO);
        nombreUsuario = llegada.getStringExtra(MainActivity.NOMBREUSUARIO);
        lat = llegada.getDoubleExtra(MainActivity.LATITUD, 0.0);
        lng = llegada.getDoubleExtra(MainActivity.LONGITUD, 0.0);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opcionesCategoria, android.R.layout.simple_spinner_item);
        categoria.setAdapter(adapter);
        categoria.setOnItemSelectedListener(this);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewOffActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnprevisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewOffActivity.this, PreviewActivity.class);
                intent.putExtra(NOMBREOFERTA, nombre.getText().toString());
                intent.putExtra(DESCRIPCION, descripcion.getText().toString());
                intent.putExtra(PRECIO, precio.getText().toString());
                intent.putExtra(NOMBRECOMPLETO2, nombreCompleto);
                intent.putExtra(IDUSUARIO2, idUsuario);
                intent.putExtra(IMGUSUARIO2, imgUsuario);
                intent.putExtra(NOMBREUSUARIO2, nombreUsuario);
                intent.putExtra(LATITUD2, lat);
                intent.putExtra(LONGITUD2, lng);
                intent.putExtra(CATEGORIA,categoriaOferta);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myTextCategoria= (TextView) view;
        categoriaOferta = myTextCategoria.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
