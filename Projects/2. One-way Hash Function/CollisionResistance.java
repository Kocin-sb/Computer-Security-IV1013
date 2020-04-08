import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class CollisionResistance {

    public byte[] digest;
    Random rand = new Random();
    public String encoding = "UTF-8";
    public String algorithm = "SHA-256";

    public void bruteForce(byte[] input, int id) {

        long c = 0;
        byte[] tryDigest;

        while(true) {
            //increment counter
            c++;
            int seed = rand.nextInt(2147483647);
            //make a trydigest to compare
            tryDigest = getDigest(Long.valueOf(seed).toString());

            //check if first 24 bits of digest and trydigest is equal
            if(input[0] == tryDigest[0] && input[1] == tryDigest[1] && input[2] == tryDigest[2]) {
                // if so, print trydigest and return
                System.out.println("\nThread nr: " + id + "\nIt took " +c+" times to generate a identical digest\n\nThe digest was: ");
                printDigest(tryDigest);
                return;
            }    
        }   
    }

    public byte[] getDigest(String inputText){

        try {
        // create object of message digest with SHA-256
        MessageDigest msgDig = MessageDigest.getInstance(algorithm);
        // call object.update with inputBytes
        msgDig.update(inputText.getBytes(encoding));
        // create digest by object.digest();
        digest = msgDig.digest();
        } catch(NoSuchAlgorithmException e) {System.out.println("The specified algorithm " + algorithm + " does not exists");}
        catch(UnsupportedEncodingException ex) {System.out.println("Encoding " + encoding + " not supported");}
    
        return digest;
}

   public void printDigest(byte[] digest) {
       for(int i = 0; i < digest.length; i++)
            System.out.format("%02x", digest[i]&0xff);
        System.out.println();
   }

    public static void main(String[] args) {

        // Read in message
        System.out.println("Type message to digest:");
        Scanner sc = new Scanner(System.in);
        String msgToDigest = sc.nextLine();
        System.out.println("Type number of threads to utilizie");
        int threads = sc.nextInt();
        sc.close();
        System.out.println("Message: " + msgToDigest + "\nThreads: " + threads);
        
        CollisionResistance cResistance = new CollisionResistance();

        byte[] digest = cResistance.getDigest(msgToDigest);
        System.out.println("The digest of the message " + msgToDigest + " is:");
        cResistance.printDigest(digest);

        for(int i = 0; i < threads; i++) {
            final worker worker = new worker(i, digest, cResistance);
            worker.start();
        }
    }
}

 class worker extends Thread {

    int id;
    byte[] digest;
    CollisionResistance cResistance;

    public worker(int id, byte[] digest, CollisionResistance cResistance) {
        this.id = id;
        this.digest = digest;
        this.cResistance = cResistance;
    }

    public void run() {

        cResistance.bruteForce(digest, id);
    }
}