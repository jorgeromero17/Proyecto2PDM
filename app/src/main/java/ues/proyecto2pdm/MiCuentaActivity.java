package ues.proyecto2pdm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import ues.proyecto2pdm.Calendario.MainActivity;
import ues.proyecto2pdm.Graficos.GraficosActivity;


public class MiCuentaActivity extends AppCompatActivity {

    TextView nombre, correo;
    ImageView foto;

    Button irObjetivos;
    int extraIdUsuario;
    String extraNombre, extraCorreo;

    Button buttonirobjetivos;
    Button irCalendario;
    Button barChart;
    int idPomodoro;

    private FirebaseAuth mAuth;
    //Variables opcionales para desloguear de google tambien private
    GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        extraIdUsuario = getIntent().getExtras().getInt("idUsuario");
        extraNombre = getIntent().getExtras().getString("nombre");
        extraCorreo = getIntent().getExtras().getString("correo");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            foto = (ImageView) findViewById(R.id.imagenUsuario);
            nombre = (TextView) findViewById(R.id.nombreUsuario);
            correo = (TextView) findViewById(R.id.correoUsuario);

            nombre.setText(extraNombre);
            nombre.setText(extraCorreo);
            Picasso.get().load(currentUser.getPhotoUrl()).into(foto);

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("912967255293-ug9ul30r06o7phe4ekhqugpvcri76380.apps.googleusercontent.com")
                    .requestEmail().build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
          
            buttonirobjetivos = (Button) findViewById(R.id.irObjetivos);
            irObjetivos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MiCuentaActivity.this, ConsultarObjetivosActivity.class);
                    intent.putExtra("idUsuario",extraIdUsuario);
                    startActivity(intent);
                }
            });


            irCalendario = (Button) findViewById(R.id.irObjetivos3);
            irCalendario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idPomodoro = getIntent().getExtras().getInt("idUsuario");
                    Intent intent = new Intent(MiCuentaActivity.this, MainActivity.class);
                    intent.putExtra("idUsuario",idPomodoro);
                    startActivity(intent);
                }
            });

            barChart = (Button) findViewById(R.id.irBarChart);
            barChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idPomodoro = getIntent().getExtras().getInt("idUsuario");
                    Intent intent = new Intent(MiCuentaActivity.this, GraficosActivity.class);
                    intent.putExtra("idUsuario",idPomodoro);
                    startActivity(intent);
                }
            });

        }

    }


}