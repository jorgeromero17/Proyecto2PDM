package ues.proyecto2pdm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class ConsultarObjetivosActivity extends AppCompatActivity {

    FloatingActionButton add_button;
    DataTable dataTable;
    DataTableHeader header;
    int extraIdUsuario;
    ControlObjetivo helper;
    ArrayList<Integer> listaIdObjetivos;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_objetivos);

        preferences = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();

        helper = new ControlObjetivo(this);
        extraIdUsuario = preferences.getInt("idUsuario",0);

        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultarObjetivosActivity.this, CrearObjetivoActivity.class);
                intent.putExtra("idUsuario",extraIdUsuario);
                startActivity(intent);
            }
        });


        dataTable = findViewById(R.id.data_table);

        header = new DataTableHeader.Builder()
                .item("Descripcion", 4)
                .item("Estado", 4)
                .item("Pomodoros", 4)
    .build();

        ArrayList<DataTableRow> rows = new ArrayList<>();
        if(consultarObjetivos()){
            //Si hay objetivos
            helper.abrir();
            ArrayList<Objetivo> registros = helper.consultarUsuario(extraIdUsuario);
            helper.cerrar();

            Objetivo obj;
            Iterator<Objetivo> it = registros.iterator();
            while(it.hasNext()) {
                obj = it.next();

                DataTableRow row = new DataTableRow.Builder()
                        .value(obj.getObjetivo())
                        .value(obj.getEstado())
                        .value(String.valueOf(obj.getCantPomodoros()))
                        .build();
                rows.add(row);
            }
            
        } else{

            // Si no hay objetivos
            DataTableRow row = new DataTableRow.Builder()
                    .value("objetivo")
                    .value("Estado")
                    .value("Pomodoros")
                    .build();
            rows.add(row);
        }

        //dataTable.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        dataTable.setHeader(header);
        dataTable.setRows(rows);
        dataTable.inflate(this);
    }

    public boolean consultarObjetivos(){
        helper.abrir();
        ArrayList<Objetivo> registros = helper.consultarUsuario(extraIdUsuario);
        helper.cerrar();

        Objetivo obj;
        if (registros == null){
            return false;
        }else {
            return true;
            }

        }

}