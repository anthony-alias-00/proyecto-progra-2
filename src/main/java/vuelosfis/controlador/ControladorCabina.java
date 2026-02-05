package vuelosfis.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import vuelosfis.vista.vistaCabina;            
import vuelosfis.vista.vistaSeleccionAsiento;  

public class ControladorCabina implements ActionListener {

    private vistaCabina vista;
    private ArrayList<String> paqueteVuelos; 

    // Modificamos el constructor para recibir los vuelos
    public ControladorCabina(vistaCabina vista, ArrayList<String> paqueteVuelos) {
        this.vista = vista;
        this.paqueteVuelos = paqueteVuelos; 
        
        this.vista.btnEconomica.addActionListener(this);
        this.vista.btnBusiness.addActionListener(this);
        
        this.vista.setLocationRelativeTo(null);
        this.vista.setVisible(true);
       
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == vista.btnEconomica) {
            System.out.println("Usuario eligió: ECONOMY");
        } 
        else if (e.getSource() == vista.btnBusiness) {
            System.out.println("Usuario eligió: BUSINESS");
        }

      
        vista.dispose();
        
 
        vistaSeleccionAsiento vPregunta = new vistaSeleccionAsiento();
        
    
        new ControladorSeleccionAsiento(vPregunta);
        
        System.out.println("Pasando a la pregunta de asientos...");
    }
}