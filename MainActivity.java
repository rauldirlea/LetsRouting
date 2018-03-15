package com.letsrouting.com.letsrouting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.letsrouting.com.letsrouting.LogReg.Login;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Si es la primera vez que abrimos la aplicacion creamos el acceso directo
        if(getFirstTimeRun() == 0) {
            crearAccesoDirectoEnEscritorio();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(this,MenuPrincipal.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
            finish();
        }
        //Activamos el modo firebase sin connexion
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }catch (Exception e){

        }
    }

    //Crear acceso directo de la aplicacion al ejecutar por primera vez
    private void crearAccesoDirectoEnEscritorio() {
        //Cogemos el nombre de nuestra app
        CharSequence contentTitle = getString(R.string.app_name);
        Intent shortcutIntent = new Intent();
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, getIntentShortcut());
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, contentTitle.toString());
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this.getApplicationContext(), R.mipmap.ic_launcher));
        shortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        this.sendBroadcast(shortcutIntent);
    }

    public Intent getIntentShortcut(){
        Intent i = new Intent();
        i.setClassName(this.getPackageName(), this.getPackageName() + "." + this.getLocalClassName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    private int getFirstTimeRun() {
        SharedPreferences sp = getSharedPreferences("MYAPP", 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt("FIRSTTIMERUN", -1);
        if (lastVersionCode == -1) result = 0; else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply();
        return result;
    }
}
