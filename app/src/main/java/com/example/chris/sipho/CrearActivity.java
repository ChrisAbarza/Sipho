package com.example.chris.sipho;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.signature.StringSignature;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.chris.sipho.R.id.idTextView;
import static com.example.chris.sipho.R.id.nameTextView;
import static com.example.chris.sipho.R.id.photoImageView;

public class CrearActivity extends AppCompatActivity {

    private Button btnCrear;
    private ImageView imageViewCrear;
    private EditText editTextCrear;
    private TextView textViewCrear;

    private ProfileTracker profileTracker;


    Profile profile = Profile.getCurrentProfile();

    Metodos met = new Metodos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);
        met.guardarDatosFacebook(profile);



        btnCrear = (Button) findViewById(R.id.buttonCrear);
        imageViewCrear = (CircleImageView) findViewById(R.id.imageViewCrear);
        editTextCrear = (EditText) findViewById(R.id.editTextCrear);
        textViewCrear = (TextView) findViewById(R.id.textViewCrear);


        //Toast.makeText(CrearActivity.this, "1", Toast.LENGTH_SHORT).show();
        final String id = profile.getId();
        displayProfileInfo();






        btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nombre = editTextCrear.getText().toString();
                if(nombre.isEmpty()){
                    editTextCrear.setError("¡Ingresa tu nombre de usuario!");
                }else{
                    if(nombre.length() < 3){
                        editTextCrear.setError("Minimo 3 caracteres");
                    }else {
                        String existe= met.getBdUrl()+"completarVerOferta.php?nombre="+nombre;
                        buscarExistencia(existe);
                    }
                }


            }
        });




    }
    public void buscarExistencia(String URL){
        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray ja;
                    ja = new JSONArray(response);
                    Toast.makeText(CrearActivity.this, "Usuario ya existe", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    String insertar = met.getBdUrl()+"registro.php?id="+profile.getId()+"&nombre="+editTextCrear.getText().toString()+"&completo="+met.getName()+"&img="+met.getPhotoUrl();
                    insertar = insertar.replaceAll(" ","%20");
                    insertarDatos(insertar);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CrearActivity.this, "Ops Error", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }
    public void insertarDatos(String URL){
        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(CrearActivity.this, "Correcto!", Toast.LENGTH_SHORT).show();
                goMainScreen();

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

    private void displayProfileInfo() {
        String id = met.getId();
        String name = met.getName();
        String photoUrl = met.getPhotoUrl();

        textViewCrear.setText(name);

        Glide.with(getApplicationContext())
                .load(photoUrl)
                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()/ (15 * 1000)))))
                .into(imageViewCrear);
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
