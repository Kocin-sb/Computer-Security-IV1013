import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HillCipher {

    private static int MAX_RADIX = 256;
    private static int MAX_BLOCKSIZE = 4;
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
                        System.out.print(encoded % radix + " ");
                        w.write(" ");
                    } catch (IOException e) {
                    }
                }
            }
            w.close();
        } catch (IOException e) {
        }
        System.out.println();
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

        readPlain(plainFile);
        System.out.println("\nPlain message:\n\n" + HillCipher.plain);

        readKey(keyFile, blocksize);
        printkey(blocksize);

        System.out.println("Encrypted message:\n");
        encrypt();

    }
}