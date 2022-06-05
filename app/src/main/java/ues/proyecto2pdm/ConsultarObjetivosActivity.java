package ues.proyecto2pdm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class ConsultarObjetivosActivity extends AppCompatActivity {

    FloatingActionButton add_button;
    DataTable dataTable;
    DataTableHeader header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_objetivos);

        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultarObjetivosActivity.this, CrearObjetivoActivity.class);
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
        // define 200 fake rows for table
        for(int i=0;i<20;i++) {
            Random r = new Random();
            int random = r.nextInt(i+1);
            int randomDiscount = r.nextInt(20);
            DataTableRow row = new DataTableRow.Builder()
                    .value("Product #" + i)
                    .value(String.valueOf(random))
                    .value(String.valueOf(random*1000).concat("$"))
            .build();
            rows.add(row);
        }

        //dataTable.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        dataTable.setHeader(header);
        dataTable.setRows(rows);
        dataTable.inflate(this);
    }
}