package com.mycompany.biblioteca_digital.servicio;

import com.mycompany.biblioteca_digital.modelo.Persona;
import com.mycompany.biblioteca_digital.modelo.ReservaSala;
import com.mycompany.biblioteca_digital.estructuras.tads.Cola;
import com.mycompany.biblioteca_digital.estructuras.tads.ListaEnlazada;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SalaControl {

    // Cola de reservas activas (orden de llegada)
    private Cola<ReservaSala> reservasActivas;

    // Lista enlazada de historial de reservas liberadas
    private ListaEnlazada<ReservaSala> historial;

    private int contadorId;

    // Horario de la biblioteca
    private static final LocalTime HORA_APERTURA = LocalTime.of(00,00);
    private static final LocalTime HORA_CIERRE   = LocalTime.of(23,59);

    // Total de salas (ajusta a 4 si tus paneles de color son solo Sala 1-4)
    private static final int TOTAL_SALAS = 5;

    public SalaControl() {
        this.reservasActivas = new Cola<>();
        this.historial = new ListaEnlazada<>();
        this.contadorId = 1;
    }

    /**
     * Reservar una sala.
     * Verifica que la sala esté disponible y que sea horario válido.
     */
    public ReservaSala reservarSala(Persona persona, int numeroSala) {
        // Validar numero de sala
        if (numeroSala < 1 || numeroSala > TOTAL_SALAS) {
            System.out.println("Error: Sala inválida. Solo existen salas del 1 al " + TOTAL_SALAS);
            return null;
        }

        // Validar horario
        LocalTime ahora = LocalTime.now();
        if (ahora.isBefore(HORA_APERTURA) || ahora.isAfter(HORA_CIERRE)) {
            System.out.println("Error: Fuera del horario de atención (07:00 - 23:00)");
            return null;
        }

        // Verificar que la sala no esté ocupada
        if (salaTieneReservaActiva(numeroSala)) {
            System.out.println("Error: La Sala " + numeroSala + " ya está ocupada");
            return null;
        }

        // Verificar que la persona no tenga ya una sala reservada
        // (el Administrador puede tener más de una reserva activa)
        boolean esAdministrador = "ADMINISTRADOR".equalsIgnoreCase(persona.getTipo());
        if (!esAdministrador && personaTieneReservaActiva(persona)) {
            System.out.println("Error: Ya tienes una sala reservada actualmente");
            return null;
        }

        // Crear reserva
        ReservaSala reserva = new ReservaSala(persona, numeroSala, ahora);
        reserva.setIdReserva(contadorId++);

        // Encolar en reservas activas
        reservasActivas.encolar(reserva);

        System.out.println("Sala " + numeroSala + " reservada para: " + persona.getNombre());
        return reserva;
    }

    /**
     * Liberar una sala — el usuario entrega la sala.
     */
    public boolean liberarSala(int idReserva) {
        ReservaSala reserva = buscarReservaActivaPorId(idReserva);

        if (reserva == null) {
            System.out.println("Error: Reserva no encontrada");
            return false;
        }

        // Marcar como liberada
        reserva.liberarSala();

        // Sacar de la cola de activas (usa el método eliminar() de tu Cola)
        reservasActivas.eliminar(reserva);

        // Agregar al historial (lista enlazada genérica)
        historial.insertar(reserva);

        System.out.println("Sala " + reserva.getNumeroSala() + " liberada exitosamente");
        return true;
    }

    /**
     * Verifica si una sala tiene reserva activa en este momento.
     */
    public boolean salaTieneReservaActiva(int numeroSala) {
        for (ReservaSala r : reservasActivas.listarTodos()) {
            if (r.getNumeroSala() == numeroSala && r.estaActiva()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si una persona ya tiene una sala activa.
     */
    public boolean personaTieneReservaActiva(Persona persona) {
        for (ReservaSala r : reservasActivas.listarTodos()) {
            if (r.getPersona().getIdPersona() == persona.getIdPersona() && r.estaActiva()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtener estado de todas las salas (disponible u ocupada).
     */
    public String[] obtenerEstadoSalas() {
        String[] estados = new String[TOTAL_SALAS + 1]; // índice 1 al N
        for (int i = 1; i <= TOTAL_SALAS; i++) {
            if (salaTieneReservaActiva(i)) {
                ReservaSala r = obtenerReservaActivaDeSala(i);
                estados[i] = "OCUPADA - " + r.getPersona().getNombre() +
                             " desde " + r.getHoraEntrada();
            } else {
                estados[i] = "DISPONIBLE";
            }
        }
        return estados;
    }

    /**
     * Obtener la reserva activa de una sala específica.
     */
    public ReservaSala obtenerReservaActivaDeSala(int numeroSala) {
        for (ReservaSala r : reservasActivas.listarTodos()) {
            if (r.getNumeroSala() == numeroSala && r.estaActiva()) {
                return r;
            }
        }
        return null;
    }

    /**
     * Buscar reserva activa por ID.
     */
    public ReservaSala buscarReservaActivaPorId(int idReserva) {
        for (ReservaSala r : reservasActivas.listarTodos()) {
            if (r.getIdReserva() == idReserva) {
                return r;
            }
        }
        return null;
    }

    /**
     * Obtener reservas activas de una persona.
     */
    public List<ReservaSala> obtenerReservasActivasPersona(Persona persona) {
        List<ReservaSala> resultado = new ArrayList<>();
        for (ReservaSala r : reservasActivas.listarTodos()) {
            if (r.getPersona().getIdPersona() == persona.getIdPersona()) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public List<ReservaSala> obtenerTodasReservasActivas() {
        return reservasActivas.listarTodos();
    }

    public List<ReservaSala> obtenerHistorial() {
        return historial.listarTodos();
    }

    public List<ReservaSala> obtenerTodo() {
        List<ReservaSala> todo = new ArrayList<>(reservasActivas.listarTodos());
        todo.addAll(historial.listarTodos());
        return todo;
    }

    public int getTotalSalas() { return TOTAL_SALAS; }
}