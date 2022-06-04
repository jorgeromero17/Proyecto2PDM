package ues.proyecto2pdm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ControlMiCuenta {
    private static final String[] camposUsuario = new String[] {"idUsuario", "correo", "nombre"};
    private final Context context;
    private DataBaseHelper DBHelper;
    private SQLiteDatabase db;

    public ControlMiCuenta(Context context){
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
    public String insertar(Usuario usuario){
        String regInsertados = "Registro Insertado No = ";
        long contador = 0;

        ContentValues car = new ContentValues();
        car.put("correo", usuario.getCorreo());
        car.put("nombre", usuario.getNombre());
        contador = db.insert("usuario", null, car);
        if(contador == -1 || contador == 0){
            regInsertados = "Error al insertar el registro. Registro duplicado. Verificar insercion";
        } else{
            regInsertados = regInsertados + contador;
        }
        return regInsertados;
    }

    //UPDATES
    public String actualizar(Usuario usuario, String[] id){
        try{
            ContentValues cv = new ContentValues();
            cv.put("correo", usuario.getCorreo());
            cv.put("nombre", usuario.getNombre());
            db.update("usuario",  cv, "idUsuario = ?", id);
            return "Usuario actualizado Correctamente ";
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //DELETES
    public String eliminar(Usuario usuario){
        String regAfectados="filas afectadas = ";
        int contador = 0;

        String[] id = {String.valueOf(usuario.getIdUsuario())};
        try{
            contador += db.delete("usuario", "idUsuario = ?", id);
            regAfectados += contador;

            return regAfectados;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //SELECTS
    public ArrayList<Usuario> consultarUsuario(){
        try{
            ArrayList<Usuario> lisUsuario = new ArrayList<>();
            Cursor cursor = db.query("usuario", camposUsuario, null, null, null, null, null);

            if(cursor.moveToFirst()){
                do{
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(cursor.getInt(0));
                    usuario.setCorreo(cursor.getString(1));
                    usuario.setNombre(cursor.getString(2));
                    lisUsuario.add(usuario);
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
    public Usuario consultarUsuario(String[] id){
        try{
            Cursor cursor = db.query("usuario", camposUsuario, "idUsuario = ? ", id, null, null, null);
            if (cursor.moveToFirst()) {
                Usuario usr = new Usuario();
                usr.setIdUsuario(cursor.getInt(0));
                usr.setNombre(cursor.getString(1));
                usr.setCorreo(cursor.getString(2));
                return usr;
            } else {
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
