
package vuelosfis.vista;

import java.util.ArrayList;

public class DatosReserva {
    

    public static String vueloGlobal;
    public static String rutaGlobal;
    public static String precioUnitario; 
    public static String claseGlobal;
    

    public static int totalPasajeros = 0; 
    public static int contadorActual = 1; 


    public static ArrayList<String> listaNombres = new ArrayList<>();
    public static ArrayList<String> listaCedulas = new ArrayList<>();
    public static ArrayList<String> listaCorreos = new ArrayList<>(); 
    public static ArrayList<String> listaAsientos = new ArrayList<>();
    
    public static void limpiar() {
        listaNombres.clear();
        listaCedulas.clear();
        listaAsientos.clear();
        contadorActual = 1;
        totalPasajeros = 0;
        vueloGlobal = "";
        precioUnitario = "";
    }
}