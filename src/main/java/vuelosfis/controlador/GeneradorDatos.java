package vuelosfis.controlador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GeneradorDatos {

    public static void generarMilVuelos() {
        // Listas de datos para elegir al azar
        String[] aerolineas = {"EQ", "AV", "LA", "CM", "AA", "IB", "KL", "DL"};
        String[] ciudades = {"UIO", "GYE", "CUE", "MEC", "GPS", "BOG", "LIM", "PTY", "MIA", "MAD", "AMS"};
        Random ran = new Random();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("vuelos.csv"))) {
            for (int i = 1; i <= 10000; i++) {
                
                String codigo = aerolineas[ran.nextInt(aerolineas.length)] + (100 + i);
                
                
                String origen = ciudades[ran.nextInt(ciudades.length)];
                
           
                String destino;
                do {
                    destino = ciudades[ran.nextInt(ciudades.length)];
                } while (origen.equals(destino));

                
                int mes = ran.nextInt(2) + 2; // Mes 2 (Feb) o 3 (Mar)
                int dia = (mes == 2) ? (ran.nextInt(28) + 1) : (ran.nextInt(31) + 1);
                String fecha = String.format("%02d/%02d/2026", dia, mes);

                
                String hora = String.format("%02d:%02d", ran.nextInt(24), ran.nextInt(6) * 10);
                double precio = 50 + (ran.nextDouble() * 950);


                bw.write(String.format("%s,%s,%s,%s,%s,%.2f\n", 
                        codigo, origen, destino, fecha, hora, precio));
            }
            System.out.println(" Archivo 'vuelos.csv' con 1000 registros creado en la raiz del proyecto.");
        } catch (IOException e) {
            System.out.println(" ERROR");
        }
    }
}