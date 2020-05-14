import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Arrays;




public class Hiddec {

    public static byte[] key, ctr, input;
    public static boolean CTR;
    static String output;
    static String algorithm = "MD5";
    public static Cipher cipher;


    private static byte[] stringToHex(String str){

        char ch[] = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < ch.length; i++) {
            sb.append(Integer.toHexString((int) ch[i]));
        }

        String hexString =sb.toString();
        System.out.println(hexString);
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] readFile(String input) {

        byte[] byteArray = null;
        try {
            byteArray = Files.readAllBytes(Paths.get(input));
        } 
        catch(Exception e) {
            System.out.println("\nAn error occured while reading from file " + input);
            System.exit(1);
        }
        return byteArray;
    }

    public static byte[] hashKey(byte[] key) throws NoSuchAlgorithmException {
        
        System.out.println("inside hashkey");

        MessageDigest md = MessageDigest.getInstance("MD5");
        
        md.update(key);
        byte[] digest = md.digest();

        System.out.println("Returning hash");
        return digest;
    }

    public static void ctr(byte[] key, byte[] encryptedKey, byte[] input, String output) {

        System.out.println("Got to ctr");

        byte[] data = extractData(input, key, findKey(input, encryptedKey, true), findKey(input, encryptedKey, false));

        try {
        byte[] verifyData = cipher.doFinal(Arrays.copyOfRange(input, findKey(input, encryptedKey, false) + 16, findKey(input, encryptedKey, false) + 32));
        byte[] hashOfData = hashKey(data);
            
        if(Arrays.equals(verifyData,hashOfData)){
            System.out.println("Data found!");
        }
        else System.out.println("No match");
        } 
        catch (IllegalBlockSizeException blockSizeException) {}
        catch(NoSuchAlgorithmException algorithmException) {}
        catch(BadPaddingException paddingException) {}
    }

    public static byte[] extractData(byte[] input, byte[] key, int start, int end) {

        System.out.println("inside extractData");
        init();
        cipher.update((Arrays.copyOfRange(input, start, start + 16)));
        return cipher.update((Arrays.copyOfRange(input, start + 16, end)));
    }

    public static int findKey(byte[] input, byte[] encryptedKey, Boolean start) {
        for(int i = 0; i <= input.length; i+= 16) {
            if(start = true) {
            init();
            System.out.println("init() called");
            }
            byte[] decrypted = cipher.update((Arrays.copyOfRange(input, i, i + 16)));
            if(Arrays.equals(decrypted,encryptedKey)){
                return i;
            }
        }
        return -1;
    }

    public static void init() {

        System.out.println("inside init()");
        try {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        System.out.println("inside try");
        IvParameterSpec iv = new IvParameterSpec(ctr);
        System.out.println("inside try");
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
        System.out.println("inside try");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        System.out.println("inside try");

        }
        catch (InvalidKeyException exception) {}
        catch (NoSuchPaddingException ePaddingException) {}
        catch (NoSuchAlgorithmException eAlgorithmPaddingException) {}
        catch (InvalidAlgorithmParameterException algorithmParameterException) {}
        
    }





    public static void main(String args[]) {

        if(args.length < 3) {
            System.out.println("Usage: --key=KEY --ctr=CTR --input=INPUT --output=OUTPUT");
            System.exit(1);
        }

        key = stringToHex(args[0]);
        ctr = stringToHex(args[1]);
        input = readFile(args[2]);
        output = args[3];

        for(int i=0; i< ctr.length ; i++) {
            System.out.print(ctr[i] +" ");
         }

        System.out.println("\n"+output);

        System.out.println("Calling ctr");

            try {
                System.out.println("inside try");
                ctr(key, hashKey(key), input, output);
               System.out.println("hashkey called");
            } catch(NoSuchAlgorithmException algorithmException) {}
            
} 
}