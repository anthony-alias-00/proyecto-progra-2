
package com.mycompany.views;

import com.mycompany.biblioteca_digital.modelo.Persona;
import com.mycompany.biblioteca_digital.modelo.ReservaSala;
import com.mycompany.biblioteca_digital.servicio.SalaControl;
import com.mycompany.biblioteca_digital.servicio.AppContext;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

public class vista7 extends javax.swing.JPanel {

    private SalaControl salaControl;
    private Persona personaActual;

    private DefaultTableModel modeloActivas;
    private DefaultTableModel modeloHistorial;

    private JPanel panelContenedor;
    private CardLayout cardLayout;

    // índice 0 = Sala 1 (amarilla/jPanel4), 1 = Sala 2 (rosada/jPanel2),
    // 2 = Sala 3 (verde/jPanel1), 3 = Sala 4 (azul/jPanel3)
    private JPanel[] panelesSalas;
    private JLabel[] labelsDisponible;

    private static final java.time.format.DateTimeFormatter FORMATO_HORA =
            java.time.format.DateTimeFormatter.ofPattern("HH:mm");

    // ---------- Constructores ----------

    public vista7() {
        initComponents();
        inicializar();
    }

    public vista7(JPanel panelContenedor, CardLayout cardLayout) {
        initComponents();
        this.panelContenedor = panelContenedor;
        this.cardLayout = cardLayout;
        inicializar();
    }

    // Constructor que recibe al usuario logueado (igual que vista2, vista4, vista6)
    public vista7(Persona usuario) {
        initComponents();
        this.personaActual = usuario;
        inicializar();
    }

    public vista7(Persona usuario, JPanel panelContenedor, CardLayout cardLayout) {
        initComponents();
        this.personaActual = usuario;
        this.panelContenedor = panelContenedor;
        this.cardLayout = cardLayout;
        inicializar();
    }

    // ---------- Inicialización ----------

    private void inicializar() {
        try {
            salaControl = AppContext.getInstance().getSalaControl();

            panelesSalas = new JPanel[]{ jPanel4, jPanel2, jPanel1, jPanel5,jPanel3 };
            labelsDisponible = new JLabel[]{ jLabel8, jLabel9, jLabel10, jLabel13,jLabel11 };

            configurarTablas();

            jTable2.setFocusable(false);
            jTable2.setCellSelectionEnabled(false);
            jTable3.setFocusable(false);
            jTable3.setCellSelectionEnabled(false);

            try {
                botonnuevo1.setIcon(new javax.swing.ImageIcon(
                    new javax.swing.ImageIcon(getClass().getResource("/imagenes/reservar.png"))
                        .getImage().getScaledInstance(80, 50, java.awt.Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                System.out.println("No se pudo cargar icono botonnuevo1");
            }
            try {
                botonliberar.setIcon(new javax.swing.ImageIcon(
                    new javax.swing.ImageIcon(getClass().getResource("/imagenes/liberar.png"))
                        .getImage().getScaledInstance(80, 36, java.awt.Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                System.out.println("No se pudo cargar icono botonliberar");
            }
         

            actualizarVista();

            System.out.println("Panel Salas de Lectura (vista7) inicializado correctamente");

        } catch (Exception e) {
            System.err.println("Error al inicializar Panel Salas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Configura las columnas de jTable2 (activas) y jTable3 (historial). */
    private void configurarTablas() {
        String[] columnas = {"ID", "Sala", "Usuario", "Fecha", "Hora entrada", "Estado"};

        modeloActivas = new DefaultTableModel(columnas, 0) {
            Class[] tipos = new Class[]{Integer.class, Integer.class, String.class, String.class, String.class, Boolean.class};
            @Override public Class getColumnClass(int col) { return tipos[col]; }
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        jTable2.setModel(modeloActivas);

        modeloHistorial = new DefaultTableModel(columnas, 0) {
            Class[] tipos = new Class[]{Integer.class, Integer.class, String.class, String.class, String.class, Boolean.class};
            @Override public Class getColumnClass(int col) { return tipos[col]; }
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        jTable3.setModel(modeloHistorial);
    }

    /** Refresca las dos tablas y los 4 paneles de color. */
    public void actualizarVista() {
        cargarReservasActivas();
        cargarHistorial();
        actualizarPaneles();
    }

    private void cargarReservasActivas() {
        modeloActivas.setRowCount(0);
        for (ReservaSala r : salaControl.obtenerTodasReservasActivas()) {
            modeloActivas.addRow(new Object[]{
                r.getIdReserva(),
                r.getNumeroSala(),
                nombreParaTabla(r.getPersona()),
                r.getFecha(),
                r.getHoraEntrada().format(FORMATO_HORA),
                r.estaActiva()
            });
        }
    }

    private void cargarHistorial() {
        modeloHistorial.setRowCount(0);
        for (ReservaSala r : salaControl.obtenerHistorial()) {
            modeloHistorial.addRow(new Object[]{
                r.getIdReserva(),
                r.getNumeroSala(),
                nombreParaTabla(r.getPersona()),
                r.getFecha(),
                r.getHoraEntrada().format(FORMATO_HORA),
                r.estaActiva()
            });
        }
    }

    /** Devuelve "Admin" si es administrador, o el nombre completo si es estudiante. */
    private String nombreParaTabla(Persona persona) {
        if (persona == null) return "N/A";
        if ("ADMINISTRADOR".equalsIgnoreCase(persona.getTipo())) {
            return "Admin";
        }
        return persona.getNombre() + " " + persona.getApellido();
    }

    /** Pinta cada panel de sala: DISPONIBLE u OCUPADA - Nombre. */
    private void actualizarPaneles() {
        for (int sala = 1; sala <= panelesSalas.length; sala++) {
            JLabel labelDisp = labelsDisponible[sala - 1];
            if (salaControl.salaTieneReservaActiva(sala)) {
                ReservaSala r = salaControl.obtenerReservaActivaDeSala(sala);
                String texto;
                if (r.getPersona() != null && "ADMINISTRADOR".equalsIgnoreCase(r.getPersona().getTipo())) {
                    texto = "Admin";
                } else {
                    texto = (r.getPersona() != null) ? r.getPersona().getNombre() : "?";
                }
                labelDisp.setText("OCUPADA - " + texto);
            } else {
                labelDisp.setText("DISPONIBLE");
            }
        }
    }

    /** Acción del botón Reservar. */
    private void reservarSalaAccion() {
        if (personaActual == null) {
            JOptionPane.showMessageDialog(this,
                "No se pudo identificar al usuario logueado.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Integer> disponibles = new ArrayList<>();
        for (int sala = 1; sala <= panelesSalas.length; sala++) {
            if (!salaControl.salaTieneReservaActiva(sala)) {
                disponibles.add(sala);
            }
        }

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay salas disponibles en este momento.",
                "Sin disponibilidad", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer[] opciones = disponibles.toArray(new Integer[0]);
        Integer salaElegida = (Integer) JOptionPane.showInputDialog(
                this,
                "Selecciona la sala que deseas reservar:",
                "Reservar sala",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (salaElegida == null) return;

        ReservaSala reserva = salaControl.reservarSala(personaActual, salaElegida);

        if (reserva != null) {
            JOptionPane.showMessageDialog(this,
                "Sala " + salaElegida + " reservada exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarVista();
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo reservar la sala.\nVerifica el horario (07:00-18:00),\n" +
                "que la sala siga libre, y que no tengas ya otra reserva activa.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Acción del botón Liberar. Usa la fila seleccionada en jTable2. */
    private void liberarSalaAccion() {
        if (personaActual == null || !"ADMINISTRADOR".equalsIgnoreCase(personaActual.getTipo())) {
            JOptionPane.showMessageDialog(this,
                "Solo un administrador puede liberar una sala.",
                "Acción no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int fila = jTable2.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecciona una reserva activa de la tabla para liberar.",
                "Ninguna reserva seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idReserva = (int) modeloActivas.getValueAt(fila, 0);

        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Confirmas que deseas liberar la sala " + modeloActivas.getValueAt(fila, 1) + "?",
            "Confirmar liberación", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) return;

        boolean ok = salaControl.liberarSala(idReserva);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Sala liberada exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            actualizarVista();
        } else {
            JOptionPane.showMessageDialog(this,
                "No se pudo liberar la sala. La reserva ya no existe.",
                "Error", JOptionPane.ERROR_MESSAGE);
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
        botonnuevo1 = new javax.swing.JButton();
        botonliberar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        titulo5 = new javax.swing.JLabel();
        titulo6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jSeparator4 = new javax.swing.JSeparator();
        titulo7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

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
        bg.add(paleta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 310, 10));

        titulo4.setBackground(new java.awt.Color(0, 0, 0));
        titulo4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        titulo4.setText("Estado de salas");
        bg.add(titulo4, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, 130, 40));

        jTable2.setBackground(new java.awt.Color(240, 240, 240));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null,  new Boolean(true)},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Sala", "Usuario", "Fecha", "Hora entrada", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
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
            jTable2.getColumnModel().getColumn(0).setHeaderValue("ID");
            jTable2.getColumnModel().getColumn(1).setHeaderValue("Sala");
            jTable2.getColumnModel().getColumn(2).setHeaderValue("Usuario");
            jTable2.getColumnModel().getColumn(3).setHeaderValue("Fecha");
            jTable2.getColumnModel().getColumn(4).setHeaderValue("Hora entrada");
            jTable2.getColumnModel().getColumn(5).setHeaderValue("Estado");
        }

        bg.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 450, 140));

        botonnuevo1.setBackground(new java.awt.Color(0, 0, 0));
        botonnuevo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono2.png"))); // NOI18N
        botonnuevo1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonnuevo1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botonnuevo1.addActionListener(this::botonnuevo1ActionPerformed);
        bg.add(botonnuevo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 420, 80, 30));

        botonliberar.setBackground(new java.awt.Color(0, 0, 0));
        botonliberar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/icono3_1.png"))); // NOI18N
        botonliberar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 51, 153), new java.awt.Color(0, 51, 153), new java.awt.Color(0, 51, 153), new java.awt.Color(0, 51, 153)));
        botonliberar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botonliberar.addActionListener(this::botonliberarActionPerformed);
        bg.add(botonliberar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 420, 80, 30));

        jLabel1.setFont(new java.awt.Font("STZhongsong", 1, 18)); // NOI18N
        jLabel1.setText("Reservar");
        bg.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 430, 100, 20));

        jLabel2.setFont(new java.awt.Font("STZhongsong", 1, 18)); // NOI18N
        jLabel2.setText("Liberar");
        bg.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, 70, 20));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        bg.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 450, 10));

        titulo5.setBackground(new java.awt.Color(0, 0, 0));
        titulo5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 28)); // NOI18N
        titulo5.setForeground(new java.awt.Color(204, 0, 51));
        titulo5.setText("Panel Salas de Lectura");
        bg.add(titulo5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 360, 40));

        titulo6.setBackground(new java.awt.Color(0, 0, 0));
        titulo6.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        titulo6.setText("Historial reservas");
        bg.add(titulo6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 360, 30));

        jTable3.setBackground(new java.awt.Color(240, 240, 240));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null,  new Boolean(true)},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Sala", "Usuario", "Fecha", "Hora entrada", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
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
        jScrollPane2.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setHeaderValue("ID");
            jTable3.getColumnModel().getColumn(1).setHeaderValue("Sala");
            jTable3.getColumnModel().getColumn(2).setHeaderValue("Usuario");
            jTable3.getColumnModel().getColumn(3).setHeaderValue("Fecha");
            jTable3.getColumnModel().getColumn(4).setHeaderValue("Hora entrada");
            jTable3.getColumnModel().getColumn(5).setHeaderValue("Estado");
        }

        bg.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 450, 140));

        jSeparator4.setBackground(new java.awt.Color(0, 51, 153));
        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        bg.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 450, 10));

        titulo7.setBackground(new java.awt.Color(0, 0, 0));
        titulo7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        titulo7.setText("Reservas activas");
        bg.add(titulo7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 360, 40));

        jPanel1.setBackground(new java.awt.Color(102, 204, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setText("Sala 3");

        jLabel10.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10.setFont(new java.awt.Font("UD Digi Kyokasho NK", 1, 12)); // NOI18N
        jLabel10.setText("DISPONIBLE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel6)
                .addContainerGap(76, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addContainerGap())
        );

        bg.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 230, 180, 60));

        jPanel2.setBackground(new java.awt.Color(255, 51, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setText("Sala 2");

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("UD Digi Kyokasho NK", 1, 12)); // NOI18N
        jLabel9.setText("DISPONIBLE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel5)
                .addContainerGap(76, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addContainerGap())
        );

        bg.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 160, 180, 60));

        jPanel4.setBackground(new java.awt.Color(255, 255, 51));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setText("Sala 1");

        jLabel8.setBackground(new java.awt.Color(0, 0, 0));
        jLabel8.setFont(new java.awt.Font("UD Digi Kyokasho NK", 1, 12)); // NOI18N
        jLabel8.setText("DISPONIBLE");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel4)
                .addContainerGap(73, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addContainerGap())
        );

        bg.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 90, 180, 60));

        jPanel5.setBackground(new java.awt.Color(0, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel12.setText("Sala 4");

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setFont(new java.awt.Font("UD Digi Kyokasho NK", 1, 12)); // NOI18N
        jLabel13.setText("DISPONIBLE");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel12)
                .addContainerGap(76, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bg.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, 180, 60));

        jPanel3.setBackground(new java.awt.Color(204, 0, 204));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel7.setText("Sala 5");

        jLabel11.setBackground(new java.awt.Color(0, 0, 0));
        jLabel11.setFont(new java.awt.Font("UD Digi Kyokasho NK", 1, 12)); // NOI18N
        jLabel11.setText("DISPONIBLE");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel7)
                .addContainerGap(71, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
                .addContainerGap())
        );

        bg.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 370, 180, 60));

        jScrollPane3.setViewportView(bg);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonliberarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonliberarActionPerformed
 liberarSalaAccion();     
    }//GEN-LAST:event_botonliberarActionPerformed
    
    private void botonnuevo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonnuevo1ActionPerformed
     reservarSalaAccion();
    
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

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTable3ComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jTable3ComponentMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3ComponentMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bg;
    private javax.swing.JButton botonliberar;
    private javax.swing.JButton botonnuevo1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel paleta1;
    private javax.swing.JLabel titulo4;
    private javax.swing.JLabel titulo5;
    private javax.swing.JLabel titulo6;
    private javax.swing.JLabel titulo7;
    // End of variables declaration//GEN-END:variables

    
}
