package ues.proyecto2pdm.Calendario;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalTime;

import ues.proyecto2pdm.Graficos.GraficosActivity;
import ues.proyecto2pdm.MiCuentaActivity;
import ues.proyecto2pdm.R;

public class EventEditActivity extends AppCompatActivity
{

    ControlCalendario helper;
    int id;
    int idUsuario;
    Event e =new Event();

    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;

    private LocalTime time;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        idUsuario = getIntent().getExtras().getInt("idUsuario");
        super.onCreate(savedInstanceState);
        helper = new ControlCalendario(this);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        time = LocalTime.now();
        eventDateTV.setText(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
    }

    private void initWidgets()
    {
        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
    }

    public void saveEventAction(View view)
    {
        String eventName = eventNameET.getText().toString();

        String fecha = eventDateTV.getText().toString();
        String nota = String.valueOf(CalendarUtils.selectedDate);
        String regInsertados;

        e.setName(eventName);
        e.setDate(fecha);
        e.setUsuario(idUsuario);
        helper.abrir();
        regInsertados=helper.insertar(e);
        helper.cerrar();

        id = helper.obtenerUltimo();

        e = helper.verUltimo(id);
        Toast.makeText(this, regInsertados, Toast.LENGTH_SHORT).show();
        Event newEvent = new Event();
        newEvent.setName(e.getName());
        newEvent.setDate(e.getDate());
        Event.eventsList.add(newEvent);
        PrefConfig.writeListInPref(getApplicationContext(), Event.eventsList);
        finish();
    }
}