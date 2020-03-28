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
    public static DenseMatrix cipher;    

   public static void readCipher(String cipherFile, int blocksize) {

        ArrayList<Real> keyList;
        ArrayList cipherArray = new ArrayList<Integer>();
        ArrayList temp = new ArrayList();
        String digit; 

        try {
            sc = new Scanner(new File(cipherFile));
            while (sc.hasNext()) {
                digit = String.valueOf(sc.nextInt());
                temp.add(digit);
                System.out.print(digit + " ");
            }
            System.out.println();
            sc.close();
        } catch (FileNotFoundException ex) {}

        for(int i = 0; i < temp.size(); i+= blocksize) {
            keyList = new ArrayList<>();
            for(int j = 0; j < blocksize; j++) {
                keyList.add(Real.valueOf(Integer.parseInt(String.valueOf(temp.get(i+j)))));
            }
            cipherArray.add(DenseVector.valueOf(keyList));
        }
        cipher = DenseMatrix.valueOf(cipherArray).transpose();
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

    public static void decrypt() {

       
        Real[][] temp = new Real[blocksize][blocksize];
        DenseMatrix<Real> inverseKey, decryptKey, plain;

        LargeInteger detKey = LargeInteger.valueOf(keyMatrix.determinant().longValue());
        Real invDet = Real.valueOf(detKey.modInverse(LargeInteger.valueOf(radix)).longValue());
        inverseKey = keyMatrix.inverse().times(Real.valueOf(detKey.longValue()).times(invDet));

        for(int i = 0; i < blocksize; i++) 
            for(int j = 0; j< blocksize; j++) {
                LargeInteger modolus = LargeInteger.valueOf(inverseKey.get(i,j).longValue()).mod(LargeInteger.valueOf(radix));
                temp[i][j] = Real.valueOf(modolus.longValue());
            }

        decryptKey = DenseMatrix.valueOf(temp);
        plain = decryptKey.times(cipher).transpose();
        writePlain(plain);
    }

    public static void writePlain(DenseMatrix<Real> plain) {

        LinkedList plainList = new LinkedList<>();
        for(int i = 0; i < plain.getNumberOfRows(); i++){
            for(int j = 0; j < plain.getNumberOfColumns(); j++){
                plainList.add(String.valueOf((plain.get(i,j).intValue()) % radix));
            }
        }
        System.out.println(plainList);
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

        decrypt();

    }
}