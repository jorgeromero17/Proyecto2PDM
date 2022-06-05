package ues.proyecto2pdm.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import java.time.Clock;

import ues.proyecto2pdm.ClockActivity;
import ues.proyecto2pdm.CompartirActivity;
import ues.proyecto2pdm.Utils;
import ues.proyecto2pdm.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    Intent intent;
    public static final String number="Value";
    public static final String LastIndex="Index";
    public static  final String myPref="pref";
    public static final String Day="day";
    public static final String mintAchive="mints";
    SharedPreferences Preferences;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final Button buttoncompartir = binding.buttoncompartir;
        buttoncompartir.setOnClickListener(view -> irACompartir());
        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //Boton para un minuto
        final Button btnOneMinute = binding.btnOneMinute;
        btnOneMinute.setOnClickListener(view -> unMinuto());


        //Boton para veinticinco minutos
        final Button btnVeinticinco = binding.btnVeinticinco;
        btnVeinticinco.setOnClickListener(view -> veintiCincoMinutos());


        TextView today = binding.today;
        today.setText(Utils.getInstanceU().getData());

        //CODIGO PRUEBA
        intent = new Intent(getContext(), ClockActivity.class);
        Preferences = getContext().getSharedPreferences(myPref, Context.MODE_PRIVATE);
        TextView slider = binding.textslide;
        TextView mints = binding.mints;

        int i=Preferences.getInt(LastIndex,-1);
        if(i==-1)
        {
            // first time when you open app
            SharedPreferences.Editor editor;
            editor = Preferences.edit();
            editor.putInt(LastIndex,0);
            editor.putString(Day,Utils.getInstanceU().getData());
            editor.putInt(mintAchive,0);
            today.setText(Utils.getInstanceU().getData());
            editor.commit();
        }
        else{

            if(i==Utils.getInstanceU().getQuotes().size())
                i=0;

            slider.setText(Utils.getInstanceU().getQuotes().get(i));
            i++;
            SharedPreferences.Editor editor=Preferences.edit();
            editor.putInt(LastIndex,i);
            editor.commit();
        }

        int achivements=Preferences.getInt(mintAchive,-1);

        if(achivements!=-1)
        {
            if(Preferences.getString(Day,"").equals(Utils.getInstanceU().getData()))
            {
                // same day
                mints.setText(achivements+" M");
            }
            else {
                // second day
                SharedPreferences.Editor editor;
                editor = Preferences.edit();
                editor.putString(Day,Utils.getInstanceU().getData());
                editor.putInt(mintAchive,0);
                editor.commit();
            }
        }

        return root;
    }

    // on click button
    public void veintiCincoMinutos()
    {
        Intent intent = new Intent(getContext(), ClockActivity.class);
        intent.putExtra(number,25);
        startActivity(intent);

    }
    private void unMinuto(){
        Intent intent = new Intent(getContext(), ClockActivity.class);
        intent.putExtra(number, 1);
        startActivity(intent);
    }
    private void irACompartir() {
        Intent intent = new Intent(getContext(), CompartirActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}