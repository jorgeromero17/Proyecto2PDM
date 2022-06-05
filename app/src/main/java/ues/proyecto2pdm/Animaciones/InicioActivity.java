package ues.proyecto2pdm.Animaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import ues.proyecto2pdm.Graficos.GraficosActivity;
import ues.proyecto2pdm.LoginActivity;
import ues.proyecto2pdm.MiCuentaActivity;
import ues.proyecto2pdm.R;

public class InicioActivity extends AppCompatActivity {
    private TextView bienvenido;
    private TextView pomodoro;
    private Button iniciar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        bienvenido = (TextView)findViewById(R.id.textBienvenidos);
        pomodoro = (TextView)findViewById(R.id.textPomodoro);
        iniciar = (Button)findViewById(R.id.buttonInicio);
        bienvenido = (TextView)findViewById(R.id.textBienvenidos);
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        bienvenido.startAnimation(animFadeIn);
        Animation animFadeInPomo = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_pomodoro);
        pomodoro.startAnimation(animFadeInPomo);
        Animation animFadeInInicio = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_button);
        iniciar.startAnimation(animFadeInInicio);


        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InicioActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }
}