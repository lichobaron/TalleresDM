/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testmp3;

/**
 *
 * @author Javeriana
 */
public class TestMP3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		//Uso:
		// Primer parametro: Nombre de archivo
		// Segundo parametro: Flujo de salida detallado (bytes) o no
        Modificador mod = new Modificador("archivo.mp3", "archivo2.mp3");
        mod.modificar();
    }
    
}
