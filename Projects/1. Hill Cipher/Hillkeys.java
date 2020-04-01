/* This program finds a invertible key matrix of the dimenssion blocksize and writes it to keyfile 
    
    usage under UNIX:
           javac -cp ".:./jscience.jar" HillKeys.java
           java -cp ".:./jscience.jar" HillKeys <radix> <blocksize> <keyfile>

    @author Emil Stahl
*/
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.number.LargeInteger;

import java.io.*;
import java.util.Random;

public class HillKeys {
    
    private static int MAX_RADIX = 256;
    private static int MAX_BLOCKSIZE = 8;
    
    public static String keyFile;
    public static int radix, blocksize;
    public static DenseMatrix<Real> keyMatrix;
    
    public static void generateKeyMatrix() {
        
        Random r = new Random();
        Real[][] temp = new Real[blocksize][blocksize];

        for (int i = 0; i < blocksize; i++)
            for (int j = 0; j < blocksize; j++)
                temp[i][j] = Real.valueOf(r.nextInt(radix));

        keyMatrix = DenseMatrix.valueOf(temp);
        
        if(checkInvertible(keyMatrix) == false) 
            generateKeyMatrix();
        
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(keyFile));
            w.write(keyMatrix.toString().replaceAll("[{,}]",""));
            w.close();
        } catch (IOException e) {System.out.println("An error occurred while writing to file");} 
    }

    public static boolean checkInvertible(DenseMatrix<Real> keyMatrix) {

        LargeInteger det = LargeInteger.valueOf(keyMatrix.determinant().longValue());
        LargeInteger gcd = det.gcd(LargeInteger.valueOf(radix));

        if ((det != LargeInteger.valueOf(0)) && (gcd.equals(LargeInteger.valueOf(1))))
            return true;

        else return false;
    }

    public static void main(String[] args) {

        if (args.length < 3 || (Integer.parseInt(args[0]) > MAX_RADIX || Integer.parseInt(args[1]) > MAX_BLOCKSIZE)) {
            System.out.println("Usage: <radix> <blocksize> <keyfile>");
            System.out.println("Max radix: " + MAX_RADIX + "\nMax blocksize: " + MAX_BLOCKSIZE);
            System.exit(1);
        }

        radix = Integer.parseInt(args[0]);
        blocksize = Integer.parseInt(args[1]);
        keyFile = args[2];

        generateKeyMatrix();
    }
}