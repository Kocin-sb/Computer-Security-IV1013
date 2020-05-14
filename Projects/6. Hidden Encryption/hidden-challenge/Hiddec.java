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

    
    public static void loadArgs(String[] args) {

        final String KEY_FLAG = "--key=";
        final String CTR_FLAG = "--ctr=";
        final String INPUT_FLAG = "--input=";
        final String OUTPUT_FLAG = "--output=";

        for(String arg : args) {
            String[] splitted = arg.split("=");

            switch(splitted[0]) {

                case KEY_FLAG:
                    key = stringToHex(splitted[1]);
                    break;
                
                case CTR_FLAG:
                    CTR = true;
                    ctr = stringToHex(splitted[1]);
                    break;
                
                case INPUT_FLAG:
                    input = readFile(splitted[1]);;
                    break;
                
                case OUTPUT_FLAG:
                    output = splitted[1];
                    break;
                
                
            }
            System.out.println("load");
        }

    }
    private static byte[] stringToHex(String str){

        char ch[] = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < ch.length; i++) {
            sb.append(Integer.toHexString((int) ch[i]));
        }
        String s =sb.toString();
        byte[] b = s.getBytes(); 
        return b;
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
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        md.update(key);
        byte[] digest = md.digest();

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

        init();
        cipher.update((Arrays.copyOfRange(input, start, start + 16)));
        return cipher.update((Arrays.copyOfRange(input, start + 16, end)));
    }

    public static int findKey(byte[] input, byte[] encryptedKey, Boolean start) {
        for(int i = 0; i <= input.length; i+= 16) {
            if(start = true)
            init();
            byte[] decrypted = cipher.update((Arrays.copyOfRange(input, i, i + 16)));
            if(Arrays.equals(decrypted,encryptedKey)){
                return i;
            }
        }
        return -1;
    }

    public static void init() {

        try {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        IvParameterSpec iv = new IvParameterSpec(ctr);
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
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

        loadArgs(args);

        byte[] key = null;

        for(String arg : args) {
            String[] splitted = arg.split("=");

            switch(splitted[0]) {

                case "--key":
                    key = stringToHex(splitted[1]);
                    break;
            }
        }
            try {
                System.out.println("inside try");

            ctr(key, hashKey(key), input, output);
            } catch(NoSuchAlgorithmException algorithmException) {}
    
} 
}