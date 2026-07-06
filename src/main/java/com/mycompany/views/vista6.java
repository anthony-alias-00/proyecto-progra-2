package com.mycompany.views;


import com.mycompany.biblioteca_digital.modelo.Prestamo;
import com.mycompany.biblioteca_digital.modelo.Persona;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;

public class vista6 extends javax.swing.JPanel {
    private com.mycompany.biblioteca_digital.servicio.PrestamoControl prestamoDAO;
    private DefaultTableModel modeloTabla;
    private DateTimeFormatter formatoFecha;
    private Persona usuarioLogueado; 
    
/**
     * Constructor que recibe el usuario logueado
     */
    public vista6(Persona usuario) {
        this.usuarioLogueado = usuario;
        initComponents();
        inicializar();
        configurarSegunRol();
    }
    
    /**
     * Constructor 
     */
    public vista6() {
        this(null);
    }
/**
 * Inicializar componentes personalizados
 */
private void inicializar() {
        prestamoDAO = com.mycompany.biblioteca_digital.servicio.AppContext.getInstance().getPrestamoControl();
        formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        configurarTabla();
        cargarReportes();
        
        jTable3.setFocusable(false);
        jTable3.setCellSelectionEnabled(false);

        boton12.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar.png")).getImage().getScaledInstance(100, 70, java.awt.Image.SCALE_SMOOTH)));
        boton12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        margen.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/margen1.png")).getImage().getScaledInstance(500, 250, java.awt.Image.SCALE_SMOOTH)));
        margen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    }
    
/**
     * Configurar interfaz según el rol del usuario
     */
    private void configurarSegunRol() {
        if (usuarioLogueado == null) {
            System.err.println("No hay usuario logueado en reportes");
            return;
        }
        
        if ("USUARIO".equalsIgnoreCase(usuarioLogueado.getTipo())) {
            
            titulo4.setText("Mis Préstamos");
        } else {
           
            titulo4.setText("Panel Reportes");
        }
    }
  /**
 * Configurar columnas de la tabla
 */
 private void configurarTabla() {
        String[] columnas = {"ID", "Usuario", "Cédula", "Libro", "ISBN", 
                            "Fecha Préstamo", "Fecha Esperada", "Fecha Real", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
    jTable3.setModel(modeloTabla);
    
    // Ajustar anchos
    jTable3.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
    jTable3.getColumnModel().getColumn(1).setPreferredWidth(150); // Usuario
    jTable3.getColumnModel().getColumn(2).setPreferredWidth(100); // Cédula
    jTable3.getColumnModel().getColumn(3).setPreferredWidth(200); // Libro
    jTable3.getColumnModel().getColumn(4).setPreferredWidth(120); // ISBN
    jTable3.getColumnModel().getColumn(5).setPreferredWidth(100); // Fecha PrEstamo
    jTable3.getColumnModel().getColumn(6).setPreferredWidth(100); // Fecha Esperada
    jTable3.getColumnModel().getColumn(7).setPreferredWidth(100); // Fecha Real
    jTable3.getColumnModel().getColumn(8).setPreferredWidth(100); // Estado
}

 /**
     * Cargar todos los prestamos segun el rol
     */
    private void cargarReportes() {
        modeloTabla.setRowCount(0);
        
        List<Prestamo> prestamos;
        
        if (usuarioLogueado != null && "USUARIO".equalsIgnoreCase(usuarioLogueado.getTipo())) {
            // MODO USUARIO: Solo sus prestamos
            prestamos = prestamoDAO.obtenerPorUsuario(usuarioLogueado.getIdPersona());
        
            
        } else {
            // MODO ADMIN: Todos los prestamos
            prestamos = prestamoDAO.obtenerTodos();
       
        }
        
        cargarDatosEnTabla(prestamos);
    }
    
    /**
     * Cargar datos en la tabla
     */
    private void cargarDatosEnTabla(List<Prestamo> prestamos) {
        modeloTabla.setRowCount(0);
        
        for (Prestamo prestamo : prestamos) {
            String nombreUsuario = prestamo.getUsuario().getNombre() + " " + 
                                  prestamo.getUsuario().getApellido();
            
            String fechaPrestamo = prestamo.getFechaPrestamo().format(formatoFecha);
            String fechaEsperada = prestamo.getFechaDevolucionEsperada().format(formatoFecha);
            
            String fechaReal = "-";
            if (prestamo.getFechaDevolucionReal() != null) {
                fechaReal = prestamo.getFechaDevolucionReal().format(formatoFecha);
            }
            
            // Determinar estado
            String estado = prestamo.getEstado();
            if ("ACTIVO".equals(estado) && 
                prestamo.getFechaDevolucionEsperada().isBefore(java.time.LocalDate.now())) {
                estado = "VENCIDO";
            }
            
            Object[] fila = {
                prestamo.getIdPrestamo(),
                nombreUsuario,
                prestamo.getUsuario().getCedula(),
                prestamo.getLibro().getTitulo(),
                prestamo.getLibro().getIsbn(),
                fechaPrestamo,
                fechaEsperada,
                fechaReal,
                estado
            };
            modeloTabla.addRow(fila);
        }
    }

    /**
     * Filtrar por estado segun el rol
     */
    private void filtrarPorEstado(String estado) {
        List<Prestamo> prestamos = new ArrayList<>();
        
        if (usuarioLogueado != null && "USUARIO".equalsIgnoreCase(usuarioLogueado.getTipo())) {
            // MODO USUARIO: Solo sus préstamos filtrados
            List<Prestamo> todosPrestamos = prestamoDAO.obtenerPorUsuario(usuarioLogueado.getIdPersona());
            
            if ("TODOS".equals(estado)) {
                prestamos = todosPrestamos;
            } else if ("ACTIVOS".equals(estado)) {
                for (Prestamo p : todosPrestamos) {
                    if ("ACTIVO".equals(p.getEstado())) {
                        prestamos.add(p);
                    }
                }
            } else if ("DEVUELTO".equals(estado)) {
                for (Prestamo p : todosPrestamos) {
                    if ("DEVUELTO".equals(p.getEstado())) {
                        prestamos.add(p);
                    }
                }
            } else if ("VENCIDOS".equals(estado)) {
                for (Prestamo p : todosPrestamos) {
                    if ("ACTIVO".equals(p.getEstado()) && 
                        p.getFechaDevolucionEsperada().isBefore(java.time.LocalDate.now())) {
                        prestamos.add(p);
                    }
                }
            }
            
        } else {
            // MODO ADMIN: Todos los préstamos filtrados
            if ("TODOS".equals(estado)) {
                prestamos = prestamoDAO.obtenerTodos();
            } else if ("ACTIVOS".equals(estado)) {
                prestamos = prestamoDAO.obtenerPrestamosActivos();
            } else if ("VENCIDOS".equals(estado)) {
                prestamos = prestamoDAO.obtenerPrestamosVencidos();
            } else if ("DEVUELTO".equals(estado)) {
                List<Prestamo> todosPrestamos = prestamoDAO.obtenerTodos();
                for (Prestamo p : todosPrestamos) {
                    if ("DEVUELTO".equals(p.getEstado())) {
                        prestamos.add(p);
                    }
                }
            }
        }
        
        cargarDatosEnTabla(prestamos);
    
    }

    /**
     * Obtener estadísticas según el rol
     */
    private String obtenerEstadisticas() {
        List<Prestamo> prestamos;
        String titulo;
        
        if (usuarioLogueado != null && "USUARIO".equalsIgnoreCase(usuarioLogueado.getTipo())) {
            // MODO USUARIO: Solo sus estadísticas
            prestamos = prestamoDAO.obtenerPorUsuario(usuarioLogueado.getIdPersona());
            titulo = "MIS ESTADISTICAS DE PRESTAMOS";
        } else {
            // MODO ADMIN: Estadísticas globales
            prestamos = prestamoDAO.obtenerTodos();
            titulo = "ESTADISTICAS GLOBALES DE PRESTAMOS";
        }
        
        int totalPrestamos = prestamos.size();
        int activos = (int) prestamos.stream()
            .filter(p -> "ACTIVO".equals(p.getEstado()))
            .count();
        int devueltos = (int) prestamos.stream()
            .filter(p -> "DEVUELTO".equals(p.getEstado()))
            .count();
        int vencidos = (int) prestamos.stream()
            .filter(p -> "ACTIVO".equals(p.getEstado()) && 
                         p.getFechaDevolucionEsperada().isBefore(java.time.LocalDate.now()))
            .count();
        
        return String.format(
            "%s\n\n" +
            "Total de préstamos: %d\n" +
            "Préstamos activos: %d\n" +
            "Préstamos devueltos: %d\n" +
            "Préstamos vencidos: %d",
            titulo, totalPrestamos, activos, devueltos, vencidos
        );
    }
   

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        bg = new javax.swing.JPanel();
        paleta1 = new javax.swing.JLabel();
        titulo4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        boton12 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        margen = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(706, 457));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setForeground(new java.awt.Color(204, 0, 51));
        bg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                bgMousePressed(evt);
            }
        });
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        paleta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/paleta.png"))); // NOI18N
        paleta1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 78, 1, 56, new java.awt.Color(0, 0, 0)));
        paleta1.setMaximumSize(new java.awt.Dimension(300, 256));
        paleta1.setMinimumSize(new java.awt.Dimension(300, 256));
        paleta1.setPreferredSize(new java.awt.Dimension(300, 200));
        bg.add(paleta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 400, 10));

        titulo4.setBackground(new java.awt.Color(0, 0, 0));
        titulo4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 28)); // NOI18N
        titulo4.setForeground(new java.awt.Color(204, 0, 51));
        titulo4.setText("Panel Reportes");
        bg.add(titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, 40));

        jTable3.setBackground(new java.awt.Color(240, 240, 240));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID Usuario", "ID Libro", "Usuario", "Fecha de Salida", "Fecha de Entrega"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable3.setGridColor(new java.awt.Color(0, 102, 204));
        jTable3.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable3.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jTable3.setShowGrid(true);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jTable3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                jTable3ComponentMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable3.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable3.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(3).setPreferredWidth(150);
            jTable3.getColumnModel().getColumn(4).setPreferredWidth(150);
        }

        bg.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 650, 260));

        boton12.setBackground(new java.awt.Color(0, 0, 0));
        boton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar.png"))); // NOI18N
        boton12.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        boton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        boton12.addActionListener(this::boton12ActionPerformed);
        bg.add(boton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 350, 100, 60));

        jLabel3.setFont(new java.awt.Font("STZhongsong", 1, 18)); // NOI18N
        jLabel3.setText("Actualizar");
        bg.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 420, 120, 20));

        margen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/margen1.png"))); // NOI18N
        bg.add(margen, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 520, 60));

        jScrollPane3.setViewportView(bg);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable3ComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable3ComponentMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3ComponentMoved

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

    private void bgMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgMousePressed
// Esto hace que al tocar el panel, la tabla pierda el foco y se limpie la selección visual
jTable3.clearSelection(); 
this.requestFocusInWindow();        // TODO add your handling code here:
    }//GEN-LAST:event_bgMousePressed

    private void boton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton12ActionPerformed
        String[] opciones = {"Actualizar", "Ver Activos", "Ver Devueltos", "Ver Vencidos", 
                        "Ver Todos"};
    
    String seleccion = (String) javax.swing.JOptionPane.showInputDialog(
        this,
        "Seleccione una opción:",
        "Opciones de Reportes",
        javax.swing.JOptionPane.QUESTION_MESSAGE,
        null,
        opciones,
        opciones[0]
    );
    
    if (seleccion != null) {
        switch (seleccion) {
            case "Actualizar":
                cargarReportes();
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Reportes actualizados",
                    "Éxito",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
                break;
                
            case "Ver Activos":
                filtrarPorEstado("ACTIVOS");
                break;
                
            case "Ver Devueltos":
                filtrarPorEstado("DEVUELTO");
                break;
                
            case "Ver Vencidos":
                filtrarPorEstado("VENCIDOS");
                break;
                
            case "Ver Todos":
                filtrarPorEstado("TODOS");
                break;
            }
        }
    }//GEN-LAST:event_boton12ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton boton12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel margen;
    private javax.swing.JLabel paleta1;
    private javax.swing.JLabel titulo4;
    // End of variables declaration//GEN-END:variables

    
}
