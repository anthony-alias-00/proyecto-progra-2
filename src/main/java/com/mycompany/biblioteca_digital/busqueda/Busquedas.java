/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca_digital.busqueda;

/**
 *
 * @author ALEJANDRO
 */
import com.mycompany.biblioteca_digital.modelo.Libro;
import java.util.List;

public class Busquedas {

    private Busquedas() {
    }

    public static Libro busquedaLinealPorTitulo(List<Libro> libros, String titulo) {
        return busquedaLinealRec(libros, titulo.toLowerCase(), 0);
    }

    private static Libro busquedaLinealRec(List<Libro> libros, String tituloLower, int indice) {
        if (indice >= libros.size()) {
            return null;
        }
        Libro actual = libros.get(indice);
        if (actual.getTitulo() != null && actual.getTitulo().toLowerCase().contains(tituloLower)) {
            return actual;
        }
        return busquedaLinealRec(libros, tituloLower, indice + 1);
    }

    public static Libro busquedaBinariaPorIsbn(List<Libro> librosOrdenados, String isbn) {
        return busquedaBinariaRec(librosOrdenados, isbn, 0, librosOrdenados.size() - 1);
    }

    private static Libro busquedaBinariaRec(List<Libro> libros, String isbn, int izquierda, int derecha) {
        if (izquierda > derecha) {
            return null;
        }

        int medio = (izquierda + derecha) / 2;
        Libro libroMedio = libros.get(medio);
        int cmp = isbn.compareTo(libroMedio.getIsbn());

        if (cmp == 0) {
            return libroMedio;
        } else if (cmp < 0) {
            return busquedaBinariaRec(libros, isbn, izquierda, medio - 1);
        } else {
            return busquedaBinariaRec(libros, isbn, medio + 1, derecha);
        }
    }
}