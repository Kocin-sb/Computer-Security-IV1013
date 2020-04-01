/* This program encrypts a message with a given key by performing matrix multiplication and returns a file cipher with the decoded message 
  
    Features: Encrypts a message that is a multiple of the keys blocksize
    
    usage under UNIX:
            javac HillCipher.java
            java HillCipher <radix> <blocksize> <keyfile> <plainfile> <cipherfile>

    @author Emil Stahl
*/
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HillCipher {

    private static int MAX_RADIX = 256;
    private static int MAX_BLOCKSIZE = 8;
    private static ArrayList<String> plain;
    private static Scanner sc;
    public static int[][] keyMatrix;

    public static int radix, blocksize;
    public static String keyFile, plainFile, cipherFile;

    public static void readKey(String key, int blocksize) {
        try {
            sc = new Scanner(new File(key));

            keyMatrix = new int[blocksize][blocksize];

            for (int i = 0; i < blocksize; i++)
                for (int j = 0; j < blocksize; j++) {
                    keyMatrix[i][j] = sc.nextInt();
                }
        } catch (FileNotFoundException ex) {
        }
    }

    public static void readPlain(String plainFile) {

        try {
            sc = new Scanner(new File(plainFile));
            plain = new ArrayList<String>();
            while (sc.hasNext()) {
                plain.add(sc.next());
            }
            sc.close();
        } catch (FileNotFoundException ex) {
        }
    }

    public static void printkey(int blocksize) {

        System.out.println("\nKey:\n");
        for (int i = 0; i < blocksize; i++) {
            System.out.print("[ ");
            for (int j = 0; j < blocksize; j++) {
                System.out.print(HillCipher.keyMatrix[i][j] + " ");
            }
            System.out.println("]");
        }
        System.out.println("");
    }

    public static void encrypt() {

        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(cipherFile));

            for (int i = 0; i < plain.size(); i += keyMatrix.length) {
                for (int j = 0; j < keyMatrix.length; j++) {
                    int encoded = 0;
                    for (int k = 0; k < keyMatrix.length; k++) {
                        encoded += (Integer.parseInt(String.valueOf(plain.get(k + i)))) * keyMatrix[j][k];
                    }
                    try {
                        w.write(String.valueOf(encoded % radix));
                        w.write(" ");
                    } catch (IOException e) {
                    }
                }
            }
            w.close();
        } catch (IOException e) {}
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
        
        readPlain(plainFile);
        readKey(keyFile, blocksize);
        encrypt();

    }
}