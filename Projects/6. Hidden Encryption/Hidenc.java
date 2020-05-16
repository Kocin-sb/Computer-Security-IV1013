import java.util.Arrays;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;

public class Hidenc {

    String key, ctr, input, output;
    public static boolean isCTR = false;
    public static byte[] globalCTR;

    public static byte[] hashKey(byte[] key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        md.update(key);
        byte[] digest = md.digest();
        return digest;
    }

    static byte[] stringToHexByteArray(String string) {
        int len = string.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4) + Character.digit(string.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] readFile(String input) throws IOException{
        
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

    public static void writeToFile(byte[] output, String outputFileName) throws IOException{
        try {
          Files.write(Paths.get(outputFileName), output);
        } catch (IOException e) {
          throw new IOException(String.format("Couldn't write data to the file: \"%s\"", outputFileName));
        }
    }

    public boolean testBlob(byte[] data, byte[] hash){
        return Arrays.equals(hash, Arrays.copyOfRange(data,0,hash.length));
    }

    public boolean testBlob(byte[] data, int offset, byte[] hash){
        return Arrays.equals(hash,Arrays.copyOfRange(data,offset,offset+hash.length));
    }

    public boolean validate(byte[] data, byte[] validationData){
        return Arrays.equals(data,validationData);
    }

    public static byte[] decrypt(byte[] key, byte[] encrypted) throws Exception{
        try{
            if(isCTR) {
                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                IvParameterSpec ivSpec = new IvParameterSpec(globalCTR);
                SecretKeySpec sKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.DECRYPT_MODE, sKey, ivSpec);
                return cipher.doFinal(encrypted);
            }
            else {
                Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                SecretKeySpec sKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.DECRYPT_MODE, sKey);
                return cipher.doFinal(encrypted);
            }

        } catch(BadPaddingException e){
            throw new BadPaddingException(e.getMessage());
        }
    }

    public static byte[] createBlob(byte[] key, byte[] input) {

        //create Arraylist to add to, length of input + key*3

        //add hash to list

        //add input to list

        //add hash again 

        //create hash of data and add too list

        //create byte[] of size list and add content of list to it

        // return byte[]
        }

    public static void main(String[] args) throws Exception{
        

        if(args.length < 3) {
            System.out.println("Usage: --key=KEY --ctr=CTR --input=INPUT --output=OUTPUT");
            System.exit(1);
        }

        Hiddec hiddec = new Hiddec();
        int offset = 0;
        
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

                case "--offset":
                offset = Integer.parseInt(argument[1]);
                break;
            }
        }

        if(hiddec.ctr != null) {
            isCTR = true;
            globalCTR = stringToHexByteArray(hiddec.ctr);
        }

        System.out.println(hiddec.key);
        System.out.println(hiddec.ctr);
        System.out.println(hiddec.input);
        System.out.println(hiddec.output);
        System.out.println(offset);
        System.out.println("CTR: " + isCTR);

        byte[] encryptedBlob = createBlob(hashKey(stringToHexByteArray(hiddec.key)), readFile(hiddec.input));
        //writeToFile(data, hiddec.output); 
    }
}