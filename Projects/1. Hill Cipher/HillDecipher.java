import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class HillDecipher {

    private static int MAX_RADIX = 256;
    private static int MAX_BLOCKSIZE = 4;

    public static int radix, blocksize;
    public static String keyFile, plainFile, cipherFile;
   
    private static Scanner sc;

    public static DenseMatrix<Real> keyMatrix;


    public static void readCipher(String cipher, int blocksize) {

        try {
            sc = new Scanner(new File(cipher));
            ArrayList cipherArray = new ArrayList<Integer>();
            while (sc.hasNext()) {
                cipherArray.add(sc.next());
            }
            sc.close();
        } catch (FileNotFoundException ex) {
        }
    }




    public static void readKey(String key, int blocksize) {
        try {
            sc = new Scanner(new File(key));

            Real [][] matrix = new Real[blocksize][blocksize];

            for (int i = 0; i < blocksize; i++)
                for (int j = 0; j < blocksize; j++) {
                    matrix[i][j] = Real.valueOf(sc.nextInt());
                }
            keyMatrix = DenseMatrix.valueOf(matrix);
        } catch (FileNotFoundException ex) {
        }
    }

    public static void printkey(String key, int blocksize) {

        try {
            sc = new Scanner(new File(key));

            for (int i = 0; i < blocksize; i++) {
                System.out.print("[ ");
                for (int j = 0; j < blocksize; j++) {
                    System.out.print(sc.nextInt() + " ");
                }
                System.out.println("]");
            }
    } catch(FileNotFoundException exception) {}
    }
    public static void main(String[] args) {

        if (args.length < 5) {
            System.out.println("Usage: <radix> <blocksize> <keyfile> <plainfile> <cipherfile>");
            System.exit(1);
        }
        radix = (Integer.parseInt(args[0]) <= MAX_RADIX) ? Integer.parseInt(args[0]) : MAX_RADIX;
        blocksize = (Integer.parseInt(args[1]) <= MAX_BLOCKSIZE) ? Integer.parseInt(args[1]) : MAX_BLOCKSIZE;
        keyFile = args[2];
        plainFile = args[3];
        cipherFile = args[4];

        readKey(keyFile, blocksize);
        readCipher(cipherFile, blocksize);
        printkey(keyFile, blocksize);

        //decrypt();

    }
}