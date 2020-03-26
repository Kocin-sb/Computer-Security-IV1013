import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HillCipher {

    private static int MAX_RADIX = 256;
    private static int MAX_BLOCKSIZE = 4;
    private static ArrayList<String> plain;
    private static Scanner sc;
    File file;

    public static void readPlain(String fileName, int blocksize) {

        try {
            Scanner sc = new Scanner(new File(fileName));
            plain = new ArrayList<String>();
            while (sc.hasNext()) {
                plain.add(sc.next());
            }
            sc.close();
        } catch (FileNotFoundException ex) {
        }
    }

    public static void main(String[] args) {

        int radix, blocksize;

        if (args.length < 5) {
            System.out.println("Usage: <radix> <blocksize> <keyfile> <plainfile> <cipherfile>");
            System.exit(1);
        }
        radix = (Integer.parseInt(args[0]) <= MAX_RADIX) ? Integer.parseInt(args[0]) : MAX_RADIX;
        blocksize = (Integer.parseInt(args[1]) <= MAX_BLOCKSIZE) ? Integer.parseInt(args[1]) : MAX_BLOCKSIZE;

        readPlain(args[4], blocksize);

        System.out.println(plain);

    }
}