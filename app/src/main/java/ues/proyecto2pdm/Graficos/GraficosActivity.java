package ues.proyecto2pdm.Graficos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ues.proyecto2pdm.DataBaseHelper;
import ues.proyecto2pdm.LabelFormatter;
import ues.proyecto2pdm.R;

public class GraficosActivity extends AppCompatActivity {
    DataBaseHelper datosDB;
    SQLiteDatabase sqLiteDatabase;
    int id;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        preferences = this.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        editor = preferences.edit();

        id = preferences.getInt("idUsuario",0);


        BarChart barChart = findViewById(R.id.idBarChart);
        BarDataSet barDataSet = new BarDataSet(getDatosBar(),"datos");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);

        barData.setValueFormatter(new LargeValueFormatter());
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Cantidad de pomodoros por día");
        barChart.animateY(2000);

        String[] xVals = {"jan","may"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.red(4));
        xAxis.setTextSize(12f);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

       @Override
       public void onValueSelected(Entry e, Highlight h) {
           e.getData();

           String id = getDato((int)e.getX());
           Toast toast = Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT);
           toast.show();
       }
       @Override
       public void onNothingSelected() {
           Log.d("BAR_CHART_SAMPLE", "nothing selected X is ");

       }
   });
    }
    private String getDato(int id){

        datosDB = new DataBaseHelper(this);
        SQLiteDatabase db = datosDB.getWritableDatabase();
        String dataVals="";
        String[] idPomo = {String.valueOf(id)};
        Cursor cursor = datosDB.getFecha(idPomo);
        for(int i=0; i<1;i++) {
            cursor.moveToNext();
            dataVals = String.valueOf(cursor.getString(0));
            Log.d("",dataVals.toString());
        }
        cursor.close();
        db.close();
        return dataVals;
    }
    private ArrayList<BarEntry> getDatosBar(){

        datosDB = new DataBaseHelper(this);
        SQLiteDatabase db = datosDB.getWritableDatabase();
        ArrayList<BarEntry> dataVals = new ArrayList<>();
        String [] idUsuario = {String.valueOf(id)};
        Cursor cursor = datosDB.getDatosBar(idUsuario);
        for(int i=0; i<cursor.getCount();i++) {
            cursor.moveToNext();
            dataVals.add(new BarEntry(cursor.getInt(0), cursor.getInt(1)));
        }
        cursor.close();
        db.close();
        return dataVals;
    }
}