package mundo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Imagen {

	/**
	 * Declaración de las variables a utilizar
	 */

	public static final int anchoPermitido = 120;
	public static final int altoPermitido = 120;
	private Color arreglo[][];
	private String matrizRGB[][];
	private int matrizBin[][];
	private int matrizRed[][];
	private int matrizGreen[][];
	private int matrizBlue[][];
	private int matrizPromRGB[][];
	private int matrizParidad[][];

	private int ancho;
	private int alto;
	private ImageIcon icon;

	/**
	 * Contructor de la clase Imagen Inicializa las matrices a utilizar Carga la
	 * imágen
	 * 
	 * @param archivo
	 * 
	 */
	public Imagen(String archivo) {
		arreglo = new Color[anchoPermitido][anchoPermitido];
		matrizRGB = new String[ancho][alto];
		cargarImagen(archivo);
		matrizBin = new int[ancho][alto];
		matrizRed = new int[ancho][alto];
		matrizGreen = new int[ancho][alto];
		matrizBlue = new int[ancho][alto];
		matrizPromRGB = new int[ancho][alto];
		matrizParidad= new int[3][7];
		inicializarMatrizParidad();
	}

	/**
	 * Convierte la imagen en un archivo entendible y manejable por java.
	 * 
	 * @apiNote inicializa y llena la matriz de tipo Color, RGB
	 * @apiNote inicializa y llena la matriz de tipo String con los colores RGB
	 *          separados por comas
	 * @param archivo
	 */
	public void cargarImagen(String archivo) {

		BufferedImage bf = null;
		try {
			bf = ImageIO.read(new File(archivo));
		} catch (IOException ex) {
			Logger.getLogger(Imagen.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (bf.getWidth() < anchoPermitido) {
			ancho = bf.getWidth();
		} else
			ancho = anchoPermitido;

		ancho = anchoPermitido;
		if (bf.getHeight() < altoPermitido) {
			alto = bf.getHeight();

		} else
			alto = altoPermitido;

		int cont = 0;
		arreglo = new Color[anchoPermitido][anchoPermitido];
		matrizRGB = new String[ancho][alto];

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				cont++;
				arreglo[i][j] = new Color(bf.getRGB(j, i));
				Color colorRGB = arreglo[i][j];
				String colorRGBString = "";
				colorRGBString = colorRGB.getRed() + "," + colorRGB.getGreen() + "," + colorRGB.getBlue() + ",";
				matrizRGB[i][j] = colorRGBString;

			}
		}
	}


	/**
	 * Este metodo llena las matrices con la informacion rgb de los pixeles de la imagen
	 * @apiNote private int matrizRed[][]; private int matrizGreen[][]; private int
	 *          matrizBlue[][]; private int matrizPromRGB[][];
	 *
	 */
	public void crearMatricesRGB() {

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {

				Color rgb = arreglo[i][j];
				int prom = (rgb.getBlue() + rgb.getRed() + rgb.getGreen()) / 3;

				matrizRed[i][j] = rgb.getRed();
				matrizGreen[i][j] = rgb.getGreen();
				matrizBlue[i][j] = rgb.getBlue();
				matrizPromRGB[i][j] = prom;

			}
		}
	}

	public BufferedImage redimensionar(String archivo, double porcentaje) {

		BufferedImage bf = null;
		try {
			bf = ImageIO.read(new File(archivo));
		} catch (IOException ex) {
			Logger.getLogger(Imagen.class.getName()).log(Level.SEVERE, null, ex);
		}
		int ancho = bf.getWidth();
		int alto = bf.getHeight();
		int escalaAncho = (int) (porcentaje * ancho);
		int escalaAlto = (int) (porcentaje * alto);
		BufferedImage bufim = new BufferedImage(escalaAncho, escalaAlto, bf.getType());
		Graphics2D g = bufim.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(bf, 0, 0, escalaAncho, escalaAlto, 0, 0, ancho, alto, null);
		g.dispose();
		return bufim;
	}

	/**
	 * Binariza la imagen (a blanco y negro) utilizando un umbral a elección.
	 * 
	 * @exceptionSe debe primero realizar el método cargarImagen(String archivo)
	 * @param umbral
	 */
	public void binarizarImagen(double umbral) {

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {

				if (matrizPromRGB[i][j] < umbral) {
					arreglo[i][j] = Color.BLUE;

				} else {
					arreglo[i][j] = Color.PINK;

				}
			}
		}
	}

	/**
	 * Este metodo comprime la informacion de cada fila de la matriz que entra por
	 * parametro. Cada matriz que entre por parametro tendra informacion de pixeles
	 * ; solamente rojo, solamente verdes o solamente azules. La informacion se
	 * compremira utilizando el algorithmo RLE.
	 * 
	 * @param matriz
	 * @return Una lista de String la cual contiene comprimida la informacion RLE
	 *         por cada fila de la matriz que entro por parametro
	 */
	
	public ArrayList<String> CompresionRLE(int matriz[][]){
		
		Queue<Integer> numeros = new LinkedList<Integer>();

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				numeros.add(matriz[i][j]);

			}
		}
		int contadorVecesAparece = 1;
		int numeroSiguiente = 0;
		int numeroActual=0;
		String cantApareceNum ="";
		int contadorFila = 0;
		boolean seAgrego=false;
		ArrayList<String> filas = new ArrayList<String>();
		
		while (!numeros.isEmpty()) {
			contadorFila++;

			if(contadorFila==120) {

				if(seAgrego) {
				
					numeroActual=numeros.remove();
					cantApareceNum+=contadorVecesAparece+"-"+numeroActual+"-";
					seAgrego=false;
					
				}
				
				else {
					cantApareceNum+=contadorVecesAparece+"-"+numeroActual+"-";
					numeros.remove();

				}
				
				filas.add(cantApareceNum);
				cantApareceNum="";
				contadorFila=0;
				contadorVecesAparece=1;
			
				
			}
			
			
			else {

				numeroActual=numeros.remove();

				
				if(!numeros.isEmpty()) {
				
					numeroSiguiente=numeros.peek();
					
					if(numeroActual==numeroSiguiente) {
						contadorVecesAparece++;
						
					}
					
					else {
						cantApareceNum+=contadorVecesAparece + "-" + numeroActual +"-";
						contadorVecesAparece=1;
						if(contadorFila==119) {
							seAgrego=true;

						}
					}
					
					
				}
				
				
			}
			
		
			
		
			
			
			
		}
		
		
		
		return filas;
	}
	
	
	
	


	/**
	 * Este metodo agrega los numeros que se encuentran en la matriz que entra por
	 * paramtero a una cola.
	 * 
	 * @param matriz
	 * @return numeros - Una cola de tipo Integer con todos los numeros de la matriz
	 */

	public Queue<Integer> colaNumerosInicial(int[][] matriz) {

		Queue<Integer> numeros = new LinkedList<Integer>();

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				numeros.add(matriz[i][j]);

			}
		}

		return numeros;
	}

	/**
	 * Este metodo convierte la informacion comprimida en RLE a informacion binaria
	 * 
	 * @param matrizRLE - Una lista de Strings la cual contiene la informacion
	 *                  comprimida en RLE
	 * @return filaBinarizada - Una lista de Strings con la informacion convertida a
	 *         binario
	 */
	public ArrayList<String> binarizarMatrizRLE(ArrayList<String> matrizRLE) {
		String cadenaBinarizada = "";
		String cadena = "";
		char guion = '-';
		String numerosActuales = "";
		ArrayList<String> filaBinarizada = new ArrayList<String>();

		for (int i = 0; i < matrizRLE.size(); i++) {

			cadena = matrizRLE.get(i);

			for (int j = 0; j < cadena.length(); j++) {

				char actual = cadena.charAt(j);
				if (cadena.length() != (j + 1)) {

					char siguiente = cadena.charAt(j + 1);

					if (actual != guion) {
						numerosActuales += actual + "";

						if (siguiente != guion) {
//							  numerosActuales+=actual+"";
						} else {
							cadenaBinarizada += decimalToBinary(numerosActuales);
							numerosActuales = "";

						}

					}

					else {
						cadenaBinarizada += guionBinarizado();
					}

				} else {

					cadenaBinarizada += guionBinarizado();

				}
			}
			filaBinarizada.add(cadenaBinarizada);
			cadenaBinarizada = "";

		}

		return filaBinarizada;

	}

	/**
	 * Este metodo convierte un numero a binario
	 * 
	 * @param numeros - una cade de String con el numro a binarizar
	 * @return cadenaNumeroBin - Una cadena de String con el numero binarizado
	 */

	public String decimalToBinary(String numeros) {
		String cadenaNumeroBin = "";
		String num = numeros;
		int numero = Integer.parseInt(num);
		String binario = "";
		if (numero > 0) {
			while (numero > 0) {
				if (numero % 2 == 0) {
					binario = "0" + binario;
				} else {
					binario = "1" + binario;
				}
				numero = (int) numero / 2;
			}
		} else if (numero == 0) {
			binario = "0";
		} else {
			binario = "No se pudo convertir el numero. Ingrese solo números positivos";
		}

		cadenaNumeroBin = concatenarCerosFaltantes(binario);
		return cadenaNumeroBin;
	}

	/**
	 * Este metodo convirte el caracter "-" en binario
	 * 
	 * @return binario - una cadena de String con el valor binario del caracter
	 */
	public String guionBinarizado() {
		String guion = "-";
		String guionBinarizado;
		String binario = "";
		int x = 0;
		x = guion.charAt(0);
		guionBinarizado = Integer.toBinaryString(x);

		binario = concatenarCerosFaltantes(guionBinarizado);

		return binario;
	}

	/**
	 * Este metodo le agrega los numeros faltantes al numero binarizado del caracter
	 * "-" que deben ser 8 en base 2.
	 * 
	 * @param binario - Una cadena de String con los numeros en binario que
	 *                corresponden al caracter "-"-
	 * @return Una cadena de String que contiene la informacion que entro por
	 *         parametro y se le concateno los ceros faltantes.
	 */
	public static String concatenarCerosFaltantes(String binario) {

		int base8 = 8;
		int cerosFaltantes = base8 - binario.length();
		String ceros = "";
		for (int i = 0; i < cerosFaltantes; i++) {
			ceros += 0;
		}
		return ceros + binario;
	}

	/**
	 * 
	 * @return
	 */
	public int[][] getMatrizRed() {
		return matrizRed;
	}

	public void setMatrizRed(int[][] matrizRed) {
		this.matrizRed = matrizRed;
	}

	public int[][] getMatrizGreen() {
		return matrizGreen;
	}

	public void setMatrizGreen(int[][] matrizGreen) {
		this.matrizGreen = matrizGreen;
	}

	public int[][] getMatrizBlue() {
		return matrizBlue;
	}

	public void setMatrizBlue(int[][] matrizBlue) {
		this.matrizBlue = matrizBlue;
	}

	/**
	 * Este metodo verifica si la cadena que le entra por parametro con numeros
	 * binarios es un numero o corresponde en binario al caracter "-".
	 * 
	 * @param cadena - Un cadena de String el cual contiene el valor binario de un
	 *               numero o del caracter "-".
	 * @return encontro - un valor booleano el cual es true si la cadena de
	 *         parametro es un numero, false en caso contrario.
	 */
	public boolean esNumero(String cadena) {
		char indicadorFila = '/';
		boolean encontro = true;
		for (int i = 0; i < cadena.length() && encontro == true; i++) {

			char caracterActual = cadena.charAt(i);

			if (caracterActual == indicadorFila) {
				encontro = false;

			}

		}
		return encontro;
	}

	public void crearCodificacionArchivoTxt(String cadena) {

		File file = new File("./huffmanEncoder.txt");
		FileWriter writer = null;

		try {
			writer = new FileWriter(file);

			writer.write(cadena + "\n");

		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			if (writer != null) {

				try {
					writer.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Método que realiza el promedio de un Color RGB
	 */

	public int promedioColorRGB(Color rgb) {

		return (rgb.getRed() + rgb.getGreen() + rgb.getBlue()) / 3;
	}

	/**
	 * Metodo que permite imprimir la imagen despues de haber
	 * 
	 * @return
	 */

	public BufferedImage imprimirImagen2(int[][] matrizRed, int[][] matrizGreen, int[][] matrizBlue) {
		BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				Color color = new Color(matrizRed[i][j], matrizGreen[i][j], matrizBlue[i][j]);
				salida.setRGB(j, i, color.getRGB());
			}
		}
		return salida;
	}

	/**
	 * Este metodo decodifica la informacion que se encuentra binarizada y lo
	 * convierte al formato incial , RLE.
	 * 
	 * @param filas - Una lista de Strings la cual se encuentra en formato binario
	 * @return filasDecodificadas - Una lista de Strings con numeros en formato
	 *         decimal y caracteres "-".
	 */
	public ArrayList<String> decodificarRLEBinarizado(ArrayList<String> filas) {
		int base2 = 8;
		int contador = 0;
		String cadenaDecodificada = "";
		String cadenaADecodificar = "";
		String valoresActuales = "";
		String valorAnterior = "";
		int contadorBina = 0;
		ArrayList<String> filasDecoficadas = new ArrayList<String>();
		for (int i = 0; i < filas.size(); i++) {
			cadenaADecodificar = filas.get(i);
			for (int j = 0; j < cadenaADecodificar.length(); j++) {
				contador++;
				if (contador < base2) {
					valoresActuales += cadenaADecodificar.charAt(j);
				}
				else if (contador == 8) {
					valoresActuales += cadenaADecodificar.charAt(j);
					if (esGuion(valoresActuales)) {
						if (valorAnterior.equals("00101101")) {
							contadorBina++;
							if (contadorBina == 1) {
								cadenaDecodificada += binarioADecimal(valoresActuales);
								valorAnterior = valoresActuales;
								valoresActuales = "";
								contador = 0;
							}
							if (contadorBina == 2) {

								cadenaDecodificada += "-";
								valorAnterior = "00101101";
								valoresActuales = "";
								contador = 0;
								contadorBina = 0;
							}
						}
						else {
							cadenaDecodificada += "-";
							valorAnterior = "00101101";
							valoresActuales = "";
							contador = 0;
						}
					}
					else {
						cadenaDecodificada += binarioADecimal(valoresActuales);
						valorAnterior = valoresActuales;
						valoresActuales = "";
						contador = 0;
					}
				} else {
					valoresActuales += cadenaADecodificar.charAt(j);
				}
			}
			filasDecoficadas.add(cadenaDecodificada);
			cadenaDecodificada = "";
		}
		return filasDecoficadas;
	}

	/**
	 * Este metodo verifica si la cadena que entra por parametro corresponde al
	 * valor en binario del caracter "-"
	 * 
	 * @param cadena - una cdena de String con informacion en binario
	 * @return un valor booleano, true si la cadena que entra por parametro
	 *         corresponde al valor binario del "-", false en caso contrario.
	 */
	public boolean esGuion(String cadena) {
		String guion = "00101101";
		if (cadena.equals(guion)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Este metodo convierte la los numeros que esten en binario a formato decimal.
	 * 
	 * @param numeroBinario - Una cadena de String con numeros binarios
	 * @return resultado - un numero.
	 */
	public int binarioADecimal(String numeroBinario) {
		int longitud = numeroBinario.length();// Numero de digitos que tiene nuestro binario
		int resultado = 0;// Aqui almacenaremos nuestra respuesta final
		int potencia = longitud - 1;
		for (int i = 0; i < longitud; i++) {// recorremos la cadena de numeros
			if (numeroBinario.charAt(i) == '1') {
				resultado += Math.pow(2, potencia);
			}
			potencia--;// drecremantamos la potencia
		}
		return resultado;
	}

	/**
	 * Este metodo descomprime la informaicon que se encuentra comprimida por el
	 * algoritmo RLE.
	 * 
	 * @param filasRLE - Una lista de String que contienene la informacion
	 *                 comprimida
	 * @return numeros - una cola con todos los numeros que se descomprimieron.
	 */
	public Queue<Integer> descomprimirRLE(ArrayList<String> filasRLE) {
		Queue<Integer> numeros = new LinkedList<Integer>();

		String cadenaRLE = "";
		int contador = 0;
		for (int i = 0; i < filasRLE.size(); i++) {
			cadenaRLE = filasRLE.get(i);
			String arreglo[] = cadenaRLE.split("-");
			for (int k = 0; k < arreglo.length; k++) {
				int actualNumero = Integer.parseInt(arreglo[k]);
				if (k % 2 == 0) {
					contador += actualNumero;
					int numeroSiguiente = Integer.parseInt(arreglo[k + 1]);
					for (int l = 0; l < actualNumero; l++) {
						numeros.add(numeroSiguiente);
					}
				}
			}
			contador = 0;
		}
		return numeros;
	}

	/**
	 * Este metodo construye una matriz de 120 de alto x 120 de ancho. Este metodo
	 * llena la matriz a partir de los numeros que se encuentran en la cola de tipo
	 * Integer.
	 * 
	 * @param colaNumeros - Una cola de tipo Integer
	 * @return matrizRGB - Una matriz con todos los numeros que se encontraban en la
	 *         cola que entro por parametro
	 */
	public int[][] matrizReconstruidaRLE(Queue<Integer> colaNumeros) {

		int[][] matrizRGB = new int[alto][ancho];

		for (int i = 0; i < alto; i++) {
			for (int j = 0; j < ancho; j++) {
				if (!colaNumeros.isEmpty()) {
					matrizRGB[i][j] = colaNumeros.remove();

				}

			}
		}

		return matrizRGB;

	}

	/**
	 * Este metodo llena una cola con la informacion que le entra por parametro
	 * 
	 * @param matriz una matriz de tipo int
	 * @return colaNumeros - una cola llena de la informacion que se encontraba
	 */

	public Queue<Integer> numerosMatriz(int[][] matriz) {

		Queue<Integer> colaNumeros = new LinkedList<Integer>();

		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 120; j++) {

				colaNumeros.add(matriz[i][j]);

			}
		}

		return colaNumeros;
	}
	
	
	public void inicializarMatrizParidad() {
		matrizParidad[0][0]=0;
		matrizParidad[0][1]=1;
		matrizParidad[0][2]=1;
		matrizParidad[0][3]=1;
		matrizParidad[0][4]=1;
		matrizParidad[0][5]=0;
		matrizParidad[0][6]=0;
		matrizParidad[1][0]=1;
		matrizParidad[1][1]=0;
		matrizParidad[1][2]=1;
		matrizParidad[1][3]=1;
		matrizParidad[1][4]=0;
		matrizParidad[1][5]=1;
		matrizParidad[1][6]=0;
		matrizParidad[2][0]=1;
		matrizParidad[2][1]=1;
		matrizParidad[2][2]=0;
		matrizParidad[2][3]=1;
		matrizParidad[2][4]=0;
		matrizParidad[2][5]=0;
		matrizParidad[2][6]=1;
	}
	
/**
 * Este metodo se encarga codificar las filas de bits que corresponden a las filas
 * de la matriz de cada matriz de color y aplicar la codificacion de canal
 * @param matrizColor
 * @return resultadoCod- una  lista de cadenas de bits codificada
 */
public ArrayList<String> codificarMatrizColorCanar(ArrayList<String> matrizColor) {
	
 int contadorPos=0;
 char[] mensaje= new char[7];
 String cadenaCodificada="";
 int constante =7-3;
 ArrayList<String> resultadoCod= new ArrayList<String>();
 
for (int i = 0; i < matrizColor.size(); i++) {
	
	String cadenaBits=matrizColor.get(i);
	contadorPos=0;
	while (contadorPos<cadenaBits.length()-1) {
		   for (int j = 0; j < constante; j++) {
			mensaje[j]=cadenaBits.charAt(contadorPos);
			contadorPos++;
		}
		   int bChequeo=constante;
		   int contadorFila=0;
		   
		   while(bChequeo<7) {
			   int cChequeo=0;
			   
			   for (int j = 0; j < constante; j++) {
				
				   if(matrizParidad[contadorFila][j]==1 && mensaje[j]=='1') {
					   cChequeo++;
				   }
			}
			   
			  
			 if(cChequeo==0||(cChequeo%2)==0) {
				 mensaje[bChequeo]='0';
			 }else if ((cChequeo%2)!=0) {
				mensaje[bChequeo]='1';
				
			}
			   
			 bChequeo++;
			 contadorFila++;
		   }
		   
		   for (int k = 0; k < mensaje.length; k++) {
			cadenaCodificada=cadenaCodificada+mensaje[k];
		}
//	System.out.println(cadenaCodificada);
}
 
	resultadoCod.add(cadenaCodificada);
	cadenaCodificada="";
	 
}
 

return resultadoCod;

	
}
/**
 * Este metodo detecta el error, si la suma es 0 no hay  error y si es diferente si lo es.
 * @param error
 * @return valor booleano 
 */
public boolean detectarElError(int[] error) {
	if(SumarValorVector(error)==0) {
		return false;
	}else {
		return true;
	}
}

/**
 * Este metodo suma el valor del vector del sindrome
 * @param error
 * @return suma - el cual es la suma de las posiciones del sindrome
 */
private int SumarValorVector(int[] error) {
	int suma=0;
	for (int i = 0; i < error.length-1; i++) {
		suma&=error[i];
	}
	return suma;
}


/**
 * Este metodo se encarga de calcular el sindrome 
 * @return
 */
public int[] cularElSindrome(int[] error) {
	int filas=3;
	int columnas=7;
	int[] elSindrome= new int[filas];
	
	for (int i = 0; i < filas; i++) {
		int suma=0;
		for (int j = 0; j < columnas; j++) {
			suma &=matrizParidad[i][j]*error[j];
		}
		elSindrome[i]=suma;
	}
	return elSindrome;
}

/**
 * Este metodo se encarga de comparar dos vectores entre si
 * y determinar si son iguales
 * @return 
 */
public boolean sonIguales(int[] v1, int[] v2) {
	for (int i = 0; i < v2.length; i++) {
		if((v1[i]^v2[i]) !=0) {
			return false;
		}
	}
	return true;
}





public int[][] getMatrizParidad() {
	return matrizParidad;
}

public void setMatrizParidad(int[][] matrizParidad) {
	this.matrizParidad = matrizParidad;
}
	
	
	
	
	
}
