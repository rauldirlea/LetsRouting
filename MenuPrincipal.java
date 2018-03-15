package com.letsrouting.com.letsrouting;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.letsrouting.com.letsrouting.MenuFragments.AjustsFragment;
import com.letsrouting.com.letsrouting.MenuFragments.FiltrosFragment;
import com.letsrouting.com.letsrouting.MenuFragments.LogrosFragment;
import com.letsrouting.com.letsrouting.MenuFragments.RutasFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuPrincipal extends AppCompatActivity

    implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private Session session;
    private CardView cvMenuPrincial;
    private Boolean cvState=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        session = new Session(this); 

        cvMenuPrincial = (CardView) findViewById(R.id.cvMenuFiltros);

        RutasFragment rutasFragment = new RutasFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, rutasFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Adaptamos el nav header menu
        setNavHeaderMenu();

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
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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
            if(cvState){
                cvMenuPrincial.setVisibility(View.VISIBLE);
                cvState=!cvState;
            }else{
                cvMenuPrincial.setVisibility(View.GONE);
                cvState=!cvState;
            }
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here. //Toast.makeText(MenuPrincipal.this, "Prueba", Toast.LENGTH_LONG).show();
        int id = item.getItemId();

        if (id == R.id.nav_rutas) {
            //Fragment Rutas
            RutasFragment rutasFragment = new RutasFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, rutasFragment).commit();
            // Handle the camera action
        } else if (id == R.id.nav_filtros) {
            //Fragment Filtros de rutas
            FiltrosFragment filtrosFragment = new FiltrosFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, filtrosFragment).commit();

        } else if (id == R.id.nav_logros) {
            //Fragment Rutas Logradas
            LogrosFragment logrosFragment = new LogrosFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, logrosFragment).commit();
        } else if (id == R.id.nav_perfil) {
            //Fragment Ajustes del usuario
            AjustsFragment ajustsFragment = new AjustsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, ajustsFragment).commit();

        }  else if (id == R.id.nav_manage) {

        }else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavHeaderMenu(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        final CircleImageView civNavFoto = (CircleImageView) hView.findViewById(R.id.civNavFoto);
        final TextView tvNombre = (TextView) hView.findViewById(R.id.txNavNameUser);
        final TextView tvEmail = (TextView) hView.findViewById(R.id.txNavEmailUser);
        tvNombre.setText(session.getNameUser());
        tvEmail.setText(firebaseAuth.getCurrentUser().getEmail());

        /*Set profile image*/
        try {

            if (firebaseAuth.getCurrentUser().getPhotoUrl() == null) {
                civNavFoto.setImageResource(R.drawable.user_default);
            } else {
                Glide.with(MenuPrincipal.this).load(firebaseAuth.getCurrentUser().getPhotoUrl().toString()).asBitmap().into(civNavFoto);
            }
        } catch (Exception e) {

        }
        /*LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.nav_header_menu_principal, null);

        final CircleImageView civNavFoto = (CircleImageView) hView.findViewById(R.id.civNavFoto);
        final TextView tvNombre = (TextView) hView.findViewById(R.id.txNavNameUser);
        final TextView tvEmail = (TextView) hView.findViewById(R.id.txNavEmailUser);

        tvNombre.setText(session.getNameUser());
        tvEmail.setText(firebaseAuth.getCurrentUser().getEmail());*/

    }
}
