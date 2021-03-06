package com.letsrouting.com.letsrouting.LogReg;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsrouting.com.letsrouting.MenuPrincipal;
import com.letsrouting.com.letsrouting.R;
import com.letsrouting.com.letsrouting.Rutas.AdapterRutas;
import com.letsrouting.com.letsrouting.Rutas.Ruta;
import com.letsrouting.com.letsrouting.Session;
import com.letsrouting.com.letsrouting.UserInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegisterHeare;
    private TextView txtRestorePassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        Login.this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();

        if(firebaseAuth.getCurrentUser() != null){
            //profile activity start here
            startActivity(new Intent(this, MenuPrincipal.class));
            finish();
        }

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegisterHeare =  (Button) findViewById(R.id.btnRegisterHeare);
        txtRestorePassword =  (TextView) findViewById(R.id.txtRestorePassword);

        btnLogin.setOnClickListener(this);
        btnRegisterHeare.setOnClickListener(this);
        txtRestorePassword.setOnClickListener(this);
    }

    private void userLogin(){
        String email = this.etEmail.getText().toString().trim();
        String password = this.etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //etEmail is empty
            getSnackbar("Por favor, ingresa una dirección.").show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //etPassword is empty
            getSnackbar("Por favor, introduzca una contraseña.").show();
            return;
        }

        progressDialog.setMessage("Cargando por favor espere...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    etEmail.getText().clear();
                    etPassword.getText().clear();
                    actualizarDatosUsuario();
                    startActivity(new Intent(getApplicationContext(), MenuPrincipal.class));
                    finishAffinity();
                    Toast.makeText(Login.this, "Inicio de sesión correcto", Toast.LENGTH_LONG).show();
                }else {
                    progressDialog.cancel();
                    etPassword.getText().clear();
                    getSnackbar("El email o la contraseña es incorrecto").show();
                }
            }
        });
    }

    private void restablecerContraseña(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mView = layoutInflater.inflate(R.layout.dialogo_restablecer_contrasena, null);
        final EditText etEmailRestore = (EditText) mView.findViewById(R.id.etEmailRestore);
        mBuilder.setView(mView);
        mBuilder.setTitle("Restablecer Contraseña");
        mBuilder.setMessage("Se enviara un email para restablecer la contraseña.");
        mBuilder.setPositiveButton("Restablecer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final FirebaseAuth auth = FirebaseAuth.getInstance();
                if(!etEmailRestore.getText().toString().isEmpty()){
                    String checkEmailAddress = etEmailRestore.getText().toString().trim();
                    firebaseAuth.fetchProvidersForEmail(checkEmailAddress).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            if(task.isSuccessful()) {
                                final String emailAddress = etEmailRestore.getText().toString().trim();
                                auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            /*Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Se le ha enviado un email a "+emailAddress+" para establecer nueva contraseña",
                                                    Snackbar.LENGTH_LONG)
                                                    .show();*/
                                            getSnackbar("Se ha enviado un email a "+emailAddress+" para establecer nueva contraseña").show();
                                        }else{
                                            /*Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Se ha producido un error, por favor intentalo de nuevo",
                                                    Snackbar.LENGTH_LONG)
                                                    .show();*/
                                            getSnackbar("Se ha producido un error, por favor intentalo de nuevo").show();
                                        }
                                    }
                                });
                            }else{
                                getSnackbar("Email introducido no existe").show();
                            }
                        }
                    });
                }else{
                    etEmailRestore.setError("Campos obligatorio!");
                    return;
                }



            }
        })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    public void actualizarDatosUsuario(){

        databaseReference.child("lista_usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                        if(firebaseAuth.getCurrentUser().getUid().equals(uniqueKeySnapshot.getKey())){
                            UserInfo userInfo = uniqueKeySnapshot.getValue(UserInfo.class);
                            session.setNameUser(userInfo.getNombre());
                            session.setEmailUser(firebaseAuth.getCurrentUser().getEmail());
                            session.setCadenaFotoDePerfil(userInfo.getUrlPhoto());
                            session.setNumPelisFin(userInfo.getNumPelisFin());
                            session.setNumSeriesFin(userInfo.getNumSeriesFin());
                            session.setNumLibrosFin(userInfo.getNumLibrosFin());
                        }
                    }
                }else{

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Snackbar getSnackbar(String text){
        CoordinatorLayout coordinatorLayout=(CoordinatorLayout)findViewById(R.id.myCoordinatorLayout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(ContextCompat.getColor(Login.this, R.color.colorAccent));
        CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        return snackbar;
    }

    @Override
    public void onClick(View view) {
        if ((view == btnLogin)){
            userLogin();
        }

        if(view == btnRegisterHeare){
            startActivity(new Intent(this, Register.class));
        }

        if (view == txtRestorePassword){
            restablecerContraseña();
        }
    }
}
