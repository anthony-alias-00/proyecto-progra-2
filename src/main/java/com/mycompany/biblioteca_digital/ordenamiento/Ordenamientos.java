/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca_digital.ordenamiento;

/**
 *
 * @author ALEJANDRO
 */
import com.mycompany.biblioteca_digital.modelo.Libro;
import java.util.ArrayList;
import java.util.List;

public class Ordenamientos {

    private Ordenamientos() {
    }

    public static List<Libro> mergeSort(List<Libro> libros, String criterio) {
        if (libros.size() <= 1) {
            return new ArrayList<>(libros);
        }

        int medio = libros.size() / 2;
        List<Libro> izquierda = mergeSort(libros.subList(0, medio), criterio);
        List<Libro> derecha = mergeSort(libros.subList(medio, libros.size()), criterio);

        return mezclar(izquierda, derecha, criterio);
    }

    private static List<Libro> mezclar(List<Libro> izquierda, List<Libro> derecha, String criterio) {
        List<Libro> resultado = new ArrayList<>();
        int i = 0, j = 0;

        while (i < izquierda.size() && j < derecha.size()) {
            if (comparar(izquierda.get(i), derecha.get(j), criterio) <= 0) {
                resultado.add(izquierda.get(i));
                i++;
            } else {
                resultado.add(derecha.get(j));
                j++;
            }
        }

        while (i < izquierda.size()) {
            resultado.add(izquierda.get(i));
            i++;
        }
        while (j < derecha.size()) {
            resultado.add(derecha.get(j));
            j++;
        }

        return resultado;
    }

    public static void quickSort(List<Libro> libros, String criterio) {
        quickSortRec(libros, 0, libros.size() - 1, criterio);
    }

    private static void quickSortRec(List<Libro> libros, int inicio, int fin, String criterio) {
        if (inicio < fin) {
            int posPivote = particionar(libros, inicio, fin, criterio);
            quickSortRec(libros, inicio, posPivote - 1, criterio);
            quickSortRec(libros, posPivote + 1, fin, criterio);
        }
    }

    private static int particionar(List<Libro> libros, int inicio, int fin, String criterio) {
        Libro pivote = libros.get(fin);
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (comparar(libros.get(j), pivote, criterio) <= 0) {
                i++;
                intercambiar(libros, i, j);
            }
        }
        intercambiar(libros, i + 1, fin);
        return i + 1;
    }

    private static void intercambiar(List<Libro> libros, int i, int j) {
        Libro temp = libros.get(i);
        libros.set(i, libros.get(j));
        libros.set(j, temp);
    }

    private static int comparar(Libro a, Libro b, String criterio) {
        switch (criterio.toLowerCase()) {
            case "titulo":
                return a.getTitulo().compareToIgnoreCase(b.getTitulo());
            case "autor":
                return a.getAutor().compareToIgnoreCase(b.getAutor());
            case "anio":
                return Integer.compare(a.getAño(), b.getAño());
            case "isbn":
            default:
                return a.getIsbn().compareTo(b.getIsbn());
        }
    }
}
