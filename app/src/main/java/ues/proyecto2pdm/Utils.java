package ues.proyecto2pdm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Utils {
    private  static  Utils instance;
    private  static ArrayList<String> quotes=new ArrayList<>();

    private Utils() {
        quotes.add("Un sueño no se hace realidad por arte de magia, necesita sudor, determinación y trabajo duro");
        quotes.add("La vida es una aventura, atrévete");
        quotes.add("Haz de cada día tu obra maestra");
        quotes.add("Con autodisciplina casi todo es posible");
        quotes.add("El 80% del éxito se basa simplemente en insistir");
        quotes.add("El secreto para salir adelante es comenzar");
        quotes.add("Sé valiente. Toma riesgos. Nada puede sustituir la experiencia ");
    }

    public static Utils getInstanceU() {
        if(instance==null)
            instance=new Utils();

        return instance;
    }

    public String getData()
    {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy");
        String date=simpleDateFormat.format(calendar.getTime());
        return date ;
    }

    public static ArrayList<String> getQuotes() {
        return quotes;
    }
}
