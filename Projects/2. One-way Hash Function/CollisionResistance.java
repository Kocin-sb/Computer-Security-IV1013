import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class CollisionResistance {

    //declare byte array for digest, inputBytes, tryDigest
    //declare encoding
    //declare algorithm
    public byte[] digest, tryDigest;
    public int c = 0;
    public String algorithm = "SHA-256";
    public String encoding = "UTF-8";


    public void bruteForce(byte[] digest) {

        while(true) {
            //increment counter
            c++;
            //make a trydigest of input text
            tryDigest = getDigest(Long.valueOf(c).toString());

            //check if first 24 bits of digest and trydigest is equal
            if(digest[0] == tryDigest[0] && digest[1] == tryDigest[1] && digest[2] == tryDigest[2]) {
                // if so, print trydigest and return
                System.out.println("It took " +c+" times to generate a identical digest\n\nThe digest was: ");
                printDigest(tryDigest);
                return;
            }    
        }   
    }

    public byte[] getDigest(String inputText){

        try {
        // create object of message digest with SHA-256
        MessageDigest msgDig = MessageDigest.getInstance(algorithm);
        // create inputBytes of inputText with correct encoding
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
        System.out.println(msgToDigest);
        sc.close();
        
        CollisionResistance obj = new CollisionResistance();

        obj.digest = obj.getDigest(msgToDigest);
        obj.printDigest(obj.digest);
        obj.bruteForce(obj.digest);
    }
}