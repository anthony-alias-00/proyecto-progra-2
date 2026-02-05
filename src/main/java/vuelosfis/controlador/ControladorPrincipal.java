package vuelosfis.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import vuelosfis.modelo.LogicaVuelos;
import vuelosfis.vista.vistaPrincipal;
import vuelosfis.vista.vistaCabina; 

        
public class ControladorPrincipal implements ActionListener {

    private vistaPrincipal vista;
    private LogicaVuelos modelo;
    private DefaultListModel<String> modeloLista;

    public ControladorPrincipal(vistaPrincipal vista) {
        this.vista = vista;
        this.modelo = new LogicaVuelos();

        this.vista.btnBuscarVuelo.addActionListener(this);
        this.vista.btnContinuar.addActionListener(this);

        modeloLista = new DefaultListModel<>();
        this.vista.listaVuelos.setModel(modeloLista);

        iniciarCombos();
        
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
    }

    private void iniciarCombos() {
        ArrayList<String> rutas = modelo.cargarRutas();
        vista.cboOrigen.removeAllItems();
        vista.cboDestino.removeAllItems();
        for (String r : rutas) {
            vista.cboOrigen.addItem(r);
            vista.cboDestino.addItem(r);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { 
        
        if (e.getSource() == vista.btnBuscarVuelo) {
            modeloLista.clear();
            
            String origenCompleto = (String) vista.cboOrigen.getSelectedItem();
            String destinoCompleto = (String) vista.cboDestino.getSelectedItem();
            
            
            String origen = (origenCompleto != null && origenCompleto.length() >= 3) ? origenCompleto.substring(0, 3) : "";
            String destino = (destinoCompleto != null && destinoCompleto.length() >= 3) ? destinoCompleto.substring(0, 3) : "";
            
            String tipoViaje = (String) vista.cboTipoViaje.getSelectedItem();
            
            if (vista.dateIda.getDate() == null) {
                JOptionPane.showMessageDialog(vista, "Selecciona fecha de IDA.");
                return;
            }
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIda = sdf.format(vista.dateIda.getDate());

            modeloLista.addElement("--- IDA (" + fechaIda + ") ---");
            
            ArrayList<String> ida = modelo.buscarVuelos(origen, destino, fechaIda);
            for(String v : ida) modeloLista.addElement(v);
            if(ida.isEmpty()) modeloLista.addElement("No hay vuelos.");

            if (tipoViaje != null && (tipoViaje.equalsIgnoreCase("Ida y Vuelta"))) {
                if (vista.dateVuelta.getDate() == null) {
                    JOptionPane.showMessageDialog(vista, "Selecciona fecha de REGRESO.");
                    return;
                }
                String fechaVuelta = sdf.format(vista.dateVuelta.getDate());
                modeloLista.addElement(" ");
                modeloLista.addElement("--- VUELTA (" + fechaVuelta + ") ---");
                
                ArrayList<String> vuelta = modelo.buscarVuelos(destino, origen, fechaVuelta);
                for(String v : vuelta) modeloLista.addElement(v);
                if(vuelta.isEmpty()) modeloLista.addElement("No hay vuelos.");
            }
        }
   
    if (e.getSource() == vista.btnContinuar) {
    // Obtenemos todos los índices seleccionados
    int[] seleccionados = vista.listaVuelos.getSelectedIndices();
    String tipoViaje = (String) vista.cboTipoViaje.getSelectedItem();

    // VALIDACIÓN: Si es Ida y Vuelta, debe haber seleccionado dos elementos
    if (tipoViaje.equalsIgnoreCase("Ida y Vuelta") && seleccionados.length < 2) {
        JOptionPane.showMessageDialog(vista, "Para viajes de Ida y Vuelta, debe seleccionar ambos vuelos (Ida y Regreso) usando Ctrl + Clic.");
        return;
    }

    // Si solo es Ida, basta con uno
    if (tipoViaje.equalsIgnoreCase("Solo Ida") && seleccionados.length < 1) {
        JOptionPane.showMessageDialog(vista, "Seleccione su vuelo de ida.");
        return;
    }

    // EXTRAER LOS DATOS
    ArrayList<String> paqueteVuelos = new ArrayList<>();
    for (int i : seleccionados) {
        String valor = modeloLista.get(i);
        // Evitamos que seleccione los encabezados "--- IDA ---"
        if (!valor.startsWith("---") && !valor.startsWith("No hay")) {
            paqueteVuelos.add(valor);
        }
    }

    // PASAR AL SIGUIENTE CONTROLADOR
    vista.dispose();
    vistaCabina vCabina = new vistaCabina();
    // Pasamos el ArrayList con los 1 o 2 vuelos seleccionados
    new ControladorCabina(vCabina, paqueteVuelos); 
}
    }
}