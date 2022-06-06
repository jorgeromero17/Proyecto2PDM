package ues.proyecto2pdm.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ues.proyecto2pdm.Publicacion;
import ues.proyecto2pdm.PublicacionAdapter;
import ues.proyecto2pdm.PublicacionesActivity;
import ues.proyecto2pdm.R;
import ues.proyecto2pdm.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private RecyclerView recyclerView;
    private PublicacionAdapter publicacionAdapter;
    private ProgressBar progressCircular;
    //variables base de datos
    private DatabaseReference miref;
    private List<Publicacion> publicaciones;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressCircular = binding.progressCircular;
        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        publicaciones = new ArrayList<>();

        miref = FirebaseDatabase.getInstance().getReference("Publicaciones");
        miref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){{
                    Publicacion publicacion = postSnapshot.getValue(Publicacion.class);
                    publicaciones.add(publicacion);
                }}

                publicacionAdapter = new PublicacionAdapter(getContext(),publicaciones);
                recyclerView.setAdapter(publicacionAdapter);
                progressCircular.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"",Toast.LENGTH_LONG).show();
            }
        });

        /*final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}