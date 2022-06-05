package ues.proyecto2pdm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PublicacionesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PublicacionAdapter publicacionAdapter;
    private ProgressBar progressCircular;
    //variables base de datos
    private DatabaseReference miref;
    private List<Publicacion> publicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicaciones);

        progressCircular = findViewById(R.id.progressCircular);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        publicaciones = new ArrayList<>();

        miref = FirebaseDatabase.getInstance().getReference("Publicaciones");
        miref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){{
                    Publicacion publicacion = postSnapshot.getValue(Publicacion.class);
                    publicaciones.add(publicacion);
                }}

                publicacionAdapter = new PublicacionAdapter(PublicacionesActivity.this,publicaciones);
                recyclerView.setAdapter(publicacionAdapter);
                progressCircular.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PublicacionesActivity.this,"",Toast.LENGTH_LONG).show();
            }
        });
    }
}