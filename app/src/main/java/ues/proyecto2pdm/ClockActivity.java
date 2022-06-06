package ues.proyecto2pdm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

import ues.proyecto2pdm.databinding.ActivityClockBinding;
import ues.proyecto2pdm.databinding.ActivityMainBinding;
import ues.proyecto2pdm.ui.dashboard.DashboardFragment;

public class ClockActivity extends AppCompatActivity {

    int seconds = 0;
    int value = 0;
    String fecha;
    int idUser=0;
    int i=0;

    public static final String number = "Value";
    public static final String myPref = "pref";
    public static final String mintAchive = "mints";

    //MUSICA
    MediaPlayer mp, notificacion;

    //PARA ALARMA
    private ActivityClockBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Button play;

    //PARA INSERTAR REGISTRO
    DataBaseHelper db;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClockBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_clock);
        setContentView(binding.getRoot());
        createNotificationChannel();
        binding.selectTimeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
        binding.setAlarmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                setAlarm();
            }
        });
        binding.cancelAlarmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });
        binding.buttoncompartir.setOnClickListener(view->irACompartir());

        TextView timer, descanso;
        CountDownTimer countDownTimer;
        timer = findViewById(R.id.timer);
        ProgressBar progreso = findViewById(R.id.progress_circular_clock);
        progreso.setProgress(0);
        Button start, end, home, again, compartir;
        start = findViewById(R.id.start);
        end = findViewById(R.id.End);
        home = findViewById(R.id.home);
        again = findViewById(R.id.agine);
        descanso =findViewById(R.id.txtDescanso);
        compartir = findViewById(R.id.buttoncompartir);
        Intent intent = getIntent();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //MEDIAPLAYER PARA MUSICA
        mp = MediaPlayer.create(ClockActivity.this, R.raw.naturaleza);
        notificacion = MediaPlayer.create(ClockActivity.this, R.raw.windows_notificacion);
        //ELEMENTOS DE LA ALARMA
        Button selectAlarma, aceptAlarma, cancelAlarma;
        TextView selectTime, selectTime2;
       //BOTONES
        selectAlarma = findViewById(R.id.selectTimeBtn);
        aceptAlarma = findViewById(R.id.setAlarmBtn);
        cancelAlarma = findViewById(R.id.cancelAlarmBtn);
        //TEXTVIEWS
        selectTime = findViewById(R.id.selectedTime);
        selectTime2 = findViewById(R.id.selectedTime2);
        //BASE DE DATOS
        //PARA INSERTAR UN NUEVO REGISTRO POMODORO
        db = new DataBaseHelper(this);
        value = intent.getIntExtra(number, -1);
        if (value != -1) {
            timer.setText(value + " : 00");
            seconds = value * 60 * 1000;
        }

        countDownTimer = new CountDownTimer(seconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 % 60 <= 9 && millisUntilFinished / 1000 / 60 <= 9) // seconds <10 && mints<10
                    timer.setText("0" + String.valueOf(millisUntilFinished / 1000 / 60) + " : 0" + String.valueOf(millisUntilFinished / 1000 % 60));

                else if (millisUntilFinished / 1000 / 60 <= 9)// if mints<10
                {
                    if (millisUntilFinished / 1000 % 60 <= 9)
                        timer.setText("0" + String.valueOf(millisUntilFinished / 1000 / 60) + " : 0" + String.valueOf(millisUntilFinished / 1000 % 60));
                    else
                        timer.setText("0" + String.valueOf(millisUntilFinished / 1000 / 60) + " : " + String.valueOf(millisUntilFinished / 1000 % 60));
                } else if (millisUntilFinished / 1000 / 60 >= 10) {
                    if (millisUntilFinished / 1000 % 60 <= 9)
                        timer.setText(String.valueOf(millisUntilFinished / 1000 / 60) + " : 0" + String.valueOf(millisUntilFinished / 1000 % 60));
                    else
                        timer.setText(String.valueOf(millisUntilFinished / 1000 / 60) + " : " + String.valueOf(millisUntilFinished / 1000 % 60));
                }
                i++;
                progreso.setProgress((int)i*100/(seconds/1000));
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                timer.setText("Finalizado");
                progreso.setProgress(100);
                SharedPreferences preferences = getSharedPreferences(myPref, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                SharedPreferences preferencesLogin = getSharedPreferences("sesion",MODE_PRIVATE);

                idUser = preferencesLogin.getInt("idUsuario", 0);
                fecha = preferences.getString("day", "");
                Pomodoro pom = new Pomodoro();
                pom.setFecha(fecha);
                pom.setIdPomodoro(1);
                pom.setIdUsuario(idUser);

                db.insertar(pom);

                int lastValue = preferences.getInt(mintAchive, -1);
                if (lastValue != -1) ;
                {
                    lastValue += value;
                }
                editor.putInt(mintAchive, lastValue);
                editor.commit();
                end.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);
                again.setVisibility(View.VISIBLE);
                descanso.setVisibility(View.VISIBLE);
                selectAlarma.setVisibility(View.VISIBLE);
                aceptAlarma.setVisibility(View.VISIBLE);
                cancelAlarma.setVisibility(View.VISIBLE);
                selectTime.setVisibility(View.VISIBLE);
                selectTime2.setVisibility(View.VISIBLE);
                compartir.setVisibility(View.VISIBLE);
                //DETENER MUSICA
                mp.stop();
                //LANZAR SONIDO DE NOTIFICACION
                notificacion.start();
                home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ClockActivity.this, MainActivity.class));
                        progreso.setProgress(0);
                        finish();
                    }
                });
                again.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        i=0;
                        progreso.setProgress(0);
                        timer.setText(value + ": 00");
                        home.setVisibility(View.GONE);
                        again.setVisibility(View.GONE);
                        start.setVisibility(View.VISIBLE);
                        descanso.setVisibility(View.GONE);
                        selectAlarma.setVisibility(View.GONE);
                        aceptAlarma.setVisibility(View.GONE);
                        cancelAlarma.setVisibility(View.GONE);
                        selectTime.setVisibility(View.GONE);
                        selectTime2.setVisibility(View.GONE);
                        compartir.setVisibility(View.GONE);
                    }
                });
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ClockActivity.this, "channel")
                        .setContentTitle("Pomodoro")
                        .setSmallIcon(R.mipmap.screenicon)
                        .setContentText("Tu sesión ha finalizado")
                        .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH);

                NotificationManagerCompat manager = NotificationManagerCompat.from(ClockActivity.this);
                manager.notify(1, builder.build());
                i=0;
            }
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.start();
                start.setVisibility(View.GONE);
                end.setVisibility(View.VISIBLE);
                //MUSICA
                mp = MediaPlayer.create(ClockActivity.this, R.raw.naturaleza);
                mp.start();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ClockActivity.this)
                        .setMessage("Finalizar sesión")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                countDownTimer.cancel();
                                i=0;
                                progreso.setProgress(0);
                                timer.setText(value + " : 00");
                                end.setVisibility(View.GONE);
                                start.setVisibility(View.VISIBLE);
                                selectAlarma.setVisibility(View.GONE);
                                aceptAlarma.setVisibility(View.GONE);
                                cancelAlarma.setVisibility(View.GONE);
                                selectTime.setVisibility(View.GONE);
                                selectTime2.setVisibility(View.GONE);
                                compartir.setVisibility(View.GONE);
                                mp.stop();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });
    }

    private void cancelAlarm() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarma Cancelada", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarma establecida satisfactoriamente", Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Seleccionar hora de alarma")
                .build();

        picker.show(getSupportFragmentManager(), "foxandroid");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(picker.getHour() > 12){
                    //binding.selectedTime.setText(String.format("%02d", String.format(String.format((picker.getHour() - 12) + " : " + String.format("%02d", picker.getMinute())+" PM"))));
                    binding.selectedTime.setText(String.format("%02d",(picker.getHour()-12))+" : "+String.format("%02d",picker.getMinute())+" PM");
                }else{
                   // binding.selectedTime.setText(picker.getHour()+" : " + picker.getMinute() + "  AM");
                    binding.selectedTime.setText(picker.getHour()+" : " + picker.getMinute() + " AM");
                }
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "foxandroidReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void irACompartir() {
        Intent intent = new Intent(ClockActivity.this, CompartirActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        mp.stop();
        super.onBackPressed();
        startActivity(new Intent(ClockActivity.this, MainActivity.class));
        finish();
    }
}