package com.example.chris.sipho;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.chris.sipho.R.id.photoImageView;

public class VerOferta extends AppCompatActivity {
    String imgusr,URL;
    Metodos met = new Metodos();
    TextView nombreCompletoFacebook;
    ImageView imageViewUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_oferta);

        TextView txtNombreOferta = (TextView) findViewById(R.id.textViewNombreOfertaVer);
        TextView txtDescripcion = (TextView) findViewById(R.id.textViewDescripcionVer);
        TextView txtPrecio = (TextView) findViewById(R.id.textViewPrecioVer);
        nombreCompletoFacebook = (TextView) findViewById(R.id.textViewNombreCompletoVer);
        TextView txtNombreUsuario = (TextView) findViewById(R.id.textViewNombreUsuarioVer);
        TextView txtCategoria = (TextView) findViewById(R.id.textViewCategoriaVer);
        imageViewUsuario = (CircleImageView) findViewById(R.id.imageViewUsuarioOfertaVer);
        ImageView imageViewOferta = (ImageView) findViewById(R.id.imageViewOfertaVer);
        Button btnCancelar = (Button) findViewById(R.id.buttonCancelarVer);
        Button btnPublicar = (Button) findViewById(R.id.buttonPublicarVer);

        Oferta off = (Oferta) getIntent().getExtras().getSerializable("oferta");

        txtNombreOferta.setText(off.getNomOferta());
        txtDescripcion.setText(off.getDescOferta());
        txtPrecio.setText("$"+String.valueOf(off.getPrecioOferta()));
        txtNombreUsuario.setText("@"+off.getUsuario());
        txtCategoria.setText(off.getCateOferta());

        URL=met.getBdUrl()+"completarVerOferta.php?nombre="+off.getUsuario();
        buscarDatosExtrasUsuario(URL);

        Glide.with(getApplicationContext())
                .load(off.getImagen())
                .into(imageViewOferta);





    }
    private void buscarDatosExtrasUsuario(String URL){
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
                        ArrayList<String> lista = new ArrayList<>();
                        lista.add(mja.getString(0)+",æè"+mja.getString(1));
                        for (int m=0;m<lista.size();m++) {
                            String[] slista = lista.get(m).split(",æè");
                            nombreCompletoFacebook.setText(slista[0]);
                            Glide.with(getApplicationContext())
                                    .load(slista[1].toString())
                                    .into(imageViewUsuario);

                            //Lista.add(new Oferta(Integer.valueOf(slista[0]),Integer.valueOf(slista[1]), slista[2],slista[3],slista[4],slista[5],slista[6], slista[7],Double.valueOf(slista[8]),Double.valueOf(slista[9])));

                        }


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
                Toast.makeText(VerOferta.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);

    }
}
