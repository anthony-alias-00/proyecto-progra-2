package com.mycompany.views;

import com.mycompany.biblioteca_digital.modelo.Prestamo;
import com.mycompany.biblioteca_digital.modelo.Libro;
import com.mycompany.biblioteca_digital.modelo.Persona;
import java.time.LocalDate;

import java.util.List;
import javax.swing.JOptionPane;

public class vista4 extends javax.swing.JPanel {

    private com.mycompany.biblioteca_digital.servicio.PrestamoControl prestamoDAO;
    private com.mycompany.biblioteca_digital.servicio.LibroOpciones libroDAO;
    private com.mycompany.biblioteca_digital.servicio.RegistroUsuario personaDAO;   
    private Persona usuarioLogueado;  // ✅ AGREGAR
    
    /**
     * Constructor que recibe el usuario logueado
     */
    public vista4(Persona usuario) {
        this.usuarioLogueado = usuario;
        initComponents();
        
        // Configurar imagen
        foto2.setIcon(new javax.swing.ImageIcon(
            new javax.swing.ImageIcon(getClass().getResource("/imagenes/entrada.png"))
            .getImage().getScaledInstance(350, 500, java.awt.Image.SCALE_SMOOTH)));
        foto2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        // Inicializar DAOs y cargar datos
        inicializar();
        
        // ✅ CONFIGURAR SEGÚN ROL
        configurarSegunRol();
    }
    
    /**
     * Constructor vacío (para compatibilidad)
     */
    public vista4() {
        this(null);
    }

/**
 * Inicializar DAOs
 */
private void inicializar() {
    prestamoDAO = com.mycompany.biblioteca_digital.servicio.AppContext.getInstance().getPrestamoControl();
    libroDAO = com.mycompany.biblioteca_digital.servicio.AppContext.getInstance().getLibroOpciones();
    personaDAO = com.mycompany.biblioteca_digital.servicio.AppContext.getInstance().getRegistroUsuario();
        
// Configurar imagen
        foto2.setIcon(new javax.swing.ImageIcon(
            new javax.swing.ImageIcon(getClass().getResource("/imagenes/entrada.png"))
            .getImage().getScaledInstance(350, 500, java.awt.Image.SCALE_SMOOTH)));
        foto2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        // Cargar usuarios
        cargarUsuarios();
    }
/**
     * Cargar usuarios en el ComboBox
     */
    private void cargarUsuarios() {
        cmbUsuarios.removeAllItems();
        cmbUsuarios.addItem("-- Seleccione un usuario --");
        
        List<Persona> usuarios = personaDAO.obtenerTodos();
        int count = 0;
        for (Persona persona : usuarios) {
            if (persona.isActivo() && "USUARIO".equals(persona.getTipo())) {
                String item = persona.getIdPersona() + " - " + 
                             persona.getNombre() + " " + persona.getApellido() + 
                             " (" + persona.getCedula() + ")";
                cmbUsuarios.addItem(item);
                count++;
            }
        }
        
        
        if (cmbUsuarios.getItemCount() > 0) {
            cmbUsuarios.setSelectedIndex(0);
        }
    }

    /**
 * Cargar libros prestados según el usuario
 */
private void cargarLibrosPrestados() {
    cmbLibros.removeAllItems();
    cmbLibros.addItem("-- Seleccione un libro --");
    
    int idUsuario = -1;
    
    if ("USUARIO".equalsIgnoreCase(usuarioLogueado.getTipo())) {
        // MODO USUARIO: Solo sus libros
        idUsuario = usuarioLogueado.getIdPersona();
        
    } else {
        // MODO ADMIN: Del usuario seleccionado en ComboBox
        if (cmbUsuarios.getSelectedIndex() <= 0) {
           
            return;
        }
        
        String usuarioSeleccionado = (String) cmbUsuarios.getSelectedItem();
        
        try {
            String[] partes = usuarioSeleccionado.split(" - ");
            if (partes.length > 0) {
                idUsuario = Integer.parseInt(partes[0].trim());
            }
        } catch (Exception e) {
            System.err.println("Error al extraer ID del usuario");
            return;
        }
    }
    
    if (idUsuario <= 0) {
        System.out.println("ID de usuario inválido");
        return;
    }
    
    // Obtener préstamos activos del usuario
    List<Prestamo> prestamos = prestamoDAO.obtenerPorUsuario(idUsuario);
    
    int count = 0;
    for (Prestamo prestamo : prestamos) {
        if ("ACTIVO".equals(prestamo.getEstado())) {
            String item = prestamo.getIdPrestamo() + " | Libro: " + 
                         prestamo.getLibro().getTitulo() + 
                         " | Prestado: " + prestamo.getFechaPrestamo();
            cmbLibros.addItem(item);
            count++;
        }
    }
    
   
    
    if (cmbLibros.getItemCount() > 0) {
        cmbLibros.setSelectedIndex(0);
    }
}
    
/**
 * Realizar devolución de un libro
 */
private void devolverLibro() {
    // Verificar que haya usuario logueado
    if (usuarioLogueado == null) {
        JOptionPane.showMessageDialog(this,
            "No hay usuario logueado",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Verificar seleccion de libro
    if (cmbLibros.getSelectedIndex() <= 0) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione un libro de la lista",
            "Libro No Seleccionado",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String libroSeleccionado = (String) cmbLibros.getSelectedItem();
    
    // Extraer ID del prestamo
    int idPrestamo = -1;
    try {
        String[] partes = libroSeleccionado.split(" \\| ");
        if (partes.length > 0) {
            idPrestamo = Integer.parseInt(partes[0].trim());
        }
        
        if (idPrestamo <= 0) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un prestamo valido",
                "Seleccion Invalida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al procesar la selección",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return;
    }
    
    
    // Buscar el prestamo
    Prestamo prestamo = prestamoDAO.buscarPrestamoPorId(idPrestamo);
    
    if (prestamo == null) {
        JOptionPane.showMessageDialog(this,
            "Prestamo no encontrado",
            "Error",
            JOptionPane.ERROR_MESSAGE);
        System.err.println("Prestamo no encontrado en BD");
        return;
    }
    
    // VERIFICAR PERMISOS: Usuario solo puede devolver sus propios libros
    if ("USUARIO".equalsIgnoreCase(usuarioLogueado.getTipo())) {
        if (prestamo.getUsuario().getIdPersona() != usuarioLogueado.getIdPersona()) {
            JOptionPane.showMessageDialog(this,
                "No tienes permiso para devolver este libro\n\n" +
                "Este libro fue prestado por otro usuario.",
                "Permiso Denegado",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    
    // Verificar prestamo activo
    if (!"ACTIVO".equals(prestamo.getEstado())) {
        JOptionPane.showMessageDialog(this,
            "Este prestamo ya fue devuelto",
            "Prestamo Ya Devuelto",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Actualizar prestamo
    prestamo.setFechaDevolucionReal(LocalDate.now());
    prestamo.setEstado("DEVUELTO");
    
    boolean exito = prestamoDAO.actualizar(prestamo);
    
    if (exito) {
        // Actualizar disponibilidad del libro
        Libro libro = prestamo.getLibro();
        int nuevaDisponibilidad = libro.getCantidadDisponible() + 1;
        libroDAO.actualizarDisponibilidad(libro.getIdLibro(), nuevaDisponibilidad);
        
        // Calcular días de retraso
        long diasRetraso = java.time.temporal.ChronoUnit.DAYS.between(
            prestamo.getFechaDevolucionEsperada(), 
            prestamo.getFechaDevolucionReal()
        );
        
        String mensajeRetraso = "";
        if (diasRetraso > 0) {
            mensajeRetraso = "\nRETRASO: " + diasRetraso + " día(s)";
        } else {
            mensajeRetraso = "\nA TIEMPO";
        }
        
        // Mensaje de rxito
        JOptionPane.showMessageDialog(this,
            "DEVOLUCIÓN REALIZADA EXITOSAMENTE\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "LIBRO: " + libro.getTitulo() + "\n" +
            "Autor: " + libro.getAutor() + "\n\n" +
            "USUARIO: " + prestamo.getUsuario().getNombre() + " " + 
                            prestamo.getUsuario().getApellido() + "\n\n" +
            "Fecha Préstamo: " + prestamo.getFechaPrestamo() + "\n" +
            "Fecha Esperada: " + prestamo.getFechaDevolucionEsperada() + "\n" +
            "Fecha Devolución: " + prestamo.getFechaDevolucionReal() + "\n" +
            mensajeRetraso + "\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
            "Devolucion Exitosa",
            JOptionPane.INFORMATION_MESSAGE);
        

        
        // Recargar lista
        cargarLibrosPrestados();
        
    } else {
        JOptionPane.showMessageDialog(this,
            "Error al realizar la devolución",
            "Error",
            JOptionPane.ERROR_MESSAGE);
       
    }
}

 /**
     * Limpiar campos
     */
    private void limpiarCampos() {
        cmbUsuarios.setSelectedIndex(0);
        cmbLibros.removeAllItems();
        cmbLibros.addItem("-- Primero seleccione un usuario --");
    }
/**
 * Configurar interfaz según el rol del usuario
 */
private void configurarSegunRol() {
      if (usuarioLogueado == null) {
        System.err.println("No hay usuario logueado en devoluciones");
        return;
    }
    
    if ("USUARIO".equalsIgnoreCase(usuarioLogueado.getTipo())) {
        // MODO USUARIO: Solo ve sus libros prestados        
        cmbUsuarios.setVisible(false);
        
        // Mostrar el nombre junto a "Usuario:"
        texto3.setText("Usuario: " + usuarioLogueado.getNombre() + " " + usuarioLogueado.getApellido());
        texto3.setBounds(texto3.getX(), texto3.getY(), 260, texto3.getHeight());
        
        cargarLibrosPrestados();
        
    } else {
        // MODO ADMIN
        cmbUsuarios.setVisible(true);
        texto3.setText("Usuario:");
    }
}
  
    
    @SuppressWarnings("unchecked")
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        foto2 = new javax.swing.JLabel();
        texto3 = new javax.swing.JLabel();
        texto4 = new javax.swing.JLabel();
        devolvers = new javax.swing.JButton();
        paleta1 = new javax.swing.JLabel();
        titulo4 = new javax.swing.JLabel();
        cmbLibros = new javax.swing.JComboBox<>();
        cmbUsuarios = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(706, 457));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setForeground(new java.awt.Color(204, 0, 51));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jSeparator2.setBackground(new java.awt.Color(153, 0, 51));
        jSeparator2.setForeground(new java.awt.Color(204, 0, 51));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        bg.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, 50, 440));

        foto2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/entrada.png"))); // NOI18N
        foto2.setMaximumSize(new java.awt.Dimension(1200, 1200));
        foto2.setPreferredSize(new java.awt.Dimension(1200, 1200));
        bg.add(foto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 380, 490));

        texto3.setBackground(new java.awt.Color(0, 51, 102));
        texto3.setFont(new java.awt.Font("STZhongsong", 0, 18)); // NOI18N
        texto3.setForeground(new java.awt.Color(0, 51, 102));
        texto3.setText("Usuario:");
        bg.add(texto3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 190, 45));

        texto4.setFont(new java.awt.Font("STZhongsong", 0, 18)); // NOI18N
        texto4.setForeground(new java.awt.Color(0, 51, 102));
        texto4.setText("Libro Prestado:");
        bg.add(texto4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, 158, 48));

        devolvers.setBackground(new java.awt.Color(0, 51, 102));
        devolvers.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        devolvers.setForeground(new java.awt.Color(255, 255, 255));
        devolvers.setText("Devolver");
        devolvers.setBorder(null);
        devolvers.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        devolvers.addActionListener(this::devolversActionPerformed);
        bg.add(devolvers, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 380, 200, 50));

        paleta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/paleta.png"))); // NOI18N
        paleta1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 78, 1, 56, new java.awt.Color(0, 0, 0)));
        paleta1.setMaximumSize(new java.awt.Dimension(300, 256));
        paleta1.setMinimumSize(new java.awt.Dimension(300, 256));
        paleta1.setPreferredSize(new java.awt.Dimension(300, 200));
        bg.add(paleta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 280, 10));

        titulo4.setBackground(new java.awt.Color(0, 0, 0));
        titulo4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 28)); // NOI18N
        titulo4.setForeground(new java.awt.Color(204, 0, 51));
        titulo4.setText("Panel Devolución");
        bg.add(titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 260, 40));

        cmbLibros.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        bg.add(cmbLibros, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 240, 40));

        cmbUsuarios.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbUsuarios.addActionListener(this::cmbUsuariosActionPerformed);
        bg.add(cmbUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 240, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void devolversActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devolversActionPerformed
        devolverLibro();
    }//GEN-LAST:event_devolversActionPerformed

    private void cmbUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUsuariosActionPerformed
        cargarLibrosPrestados();
    }//GEN-LAST:event_cmbUsuariosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JComboBox<String> cmbLibros;
    private javax.swing.JComboBox<String> cmbUsuarios;
    private javax.swing.JButton devolvers;
    private javax.swing.JLabel foto2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel paleta1;
    private javax.swing.JLabel texto3;
    private javax.swing.JLabel texto4;
    private javax.swing.JLabel titulo4;
    // End of variables declaration//GEN-END:variables

    
}
