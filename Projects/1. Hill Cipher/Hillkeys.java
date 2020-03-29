import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;

import java.util.Random;


public class HillKeys {


    private static int MAX_RADIX = 256;
    private static int MAX_BLOCKSIZE = 4;

    public static String keyFile;
    public static int radix, blocksize;

    public static void generateKeyMatrix() {
        
        Random r = new Random();
        Real[][] temp = new Real[blocksize][blocksize];
        DenseMatrix<Real> keyMatrix;

        for (int i = 0; i < blocksize; i++)
            for (int j = 0; j < blocksize; j++)
                temp[i][j] = Real.valueOf(r.nextInt(radix));

        keyMatrix = DenseMatrix.valueOf(temp);

        checkInvertible(keyMatrix);
    }

    public static boolean checkInvertible(DenseMatrix<Real> keyMatrix) {

        LargeInteger determinant = LargeInteger.valueOf(keyMatrix.determinant().longValue());

        if ((keyMatrix.determinant() != Real.valueOf(0)) && (determinant.gcd(LargeInteger.valueOf(radix)).equals(LargeInteger.valueOf(1)))
        && keyMatrix.getNumberOfRows() == keyMatrix.getNumberOfColumns()) {

            return true;
        }
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