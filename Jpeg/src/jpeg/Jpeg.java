package jpeg;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
public class Jpeg {
	private BufferedImage mImagen;
	private int[][] luminanceMatrix;
	private int[][] luminanceMatrixAux;
	private int[][] luminanceMatrixF;
	private int[][] huffmanMatrixF;
	
	public Jpeg(String file) {
		try {
			mImagen = ImageIO.read(new File(file));
			luminanceMatrix = new int [8][8];
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void Execute() {
		luminanceMatrix();
		System.out.println("----------------------------------");
		OperationWith128(true);
		System.out.println("----------------------------------");
		DCT();
		System.out.println("----------------------------------");
		Quantization(false);
		System.out.println("----------------------------------");
		Huffman();
		System.out.println("----------------------------------");
		Quantization(true);
		System.out.println("----------------------------------");
		IDCT();
		System.out.println("----------------------------------");
		OperationWith128(false);
		System.out.println("----------------------------------");
		differences();
	}
	
	private void luminanceMatrix() {
		System.out.println("1. Matriz de luminancia y conversión a YCbCr: ");
		int widht = mImagen.getWidth();
		int height = mImagen.getHeight();
		for(int i=0; i<height;i++) {
			for(int j=0; j<widht;j++) {
				int pixel = mImagen.getRGB(j, i);
				luminanceMatrix[i][j] = (int) getLuminance(pixel);
				System.out.print(luminanceMatrix[i][j]+"  ");
			}
			System.out.println();
		}
	}
	
	private void OperationWith128(boolean minus) {
		if(minus) {
			luminanceMatrixAux = new int [8][8];
			System.out.println("2. Matriz de luminancia - 128: ");
		}
		else {
			System.out.println("8. Matriz comprimida + 128: ");
		}
		int widht = mImagen.getWidth();
		int height = mImagen.getHeight();
		for(int i=0; i<height;i++) {
			for(int j=0; j<widht;j++) {
				if(minus) {
					luminanceMatrixAux[i][j] = (int) (luminanceMatrix[i][j]-128);
					System.out.print(luminanceMatrixAux[i][j]+"  ");
				}
				else {
					luminanceMatrixAux[i][j] = (int) (luminanceMatrixAux[i][j]+128);
					System.out.print(luminanceMatrixAux[i][j]+"  ");
				}		
			}
			System.out.println();
		}
	}
	
	private float getLuminance(int pixel){
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		return (float) ((0.299*red)+(0.587*green)+(0.114*blue));
	}
	
	private void DCT() {
		System.out.println("3. DCT: ");
		luminanceMatrixF = new int [8][8];
		int widht = mImagen.getWidth();
		int height = mImagen.getHeight();
		for(int i=0; i<height;i++) {
			for(int j=0; j<widht;j++) {
				luminanceMatrixF[i][j]=(int) (0.25*C(i)*C(j)*Summation(i,j));
				System.out.print(luminanceMatrixF[i][j]+"  ");
			}
			System.out.println();
		}
	}
	
	private double C(int x) {
		double r=0;
		if (x==0) {
			r= 1/(Math.sqrt(2));
		}
		else {
			r= 1;
		}
		return r;
	}
	
	private double Summation(int u, int v) {
		double r=0;
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				double firstTerm = Math.cos(((2*i+1)*u*Math.PI)/16);
				double secondTerm = Math.cos(((2*j+1)*v*Math.PI)/16);
				r+= luminanceMatrixAux[i][j]*firstTerm*secondTerm;
			} 
		}
		return r;
	}
	
	private void Quantization(boolean h) {
		if(h) {
			System.out.println("6. Multiplicación por la cuantización: ");
		}
		else {
			System.out.println("4. División por la cuantización: ");
		}
		int [][] QuantizationMatrix={{16,11,10,16,24,40,51,61}
									,{12,12,14,19,26,58,60,55}
									,{14,13,16,24,40,57,69,56}
									,{14,17,22,29,51,87,80,62}
									,{18,22,37,56,68,109,103,77}
									,{24,35,55,64,81,104,113,92}
									,{49,64,78,87,103,121,120,101}
									,{72,92,95,98,112,100,103,99}};
		int widht = mImagen.getWidth();
		int height = mImagen.getHeight();
		for(int i=0; i<height;i++) {
			for(int j=0; j<widht;j++) {
				if(h) {
					huffmanMatrixF[i][j] = huffmanMatrixF[i][j]* QuantizationMatrix[i][j];
					System.out.print(huffmanMatrixF[i][j]+"  ");
				}
				else {
					luminanceMatrixF[i][j] = luminanceMatrixF[i][j]/ QuantizationMatrix[i][j];
					System.out.print(luminanceMatrixF[i][j]+"  ");
				}
				
			}
			System.out.println();
		}
	}
	
	private void Huffman() {
		System.out.println("5. Huffman:");
		Vector<Integer> output = HuffmanZigZag();
		HuffmanMatrix(output);		
	}
	
	private Vector<Integer> HuffmanZigZag() {
		Vector<Integer> output = new Vector<Integer>();
		int size = 8;
		int i = 1,j = 1;	
		for (int element = 0; element < size * size; element++) {
			output.addElement(luminanceMatrixF[i-1][j-1]);
			if ((i + j) % 2 == 0){
				if (j < size){
					j++;
				}				
				else{
					i+= 2;
				}		
				if (i > 1) {
					i--;
				}		
			}
			else{
				if (i < size) {
					i++;
				}	
				else {
					j+= 2;
				}
				if (j > 1) {
					j--;
				}	
			}
		}
		return  HuffmanShort(output);
	}
	
	private Vector<Integer> HuffmanShort(Vector<Integer> output){
		Vector<Integer> shortoutput = new Vector<Integer>();
		int end = output.size()-1;
		while(output.get(end) == 0 && end>0) {
			end--;
		}
		int i =0;
		for(;i<=end;i++) {
			shortoutput.addElement(output.get(i));
			if(i==end) {
				System.out.print(output.get(i));
			}
			else {
				System.out.print(output.get(i)+", ");
			}
		}
		if(end+1!=output.size()) {
			System.out.println(", EOB");
		}
		return shortoutput;
	}
	private void HuffmanMatrix(Vector<Integer> output){
		System.out.println("----------------------------------");
		System.out.println("5.1. Huffman matrix:");
		huffmanMatrixF = new int [8][8];
		int size = 8;
		int i = 1,j = 1;	
		for (int element = 0; element < size * size; element++) {
			if(element>output.size()-1) {
				huffmanMatrixF[i-1][j-1] = 0;
			}
			else {
				huffmanMatrixF[i-1][j-1] = output.get(element);
			}
			if ((i + j) % 2 == 0){
				if (j < size){
					j++;
				}				
				else{
					i+= 2;
				}		
				if (i > 1) {
					i--;
				}		
			}
			else{
				if (i < size) {
					i++;
				}	
				else {
					j+= 2;
				}
				if (j > 1) {
					j--;
				}	
			}
		}
		int widht = mImagen.getWidth();
		int height = mImagen.getHeight();
		for(int x=0; x<height;x++) {
			for(int y=0; y<widht;y++) {
				System.out.print(huffmanMatrixF[x][y]+"  ");
			}
			System.out.println();
		}
	}
	
	private void IDCT() {
		System.out.println("7. IDCT: ");
		int widht = mImagen.getWidth();
		int height = mImagen.getHeight();
		for(int i=0; i<height;i++) {
			for(int j=0; j<widht;j++) {
				luminanceMatrixAux[i][j]=(int) (0.25*SummationI(i,j));
				System.out.print(luminanceMatrixAux[i][j]+"  ");
			}
			System.out.println();
		}
	}
	
	private double SummationI(int x, int y) {
		double r=0;
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				double firstTerm = Math.cos(((2*x+1)*i*Math.PI)/16);
				double secondTerm = Math.cos(((2*y+1)*j*Math.PI)/16);
				r+= C(i)*C(j)*huffmanMatrixF[i][j]*firstTerm*secondTerm;
			} 
		}
		return r;
	}
	
	private void differences() {
		System.out.println("9. Diferencias: ");
		int widht = mImagen.getWidth();
		int height = mImagen.getHeight();
		for(int i=0; i<height;i++) {
			for(int j=0; j<widht;j++) {
				System.out.print(luminanceMatrix[i][j] - luminanceMatrixAux[i][j]+"  ");
			}
			System.out.println();
		}
	}
}
