package com.mycompany.biblioteca_digital.views1;

import com.mycompany.biblioteca_digital.base_datos.PersonaDAO;
import com.mycompany.biblioteca_digital.modelo.Usuario;
import javax.swing.JOptionPane;

public class registroU1 extends javax.swing.JPanel {

    
    public registroU1() {
        initComponents();
        imagen9.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/imagen6.png")).getImage().getScaledInstance(280, 220, java.awt.Image.SCALE_SMOOTH)));

// 2. Forzar el centrado horizontal dentro del espacio del Label
imagen9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        forma3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        forma4 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        forma5 = new javax.swing.JLabel();
        forma6 = new javax.swing.JLabel();
        forma7 = new javax.swing.JLabel();
        txtConfirmarContraseña = new javax.swing.JPasswordField();
        btnCancelar = new javax.swing.JButton();
        txtContraseña = new javax.swing.JPasswordField();
        forma21 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        forma = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        forma1 = new javax.swing.JLabel();
        txtMail = new javax.swing.JTextField();
        forma2 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        imagen9 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        forma3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        forma3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        forma3.setText("Correo:");

        forma4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        forma4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        forma4.setText("Dirección:");

        txtCedula.setForeground(new java.awt.Color(153, 153, 153));
        txtCedula.addActionListener(this::txtCedulaActionPerformed);

        forma5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        forma5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        forma5.setText("Usuario:");

        forma6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        forma6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        forma6.setText("Constraseña:");

        forma7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        forma7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        forma7.setText("Confirmar Contraseña:");

        btnCancelar.setBackground(new java.awt.Color(0, 51, 102));
        btnCancelar.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.setBorder(null);
        btnCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        txtContraseña.addActionListener(this::txtContraseñaActionPerformed);

        forma21.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        forma21.setForeground(new java.awt.Color(153, 0, 51));
        forma21.setText("Registro Nuevo Usuario");

        forma.setBackground(new java.awt.Color(255, 255, 255));
        forma.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        forma.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        forma.setText("Cédula:");

        forma1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        forma1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        forma1.setText("Nombre:");

        forma2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        forma2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        forma2.setText("Apellido:");

        txtApellido.addActionListener(this::txtApellidoActionPerformed);

        imagen9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/imagen6.png"))); // NOI18N

        btnRegistrar.setBackground(new java.awt.Color(0, 51, 102));
        btnRegistrar.setFont(new java.awt.Font("Segoe UI Historic", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setText("Registrar");
        btnRegistrar.setBorder(null);
        btnRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(this::btnRegistrarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(forma21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(67, 67, 67))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(imagen9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(80, 80, 80))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(forma7, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtConfirmarContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(66, 66, 66))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(forma, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(forma1)
                        .addGap(19, 19, 19)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(forma2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(forma3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(forma4, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(forma5, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(forma6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(txtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(forma21, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addGap(19, 19, 19)
                .addComponent(imagen9, javax.swing.GroupLayout.PREFERRED_SIZE, 195, Short.MAX_VALUE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forma)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forma1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forma2)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forma3)
                    .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forma4)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forma5)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forma6)
                    .addComponent(txtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(forma7)
                    .addComponent(txtConfirmarContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
 // Limpiar formulario
    limpiarFormulario();
    
    // Volver al panel de login
    volverAlLogin();
    
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseñaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContraseñaActionPerformed

    private void txtApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtApellidoActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
    // Obtener datos
    String cedula = txtCedula.getText().trim();
    String nombre = txtNombre.getText().trim();
    String apellido = txtApellido.getText().trim();
    String Mail = txtMail.getText().trim();
    String direccion = txtDireccion.getText().trim();
    String usuario = txtUsuario.getText().trim();
    String contraseña = new String(txtContraseña.getPassword());
    String confirmarContraseña = new String(txtConfirmarContraseña.getPassword());
    
    // Validación: Campos vacíos
    if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || 
        Mail.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Por favor complete todos los campos obligatorios",
            "Campos Vacíos",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Validación: Cédula (10 dígitos)
    if (cedula.length() != 10 || !cedula.matches("\\d+")) {
        JOptionPane.showMessageDialog(this,
            "La cédula debe tener exactamente 10 dígitos numéricos",
            "Cédula Inválida",
            JOptionPane.WARNING_MESSAGE);
        txtCedula.requestFocus();
        return;
    }
    
    // Validación: Mail
    if (!Mail.contains("@") || !Mail.contains(".")) {
        JOptionPane.showMessageDialog(this,
            "Por favor ingrese un Mail válido\n(debe contener @ y un dominio)",
            "Mail Inválido",
            JOptionPane.WARNING_MESSAGE);
        txtMail.requestFocus();
        return;
    }
    
    // Validación: Contraseñas coinciden
    if (!contraseña.equals(confirmarContraseña)) {
        JOptionPane.showMessageDialog(this,
            "Las contraseñas no coinciden\n\nPor favor verifique que ambas contraseñas sean iguales",
            "Contraseñas No Coinciden",
            JOptionPane.WARNING_MESSAGE);
        txtContraseña.setText("");
        txtConfirmarContraseña.setText("");
        txtContraseña.requestFocus();
        return;
    }
    
    // Validación: Contraseña mínimo 6 caracteres
    if (contraseña.length() < 6) {
        JOptionPane.showMessageDialog(this,
            "La contraseña debe tener al menos 6 caracteres\n\nActual: " + contraseña.length() + " caracteres",
            "Contraseña Muy Corta",
            JOptionPane.WARNING_MESSAGE);
        txtContraseña.requestFocus();
        return;
    }
    
    // Crear objeto Usuario
    Usuario nuevoUsuario = new Usuario();
    nuevoUsuario.setCedula(cedula);
    nuevoUsuario.setNombre(nombre);
    nuevoUsuario.setApellido(apellido);
    nuevoUsuario.setMail(Mail);
    nuevoUsuario.setDireccion(direccion.isEmpty() ? "N/A" : direccion);
    
    nuevoUsuario.setUsuario(usuario);
    nuevoUsuario.setContraseña(contraseña);
    nuevoUsuario.setActivo(true);
    
    // Intentar registrar
    PersonaDAO personaDAO = new PersonaDAO();
    boolean exito = personaDAO.insertar(nuevoUsuario);
    
    // Mostrar resultado
    if (exito) {
        // ÉXITO
        JOptionPane.showMessageDialog(this,
            "Usuario registrado exitosamente\n\n" +
            "Usuario: " + usuario + "\n" +
            "Nombre: " + nombre + " " + apellido + "\n\n" +
            "Ya puede iniciar sesión en el sistema",
            "Registro Exitoso",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Limpiar formulario
        limpiarFormulario();
        
        // Volver al panel de login
        volverAlLogin();
        
    } else {
        // ERROR - Usuario o cédula duplicados
        JOptionPane.showMessageDialog(this,
            "No se pudo registrar el usuario\n\n" +
            "MOTIVOS POSIBLES:\n" +
            "• La cédula " + cedula + " ya está registrada\n" +
            "• El nombre de usuario '" + usuario + "' ya está en uso\n\n" +
            "Por favor verifique sus datos e intente con otros valores.",
            "Error de Registro",
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnRegistrarActionPerformed

    /**
 * Limpiar todos los campos del formulario
 */
private void limpiarFormulario() {
    txtCedula.setText("");
    txtNombre.setText("");
    txtApellido.setText("");
    txtMail.setText("");
    txtDireccion.setText("");
    txtUsuario.setText("");
    txtContraseña.setText("");
    txtConfirmarContraseña.setText("");
    txtCedula.requestFocus();
}

/**
 * Volver al panel de login
 */
private void volverAlLogin() {
   // Obtener la ventana padre
    java.awt.Window ventana = javax.swing.SwingUtilities.getWindowAncestor(this);
    
    if (ventana instanceof com.mycompany.biblioteca_digital.vista.VentanaLoading1) {
        com.mycompany.biblioteca_digital.vista.VentanaLoading1 loading = 
            (com.mycompany.biblioteca_digital.vista.VentanaLoading1) ventana;
        
        // Crear nuevo panel modem
        com.mycompany.biblioteca_digital.views1.modem panelLogin = 
            new com.mycompany.biblioteca_digital.views1.modem();
        
        // Llamar a ShowJPanel
        loading.ShowJPanel(panelLogin);
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JLabel forma;
    private javax.swing.JLabel forma1;
    private javax.swing.JLabel forma2;
    private javax.swing.JLabel forma21;
    private javax.swing.JLabel forma3;
    private javax.swing.JLabel forma4;
    private javax.swing.JLabel forma5;
    private javax.swing.JLabel forma6;
    private javax.swing.JLabel forma7;
    private javax.swing.JLabel imagen9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JPasswordField txtConfirmarContraseña;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
