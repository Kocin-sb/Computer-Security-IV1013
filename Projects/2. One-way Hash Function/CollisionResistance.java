import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class CollisionResistance {

    //declare byte array for digest, inputBytes, tryDigest
    //declare encoding
    //declare algorithm
    public static byte[] digest;
    public static String algorithm = "SHA-256";
    public static String encoding = "UTF-8";


    public void bruteForce(byte[] digest) {

        //increment counter
        //create string input text to make a second digest of
        //make a trydigest of input text

        //check if first 24 bits of digest and trydigest is equal
            // if so, print trydigest and return
        

    }

    public static byte[] getDigest(String inputText){

        try {
        // create object of message digest with SHA-256
        MessageDigest msgDig = MessageDigest.getInstance(algorithm);
        // create inputBytes of inputText with correct encoding
        // call object.update with inputBytes
        msgDig.update(inputText.getBytes(encoding));
        // create digest by object.digest();
        digest = msgDig.digest();
        printDigest(digest);
        } catch(NoSuchAlgorithmException e) {System.out.println("The specified algorithm " + algorithm + " does not exists");}
        catch(UnsupportedEncodingException ex) {System.out.println("Encoding " + encoding + " not supported");}
    
        return digest;
}

   public static void printDigest(byte[] digest) {
       for(int i = 0; i < digest.length; i++)
            System.out.format("%02x", digest[i]&0xff);
        System.out.println();
   }

    public static void main(String[] args) {

        // Read in message
        System.out.println("Type message to digest:");
        Scanner sc = new Scanner(System.in);
        String msgToDigest = sc.nextLine();
        System.out.println(msgToDigest);

        byte[] digest = getDigest(msgToDigest);
        bruteForce(digest);
    }
}