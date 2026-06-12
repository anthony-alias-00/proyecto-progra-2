package com.mycompany.biblioteca_digital.estructuras.tads;

import java.util.ArrayList;
import java.util.List;

public class ListaEnlazada<T> {

    private NodoLista<T> cabeza;
    private int tamano;

    public ListaEnlazada() {
        cabeza = null;
        tamano = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamano() {
        return tamano;
    }

    public void insertar(T dato) {
        NodoLista<T> nuevo = new NodoLista<>(dato);
        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            NodoLista<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamano++;
    }

    public boolean eliminar(T dato) {
        if (estaVacia()) return false;
        if (cabeza.dato.equals(dato)) {
            cabeza = cabeza.siguiente;
            tamano--;
            return true;
        }
        NodoLista<T> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(dato)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamano--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    public List<T> listarTodos() {
        List<T> resultado = new ArrayList<>();
        NodoLista<T> actual = cabeza;
        while (actual != null) {
            resultado.add(actual.dato);
            actual = actual.siguiente;
        }
        return resultado;
    }
}