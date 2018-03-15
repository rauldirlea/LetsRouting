package com.letsrouting.com.letsrouting.LogReg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.letsrouting.com.letsrouting.MenuPrincipal;
import com.letsrouting.com.letsrouting.R;
import com.letsrouting.com.letsrouting.Session;
import com.letsrouting.com.letsrouting.UserInfo;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText registerEditName, registerEditEmail, registerEditPassword;
    private Button btnRegistration;
    private TextView textLoginHeare;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        Register.this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        session = new Session(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("lista_usuarios");//Nuestra sala de usuarios registrados
        if(firebaseAuth.getCurrentUser() != null){
            //menu activity start here
            startActivity(new Intent(this, MenuPrincipal.class));
            finish();
        }


        registerEditName = (EditText) findViewById(R.id.etNameReg);
        registerEditEmail = (EditText) findViewById(R.id.etEmailReg);
        registerEditPassword = (EditText) findViewById(R.id.etPasswordReg);
        btnRegistration = (Button) findViewById(R.id.btnRegister);
        textLoginHeare =  (TextView) findViewById(R.id.textLoginHeare);
        progressDialog = new ProgressDialog(this);

        btnRegistration.setOnClickListener(this);
        textLoginHeare.setOnClickListener(this);
    }

    private void registerUser(){
        final String name = registerEditName.getText().toString().trim();
        final String email = registerEditEmail.getText().toString().trim();
        String password = registerEditPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            //name is empty
            getSnackbar("Please enter name.").show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            //email is empty
            getSnackbar("Please enter email.").show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            getSnackbar("Please enter password.").show();
            return;
        }else if(password.length() < 6){
            //password minimum 6 characters
            getSnackbar("Password minimum 6 characters.").show();
            return;
        }

        progressDialog.setMessage("Loanding please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    UserInfo userInfo = new UserInfo(name,email,"",0,0,0);
                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userInfo);
                    progressDialog.dismiss();
                    session.setNameUser(registerEditName.getText().toString().trim());
                    session.setEmailUser(registerEditEmail.getText().toString().trim());
                    registerEditName.getText().clear();
                    registerEditEmail.getText().clear();
                    registerEditPassword.getText().clear();

                    Toast.makeText(Register.this, "Registration succesfull", Toast.LENGTH_LONG).show();

                    //login activity start here
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finishAffinity();

                }else {
                    progressDialog.cancel();
                    getSnackbar("Email ya registrado!").show();
                }
            }
        });
    }


    private Snackbar getSnackbar(String text){
        CoordinatorLayout coordinatorLayout=(CoordinatorLayout)findViewById(R.id.myCoordinatorLayout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(ContextCompat.getColor(Register.this, R.color.colorAccent));
        CoordinatorLayout.LayoutParams params=(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);

        return snackbar;
    }

    /*GET STATE NETWORK*/
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View view) {
        if ((view == btnRegistration)){
            registerUser();
        }

        if(view == textLoginHeare){
            startActivity(new Intent(this, Login.class));
            finish();
        }
    }
}
