package ues.proyecto2pdm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String NOMBRE_BASEDATOS = "pomodoro.s3db";
    private  static final int VERSION_BASEDATOS = 1;

    public DataBaseHelper(@Nullable Context context) {
        super(context, NOMBRE_BASEDATOS, null,VERSION_BASEDATOS);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try{

            db.execSQL("CREATE TABLE usuario (idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, correo VARCHAR(50) NOT NULL,nombre VARCHAR(100) NOT NULL);");
            db.execSQL("CREATE TABLE calendario\n" +
                    "(idCalendario INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  fechaCalendario VARCHAR (100) NOT NULL,\n" +
                    "  nota VARCHAR(100),\n" +
                    "  idUsuario INTEGER,\n" +
                    "  CONSTRAINT fk_usuario\n" +
                    "    FOREIGN KEY (idUsuario)\n" +
                    "    REFERENCES usuario(idUsuario)\n" +
                    ");");
            db.execSQL("CREATE TABLE pomodoro (idPomodoro INTEGER PRIMARY KEY AUTOINCREMENT, idUsuario INTEGER, fecha VARCHAR(10));");
            db.execSQL("CREATE TABLE objetivo (idObjetivo INTEGER PRIMARY KEY AUTOINCREMENT, idUsuario INTEGER NOT NULL, objetivo VARCHAR(255) NOT NULL,estado VARCHAR(15) NOT NULL, cantPomodoro INTEGER);");
            db.execSQL("INSERT INTO pomodoro VALUES(1,1,'3/06/2022')");
            db.execSQL("INSERT INTO pomodoro VALUES(2,1,'3/06/2022')");
            db.execSQL("INSERT INTO pomodoro VALUES(3,1,'4/06/2022')");
            db.execSQL("INSERT INTO pomodoro VALUES(4,2,'5/06/2022')");

        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public Cursor getDatosBar(String[] id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c= db.rawQuery("Select idPomodoro, count(fecha) as cantPomodoro from pomodoro WHERE idUsuario = ? group by fecha;",id);
        return c;
    }

    public Cursor getFecha(String[] idPomodoro){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("Select fecha FROM pomodoro where idPomodoro = ?",idPomodoro);
        return c;
    }

    public void insertar(Pomodoro pomodoro){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues pom = new ContentValues();
        pom.put("idUsuario", pomodoro.getIdUsuario());
        pom.put("fecha", pomodoro.getFecha());
        db.insert("pomodoro", null, pom);
    }
}
