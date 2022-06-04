package ues.proyecto2pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class CrearObjetivoActivity extends AppCompatActivity {

    ControlObjetivo helper;
    Spinner spinerEstado;
    ArrayList<String> estadosSpinner; //para el spinner
    String estado;

    EditText txtObjetivo;
    EditText txtPomodoros;
    Button btnGuardar;
    boolean campEst = false; //True si no se ha seleccionado nada

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_objetivo);

        helper = new ControlObjetivo(this);
        spinerEstado = (Spinner) findViewById(R.id.spinnerEstadoObjetivo);
        txtObjetivo = (EditText) findViewById(R.id.editTextDescripcionObjetivo);
        txtPomodoros = (EditText) findViewById(R.id.editTextPomodorosObjetivo);

        estadosSpinner = new ArrayList<>();
        estadosSpinner.add("Seleccione un estado");
        estadosSpinner.add("No iniciado");
        estadosSpinner.add("En proceso");
        estadosSpinner.add("Finalizado");
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, estadosSpinner);
        spinerEstado.setAdapter(adaptador);


        //Validacion del Spinner
        spinerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    estado = spinerEstado.getSelectedItem().toString();
                    campEst = false;
                } else {
                    campEst = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnGuardar = (Button) findViewById(R.id.btnGuardarObjetivo);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (verificarCamposLlenos(campEst)) {
                    String objetivo = txtObjetivo.getText().toString();
                    int cantPomodoros = Integer.valueOf(txtPomodoros.getText().toString());
                    String regInsertados;

                    Objetivo obj = new Objetivo();
                    obj.setObjetivo(objetivo);
                    obj.setEstado(estado);
                    obj.setCantPomodoros(cantPomodoros);

                    helper.abrir();
                    regInsertados = helper.insertar(obj);
                    helper.cerrar();

                    Toast.makeText(CrearObjetivoActivity.this, regInsertados, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CrearObjetivoActivity.this, ConsultarObjetivosActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(CrearObjetivoActivity.this, "Debe llenar los campos", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    public boolean verificarCamposLlenos(boolean cmpEst) {
        if (txtObjetivo.getText().toString().isEmpty() || txtObjetivo.getText().toString() == null){
            return false;
        }else if (txtPomodoros.getText().toString().isEmpty() || txtPomodoros.getText().toString() == null) {
            return false;
        }else if (cmpEst) {
            return false;

        }else {
            return true;
        }
    }
}