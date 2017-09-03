package com.example.chris.sipho;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private int resp;
    Metodos met = new Metodos();
    private ProfileTracker profileTracker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        final String bd = met.getBdUrl();




        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                profileTracker = new ProfileTracker() {


                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        if (currentProfile != null) {
                            met.guardarDatosFacebook(currentProfile);
                            String url= bd+"consulta.php?id="+met.getId();
                            buscarExistencia(url);
                            Toast.makeText(LoginActivity.this, "1", Toast.LENGTH_SHORT).show();
                            if(resp == 1){
                                goCrearScreen();
                                Toast.makeText(LoginActivity.this, "12", Toast.LENGTH_SHORT).show();


                            }else {
                                goMainScreen();
                                Toast.makeText(LoginActivity.this, "Bienvenido de vuelta", Toast.LENGTH_LONG).show();

                            }
                        }
                    }

                };
                if (AccessToken.getCurrentAccessToken() == null) {
                    Toast.makeText(LoginActivity.this, "Error facebook", Toast.LENGTH_SHORT).show();

                } else {
                    final Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        met.guardarDatosFacebook(profile);
                        String url= bd+"consulta.php?id="+met.getId();
                        buscarExistencia(url);
                        if(resp == 1){
                            goCrearScreen();


                        }else {
                            goMainScreen();
                            Toast.makeText(LoginActivity.this, "Bienvenido de vuelta", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Profile.fetchProfileForCurrentAccessToken();
                    }

                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goCrearScreen() {
        Intent intent = new Intent(this, CrearActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void buscarExistencia(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(LoginActivity.this, "69", Toast.LENGTH_SHORT).show();
                resp = 1;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Ops Error", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
    }
}
