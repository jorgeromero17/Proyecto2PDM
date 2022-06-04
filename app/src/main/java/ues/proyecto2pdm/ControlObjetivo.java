package ues.proyecto2pdm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ControlObjetivo {
    private static final String[] camposObjetivo = new String[] {"idObjetivo", "idUsuario", "objetivo", "estado", "cantPomodoro"};
    private final Context context;
    private DataBaseHelper DBHelper;
    private SQLiteDatabase db;

    public ControlObjetivo(Context context){
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

    //INSERT
    public String insertar(Objetivo objetivo){
        String regInsertados = "Registro Insertado No = ";
        long contador = 0;

        ContentValues cv = new ContentValues();
        cv.put("idUsuario", objetivo.getIdUsuario());
        cv.put("objetivo", objetivo.getObjetivo());
        cv.put("estado", objetivo.getEstado());
        cv.put("cantPomodoro", objetivo.getCantPomodoros());
        contador = db.insert("objetivo", null, cv);
        if(contador == -1 || contador == 0){
            regInsertados = "Error al insertar el registro. Registro duplicado. Verificar insercion";
        } else{
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    //UPDATES
    public String actualizar(Objetivo objetivo, String[] id){
        try{
            ContentValues cv = new ContentValues();
            cv.put("idUsuario", objetivo.getIdUsuario());
            cv.put("objetivo", objetivo.getObjetivo());
            cv.put("estado", objetivo.getEstado());
            cv.put("cantPomodoro", objetivo.getCantPomodoros());
            db.update("objetivo",  cv, "idObjetivo = ?", id);
            return "Usuario actualizado Correctamente ";
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //DELETES
    public String eliminar(Objetivo objetivo){
        String regAfectados="filas afectadas = ";
        int contador = 0;

        String[] id = {String.valueOf(objetivo.getIdObjetivo())};
        try{
            contador += db.delete("objetivo", "idObjetivo = ?", id);
            regAfectados += contador;

            return regAfectados;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //SELECTS
    public ArrayList<Objetivo> consultarUsuario(){
        try{
            ArrayList<Objetivo> lisUsuario = new ArrayList<>();
            Cursor cursor = db.query("objetivo", camposObjetivo, null, null, null, null, null);

            if(cursor.moveToFirst()){
                do{
                    Objetivo objetivo = new Objetivo();
                    objetivo.setIdObjetivo(cursor.getInt(0));
                    objetivo.setIdUsuario(cursor.getInt(1));
                    objetivo.setObjetivo(cursor.getString(2));
                    objetivo.setEstado(cursor.getString(3));
                    objetivo.setCantPomodoros(cursor.getInt(4));
                    lisUsuario.add(objetivo);
                }while(cursor.moveToNext());

                return lisUsuario;
            }else{
                return null;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public Objetivo consultarUsuario(String[] id){
        try{
            Cursor cursor = db.query("objetivo", camposObjetivo, "idObjetivo = ? ", id, null, null, null);
            if (cursor.moveToFirst()) {
                Objetivo obj = new Objetivo();
                obj.setIdObjetivo(cursor.getInt(0));
                obj.setIdUsuario(cursor.getInt(1));
                obj.setObjetivo(cursor.getString(2));
                obj.setEstado(cursor.getString(3));
                obj.setCantPomodoros(cursor.getInt(4));
                return obj;
            } else {
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
