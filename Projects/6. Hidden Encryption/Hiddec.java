import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import javax.crypto.*;
import java.security.NoSuchAlgorithmException;


public class Hiddec {

    String key, ctr, input, output;

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

    private static byte[] hexStringToByte(String str){

        char ch[] = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < ch.length; i++) {
            sb.append(Integer.toHexString((int) ch[i]));
        }

        String hexString =sb.toString();
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public byte[] hashKey(byte[] key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        md.update(key);
        byte[] digest = md.digest();
        return digest;
    }

    public static void main(String args[]) {

        if(args.length < 3) {
            System.out.println("Usage: --key=KEY --ctr=CTR --input=INPUT --output=OUTPUT");
            System.exit(1);
        }

        Hiddec hiddec = new Hiddec();
        
        for (String arg: args) {
            String[] argument = arg.split("=");
            switch (argument[0]) {
                case "--key":
                hiddec.key = argument[1];
                break;
                
                case "--ctr":
                hiddec.ctr = argument[1];
                break;
                
                case "--input":
                hiddec.input = argument[1];
                break;
                
                case "--output":
                hiddec.output = argument[1];
                break;
            }
        }
        System.out.println(hiddec.key);
        System.out.println(hiddec.ctr);
        System.out.println(hiddec.input);
        System.out.println(hiddec.output);

        byte[] byteInput = readFile(hiddec.input);
        byte[] byteKey = hexStringToByte(hiddec.key);
        byte[] hashedKey;
        try{
            hashedKey = hiddec.hashKey(byteKey);
        }catch(NoSuchAlgorithmException e){
            System.out.println("Could not create MD5 hash");
        }
        
        if(hiddec.ctr != null) {
            byte[] byteCTR = hexStringToByte(hiddec.ctr);
        }
    }
}