/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testmp3;

import java.io.*;

import com.sun.javafx.scene.control.behavior.MenuButtonBehaviorBase;

/**
 *
 * @author Javeriana
 */
public class Modificador {

    private final int limite1;
    private final int limite2;
	
    private final String mArchivo;
    private final String mArchivo2;

    public Modificador(String nombreArchivo, String nombreArchivo2) {
        limite1 = 13000;
        limite2 = 20000;
        mArchivo = nombreArchivo;
        mArchivo2 = nombreArchivo2;
    }

    public void modificar() {
        try {
            File cancion = new File(mArchivo);
            File cancion2 = new File(mArchivo2);
            FileInputStream archivo = new FileInputStream(cancion);
            FileInputStream archivo2 = new FileInputStream(cancion2);
            File modificada = new File("cancion.mp3");
            FileOutputStream salida = new FileOutputStream(modificada);
            boolean eof = false, write1 = false ;
            int entrada = 0, entrada2= 0;

            while (!eof) {
                if (entrada == -1 || entrada2 == -1) {
                    eof = true;
                } else {
                    if (write1) {
                        int i =0;
                        while(i<limite1){
                            if(entrada == -1){
                                break;
                            }
                            entrada = archivo.read();
                            salida.write(entrada);
                            i++;
                        }
                        write1 = false;
                    } else {
                        int i =0;
                        while(i<limite2){
                            if(entrada2 == -1){
                                break;
                            }
                            entrada2 = archivo2.read();
                            salida.write(entrada2);
                            i++;
                        }
                        write1 = true;
                    }
                }
            }            
            archivo.close();
            archivo2.close();
            salida.close();
        } catch (Exception e) {
            System.out.println("Error — " + e.toString());
        }
    }

}
