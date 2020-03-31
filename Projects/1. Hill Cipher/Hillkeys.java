import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;

import java.util.Random;
import java.util.ArrayList;
import java.io.*;


public class HillKeys {


    private static int MAX_RADIX = 256;
    private static int MAX_BLOCKSIZE = 8;

    public static String keyFile;
    public static int radix, blocksize;
    public static DenseMatrix<Real> key;

    public static void generateKeyMatrix() {
        Real[][] matrixArray = new Real[blocksize][blocksize];
        Random rand = new Random();

        while (true) {
            for (int i = 0; i < blocksize; i++)
                for (int j = 0; j < blocksize; j++)
                    matrixArray[i][j] = Real.valueOf(rand.nextInt(radix));

                key = DenseMatrix.valueOf(matrixArray);
                LargeInteger determinant = LargeInteger.valueOf(key.determinant().longValue());

                if ((key.determinant() != Real.valueOf(0)) && (determinant.gcd(LargeInteger.valueOf(radix)).equals(LargeInteger.valueOf(1)))
                        && key.getNumberOfRows() == key.getNumberOfColumns()) {
                    break;
                }
            }
            try {
                BufferedWriter w = new BufferedWriter(new FileWriter(keyFile));
                w.write(key.toString().replaceAll("[{,}]",""));
                w.close();
            } catch (IOException e) {} 
    }

    public static boolean checkInvertible(DenseMatrix<Real> keyMatrix) {

        LargeInteger determinant = LargeInteger.valueOf(keyMatrix.determinant().longValue());

        if ((keyMatrix.determinant() != Real.valueOf(0)) && (determinant.gcd(LargeInteger.valueOf(radix)).equals(LargeInteger.valueOf(1)))
                && keyMatrix.getNumberOfRows() == keyMatrix.getNumberOfColumns()) 
                return true;

        else return false;
    
    }

    public static void main(String[] args) {


        if (args.length < 3) {
            System.out.println("Usage: <radix> <blocksize> <keyfile>");
            System.exit(1);
        }
        radix = (Integer.parseInt(args[0]) <= MAX_RADIX) ? Integer.parseInt(args[0]) : MAX_RADIX;
        blocksize = (Integer.parseInt(args[1]) <= MAX_BLOCKSIZE) ? Integer.parseInt(args[1]) : MAX_BLOCKSIZE;
        keyFile = args[2];

        generateKeyMatrix();


    }
}