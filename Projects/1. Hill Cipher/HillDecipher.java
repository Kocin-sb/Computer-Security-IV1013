/* This program decrypts a message with a given key by calculating the mod inverse of the given key and then performing matrix multiplication to get the decrypted message

    Features: Decrypts a message that is a multiple of the keys blocksize and writes the result to plainfile
    
    usage under UNIX:
           javac -cp ".:./jscience.jar" HillDecipher.java
           java -cp ".:./jscience.jar" HillDecipher <radix> <blocksize> <keyfile> <plainfile> <cipherfile>

    @author Emil Stahl
*/
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.number.LargeInteger;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HillDecipher {

    private static int MAX_RADIX = 256;
    private static int MAX_BLOCKSIZE = 8;

    public static int radix, blocksize;
    public static String keyFile, plainFile, cipherFile;
   
    private static Scanner sc;

    public static DenseMatrix<Real> keyMatrix;
    public static DenseMatrix cipher;    

   public static void getCipher(String cipherFile, int blocksize) {

        ArrayList<Real> keyList;
        ArrayList cipherArray = new ArrayList<Integer>();
        ArrayList temp = new ArrayList();
        String digit; 

        try {
            sc = new Scanner(new File(cipherFile));
            while (sc.hasNext()) {
                digit = String.valueOf(sc.nextInt());
                temp.add(digit);
            }
            sc.close();
        } catch (FileNotFoundException ex) {System.out.println("File + " + cipherFile + " not found");}

        for(int i = 0; i < temp.size(); i+= blocksize) {
            keyList = new ArrayList<>();
            for(int j = 0; j < blocksize; j++) 
                keyList.add(Real.valueOf(Integer.parseInt(String.valueOf(temp.get(i+j)))));
            
            cipherArray.add(DenseVector.valueOf(keyList));
        }
        cipher = DenseMatrix.valueOf(cipherArray).transpose();
    }

    public static void getKey(String key, int blocksize) {
       
        try {
            sc = new Scanner(new File(key));

            Real [][] temp = new Real[blocksize][blocksize];

            for (int i = 0; i < blocksize; i++)
                for (int j = 0; j < blocksize; j++)
                    temp[i][j] = Real.valueOf(sc.nextInt());
        
            keyMatrix = DenseMatrix.valueOf(temp);

        } catch (FileNotFoundException ex) {System.out.println("File + " + keyFile + " not found");}
    }

    public static void printkey(DenseMatrix<Real> keyArray) {

            System.out.println();            
            for(int i = 0; i < keyArray.getNumberOfRows(); i++) {
                for(int j = 0; j < keyArray.getNumberOfColumns(); j++) 
                    System.out.print(String.valueOf((keyArray.get(i,j).intValue()) % radix) + " ");
                System.out.println();
            }
    }

    /* Decrypts the cipher by inverting the key mod radix */
    public static void decrypt() {

        Real[][] temp = new Real[blocksize][blocksize];

        DenseMatrix<Real> inverseKey = keyMatrix.inverse().times(Real.valueOf(LargeInteger.valueOf(keyMatrix.determinant().longValue()).longValue()).times(Real.valueOf(LargeInteger.valueOf(keyMatrix.determinant().longValue()).modInverse(LargeInteger.valueOf(radix)).longValue())));

        for(int i = 0; i < blocksize; i++) 
            for(int j = 0; j< blocksize; j++) 
                temp[i][j] = Real.valueOf(LargeInteger.valueOf(inverseKey.get(i,j).longValue()).mod(LargeInteger.valueOf(radix)).longValue());

        writePlain(DenseMatrix.valueOf(temp).times(cipher).transpose());
    }

    /* Write the result from decrypt() to specified plainfile  */
    public static void writePlain(DenseMatrix<Real> plain) {

        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(plainFile));
            
            for(int i = 0; i < plain.getNumberOfRows(); i++) {
                for(int j = 0; j < plain.getNumberOfColumns(); j++)
                    w.write(String.valueOf((plain.get(i,j).intValue()) % radix) + " ");
            }
            w.close();
        } catch (IOException e) {System.out.println("An errror occured while writing to " + plainFile);}
    }

    public static void main(String[] args) {

        if (args.length < 5 || (Integer.parseInt(args[0]) > MAX_RADIX || Integer.parseInt(args[1]) > MAX_BLOCKSIZE)) {
            System.out.println("Usage: <radix> <blocksize> <keyfile> <plainfile> <cipherfile>");
            System.out.println("Max radix: " + MAX_RADIX + "\nMax blocksize: " + MAX_BLOCKSIZE);
            System.exit(1);
        }

        radix = (Integer.parseInt(args[0]));
        blocksize = (Integer.parseInt(args[1]));
        keyFile = args[2];
        plainFile = args[3];
        cipherFile = args[4];

        getKey(keyFile, blocksize);
        getCipher(cipherFile, blocksize);

        decrypt();
    }
}