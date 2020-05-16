import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
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

    public static byte[] encrypt(byte[] key, byte[] blob) throws Exception{
        try{
            if(isCTR) {
                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                IvParameterSpec ivSpec = new IvParameterSpec(globalCTR);
                SecretKeySpec sKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.ENCRYPT_MODE, sKey, ivSpec);
                return cipher.doFinal(blob);
            }
            else {
                Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                SecretKeySpec sKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.ENCRYPT_MODE, sKey);
                return cipher.doFinal(blob);
            }

        } catch(BadPaddingException e){
            throw new BadPaddingException(e.getMessage());
        }
    }

    public static byte[] createBlob(byte[] key, byte[] hashedKey, int keyLength, byte[] input, byte[] hashedInput) {

        //create Arraylist to add to, length of input + key*3
        List<Byte> blobList = new ArrayList<>(input.length + 3*keyLength);

        //add hash to list
        for(byte hk : hashedKey)
            blobList.add(hk);
        //add input to list
        for(byte in : input)
            blobList.add(in);
        //add hash again 
        for(byte hk : hashedKey)
            blobList.add(hk);
        //create hash of data and add too list
        for(byte b : hashedInput)
            blobList.add(b);
        //create byte[] of size list and add content of list to it
        byte[] blob = new byte[blobList.size()];

        for(int i = 0; i < blobList.size(); i++)
            blob[i] = blobList.get(i);

        try {
        blob = encrypt(key, blob);
        } catch (Exception e) {}

        return blob;
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

        byte[] encryptedBlob = createBlob(stringToHexByteArray(hiddec.key), hashKey(stringToHexByteArray(hiddec.key)), hiddec.key.length(), readFile(hiddec.input), hashKey(stringToHexByteArray(hiddec.input)));
        //writeToFile(data, hiddec.output); 
    }
}