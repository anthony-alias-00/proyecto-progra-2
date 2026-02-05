package vuelosfis.modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class LogicaVuelos {

public ArrayList<String> buscarVuelos(String origen, String destino, String fecha) {
    ArrayList<String> resultados = new ArrayList<>();
    java.io.File archivo = new java.io.File("vuelos.csv");

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length < 6) continue;

            String origenCSV = datos[1].trim();
            String destinoCSV = datos[2].trim();
            String fechaCSV = datos[3].trim();

            // Lógica flexible: 
            // 1. Comparamos Origen y Destino (obligatorios)
            // 2. Si la fecha es null o vacía, NO filtramos por fecha y traemos todo
            if (origenCSV.equalsIgnoreCase(origen) && destinoCSV.equalsIgnoreCase(destino)) {
                
                // Si no nos importa la fecha o coincide exactamente:
                if (fecha == null || fecha.isEmpty() || fechaCSV.equals(fecha)) {
                    resultados.add("Vuelo " + datos[0] + " | Fecha: " + datos[3] + " | Hora: " + datos[4] + " | Precio: $" + datos[5]);
                }
            }
        }
    } catch (Exception e) {
        System.out.println("Error al leer: " + e.getMessage());
    }
    return resultados;
}

    public ArrayList<String> cargarRutas() {
        ArrayList<String> lista = new ArrayList<>();
        // Asumiendo que rutas.txt sigue en resources
        try (java.io.InputStream is = getClass().getResourceAsStream("/rutas.txt");
             BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(linea);
            }
        } catch (Exception e) {
            System.out.println("Error en rutas: " + e.getMessage());
        }
        return lista;
    }
}