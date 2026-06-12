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

public class Pila<T> {

    private NodoPila<T> tope;
    private int tamano;

    public Pila() {
        tope = null;
        tamano = 0;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int getTamano() {
        return tamano;
    }

    public void apilar(T dato) {
        NodoPila<T> nuevo = new NodoPila<>(dato);
        nuevo.siguiente = tope;
        tope = nuevo;
        tamano++;
    }

    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila esta vacia");
        }
        T dato = tope.dato;
        tope = tope.siguiente;
        tamano--;
        return dato;
    }

    public T verTope() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila esta vacia");
        }
        return tope.dato;
    }

    public List<T> listarTodos() {
        List<T> resultado = new ArrayList<>();
        NodoPila<T> actual = tope;
        while (actual != null) {
            resultado.add(actual.dato);
            actual = actual.siguiente;
        }
        return resultado;
    }

    public void limitarA(int maxElementos) {
        if (tamano <= maxElementos) {
            return;
        }
        NodoPila<T> actual = tope;
        for (int i = 1; i < maxElementos; i++) {
            actual = actual.siguiente;
        }
        actual.siguiente = null;
        tamano = maxElementos;
    }
}
