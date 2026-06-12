package com.mycompany.views;

import java.awt.Image;
import javax.swing.ImageIcon;
import com.mycompany.biblioteca_digital.modelo.Libro;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;


public class vista5 extends javax.swing.JPanel {

    private com.mycompany.biblioteca_digital.servicio.LibroOpciones libroDAO;
    private DefaultTableModel modeloTabla;
     private JPanel panelContenedor;
    private CardLayout cardLayout;
    
    public vista5() {
        initComponents();
     inicializar();
    }
    public vista5(JPanel panelContenedor, CardLayout cardLayout) {
          initComponents();
        
        // Guardar referencias
        this.panelContenedor = panelContenedor;
        this.cardLayout = cardLayout;
        
        // SEGUNDO: Inicializar logica
        inicializar();
    }
    private void inicializar() {
        
        try {
            // Inicializar DAO
            libroDAO = com.mycompany.biblioteca_digital.servicio.AppContext.getInstance().getLibroOpciones();
            
            // Configurar tabla
            configurarTabla();
            
            // Cargar libros
            cargarLibros();
            
            // Configurar tabla no editable
            jTable2.setFocusable(false);
            jTable2.setCellSelectionEnabled(false);
            
            // Configurar iconos
            try {
                boton8.setIcon(new javax.swing.ImageIcon(
                    new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono.png"))
                        .getImage().getScaledInstance(120, 110, java.awt.Image.SCALE_SMOOTH)));
                boton8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            } catch (Exception e) {
                System.out.println("No se pudo cargar icono boton8");
            }

            try {
                botonnuevo1.setIcon(new javax.swing.ImageIcon(
                    new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono2.png"))
                        .getImage().getScaledInstance(80, 50, java.awt.Image.SCALE_SMOOTH)));
                botonnuevo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            } catch (Exception e) {
                System.out.println("No se pudo cargar icono botonnuevo1");
            }

            try {
                botoneditar1.setIcon(new javax.swing.ImageIcon(
                    new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono3_1.png"))
                        .getImage().getScaledInstance(80, 50, java.awt.Image.SCALE_SMOOTH)));
                botoneditar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            } catch (Exception e) {
                System.out.println("No se pudo cargar icono botoneditar1");
            }
            
            try {
                botonborrar1.setIcon(new javax.swing.ImageIcon(
                    new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono4_1.png"))
                        .getImage().getScaledInstance(80, 50, java.awt.Image.SCALE_SMOOTH)));
                botonborrar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            } catch (Exception e) {
                System.out.println("No se pudo cargar icono botonborrar1");
            }
            
            System.out.println("Panel Libros inicializado correctamente");
            
        } catch (Exception e) {
            System.err.println("Error al inicializar Panel Libros: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
   private void configurarIconos() {
        boton8.setIcon(new ImageIcon(
            new ImageIcon(getClass().getResource("/imagenes/icono.png"))
                .getImage().getScaledInstance(120, 110, Image.SCALE_SMOOTH)));

        botonnuevo1.setIcon(new ImageIcon(
            new ImageIcon(getClass().getResource("/imagenes/icono2.png"))
                .getImage().getScaledInstance(80, 50, Image.SCALE_SMOOTH)));

        botoneditar1.setIcon(new ImageIcon(
            new ImageIcon(getClass().getResource("/imagenes/icono3_1.png"))
                .getImage().getScaledInstance(80, 50, Image.SCALE_SMOOTH)));
        
        botonborrar1.setIcon(new ImageIcon(
            new ImageIcon(getClass().getResource("/imagenes/icono4_1.png"))
                .getImage().getScaledInstance(80, 50, Image.SCALE_SMOOTH)));
    }
    
  /**
 * Configurar columnas de la tabla
 */
private void configurarTabla() {
    // Crear modelo de tabla
    String[] columnas = {"ID", "ISBN", "Titulo", "Autor", "Editorial", 
                        "Año", "Categoría", "Cantidad Total", "Disponible"};
    modeloTabla = new DefaultTableModel(columnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // No editable
        }
    };
    
    jTable2.setModel(modeloTabla);
    
    // Ajustar ancho de columnas
    jTable2.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
    jTable2.getColumnModel().getColumn(1).setPreferredWidth(120); // ISBN
    jTable2.getColumnModel().getColumn(2).setPreferredWidth(200); // Titulo
    jTable2.getColumnModel().getColumn(3).setPreferredWidth(150); // Autor
    jTable2.getColumnModel().getColumn(4).setPreferredWidth(120); // Editorial
    jTable2.getColumnModel().getColumn(5).setPreferredWidth(60);  // Año
    jTable2.getColumnModel().getColumn(6).setPreferredWidth(100); // Categoría
    jTable2.getColumnModel().getColumn(7).setPreferredWidth(80);  // Total
    jTable2.getColumnModel().getColumn(8).setPreferredWidth(80);  // Disponible
}

/**
 * Cargar todos los libros en la tabla
 */
public void cargarLibros() {
    // Limpiar tabla
    modeloTabla.setRowCount(0);
    
    // Obtener libros de la BD
    List<Libro> libros = libroDAO.obtenerTodosLosLibros();
    
    // Llenar tabla
    for (Libro libro : libros) {
        Object[] fila = {
            libro.getIdLibro(),
            libro.getIsbn(),
            libro.getTitulo(),
            libro.getAutor(),
            libro.getEditorial(),
            libro.getAño(),
            libro.getCategoria(),
            libro.getCantidadTotal(),
            libro.getCantidadDisponible()
        };
        modeloTabla.addRow(fila);
    }
   
}

/**
 * Buscar libros por título
 */
private void buscarLibros(String texto) {
    // Limpiar tabla
    modeloTabla.setRowCount(0);
    
    if (texto.trim().isEmpty()) {
        // Si está vacio, cargar todos
        cargarLibros();
        return;
    }
    
    // Buscar en BD
    List<Libro> libros = libroDAO.buscarPorTitulo(texto);
    
    // Llenar tabla
    for (Libro libro : libros) {
        Object[] fila = {
            libro.getIdLibro(),
            libro.getIsbn(),
            libro.getTitulo(),
            libro.getAutor(),
            libro.getEditorial(),
            libro.getAño(),
            libro.getCategoria(),
            libro.getCantidadTotal(),
            libro.getCantidadDisponible()
        };
        modeloTabla.addRow(fila);
    }

}

/**
 * Obtener libro seleccionado
 */
private Libro obtenerLibroSeleccionado() {
    int filaSeleccionada = jTable2.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this,
            "Por favor seleccione un libro de la tabla",
            "Ningun Libro Seleccionado",
            JOptionPane.WARNING_MESSAGE);
        return null;
    }
    
    // Obtener ID del libro
    int idLibro = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
    
    // Buscar el libro completo
    return libroDAO.buscarPorId(idLibro);
}
private void nuevoLibro() {
        if (panelContenedor != null && cardLayout != null) {
            cardLayout.show(panelContenedor, "registroL");
        } else {
            JOptionPane.showMessageDialog(this,
                "No se puede abrir el formulario de registro",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }}
        private void editarLibro() {
        Libro libro = obtenerLibroSeleccionado();
        if (libro == null) return;
        
        JPanel panel = new JPanel(new java.awt.GridLayout(8, 2, 10, 10));
        
        javax.swing.JTextField txtISBN = new javax.swing.JTextField(libro.getIsbn());
        javax.swing.JTextField txtTitulo = new javax.swing.JTextField(libro.getTitulo());
        javax.swing.JTextField txtAutor = new javax.swing.JTextField(libro.getAutor());
        javax.swing.JTextField txtEditorial = new javax.swing.JTextField(libro.getEditorial());
        javax.swing.JTextField txtAño = new javax.swing.JTextField(String.valueOf(libro.getAño()));
        javax.swing.JTextField txtCategoria = new javax.swing.JTextField(libro.getCategoria());
        javax.swing.JTextField txtUbicacion = new javax.swing.JTextField(libro.getUbicacion());
        javax.swing.JTextField txtCantidad = new javax.swing.JTextField(String.valueOf(libro.getCantidadTotal()));
        
        panel.add(new javax.swing.JLabel("ISBN:"));
        panel.add(txtISBN);
        panel.add(new javax.swing.JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new javax.swing.JLabel("Autor:"));
        panel.add(txtAutor);
        panel.add(new javax.swing.JLabel("Editorial:"));
        panel.add(txtEditorial);
        panel.add(new javax.swing.JLabel("Año:"));
        panel.add(txtAño);
        panel.add(new javax.swing.JLabel("Categoría:"));
        panel.add(txtCategoria);
        panel.add(new javax.swing.JLabel("Ubicación:"));
        panel.add(txtUbicacion);
        panel.add(new javax.swing.JLabel("Cantidad:"));
        panel.add(txtCantidad);
        
        int resultado = JOptionPane.showConfirmDialog(this, panel, 
            "Editar Libro", JOptionPane.OK_CANCEL_OPTION);
        
        if (resultado == JOptionPane.OK_OPTION) {
            try {
                if (txtTitulo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El titulo no puede estar vacio", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                libro.setIsbn(txtISBN.getText().trim());
                libro.setTitulo(txtTitulo.getText().trim());
                libro.setAutor(txtAutor.getText().trim());
                libro.setEditorial(txtEditorial.getText().trim());
                libro.setAño(Integer.parseInt(txtAño.getText().trim()));
                libro.setCategoria(txtCategoria.getText().trim());
                libro.setUbicacion(txtUbicacion.getText().trim());
                libro.setCantidadTotal(Integer.parseInt(txtCantidad.getText().trim()));
                
                if (libroDAO.actualizar(libro)) {
                    JOptionPane.showMessageDialog(this, "Libro actualizado exitosamente", "Exito", JOptionPane.INFORMATION_MESSAGE);
                    cargarLibros();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar el libro", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarLibro() {
     Libro libro = obtenerLibroSeleccionado();
        
        if (libro == null) {
            return;
        }
        
        int prestamosActivos = libro.getCantidadTotal() - libro.getCantidadDisponible();
        
        if (prestamosActivos > 0) {
            JOptionPane.showMessageDialog(this,
                "NO SE PUEDE ELIMINAR\n\n" +
                "Este libro tiene " + prestamosActivos + " préstamo(s) activo(s).",
                "Operación No Permitida",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Eliminar este libro?\n\n %s\n✍️ %s",
                libro.getTitulo(), libro.getAutor()),
            "Confirmar EliminaciOn",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                if (libroDAO.eliminar(libro.getIdLibro())) {
                    System.out.println("Libro eliminado");
                    
                    JOptionPane.showMessageDialog(this,
                        "Libro eliminado correctamente",
                        "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    cargarLibros();
                    jTable2.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el libro",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(this,
                    "Error:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        bg = new javax.swing.JPanel();
        paleta1 = new javax.swing.JLabel();
        titulo4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        boton8 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        botonnuevo1 = new javax.swing.JButton();
        botoneditar1 = new javax.swing.JButton();
        botonborrar1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

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
        bg.add(paleta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 420, 10));

        titulo4.setBackground(new java.awt.Color(0, 0, 0));
        titulo4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 28)); // NOI18N
        titulo4.setForeground(new java.awt.Color(204, 0, 51));
        titulo4.setText("Panel libros");
        bg.add(titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 190, 40));

        jTable2.setBackground(new java.awt.Color(240, 240, 240));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
                "ID", "ISBN", "Titulo", "Autor", "Editorial", "Año", "Categoria", "Cantidad Total", "Cantidad libre", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable2.setGridColor(new java.awt.Color(0, 102, 204));
        jTable2.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable2.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jTable2.setShowGrid(true);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jTable2.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                jTable2ComponentMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(9).setMinWidth(30);
            jTable2.getColumnModel().getColumn(9).setMaxWidth(18);
        }

        bg.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 670, 220));

        boton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono.png"))); // NOI18N
        boton8.setText("jButton1");
        boton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bg.add(boton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 60, 90, 60));

        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(153, 153, 153));
        jTextField1.setText("Busqueda de Libros");
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

        botonnuevo1.setBackground(new java.awt.Color(0, 0, 0));
        botonnuevo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono2.png"))); // NOI18N
        botonnuevo1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonnuevo1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botonnuevo1.addActionListener(this::botonnuevo1ActionPerformed);
        bg.add(botonnuevo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 360, 80, 50));

        botoneditar1.setBackground(new java.awt.Color(0, 0, 0));
        botoneditar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono3_1.png"))); // NOI18N
        botoneditar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 153), new java.awt.Color(0, 51, 153), new java.awt.Color(0, 51, 153), new java.awt.Color(0, 51, 153)));
        botoneditar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botoneditar1.addActionListener(this::botoneditar1ActionPerformed);
        bg.add(botoneditar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 360, 80, 50));

        botonborrar1.setBackground(new java.awt.Color(0, 0, 0));
        botonborrar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono4_1.png"))); // NOI18N
        botonborrar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));
        botonborrar1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botonborrar1.addActionListener(this::botonborrar1ActionPerformed);
        bg.add(botonborrar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 360, 80, 50));

        jLabel1.setFont(new java.awt.Font("STZhongsong", 1, 18)); // NOI18N
        jLabel1.setText("Nuevo");
        bg.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 420, 60, 20));

        jLabel2.setFont(new java.awt.Font("STZhongsong", 1, 18)); // NOI18N
        jLabel2.setText("Editar");
        bg.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 420, 70, 20));

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
        if (jTextField1.getText().equals("Busqueda de Libros")) {
    jTextField1.setText("");
    jTextField1.setForeground(java.awt.Color.BLACK); // El texto se vuelve negro al escribir
}
    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // TODO add your handling code here:
        if (jTextField1.getText().isEmpty()) {
    jTextField1.setForeground(new java.awt.Color(153, 153, 153));
    jTextField1.setText("Busqueda de Libros");
}
    }//GEN-LAST:event_jTextField1FocusLost

    private void botoneditar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botoneditar1ActionPerformed
 editarLibro();     
    }//GEN-LAST:event_botoneditar1ActionPerformed
    
    private void botonnuevo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonnuevo1ActionPerformed
     nuevoLibro();
    
    }//GEN-LAST:event_botonnuevo1ActionPerformed

    private void jTable2ComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable2ComponentMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2ComponentMoved

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable2MouseClicked

    private void bgMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bgMousePressed
// Esto hace que al tocar el panel, la tabla pierda el foco y se limpie la selección visual
jTable2.clearSelection(); 
this.requestFocusInWindow();        // TODO add your handling code here:
    }//GEN-LAST:event_bgMousePressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
      buscarLibros(jTextField1.getText());
    }//GEN-LAST:event_jTextField1KeyReleased

    private void botonborrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonborrar1ActionPerformed
       eliminarLibro();
    }//GEN-LAST:event_botonborrar1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton boton8;
    private javax.swing.JButton botonborrar1;
    private javax.swing.JButton botoneditar1;
    private javax.swing.JButton botonnuevo1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel paleta1;
    private javax.swing.JLabel titulo4;
    // End of variables declaration//GEN-END:variables

    
}
