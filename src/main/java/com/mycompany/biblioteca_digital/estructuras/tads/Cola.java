/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca_digital.estructuras.tads;

/**
 *
 * @author ALEJANDRO
 */

import java.util.ArrayList;
import java.util.List;

public class Cola<T> {

    private NodoCola<T> frente;
    private NodoCola<T> fin;
    private int tamano;

    public Cola() {
        frente = null;
        fin = null;
        tamano = 0;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamano() {
        return tamano;
    }

    public void encolar(T dato) {
        NodoCola<T> nuevo = new NodoCola<>(dato);
        if (estaVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamano++;
    }

    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola esta vacia");
        }
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamano--;
        return dato;
    }

    public T verFrente() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola esta vacia");
        }
        return frente.dato;
    }

    public List<T> listarTodos() {
        List<T> resultado = new ArrayList<>();
        NodoCola<T> actual = frente;
        while (actual != null) {
            resultado.add(actual.dato);
            actual = actual.siguiente;
        }
        return resultado;
    }

    public boolean eliminar(T dato) {
        if (estaVacia()) {
            return false;
        }
        if (frente.dato.equals(dato)) {
            desencolar();
            return true;
        }
        boolean eliminado = eliminarRec(frente, dato);
        if (eliminado) {
            tamano--;
        }
        return eliminado;
    }

    private boolean eliminarRec(NodoCola<T> anterior, T dato) {
        if (anterior.siguiente == null) {
            return false;
        }
        if (anterior.siguiente.dato.equals(dato)) {
            if (anterior.siguiente == fin) {
                fin = anterior;
            }
            anterior.siguiente = anterior.siguiente.siguiente;
            return true;
        }
        return eliminarRec(anterior.siguiente, dato);
    }
}
