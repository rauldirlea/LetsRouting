package com.letsrouting.com.letsrouting.MenuFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.letsrouting.com.letsrouting.Rutas.AdapterRutas;
import com.letsrouting.com.letsrouting.Rutas.AddRuta;
import com.letsrouting.com.letsrouting.R;
import com.letsrouting.com.letsrouting.Rutas.Ruta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RutasFragment extends Fragment {

    private View view;
    private RecyclerView recyclerViewRutas;
    private List<Ruta> listRutas;
    private RecyclerView.Adapter adapterRutas;
    private FloatingActionButton floatingActionButton;
    private DatabaseReference databaseReferenceRutas;
    private FirebaseDatabase firebaseDatabase;


    public RutasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rutas, container, false);
        getActivity().setTitle("Rutas");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceRutas = firebaseDatabase.getInstance().getReference();
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabAddRuta);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddRuta.class));
            }
        });

        return view;
    }


    public void actualizarRutas(){

        recyclerViewRutas = (RecyclerView) view.findViewById(R.id.recycleViewRutasDisponibles);
        recyclerViewRutas.setHasFixedSize(false);
        //recyclerViewRutas.setLayoutManager(new LinearLayoutManager(view.getContext().getApplicationContext()));
        recyclerViewRutas.setLayoutManager(new GridLayoutManager(view.getContext().getApplicationContext(),3));

        listRutas = new ArrayList<>();

        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReferenceRutas = FirebaseDatabase.getInstance().getReference();
        databaseReferenceRutas.child("lista_rutas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                        Ruta ruta = uniqueKeySnapshot.getValue(Ruta.class);
                        listRutas.add(ruta);
                        adapterRutas = new AdapterRutas(listRutas, view.getContext().getApplicationContext());
                        Collections.reverse(listRutas);
                        recyclerViewRutas.setAdapter(adapterRutas);

                    }
                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        actualizarRutas();
    }

}
