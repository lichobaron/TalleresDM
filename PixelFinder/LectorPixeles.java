package co.edu.javeriana.desarrollomm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LectorPixeles {
	public static final int RGB = 10;
	public static final int ARGB = 11;

	private int mModo;
	private BufferedImage mImagen;
	private BufferedImage mImagen2;

	public LectorPixeles(String archivo, String archivo2, int modo) {
		try {
			mImagen = ImageIO.read(new File(archivo));
			mImagen2 = ImageIO.read(new File(archivo2));
			mModo = modo;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	public void iniciarProceso() {
		int ancho = mImagen.getWidth();
		int alto = mImagen.getHeight();
		int rojo=0;
		int verde=0;
		int azul=0;
		System.out.println("ancho, alto: " + ancho + ", " + alto);
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				//System.out.println("x,y: " + j + ", " + i);
				int pixel1 = mImagen.getRGB(j, i);
				int pixel2 = mImagen2.getRGB(j, i);
				rojo = rojo+difRojo(pixel1, pixel2);
				verde = verde+difVerde(pixel1, pixel2);
				azul = azul+difAzul(pixel1, pixel2);
			}
		}
		
		float ECMRojo = rojo/(alto*ancho);
		float ECMVerde = verde/(alto*ancho);
		float ECMAzul = azul/(alto*ancho);
		System.out.println("Rojo: "+ECMRojo);
		System.out.println("Verde: "+ECMVerde);
		System.out.println("Azul: "+ECMAzul);
		float ECM = (ECMRojo+ECMVerde+ECMAzul)/3;
		System.out.println("ECM: "+ ECM);
		float PSNR = (float) (10* Math.log10((255*255)/ECM));
		System.out.println("PSNR: "+ PSNR);
		mImagen.flush();
		mImagen2.flush();
		
	}



	
	private int difRojo(int pixel1, int pixel2) {
		int rojo1 = (pixel1 >> 16) & 0xff;
		int rojo2 = (pixel2 >> 16) & 0xff;
		return (rojo1-rojo2)*(rojo1-rojo2);
	}
	
	private int difVerde(int pixel1, int pixel2) {
		int verde1 = (pixel1 >> 8) & 0xff;
		int verde2 = (pixel2 >> 8) & 0xff;
		return (verde1-verde2)*(verde1-verde2);
	}
	
	private int difAzul(int pixel1, int pixel2) {
		int azul1 = (pixel1) & 0xff;
		int azul2 = (pixel2) & 0xff;
		return (azul1-azul2)*(azul1-azul2);
	}
	
}