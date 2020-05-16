import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import java.util.Random; 

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

    public static byte[] pad(byte[] data, int offset){
        Random rnd = new Random();
        byte[] blob = new byte[2048];
        rnd.nextBytes(blob);
        for(int i=0; i<data.length; i++){
            blob[i+offset] = data[i];
        }
        return blob;
    }

    public static byte[] createBlob(byte[] input, byte[] key)throws Exception{
        try{
            List<Byte> blob = new ArrayList<>(input.length + key.length*3);
            byte[] hash = hashKey(key);

            for(byte b: hash){
                blob.add(b);
            }
            for(byte b: input){
                blob.add(b);
            }
            for(byte b: hash){
                blob.add(b);
            }
            for(byte b : MessageDigest.getInstance("MD5").digest(input)){
                blob.add(b);
            }

            byte[] blobArray = new byte[blob.size()];
            for(int i=0; i<blob.size(); i++){
                blobArray[i] = blob.get(i);
            }

            return blobArray;
        }catch(Exception e){
          throw new Exception(e.getMessage());
        }
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


        byte[] blob = createBlob(readFile(hiddec.input), stringToHexByteArray(hiddec.key));

        byte[] encryptedBlob = encrypt(stringToHexByteArray(hiddec.key), blob);

        byte[] pad = pad(encryptedBlob, offset);


        writeToFile(pad, hiddec.output); 
    }
}