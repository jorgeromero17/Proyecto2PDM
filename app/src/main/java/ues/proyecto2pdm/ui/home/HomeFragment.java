package ues.proyecto2pdm.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import ues.proyecto2pdm.Calendario.MainActivity;
import ues.proyecto2pdm.ConsultarObjetivosActivity;
import ues.proyecto2pdm.Graficos.GraficosActivity;
import ues.proyecto2pdm.LoginActivity;
import ues.proyecto2pdm.MiCuentaActivity;
import ues.proyecto2pdm.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    //Variables opcionales para desloguear de google tambien private
    GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    TextView nombre, correo;
    ImageView foto;
    Button irObjetivos, irCalendario;
    Button barChart;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        irObjetivos = binding.irObjetivos;
        irCalendario = binding.irObjetivos3;
        barChart = binding.irBarChart;

        irCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        irObjetivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConsultarObjetivosActivity.class);
                startActivity(intent);
            }
        });

        barChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GraficosActivity.class);
                startActivity(intent);
            }
        });

        if(currentUser!=null){
            foto = binding.imagenUsuario;
            nombre = binding.nombreUsuario;
            correo = binding.correoUsuario;

            nombre.setText(currentUser.getDisplayName());
            correo.setText(currentUser.getEmail());
            Picasso.get().load(currentUser.getPhotoUrl()).into(foto);

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("912967255293-ug9ul30r06o7phe4ekhqugpvcri76380.apps.googleusercontent.com").requestEmail().build();
            mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

            Button logout = binding.logout;
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override public void onComplete(@NonNull Task<Void> task) {
                            //Abrir MainActivity con SigIn button
                            if(task.isSuccessful()){
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }else{
                                Toast.makeText(getContext(), "No se pudo cerrar sesión con google", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }


        return root;
    }

    private void irActivityObjetivos() {
        Toast.makeText(getContext(),"Funciono",Toast.LENGTH_SHORT).show();
    }

    private void irActivityCalendario() {
        Toast.makeText(getContext(),"Funciono",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}