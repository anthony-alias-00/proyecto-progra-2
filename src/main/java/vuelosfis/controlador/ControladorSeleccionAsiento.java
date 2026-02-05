package vuelosfis.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vuelosfis.vista.vistaSeleccionAsiento; 
import vuelosfis.vista.vistaDatosPasajero;    
import vuelosfis.vista.vistaMapaAsientos;     

public class ControladorSeleccionAsiento implements ActionListener {

    private vistaSeleccionAsiento vista;

    public ControladorSeleccionAsiento(vistaSeleccionAsiento vista) {
        this.vista = vista;
        

        this.vista.btnNo.addActionListener(this);
        this.vista.btnSi.addActionListener(this);
        
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
     
        if (e.getSource() == vista.btnNo) {
            System.out.println("Usuario NO quiso asiento extra.");
            

            vista.dispose();
            
  
            vistaDatosPasajero vDatos = new vistaDatosPasajero();
            vDatos.setVisible(true);
            vDatos.setLocationRelativeTo(null);
        }
        
   
        else if (e.getSource() == vista.btnSi) {
            System.out.println("Usuario SÍ quiere elegir asiento.");
            

            vista.dispose();
            

            vistaMapaAsientos vMapa = new vistaMapaAsientos();
            vMapa.setVisible(true);
            vMapa.setLocationRelativeTo(null);
            

        }
    }
}