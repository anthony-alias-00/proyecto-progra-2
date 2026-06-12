package com.mycompany.views;

import java.awt.Image;
import javax.swing.ImageIcon;

import com.mycompany.biblioteca_digital.modelo.Persona;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
 
public class vista3 extends javax.swing.JPanel {

    private com.mycompany.biblioteca_digital.servicio.RegistroUsuario personaDAO;
    private DefaultTableModel modeloTabla;
    
    public vista3() {
        initComponents();
        inicializar();
}

/**
 * Inicializar componentes personalizados
 */
private void inicializar() {
    personaDAO = com.mycompany.biblioteca_digital.servicio.AppContext.getInstance().getRegistroUsuario();
    configurarTabla();
    cargarUsuarios();

        
boton3.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono.png")).getImage().getScaledInstance(125, 110, java.awt.Image.SCALE_SMOOTH)));

// 2. Forzar el centrado horizontal dentro del espacio del Label
boton3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

botonborrar.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono4_1.png")).getImage().getScaledInstance(80, 50, java.awt.Image.SCALE_SMOOTH)));

// 2. Forzar el centrado horizontal dentro del espacio del Label
botonborrar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
}
    /**
 * Configurar columnas de la tabla
 */
private void configurarTabla() {
    String[] columnas = {"ID", "Cédula", "Nombre", "Apellido", "Email", 
                        "Dirección", "Tipo", "Usuario", "Activo"};
    modeloTabla = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    jTable1.setModel(modeloTabla);
    
    // Ajustar anchos
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
    jTable1.getColumnModel().getColumn(1).setPreferredWidth(100); // Cédula
    jTable1.getColumnModel().getColumn(2).setPreferredWidth(120); // Nombre
    jTable1.getColumnModel().getColumn(3).setPreferredWidth(120); // Apellido
    jTable1.getColumnModel().getColumn(4).setPreferredWidth(180); // Email
    jTable1.getColumnModel().getColumn(5).setPreferredWidth(150); // Dirección
    jTable1.getColumnModel().getColumn(6).setPreferredWidth(120); // Tipo
    jTable1.getColumnModel().getColumn(7).setPreferredWidth(100); // Usuario
    jTable1.getColumnModel().getColumn(8).setPreferredWidth(60);  // Activo
}

/**
 * Cargar todos los usuarios
 */
private void cargarUsuarios() {
    modeloTabla.setRowCount(0);
    
    List<Persona> usuarios = personaDAO.obtenerTodos();
    
    for (Persona persona : usuarios) {
        Object[] fila = {
            persona.getIdPersona(),
            persona.getCedula(),
            persona.getNombre(),
            persona.getApellido(),
            persona.getMail(),
            persona.getDireccion(),
            persona.getTipo(),
            persona.getUsuario(),
            persona.isActivo() ? "Sí" : "No"
        };
        modeloTabla.addRow(fila);
    }
    
    System.out.println("Usuarios cargados: " + usuarios.size());
}

/**
 * Buscar usuarios por nombre o apellido
 */
private void buscarUsuarios(String texto) {
    modeloTabla.setRowCount(0);
    
    if (texto.trim().isEmpty()) {
        cargarUsuarios();
        return;
    }
    
    List<Persona> todosUsuarios = personaDAO.obtenerTodos();
    
    // Filtrar localmente
    for (Persona persona : todosUsuarios) {
        String nombreCompleto = (persona.getNombre() + " " + persona.getApellido()).toLowerCase();
        if (nombreCompleto.contains(texto.toLowerCase()) || 
            persona.getCedula().contains(texto)) {
            
            Object[] fila = {
                persona.getIdPersona(),
                persona.getCedula(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getMail(),
                persona.getDireccion(),
                persona.getTipo(),
                persona.getUsuario(),
                persona.isActivo() ? "Sí" : "No"
            };
            modeloTabla.addRow(fila);
        }
    }
}

/**
 * Obtener usuario seleccionado
 */
private Persona obtenerUsuarioSeleccionado() {
    int fila = jTable1.getSelectedRow();
    
    if (fila == -1) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione un usuario de la tabla",
            "Ningún Usuario Seleccionado",
            JOptionPane.WARNING_MESSAGE);
        return null;
    }
    
    int id = (int) modeloTabla.getValueAt(fila, 0);
    return personaDAO.buscarPorId(id);
}
 private void eliminarUsuario() {
       
    Persona persona = obtenerUsuarioSeleccionado();
    
    if (persona == null) {
        return;
    }
    
    // Verificar si es el único administrador
    if ("ADMINISTRADOR".equalsIgnoreCase(persona.getTipo())) {
        List<Persona> todos = personaDAO.obtenerTodos();
        long adminActivos = todos.stream()
            .filter(p -> "ADMINISTRADOR".equalsIgnoreCase(p.getTipo()) && p.isActivo())
            .count();
        
        if (adminActivos <= 1) {
            JOptionPane.showMessageDialog(this,
                "NO SE PUEDE ELIMINAR\n\n" +
                "No puedes eliminar el único administrador activo del sistema.",
                "Operación no permitida",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    // Confirmar eliminación (soft delete)
    String mensaje = String.format(
        "¿Está seguro de que desea eliminar este usuario?\n\n" +
        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
        "Usuario: %s\n" +
        "Nombre: %s %s\n" +
        "Cédula: %s\n" +
        "Tipo: %s\n" +
        "========================\n\n" +
        "El usuario no podrá iniciar sesión pero sus registros\n" +
        "permanecerán en el sistema para historial.",
        persona.getUsuario(),
        persona.getNombre(),
        persona.getApellido(),
        persona.getCedula(),
        persona.getTipo()
    );
    
    int confirmacion = JOptionPane.showConfirmDialog(this,
        mensaje,
        "Confirmar Eliminacion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    
    if (confirmacion == JOptionPane.YES_OPTION) {
        try {
           
            
            boolean exito = personaDAO.eliminar(persona.getIdPersona());
            
            if (exito) {
              
                
                JOptionPane.showMessageDialog(this,
                    "uSUARIO ELIMINADO EXITOSAMENTE\n\n" +
                    "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                    "El usuario '" + persona.getUsuario() + "' ha sido marcado como inactivo.\n" +
                    ".........................",
                    "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                cargarUsuarios();
                jTable1.clearSelection();
            } else 
            {
                
                JOptionPane.showMessageDialog(this,
                    "ERROR AL ELIMINAR\n\n" +
                    "No se pudo eliminar el usuario.\n" +
                    "Vereficar la conexion a la base de datos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Excepción al eliminar: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(this,
                "ERROR AL ELIMINAR\n\n" +
                "Ocurrio un error al eliminar el usuario:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    } else {
        System.out.println("ℹ️ Eliminación cancelada");
    }
}
        
        
           
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        bg = new javax.swing.JPanel();
        paleta1 = new javax.swing.JLabel();
        titulo4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        boton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        botonborrar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(706, 457));

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setForeground(new java.awt.Color(204, 0, 51));
        bg.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        paleta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/paleta.png"))); // NOI18N
        paleta1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 78, 1, 56, new java.awt.Color(0, 0, 0)));
        paleta1.setMaximumSize(new java.awt.Dimension(300, 256));
        paleta1.setMinimumSize(new java.awt.Dimension(300, 256));
        paleta1.setPreferredSize(new java.awt.Dimension(300, 200));
        bg.add(paleta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 410, 10));

        titulo4.setBackground(new java.awt.Color(0, 0, 0));
        titulo4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 28)); // NOI18N
        titulo4.setForeground(new java.awt.Color(204, 0, 51));
        titulo4.setText("Panel Usuario");
        bg.add(titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 220, 40));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null,  new Boolean(true)},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Apellido", "Cedula", "Mail", "Dirección", "Tipo", "Usuario", "Contraseña", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setGridColor(new java.awt.Color(0, 102, 204));
        jTable1.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jTable1.setSelectionForeground(new java.awt.Color(204, 255, 255));
        jTable1.setShowGrid(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        bg.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 650, 220));

        boton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono.png"))); // NOI18N
        boton3.setText("jButton1");
        bg.add(boton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 60, 90, 60));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(153, 153, 153));
        jTextField1.setText("Busqueda de Usuario");
        jTextField1.setBorder(null);
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jTextField1.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        bg.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 500, 30));

        jSeparator1.setBackground(new java.awt.Color(0, 51, 153));
        jSeparator1.setForeground(new java.awt.Color(0, 102, 153));
        bg.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 113, 500, 10));

        botonborrar.setBackground(new java.awt.Color(0, 0, 0));
        botonborrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono4_1.png"))); // NOI18N
        botonborrar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        botonborrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botonborrar.addActionListener(this::botonborrarActionPerformed);
        bg.add(botonborrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 360, 80, 50));

        jLabel3.setFont(new java.awt.Font("STZhongsong", 1, 18)); // NOI18N
        jLabel3.setText("Borrar");
        bg.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 420, 70, 20));

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

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        // TODO add your handling code here:
        if (jTextField1.getText().equals("Busqueda de Usuario")) {
    jTextField1.setText("");
    jTextField1.setForeground(java.awt.Color.BLACK); // El texto se vuelve negro al escribir
}
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // TODO add your handling code here:
        if (jTextField1.getText().isEmpty()) {
    jTextField1.setForeground(new java.awt.Color(153, 153, 153));
    jTextField1.setText("Busqueda de Usuario");
}
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed
// Esto hace que al tocar el panel, la tabla pierda el foco y se limpie la selección visual
jTable1.clearSelection(); 
this.requestFocusInWindow();        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MousePressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String texto = jTextField1.getText();
    buscarUsuarios(texto);
    }//GEN-LAST:event_jTextField1KeyReleased

    private void botonborrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonborrarActionPerformed
         eliminarUsuario();
    }//GEN-LAST:event_botonborrarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton boton3;
    private javax.swing.JButton botonborrar;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel paleta1;
    private javax.swing.JLabel titulo4;
    // End of variables declaration//GEN-END:variables

    
}
