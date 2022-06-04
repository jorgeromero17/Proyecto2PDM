package ues.proyecto2pdm.Calendario;

import static ues.proyecto2pdm.Calendario.CalendarUtils.daysInWeekArray;
import static ues.proyecto2pdm.Calendario.CalendarUtils.monthYearFromDate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import ues.proyecto2pdm.R;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Event> eventsForDate(String date)
    {
        LocalDate fechaElegida = LocalDate.parse(date);
        ArrayList<Event> events = new ArrayList<>();
       Event.eventsList = (ArrayList<Event>) PrefConfig.readListFromPref(this);
        if (Event.eventsList == null)
            Event.eventsList = new ArrayList<>();
        for(Event event : Event.eventsList)
        {
            LocalDate fecha = LocalDate.parse(event.getDate());
            if(fecha.equals(fechaElegida))
                events.add(event);
        }

        return events;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView()
    {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeekAction(View view)
    {
      CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
       CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEventAdpater()
    {
        String fecha = CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        ArrayList<Event> dailyEvents = eventsForDate(fecha);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }
}