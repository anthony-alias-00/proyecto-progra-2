package vuelosfis.Controlador;
import vuelosfis.controlador.ControladorPrincipal;
import vuelosfis.vista.vistaPrincipal;
import vuelosfis.controlador.GeneradorDatos;

public class Main {
    public static void main(String[] args) {
   
        vistaPrincipal v = new vistaPrincipal();

        ControladorPrincipal c = new ControladorPrincipal(v);
        GeneradorDatos.generarMilVuelos();
    }
}