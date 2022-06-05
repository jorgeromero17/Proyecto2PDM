package ues.proyecto2pdm.Calendario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import ues.proyecto2pdm.DataBaseHelper;

public class ControlCalendario {
    private final Context context;
    private DataBaseHelper DBHelper;
    private SQLiteDatabase db;

    public ControlCalendario(Context context) {
        this.context = context;
        DBHelper = new DataBaseHelper(this.context);
    }

    public void abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return;
    }

    public void abrirParaLeer() throws SQLException {
        db = DBHelper.getReadableDatabase();
        return;
    }

    public void cerrar(){
        db.close();
    }

    //INSERTS
    public String insertar(Event event){
        String regInsertados = "Registro Insertado No = ";
        long contador = 0;

        ContentValues mate = new ContentValues();
        mate.put("fechaCalendario", String.valueOf(event.getDate()));
        mate.put("nota",event.getName());
        mate.put("idUsuario",event.getUsuario());
        contador = db.insert("calendario", null, mate);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al insertar el registro. Registro duplicado. Verificar insercion";
        } else{
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    public  int obtenerUltimo(){
        db = DBHelper.getWritableDatabase();
        int id = 0 ;
        Cursor cursor = null;
        cursor = db.rawQuery("SELECT idCalendario FROM calendario ORDER BY idCalendario DESC LIMIT 1; ",null);
        if (cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public Event verUltimo(int id){

        db = DBHelper.getWritableDatabase();
        Event event = null;
        Cursor cursorEvento = null;
        cursorEvento = db.rawQuery("SELECT * FROM calendario WHERE idCalendario ="+ id + " LIMIT 1",null);
        if(cursorEvento.moveToFirst()){
            event = new Event();
            //          event.setName(cursorEvento.getString(1));
            event.setDate(cursorEvento.getString(1));
            event.setName(cursorEvento.getString(2));

        }
        cursorEvento.close();
        return event;
    }




}
