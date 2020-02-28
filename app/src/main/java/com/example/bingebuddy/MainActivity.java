/*
This is a course requirement for CS 192
Software Engineering II under the
supervision of Asst. Prof. Ma. Rowena C.
Solamo of the Department of Computer
Science, College of Engineering, University
of the Philippines, Diliman for the AY 2015-
2016.
*/
//File Creation Date: 01/30/2020
//Software purpose: This is the main activity of the application. This sets the default views/ layouts that will be
//displayed on application startup

//changelog
//Update: Samuel Jose, 01/30/2020
//Update: Samuel Jose, 02/14/2020
//  -added some variables in preparation for trakt api

package com.example.bingebuddy;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import com.example.bingebuddy.ui.api.AccessToken;
import com.example.bingebuddy.ui.api.ApiServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    //Trakt.tv API stuff (redirecturi, clientId and secret should be stored in gradle.properties, and access token and refresh token should be
    //stored in database once it is connected

    private String clientId = "b3825447e716f64c278c9768add7022ebe67644fc9adb4530f343bd8b56b6c6f";
    private String clientSecret = "8bc0fd46dc7ab5b77650ccfd46a4b6ecd618a85dc53bed09875026ce05ba2170";
    private String redirectUri = "bingebuddy://callback";
    private static String accessToken = ""; //pls dont judge

    //to be changed since we will use database
    public String getAccessToken(){
        return accessToken;
    }
    public String getClientId(){
        return clientId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set first view
        setContentView(R.layout.activity_main);

        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //floating action button for addshows
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isConnected()) {
                    Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    Snackbar.make(view, "May internet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        //nav bar
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_suggestions,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Toast.makeText(MainActivity.this,"tmdb the access token is:"+ BuildConfig.THE_MOVIE_DB_API_TOKEN, Toast.LENGTH_SHORT).show();
        if (accessToken == ""){
            //Toast.makeText(MainActivity.this,"no the access token is:"+ accessToken, Toast.LENGTH_SHORT).show();
            //authorize applicaton
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.trakt.tv/oauth/authorize?response_type=code"+"&client_id="+clientId+
                    "&redirect_uri="+redirectUri));
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith(redirectUri)){
            String code = uri.getQueryParameter("code");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.trakt.tv/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiServices client = retrofit.create(ApiServices.class);
            Call<AccessToken> accessTokenCall = client.getAccessToken(
                    code,
                    clientId,
                    clientSecret,
                    redirectUri,
                    "authorization_code"
            );

            accessTokenCall.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    accessToken = response.body().getAccessToken();
                    Toast.makeText(MainActivity.this,"yay the access token is:"+ accessToken,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t){
                    Toast.makeText(MainActivity.this,"no", Toast.LENGTH_SHORT).show();
                }
            });
            //Toast.makeText(this,"code: "+code, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onSupportNavigateUp() {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean isConnected(){
        ConnectivityManager manager = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
