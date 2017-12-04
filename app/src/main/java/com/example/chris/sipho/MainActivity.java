package com.example.chris.sipho;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.signature.StringSignature;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.id;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLng;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    public static final String IDUSUARIO ="idUsuario" ;
    public static final String LATITUD = "latitud" ;
    public static final String LONGITUD = "longitud" ;
    public static final String NOMBREUSUARIO = "nombreUsuario" ;
    public static final String IMGUSUARIO = "imgUsuario";
    public static final String NOMBRECOMPLETO ="nombreCompleto" ;
    private GoogleMap map;
    private ImageView photoImageView,imageDialog;
    private TextView nameTextView,nomDialog,precDialog,descDialog,cancelDialog,showDialog;
    private TextView idTextView;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    JSONArray ja;
    private String usrname;
    double lat = 0.0;
    double lng = 0.0;
    Dialog dialog;
    String usrid,idOfertaDialog ;
    List cargar = new ArrayList();

    private ProfileTracker profileTracker;


    Metodos meto = new Metodos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        photoImageView = (CircleImageView) hView.findViewById(R.id.photoImageView);
        nameTextView = (TextView) hView.findViewById(R.id.nameTextView);
        idTextView = (TextView) hView.findViewById(R.id.idTextView);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    meto.guardarDatosFacebook(currentProfile);
                    displayProfileInfo();
                }
            }
        };

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginScreen();
        } else {
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                meto.guardarDatosFacebook(profile);
                usrid = meto.getId().toString();
                String urlBuscarUsr = meto.getBdUrl() + "consultaNomUsr.php?id=" + usrid;
                buscarNombreUsuario(urlBuscarUsr);
                displayProfileInfo();

            } else {
                Profile.fetchProfileForCurrentAccessToken();
            }

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, NewOffActivity.class);
                intent.putExtra(IDUSUARIO, usrid);
                intent.putExtra(LATITUD, lat);
                intent.putExtra(LONGITUD, lng);
                intent.putExtra(NOMBREUSUARIO,idTextView.getText().toString());
                intent.putExtra(IMGUSUARIO,meto.getPhotoUrl());
                intent.putExtra(NOMBRECOMPLETO,meto.getName());
                startActivity(intent);

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);




    }




    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }

    private void displayProfileInfo() {


        String name = meto.getName();
        String photoUrl = meto.getPhotoUrl();

        nameTextView.setText(name);


        Glide.with(getApplicationContext())
                .load(photoUrl)
                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()))))
                .into(photoImageView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DialogFragment dialogFragment = new DialogoGustos();
            dialogFragment.show(getSupportFragmentManager(),"dialogogusto");
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Intent intent = new Intent(this, VerMiUsuario.class);
            intent.putExtra("nombre",idTextView.getText().toString());
            startActivity(intent);
        } else if (id == R.id.nav_categoria) {
            Intent intent = new Intent(this, BuscarCategoria.class);
            startActivity(intent);

        } else if (id == R.id.nav_seguidos) {
            Intent intent = new Intent(this, BuscarSeguido.class);
            startActivity(intent);

        } else if (id == R.id.nav_mis_publicaciones) {
            Intent intent = new Intent(this, MisPublicaciones.class);
            startActivity(intent);

        } else if (id == R.id.nav_ayuda) {


        } else if (id == R.id.nav_end_session) {
            LoginManager.getInstance().logOut();
            goLoginScreen();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        profileTracker.stopTracking();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        miUbicacion();
        TinyDB tinydb2 = new TinyDB(this);
        cargar=tinydb2.getListInt("SIPHO_GUSTOS");
        String[] argusto=getResources().getStringArray(R.array.opcionesCategoria);
        int largo = cargar.size();
        String url = meto.getBdUrl()+"cargarMarcador.php";
        switch (largo){
            case 0:
                 url = meto.getBdUrl()+"cargarMarcador.php";
                break;
            case 1:
                 url = meto.getBdUrl()+"cargarMarcador1.php?1="+argusto[Integer.valueOf(cargar.get(0).toString())];
                break;
            case 2:
                 url = meto.getBdUrl()+"cargarMarcador2.php?1="+argusto[Integer.valueOf(cargar.get(0).toString())]+"&2="+argusto[Integer.valueOf(cargar.get(1).toString())];
                break;
            case 3:
                url = meto.getBdUrl()+"cargarMarcador3.php?1="+argusto[Integer.valueOf(cargar.get(0).toString())]+"&2="+argusto[Integer.valueOf(cargar.get(1).toString())]+"&3="+argusto[Integer.valueOf(cargar.get(2).toString())];
                break;
            case 4:
                url = meto.getBdUrl()+"cargarMarcador4.php?1="+argusto[Integer.valueOf(cargar.get(0).toString())]+"&2="+argusto[Integer.valueOf(cargar.get(1).toString())]+"&3="+argusto[Integer.valueOf(cargar.get(2).toString())]
                      +"&4="+argusto[Integer.valueOf(cargar.get(3).toString())];

                break;
            case 5:
                url = meto.getBdUrl()+"cargarMarcador5.php?1="+argusto[Integer.valueOf(cargar.get(0).toString())]+"&2="+argusto[Integer.valueOf(cargar.get(1).toString())]+"&3="+argusto[Integer.valueOf(cargar.get(2).toString())]
                        +"&4="+argusto[Integer.valueOf(cargar.get(3).toString())]+"&5="+argusto[Integer.valueOf(cargar.get(4).toString())];
                break;
            case 6:
                url = meto.getBdUrl()+"cargarMarcador6.php?1="+argusto[Integer.valueOf(cargar.get(0).toString())]+"&2="+argusto[Integer.valueOf(cargar.get(1).toString())]+"&3="+argusto[Integer.valueOf(cargar.get(2).toString())]
                        +"&4="+argusto[Integer.valueOf(cargar.get(3).toString())]+"&5="+argusto[Integer.valueOf(cargar.get(4).toString())]+"&6="+argusto[Integer.valueOf(cargar.get(5).toString())];
                break;
            case 7:
                url = meto.getBdUrl()+"cargarMarcador7.php?1="+argusto[Integer.valueOf(cargar.get(0).toString())]+"&2="+argusto[Integer.valueOf(cargar.get(1).toString())]+"&3="+argusto[Integer.valueOf(cargar.get(2).toString())]
                        +"&4="+argusto[Integer.valueOf(cargar.get(3).toString())]+"&5="+argusto[Integer.valueOf(cargar.get(4).toString())]+"&6="+argusto[Integer.valueOf(cargar.get(5).toString())]
                        +"&7="+argusto[Integer.valueOf(cargar.get(6).toString())];
                break;
            case 8:
                url = meto.getBdUrl()+"cargarMarcador8.php?1="+argusto[Integer.valueOf(cargar.get(0).toString())]+"&2="+argusto[Integer.valueOf(cargar.get(1).toString())]+"&3="+argusto[Integer.valueOf(cargar.get(2).toString())]
                        +"&4="+argusto[Integer.valueOf(cargar.get(3).toString())]+"&5="+argusto[Integer.valueOf(cargar.get(4).toString())]+"&6="+argusto[Integer.valueOf(cargar.get(5).toString())]
                        +"&7="+argusto[Integer.valueOf(cargar.get(6).toString())]+"&8="+argusto[Integer.valueOf(cargar.get(7).toString())];
                break;
            case 9:
                url = meto.getBdUrl()+"cargarMarcador.php";
                break;
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            map.setMyLocationEnabled(true);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }


        }
        url = url.replaceAll(" ","%20");
        cargarMarcadores(url);

    }

    private void actualizarCamara(double lat, double lng) {
        LatLng coordenada = new LatLng(lat, lng);
        CameraUpdate ubicacion = CameraUpdateFactory.newLatLngZoom(coordenada, 16);
        map.moveCamera(ubicacion);

    }

    private void actualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            actualizarCamara(lat, lng);
        }

    }

    LocationListener locListen = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            actualizarUbicacion(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarUbicacion(loc);
    }

    private void cargarMarcadores(String url){
        Log.i("url",""+url);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.replace("][",",");
                if(response.length()>0){
                    try {
                        JSONArray mja = new JSONArray(response);
                        Log.i("sizejson",""+mja.length());
                        insertarMarcadores(mja);


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
                Toast.makeText(MainActivity.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);


    }
    private void insertarMarcadores(JSONArray mja){
        ArrayList<String> lista = new ArrayList<>();
        for (int i=0;i<mja.length();i+=5){
            try {
                  lista.add(mja.getString(i)+",æè"+mja.getString(i+1)+",æè"+mja.getString(i+2)+",æè"+mja.getString(i+3)+",æè"+mja.getString(i+4));
            }catch (JSONException e){

            }
        }
        for (int m=0;m<lista.size();m++){
            String[] slatlng = lista.get(m).split(",æè");
            LatLng latLng = new LatLng(Double.valueOf(slatlng[1]),Double.valueOf(slatlng[2]));
            switch (slatlng[3]){
                case "Comida":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.comida))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)


                    );
                    break;
                case "Fiesta":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.copete))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)

                    );
                    break;
                case "Actividades y eventos":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.eventos))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)

                    );
                    break;
                case "Servicios":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.servicio))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)

                    );
                    break;
                case "Moda":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.moda))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)

                    );
                    break;
                case "Electrodomésticos":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.electrodomesticos))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)

                    );
                    break;
                case "Autos y motos":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.rueda))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)

                    );
                    break;
                case "Hogar":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.casa))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)

                    );
                    break;
                case "Otros":
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.otros))
                            .title(slatlng[0])
                            .snippet(slatlng[4].toString())
                            .position(latLng)

                    );
                    break;
            }
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    String idOferta = marker.getSnippet();

                    buscarDialogoInfo(idOferta);
                    return false;
                }
            });

        }

    }

    private void buscarDialogoInfo(String idOferta) {
        String url=meto.getBdUrl()+"cargarDialogInfo.php?id="+idOferta;
        Log.i("url",""+url);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                response = response.replace("][",",");
                if(response.length()>0){
                    try {
                        JSONArray mja = new JSONArray(response);
                        Log.i("sizejson",""+mja.length());
                        cargarDialogoInfo(mja);


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
                Toast.makeText(MainActivity.this, "Ops Error 1"+error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }

    private void cargarDialogoInfo(JSONArray mja) {
        ArrayList<String> lista = new ArrayList<>();
        String image="",nombre="",precio="",desc="",id="";
        for (int i=0;i<mja.length();i+=5){
            try {
                lista.add(mja.getString(i)+",æè"+mja.getString(i+1)+",æè"+mja.getString(i+2)+",æè"+mja.getString(i+3)+",æè"+mja.getString(i+4));
            }catch (JSONException e){

            }
            for (int m=0;m<lista.size();m++){
                String[] lista2 = lista.get(m).split(",æè");
                nombre = lista2[0].toString();
                precio = lista2[1].toString();
                desc = lista2[2].toString();
                image = lista2[3].toString();
                id = lista2[4].toString();

            }

        }
        mostrarDialogoInfo(nombre,precio,desc,image,id);

    }

    private void mostrarDialogoInfo(String nombre, String precio, String desc, String image, String id) {
        final String idsd = id;
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialogo_info, null);
        mBuilder.setView(mView);
        imageDialog = (ImageView) mView.findViewById(R.id.imageViewDialog);
        nomDialog = (TextView) mView.findViewById(R.id.textViewNomDialog);
        precDialog = (TextView) mView.findViewById(R.id.textViewPreDialog);
        descDialog = (TextView) mView.findViewById(R.id.textViewDescDialog);
        cancelDialog = (TextView) mView.findViewById(R.id.cancelTxt);
        showDialog = (TextView) mView.findViewById(R.id.showTxt);
        final AlertDialog dialog = mBuilder.create();

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        showDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VerOfertaDialog.class);
                intent.putExtra("idOferta", idsd);
                startActivity(intent);

            }
        });
        nomDialog.setText(nombre);
        precDialog.setText("$"+precio);
        descDialog.setText(desc);

        Glide.with(getApplicationContext())
                .load(image)
                .signature((new StringSignature(String.valueOf(System.currentTimeMillis()))))
                .into(imageDialog);


        dialog.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        
                        map.setMyLocationEnabled(true);
                    }


                }else{
                    Toast.makeText(getApplicationContext(), "Dáme los permisos o moriré", Toast.LENGTH_LONG).show();
                    finish();

                }
                break;
        }
    }
    public void buscarNombreUsuario(String URL){
        Log.i("url",""+URL);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    ja = new JSONArray(response);
                    usrname = ja.getString(0);
                    if(usrname.equals("")){
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();

                    }else{
                        idTextView.setText("@"+usrname);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Error "+e,Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Ops Error", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }
}

