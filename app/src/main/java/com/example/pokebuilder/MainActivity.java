package com.example.pokebuilder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.pokebuilder.ui.TeamBuilder;
import com.example.pokebuilder.ui.ui.login.LoginActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokebuilder.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedPreferences sharedPreferences;
    private TextView usernameTextView, emailTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        if(!sharedPreferences.contains("username")) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTeamBuilder = new Intent(getApplicationContext(), TeamBuilder.class);
                startActivity(toTeamBuilder);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View hview = navigationView.getHeaderView(0);
        usernameTextView = (TextView) hview.findViewById(R.id.nav_header_username);
        emailTextView = (TextView) hview.findViewById(R.id.nav_header_email);
        usernameTextView.setText(sharedPreferences.getString("username", ""));
        emailTextView.setText(sharedPreferences.getString("email", ""));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_pokedex, R.id.nav_teams, R.id.sign_out)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onResume() {
        super.onResume();
        NavigationView  navigationView = binding.navView;
        View hview = navigationView.getHeaderView(0);
        usernameTextView = (TextView) hview.findViewById(R.id.nav_header_username);
        emailTextView = (TextView) hview.findViewById(R.id.nav_header_email);
        usernameTextView.setText(sharedPreferences.getString("username", ""));
        emailTextView.setText(sharedPreferences.getString("email", ""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}