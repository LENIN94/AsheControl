package com.mx.ashe.ashecontrol.helper;

import android.os.Handler;
import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by francis on 21/06/15.
 */
public class Cronometro implements Runnable
{
    // Atributos privados de la clase
                 // Etiqueta para mostrar la información
    private String nombrecronometro;        // Nombre del cronómetro
    private int segundos, minutos, horas;   // Segundos, minutos y horas que lleva activo el cronómetro
    private Handler escribirenUI;           // Necesario para modificar la UI
    private Boolean pausado;
    private boolean tiempo30;
    // Para pausar el cronómetro
    private String salida;                  // Salida formateada de los datos del cronómetro

    /**
     * Constructor de la clase
     * @param nombre Nombre del cronómetro

     */
    public Cronometro(String nombre,boolean tiempo30 )
    {

        salida = "";
        segundos = 0;
        minutos = 0;
        horas = 0;
        nombrecronometro = nombre;
        escribirenUI = new Handler();
        pausado = Boolean.FALSE;
        this.tiempo30=tiempo30;
    }

    @Override
    /**
     * Acción del cronómetro, contar tiempo en segundo plano
     */
    public void run()
    {
        try
        {
            while(Boolean.TRUE)
            {
                Thread.sleep(1000);
                salida = "";
                if( !pausado )
                {
                    segundos++;
                    if(segundos == 60)
                    {
                        segundos = 0;
                        minutos++;
                    }
                    if(minutos == 60)
                    {
                        minutos = 0;
                        horas++;
                    }
                    // Formateo la salida
                    salida += "0";
                    salida += horas;
                    salida += ":";
                    if( minutos <= 9 )
                    {
                        salida += "0";
                    }
                    salida += minutos;
                    salida += ":";
                    if( segundos <= 9 )
                    {
                        salida += "0";
                    }
                    salida += segundos;
                    // Modifico la UI
                    try
                    {
                        escribirenUI.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Log.e("time" ,salida);
                                tiempo30=valida("00:00:30",salida);
                                boolean cc=tiempo30;

                            }
                        });
                    }
                    catch (Exception e)
                    {
                        Log.i("Cronometro", "Error en el cronometro " + nombrecronometro + " al escribir en la UI: " + e.toString());
                    }
                }
            }
        }
        catch (InterruptedException e)
        {
            Log.i("Cronometro", "Error en el cronometro " + nombrecronometro + ": " + e.toString());
        }
    }

    public boolean valida(String fecha1, String fechaActual)
    {


        boolean resultado=false;
        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            SimpleDateFormat formateador = new SimpleDateFormat("HH:mm:ss");
            Date fechaDate1 = formateador.parse(fecha1);
            Date fechaDate2 = formateador.parse(fechaActual);

            if ( fechaDate1.before(fechaDate2) ){
                resultado= true;
            }else{
              /*  if ( fechaDate2.before(fechaDate1) ){*/
                    resultado= false;
              /* }else{
                    resultado= true;
                }*/
            }
        } catch (ParseException e) {
            System.out.println("Se Produjo un Error!!!  "+e.getMessage());
        }
        return resultado;

    }
    /**
     * Reinicia el cronómetro
     */
    public void reiniciar()
    {
        segundos = 0;
        minutos = 0;
        horas = 0;
        pausado = Boolean.FALSE;
    }

    /**
     * Pausa/Continua el cronómetro
     */
    public void pause()
    {
        pausado = !pausado;
    }

}
