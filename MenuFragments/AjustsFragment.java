package com.letsrouting.com.letsrouting.MenuFragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.letsrouting.com.letsrouting.LogReg.Login;
import com.letsrouting.com.letsrouting.R;
import com.letsrouting.com.letsrouting.Session;
import com.letsrouting.com.letsrouting.UserInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AjustsFragment extends Fragment {

    private Button btnLogOut, btnChangeProfilePhoto;
    private CircleImageView circle_profile_edit;
    private TextView txtNameUser;
    private View view;
    private Session session;
    private static final int PHOTO_PERFIL = 2;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String fotoPerfilCadena = "";
    private TextView txNumeroPeliculasVisitadas,txNumeroSeriesVisitadas, txNumeroLibrosVisitadas;

    public AjustsFragment() {
        // Required empty public constructor
    }

    //Toast.makeText(getContext(), "prueba", Toast.LENGTH_LONG).show();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ajustes, container, false);
        session = new Session(getContext());

        getActivity().setTitle("Ajustes Perfil");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("lista_usuarios");//Nuestra sala de chat (nombre)
        //**** Intent to get data from chat list
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("lista_usuarios");//Nuestra sala de chat (nombre)
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        txNumeroPeliculasVisitadas = (TextView) view.findViewById(R.id.txNumeroPeliculasVisitadas);
        txNumeroSeriesVisitadas = (TextView) view.findViewById(R.id.txNumeroSeriesVisitadas);
        txNumeroLibrosVisitadas = (TextView) view.findViewById(R.id.txNumeroLibrosVisitadas);

        circle_profile_edit = (CircleImageView) view.findViewById(R.id.circle_profile_edit);
        txtNameUser = (TextView) view.findViewById(R.id.txtNameUser);
        txtNameUser.setText(session.getNameUser());
        txNumeroPeliculasVisitadas.setText(String.valueOf(session.getNumPelisFin()));
        txNumeroSeriesVisitadas.setText(String.valueOf(session.getNumSeriesFin()));
        txNumeroLibrosVisitadas.setText(String.valueOf(session.getNumLibrosFin()));

        btnLogOut = (Button) view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                session.setEmailUser("");
                session.setNameUser("");
                session.setCadenaFotoDePerfil("");
                session.setNumPelisFin(0);
                session.setNumSeriesFin(0);
                session.setNumLibrosFin(0);
                startActivity(new Intent(getContext(), Login.class));
                getActivity().finishAffinity();
            }
        });


        btnChangeProfilePhoto = (Button) view.findViewById(R.id.btnChangeProfilePhoto);
        btnChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            try{
                if (firebaseAuth.getCurrentUser().getPhotoUrl() == null) {

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/jpeg");
                    i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_PERFIL);

                } else {

                    // DIALOG TO
                    Inflater inflater = new Inflater();
                    // custom dialog
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                    mBuilder.setTitle("Imagen de perfil");
                    mBuilder.setMessage("Que desea hacer?");
                    mBuilder.setPositiveButton("CAMBIAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            // Add new image!
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.setType("image/jpeg");
                            i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                            startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), PHOTO_PERFIL);

                        }
                    })
                            .setNegativeButton("ELIMINAR", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    //DELETE PREVIOS PHOTO
                                    deletePreviousImage();
                                    circle_profile_edit.setImageResource(R.drawable.user_default);

                                }
                            });

                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
            }catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }


            }
        });

        /*Set profile image*/
        try {

            if (firebaseAuth.getCurrentUser().getPhotoUrl().toString().isEmpty()) {
                circle_profile_edit.setImageResource(R.drawable.user_default);
            } else {
                Glide.with(view.getContext()).load(firebaseAuth.getCurrentUser().getPhotoUrl().toString()).asBitmap().into(circle_profile_edit);
                fotoPerfilCadena = firebaseAuth.getCurrentUser().getPhotoUrl().toString();
            }
        } catch (Exception e) {

        }


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode nos devuelve el nr 1 del startActivityForResult si hemos elegido una foto corectamente
        //resultCode nos dice si el resultado es corecto
        //Si la foto la hemos elegido corectamente y el resultado es corecto
        if(requestCode == PHOTO_PERFIL && resultCode == RESULT_OK){

            Uri u = data.getData();
            storageReference = firebaseStorage.getReference("foto_perfil_" + firebaseAuth.getCurrentUser().getEmail());//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());
            fotoReferencia.putFile(u).addOnSuccessListener((Activity) view.getContext(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //IF IMAGE HAS BEN SUCCESFUL SELECTED PUSH TO DATABASE
                    Uri u = taskSnapshot.getDownloadUrl();
                    fotoPerfilCadena = u.toString();
                    session.setCadenaFotoDePerfil(u.toString());

                    Glide.with(getContext()).load(u.toString()).into(circle_profile_edit);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse(u.toString())) //
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "User profile updated.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                    if(isConnected(getActivity())){
                        //UPDATE IMAGE NAME IN FIREBASE DATABASE
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("/");

                        DatabaseReference usersRef = ref.child("lista_usuarios");

                        Map<String, Object> UserInfo = new HashMap<>();
                        String userID = firebaseAuth.getCurrentUser().getUid();

                        UserInfo.put(userID+"/urlPhoto", taskSnapshot.getMetadata().getName());

                        usersRef.updateChildren(UserInfo);

                        session.setCadenaFotoDePerfil(taskSnapshot.getMetadata().getName());

                    }else {
                        Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_LONG).show();
                    }
                }
            });

            //DELETE PREVIOS PHOTO
            // Create a storage reference from our app
            StorageReference storageRef = firebaseStorage.getReference();

            // Create a reference to the file to delete
            StorageReference desertRef = storageRef.child("foto_perfil_" + firebaseAuth.getCurrentUser().getEmail()+"/"+session.getCadenaFotoDePerfil());

            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    Toast.makeText(getContext(), "Delete Succesfull!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Toast.makeText(getContext(), "Delete Error!", Toast.LENGTH_LONG).show();
                }
            });
            //FI DELETE PREVIOS PHOTO
        }
    }

    private void deletePreviousImage(){

        //DELETE PREVIOS PHOTO
        // Create a storage reference from your app
        StorageReference storageRef = firebaseStorage.getReference();

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("foto_perfil_" + firebaseAuth.getCurrentUser().getEmail()+"/"+session.getCadenaFotoDePerfil());

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Toast.makeText(getContext(), "Delete Succesfull!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Toast.makeText(getContext(), "Delete Error!", Toast.LENGTH_LONG).show();
            }
        });
        //FI DELETE PREVIOS PHOTO

        //Delete image name from user information
        if(isConnected(getContext())){
            //UPDATE INFO TO FIREBASE DATABASE OF THE CURRENT USER
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("/");

            DatabaseReference usersRef = ref.child("lista_usuarios");

            Map<String, Object> UserInformation = new HashMap<>();
            String userID = firebaseAuth.getCurrentUser().getUid();

            UserInformation.put(userID+"/urlPhoto", "");

            usersRef.updateChildren(UserInformation);

                                            /*Sessions update*/
            session.setCadenaFotoDePerfil("");

        }else {
            Toast.makeText(getContext(), "No internet connection.", Toast.LENGTH_LONG).show();
        }

        // User cancelled the dialog
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(null) //
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Image profile has ben deleted.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /*GET STATE NETWORK*/
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
