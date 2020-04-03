import java.util.Scanner;



public class CollisionResistance {

    //declare byte array for digest, inputBytes, tryDigest
    //declare encoding
    //declare algoorithm


    public void bruteForce(byte[] digest) {

        //increment counter
        //create string input text to make a second digest of
        //make a trydigest of input text

        //check if first 24 bits of digest and trydigest is equal
            // if so, print trydigest and return
        

    }

    //public byte[] makeDigest(String inputText){

        // create object of message digest with SHA-256
        // create inputBytes of inputText with correct encoding
        // call object.update with inputBytes
        // create digest by object.digest();

       // return digest;
   // }

    public static void main(String[] args) {

        // Read in message
        System.out.println("Type message to digest:");
        Scanner sc = new Scanner(System.in);
        String msgToDigest = sc.nextLine();
        System.out.println(msgToDigest);
        // Make digest by calling function makeDigest
        //Print digest
        //Perform brute force with digest
    }
}