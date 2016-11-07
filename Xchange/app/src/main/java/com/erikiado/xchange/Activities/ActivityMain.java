package com.erikiado.xchange.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.erikiado.xchange.FragmentExplore;
import com.erikiado.xchange.FragmentSeller;
import com.erikiado.xchange.FragmentSettings;
import com.erikiado.xchange.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context context = this;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        sharedPreferences = getSharedPreferences(getString(R.string.deal_preferences),MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initializeToolbar();
        initializeDrawer();
        initializeComponents();
        initializeFragment();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
////            case R.id.sign_out_menu:
////                mFirebaseAuth.signOut();
////                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
////                mUsername = ANONYMOUS;
////                startActivity(new Intent(this, SignInActivity.class));
////                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;
        Bundle bundle = new Bundle();

        if (id == R.id.nav_cerca) {
            fragmentClass = FragmentExplore.class;
        } else if (id == R.id.nav_restaurants) {
            fragmentClass = FragmentExplore.class;
        } else if (id == R.id.nav_drinks) {
            fragmentClass = FragmentExplore.class;
        } else if (id == R.id.nav_hotel) {
            fragmentClass = FragmentExplore.class;
        } else if (id == R.id.nav_turismo) {
            fragmentClass = FragmentExplore.class;
        } else if (id == R.id.nav_seller) {
            fragmentClass = FragmentSeller.class;
        } else if (id == R.id.nav_fav) {
            fragmentClass = FragmentExplore.class;
        } else if (id == R.id.nav_settings) {
            fragmentClass = FragmentSettings.class;
        }



        try {
            if(fragmentClass != null){
                fragment = (Fragment) fragmentClass.newInstance();
//                bundle.put
                fragment.setArguments(bundle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragments, fragment).commit();

        setTitle(item.getTitle());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);
//            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Deal");
//            toolbar.setTitleTextColor(getResources().getColor(R.color.cml_text_icons));
//            toolbar.setBackgroundColor(getResources().getColor(R.color.cml_primary_color));
        }

    }


    public void initializeDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    public void initializeComponents(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hola", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
//                startActivity(new Intent(ActivityMain.this,ActivityLogin.class));
            }
        });
        fab.hide();

    }

    public void initializeFragment(){
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = FragmentExplore.class;
        Bundle bundle = new Bundle();
        try {
            if(fragmentClass != null){
                fragment = (Fragment) fragmentClass.newInstance();
//                bundle.putString("Estado", nombreEstado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_fragments, fragment).commit();

//        toolbar.setTitle(nombreEstado);
    }

}
