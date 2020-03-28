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

        //readKey(keyFile, blocksize);
        //printkey(blocksize);

        //decrypt();

    }
}