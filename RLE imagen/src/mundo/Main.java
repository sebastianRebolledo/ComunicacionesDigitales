package mundo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {

		Imagen obj = new Imagen("./src/mundo/rostro 1.jpg");
//        obj.cargarImagen("./src/mundo/rostro 1.jpg");
		obj.crearMatricesRGB();
		
		
		ArrayList<String> filasRed=obj.CompresionRLE(obj.getMatrizRed());
		ArrayList<String> filasGreen=obj.CompresionRLE(obj.getMatrizGreen());
		ArrayList<String> filasBlue=obj.CompresionRLE(obj.getMatrizBlue());
		
		


//		/// Este metodo convirte la compresion RLE a formato binario
		ArrayList<String> filasRedBinarizada = obj.binarizarMatrizRLE(filasRed);
		ArrayList<String> filasGreenBinarizada = obj.binarizarMatrizRLE(filasGreen);
		ArrayList<String> filasBlueBinarizada = obj.binarizarMatrizRLE(filasBlue);

        //Este metodo decodifica la informacion de binario al fomato incial RLE
		ArrayList<String> decodificarRLERed = obj.decodificarRLEBinarizado(filasRedBinarizada);
		ArrayList<String> decodificarRLEGrenn = obj.decodificarRLEBinarizado(filasGreenBinarizada);
		ArrayList<String> decodificarRLEBlue = obj.decodificarRLEBinarizado(filasBlueBinarizada);

		Queue<Integer> colaNumerosFinalesRed = obj.descomprimirRLE(decodificarRLERed);
		Queue<Integer> colaNumerosFinalesGreen = obj.descomprimirRLE(decodificarRLEGrenn);
		Queue<Integer> colaNumerosFinalesBlue = obj.descomprimirRLE(decodificarRLEBlue);
//
		int mtri1[][] = new int[120][120];
		int mtri2[][] = new int[120][120];
		int mtri3[][] = new int[120][120];
		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 120; j++) {
				
				
				if (!colaNumerosFinalesRed.isEmpty()) {
					int numero=colaNumerosFinalesRed.remove();
					mtri1[i][j] = numero;
					

				}
				
			}
		}

		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 120; j++) {
				if (!colaNumerosFinalesGreen.isEmpty()) {
					mtri2[i][j] = colaNumerosFinalesGreen.remove();

				}
			}
		}

		for (int i = 0; i < 120; i++) {
			for (int j = 0; j < 120; j++) {
				if (!colaNumerosFinalesBlue.isEmpty()) {
					mtri3[i][j] = colaNumerosFinalesBlue.remove();

				}
			}
		}

		BufferedImage img = obj.imprimirImagen2(mtri1, mtri2, mtri3);
		ImageIO.write(img, "jpg", new File("./src/salida rostro 1.jpg"));
		



		

	}
}
