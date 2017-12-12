package com.apaza.proyecto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.apaza.proyecto.Clases.ApiServiceGenerator;
import com.apaza.proyecto.Clases.ResponseMessage;
import com.apaza.proyecto.Interface.ApiService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity2 extends AppCompatActivity {
    private static final String TAG = LoginActivity2.class.getSimpleName();

    private GoogleApiClient googleApiClient;
    private EditText user;
    private EditText pass;
    private SignInButton signInButton2;
    public static final int SIGN_IN_CODE = 777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        user = (EditText)findViewById(R.id.editText);
        pass = (EditText)findViewById(R.id.editText2);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
              //  .enableAutoManage(this,  this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton2 = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });

    }

    public void callLogin(View view){
        String username = user.getText().toString();
        String password = pass.getText().toString();

        ApiService servicio = ApiServiceGenerator.createService(ApiService.class);
        Call<ResponseMessage> call = null;

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Usuario y Contraseña son campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }else{
            call = servicio.createAuth(username,password);

            // Intent a = new Intent(this, MainActivity.class);
            // startActivity(a);
        }
        final Intent a = new Intent(this, MainActivity.class);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        ResponseMessage responseMessage = response.body();
                        Log.d(TAG, "responseMessage: " + responseMessage);

                        // Toast.makeText(LoginActivity.this, responseMessage.getMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(LoginActivity2.this, "Usuario válido, ingresando...", Toast.LENGTH_LONG).show();
                        startActivity(a);
                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Credenciales incorrectas");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(LoginActivity2.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(LoginActivity2.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== 777){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            goMainScreen();
        }else{
            Toast.makeText(this, "No se puede iniciar sesion", Toast.LENGTH_SHORT).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
