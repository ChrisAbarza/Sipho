package com.example.chris.sipho;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.R.string.no;
import static com.example.chris.sipho.NewOffActivity.NOMBRECOMPLETO2;
import static com.example.chris.sipho.R.id.imageViewOferta;

/**
 * Created by salv8 on 22/10/2017.
 */

public class ModificarOffActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView imageView;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private int ban;
    public static final String NOMBREOFERTA = "nombreOferta" ;
    public static final String DESCRIPCION = "descripcion";
    public static final String PRECIO = "precio";
    public static final String IDOFERTA = "IdOferta";
    public static final String IDUSUARIO2 = "idUsuario";
    public static final String IMGUSUARIO2 = "imgUsuario" ;
    public static final String NOMBREUSUARIO2 ="nombreUsuario" ;
    public static final String LATITUD2 ="latitud" ;
    public static final String LONGITUD2 = "longitud";
    public static final String CATEGORIA = "categoria";
    Spinner categoria;
    EditText nombre,descripcion,precio;
    Button btnCancelar,btnprevisualizar,buttonChoose,buttonCamera;
    Oferta off;


    public String nombreCompleto, idUsuario,imgUsuario,nombreUsuario,categoriaOferta;
    Double lat,lng;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_off);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opcionesCategoria, android.R.layout.simple_spinner_item);

        categoria = (Spinner) findViewById(R.id.spCategoria);
        nombre = (EditText) findViewById(R.id.editTextNombre);
        descripcion= (EditText) findViewById(R.id.editTextDescripcion);
        precio = (EditText) findViewById(R.id.editTextPrecio);
        btnCancelar = (Button) findViewById(R.id.buttonCancelarOferta);
        btnprevisualizar = (Button) findViewById(R.id.buttonPrevisualizar);
        imageView  = (ImageView) findViewById(R.id.imageViewNewOff);
        buttonCamera = (Button) findViewById(R.id.buttonChooseCamera);

        off = (Oferta) getIntent().getExtras().getSerializable("oferta");

        nombre.setText(off.getNomOferta());
        descripcion.setText(off.getDescOferta());
        precio.setText(String.valueOf(off.getPrecioOferta()));

        ban = 0;

        Glide.with(getApplicationContext())
                .load(off.getImagen())
                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()/ (15 * 1000)))))
                .into(imageView);

        setupActionBar();

        categoria.setAdapter(adapter);
        categoria.setOnItemSelectedListener(this);

        btnCancelar.setText("Eliminar");

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModificarOffActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnprevisualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreoff= nombre.getText().toString();
                String descoff = descripcion.getText().toString();
                String preciooff= precio.getText().toString();

                if(nombreoff.isEmpty() || descoff.isEmpty() || preciooff.isEmpty() ){
                    if(nombreoff.isEmpty()){
                        nombre.setError("¡Ingresa el nombre de la oferta!");
                    }
                    if(descoff.isEmpty()){
                        descripcion.setError("¡Ingresa una descripción!");
                    }
                    if(preciooff.isEmpty()){
                        precio.setError("¡Ingresa el precio de la oferta!");
                    }

                }else {
                int bandera;

                Intent intent = new Intent(ModificarOffActivity.this, PreviewModificar.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if(bitmap != null && ban == 1){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("bitmap",byteArray);
                    bandera = 1;
                }else{
                    intent.putExtra("image",off.getImagen());
                    bandera = 2;
                }
                intent.putExtra("bandera",bandera);
                intent.putExtra(NOMBREOFERTA, nombre.getText().toString());
                intent.putExtra(DESCRIPCION, descripcion.getText().toString());
                intent.putExtra(PRECIO, precio.getText().toString());
                intent.putExtra(IDOFERTA, String.valueOf(off.getId()));
                intent.putExtra(NOMBREUSUARIO2, off.getUsuario());
                intent.putExtra(LATITUD2, off.getLat());
                intent.putExtra(LONGITUD2, off.getLng());
                intent.putExtra(CATEGORIA,categoriaOferta);

                startActivity(intent);
                }
            }
        });

        categoria.setSelection(adapter.getPosition(off.getCateOferta()));
    }
    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Editar Publicacion");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myTextCategoria= (TextView) view;
        categoriaOferta = myTextCategoria.getText().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 0:
                //Getting the Bitmap from Gallery
                bitmap = (Bitmap) data.getExtras().get("data");
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
                ban = 1;

                break;
            case 1:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    try {
                        //Getting the Bitmap from Gallery
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        //Setting the Bitmap to ImageView
                        imageView.setImageBitmap(bitmap);
                        ban = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
