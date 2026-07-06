/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.biblioteca_digital.modelo;

/**
 *
 * @author ALEJANDRO
 */

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaSala {

    private int idReserva;
    private Persona persona;        // Usuario o Administrador
    private int numeroSala;         // 1 al 5
    private LocalDate fecha;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;   // null hasta que libera la sala
    private String estado;          // "ACTIVA", "LIBERADA", "VENCIDA"

    public ReservaSala() {
        this.estado = "ACTIVA";
    }

    public ReservaSala(Persona persona, int numeroSala, LocalTime horaEntrada) {
        this.persona = persona;
        this.numeroSala = numeroSala;
        this.fecha = LocalDate.now();
        this.horaEntrada = horaEntrada;
        this.horaSalida = null;
        this.estado = "ACTIVA";
    }

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public int getNumeroSala() { return numeroSala; }
    public void setNumeroSala(int numeroSala) { this.numeroSala = numeroSala; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public void liberarSala() {
        this.horaSalida = LocalTime.now();
        this.estado = "LIBERADA";
    }

    public boolean estaActiva() {
        return "ACTIVA".equals(estado);
    }

    @Override
    public String toString() {
        return "ReservaSala{" +
                "idReserva:" + idReserva +
                ", sala:" + numeroSala +
                ", persona:" + (persona != null ? persona.getNombre() : "null") +
                ", fecha:" + fecha +
                ", horaEntrada:" + horaEntrada +
                ", horaSalida:" + horaSalida +
                ", estado:'" + estado + '\'' +
                '}';
    }
}