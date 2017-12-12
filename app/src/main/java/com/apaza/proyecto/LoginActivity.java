package com.apaza.proyecto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apaza.proyecto.Clases.ApiServiceGenerator;
import com.apaza.proyecto.Clases.ResponseMessage;
import com.apaza.proyecto.Interface.ApiService;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
  //  private GoogleApiClient googleApiClient;

  //  private SignInButton signInButton2;

    private ProgressBar progressBar;
    private View loginPanel;
    private EditText user;
    private EditText pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        loginPanel = findViewById(R.id.login_panel);
        user = (EditText)findViewById(R.id.editText);
        pass = (EditText)findViewById(R.id.editText2);

        // Init FirebaseAuth
        initFirebaseAuth();

        // Init GoogleSignIn
        initGoogleSignIn();

        // Init FirebaseAuthStateListener
        initFirebaseAuthStateListener();

  /*     GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton2 = (SignInButton) findViewById(R.id.sign_in_button2);
        signInButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,777);
            }
        });

*/



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
                        Toast.makeText(LoginActivity.this, "Usuario válido, ingresando...", Toast.LENGTH_LONG).show();
                        startActivity(a);
                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Credenciales incorrectas");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

    }

    private void initFirebaseAuth(){
        // initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //emailInput = (EditText)findViewById(R.id.email_input);
        // passwordInput = (EditText)findViewById(R.id.password_input);
    }

    /**
     * Firebase AuthStateListener
     */
    private FirebaseAuth.AuthStateListener mAuthListener;

    private void initFirebaseAuthStateListener(){
        // and the AuthStateListener method so you can track whenever the user signs in or out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    Toast.makeText(LoginActivity.this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

                    // Go MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }


            }
        };
    }



    /**
     * Google SignIn
     */

    /* Request code used to invoke sign in user interactions for Google+ */
    private static final int GOOGLE_SIGNIN_REQUEST = 1000;
    private FirebaseAuth mAuth;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    private void initGoogleSignIn(){

        // Configure SingIn Button
        SignInButton mGoogleLoginButton = (SignInButton) findViewById(R.id.sign_in_button);
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPanel.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                // OnClick Google SingIn Button
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, GOOGLE_SIGNIN_REQUEST);
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("925088591879-l8p4cb9nr8ng4g0p33ftvjlpulldvrmp.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.
                        Log.e(TAG, "onConnectionFailed:" + connectionResult);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Log.d(TAG, "onActivityResult: " + requestCode);
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == GOOGLE_SIGNIN_REQUEST) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {

                    // Google Sign In was successful
                    GoogleSignInAccount account = result.getSignInAccount();
                    Log.d(TAG, "IC: " + account.getId());
                    Log.d(TAG, "DISPLAYNAME: " + account.getDisplayName());
                    Log.d(TAG, "EMAIL: " + account.getEmail());
                    Log.d(TAG, "PHOTO: " + account.getPhotoUrl());
                    Log.d(TAG, "TOKEN: " + account.getIdToken());
                    Intent a = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(a);
                    // SignIn in firebaseAuthWithGoogle
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        loginPanel.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.GONE);
                                        Log.e(TAG, "signInWithCredential:failed", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                } else {
                    // Google Sign In failed, hide Progress Bar & Show Login Panel again
                    loginPanel.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Google Sign In failed!");
                }
            }

        }catch (Throwable t){
            try {
                // Google Sign In failed, hide Progress Bar & Show Login Panel again
                loginPanel.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onThrowable: " + t.getMessage(), t);
                if(getApplication()!=null) Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_LONG).show();
            } catch (Throwable x) {}
        }

    }

}
