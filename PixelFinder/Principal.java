package co.edu.javeriana.desarrollomm;


 
public class Principal {
 
  public static void main(String[] foo) {
	  LectorPixeles miLector = new LectorPixeles("pics/art.gif", "pics/art.jpg" ,LectorPixeles.ARGB);
	  miLector.iniciarProceso();
  }
}