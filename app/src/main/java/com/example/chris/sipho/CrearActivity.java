package com.example.chris.sipho;

import android.content.Intent;
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

    Metodos met = new Metodos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear);

        final Profile profile = Profile.getCurrentProfile();



        btnCrear = (Button) findViewById(R.id.buttonCrear);
        imageViewCrear = (CircleImageView) findViewById(R.id.imageViewCrear);
        editTextCrear = (EditText) findViewById(R.id.editTextCrear);
        textViewCrear = (TextView) findViewById(R.id.textViewCrear);


        //Toast.makeText(CrearActivity.this, "1", Toast.LENGTH_SHORT).show();
        met.guardarDatosFacebook(profile);
        displayProfileInfo();






        btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String insertar = met.getBdUrl()+"registro.php?id="+met.getId()+"&nombre="+editTextCrear.getText();
                insertarDatos(insertar);

            }
        });




    }
    public void insertarDatos(String URL){



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
                .into(imageViewCrear);
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
