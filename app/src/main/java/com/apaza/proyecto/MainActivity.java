package com.apaza.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView photoimageView,imageView;
    private TextView nametextview,textviewemail,textviewusername;
    private TextView emailtextview;
    private TextView idtextview;
    //private TextView emaill;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        textviewemail = navigationView.getHeaderView(0).findViewById(R.id.textViewEmaill);
        textviewusername = navigationView.getHeaderView(0).findViewById(R.id.textViewUsername);
        navigationView.setNavigationItemSelectedListener(this);

        photoimageView = (ImageView) findViewById(R.id.photoImageView);
        nametextview = (TextView) findViewById(R.id.nameTextView);
        emailtextview = (TextView) findViewById(R.id.emailTextView);
        idtextview = (TextView) findViewById(R.id.idTextView);

        //emaill = (TextView) findViewById(R.id.textViewEmaill);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
            //    .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }


    @Override
    protected void onStart(){
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

        private void handleSignInResult(GoogleSignInResult result) {
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                nametextview.setText(account.getDisplayName());
                emailtextview.setText(account.getEmail());
                idtextview.setText(account.getId());
               // emaill.setText(account.getEmail());
                textviewusername.setText(account.getDisplayName());
                textviewemail.setText(account.getEmail());
                Glide.with(this).load(account.getPhotoUrl()).into(photoimageView);
                Glide.with(this).load(account.getPhotoUrl()).into(imageView);
            }else {
                goLogInScreen();
            }
        }

            private void goLogInScreen() {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent a = new Intent(MainActivity.this,ListActivity.class);
            startActivity(a);
        } else if (id == R.id.nav_gallery) {
            Intent b = new Intent(this, PresentationActivity.class);
            startActivity(b);
        }   else if (id == R.id.nav_share) {
            Intent c = new Intent(this, LoginActivity2.class);
            startActivity(c);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
