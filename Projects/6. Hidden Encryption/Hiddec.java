import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import java.util.Base64;

public class Hiddec {

    String key, ctr, input, output;

    public static void main(String[] args) throws Exception{
        

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

        if(hiddec.ctr != null) {
            byte[] data = hiddec.findDataCTR(stringToHexByteArray(hiddec.key), readFile(hiddec.input), hashKey(stringToHexByteArray(hiddec.key)), stringToHexByteArray(hiddec.ctr));            
            writeToFile(data, hiddec.output); 
        }
        else {
        byte[] data = hiddec.findDataECB(stringToHexByteArray(hiddec.key), readFile(hiddec.input), hashKey(stringToHexByteArray(hiddec.key)));
        writeToFile(data, hiddec.output); 
        }
    }

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
    public byte[] findDataCTR(byte[] key, byte[] input, byte[] hash, byte[] ctr) throws Exception{
        byte[] encData = {};
        for(int i=0; i<input.length; i+=16){
            encData = decryptCTR(key, ctr, Arrays.copyOfRange(input,i,input.length));
            if(testBlob(encData, hash)){
                break;
            }
        }
        if(!testBlob(encData, hash)){
            throw new Exception("Could not find blob");
        }
        for(int i=hash.length; i<encData.length; i++){
            if(testBlob(encData,i, hash)){
                byte[] foundData = Arrays.copyOfRange(encData,hash.length,i);
                int start = i;
                start += hash.length;
                byte[] validationData = Arrays.copyOfRange(encData,start,start+hash.length);
                if(validate(MessageDigest.getInstance("MD5").digest(foundData),validationData)){
                    return foundData;
                }
                else{
                    throw new Exception("Data could not be validated");
                }
            }
        }
        throw new Exception("Data could not be found in the blob");
    }

    public byte[] findDataECB(byte[] key, byte[] input, byte[] hash) throws Exception{
        byte[] encData = {};
        for(int i=0; i<input.length; i+=16){
            encData = decryptECB(key, Arrays.copyOfRange(input,i,input.length));
            if(testBlob(encData, hash)){
                break;
            }
        }
        if(!testBlob(encData, hash)){
            throw new Exception("Could not find blob");
        }
        for(int i=hash.length; i<encData.length; i++){
            if(testBlob(encData,i, hash)){
                byte[] foundData = Arrays.copyOfRange(encData,hash.length,i);
                int start = i;
                start += hash.length;
                byte[] validationData = Arrays.copyOfRange(encData,start,start+hash.length);
                if(validate(MessageDigest.getInstance("MD5").digest(foundData),validationData)){
                    return foundData;
                }
                else{
                    throw new Exception("Data could not be validated");
                }
            }
        }
        throw new Exception("Data could not be found in the blob");
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

    public static byte[] decryptCTR(byte[] key, byte[] ctr, byte[] encrypted) throws Exception{
            try{
                Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
                IvParameterSpec ivSpec = new IvParameterSpec(ctr);
                SecretKeySpec sKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.DECRYPT_MODE, sKey, ivSpec);
                return cipher.doFinal(encrypted);

            }catch(BadPaddingException e){
                throw new BadPaddingException(e.getMessage());
            }
        }

    public static byte[] decryptECB(byte[] key, byte[] encrypted) throws Exception{
            try{
                Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                SecretKeySpec sKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.DECRYPT_MODE, sKey);
                return cipher.doFinal(encrypted);

            }catch(BadPaddingException e){
                throw new BadPaddingException(e.getMessage());
            }
        }
}