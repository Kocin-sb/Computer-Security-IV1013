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

        byte[] byteInput = readBinaryFile(hiddec.input);
        byte[] byteKey = hex2Byte(hiddec.key);
        byte[] hashedKey;
        try{
            hashedKey = hiddec.hashKey(byteKey);
        }catch(NoSuchAlgorithmException e){
            System.out.println("Could not create MD5 hash");
        }
        
        if(hiddec.ctr != null) {
            byte[] byteCTR = hex2Byte(hiddec.ctr);
        }

        try {
           EncodeFinder encFinder = new EncodeFinder(byteKey, byteInput);
                writeToFile(encFinder.findDataECB(), hiddec.output);
    
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
}

    public byte[] hashKey(byte[] key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        
        md.update(key);
        byte[] digest = md.digest();
        return digest;
    }

    static byte[] hex2Byte(String data) {
        BigInteger hex = new BigInteger(data, 16);
        byte[] hexByteArray = hex.toByteArray();

        if (hexByteArray.length > 16)
          hexByteArray = Arrays.copyOfRange(hexByteArray, hexByteArray.length - 16, hexByteArray.length);

        return hexByteArray;
    }

    public static List<String> readFile(String fileName) throws IOException{
        Path file = Paths.get(fileName);
        List<String> lines;
        try {
          lines = Files.readAllLines(file, Charset.defaultCharset());
        } catch (IOException e) {
          throw new IOException(String.format("Couldn't read the file: \"%s\"\n", fileName));
        }
        List<String> emptyLines = new ArrayList<>();
        for (String line : lines){
            if (line.equals("")){
                emptyLines.add(line);
            }
        }
        lines.removeAll(emptyLines);
        return lines;
    }
    public static byte[] readBinaryFile(String fileName) throws IOException{
        Path file = Paths.get(fileName);
        byte[] lines;
        try {
          lines = Files.readAllBytes(file);
        } catch (IOException e) {
          throw new IOException(String.format("Couldn't read the file: \"%s\"\n", fileName));
        }
        return lines;
    }

    public static void writeToFile(byte[] output, String outputFileName) throws IOException{
        try {
          Files.write(Paths.get(outputFileName), output);
        } catch (IOException e) {
          throw new IOException(String.format("Couldn't write data to the file: \"%s\"", outputFileName));
        }
    }

    private static class EncodeFinder{
        byte[] key,ctr,input,hash;
        EncodeFinder(byte[] key, byte[] ctr, byte[] input)throws NoSuchAlgorithmException{
            this.key=key;
            this.ctr=ctr;
            this.input=input;
            try{
                this.hash=MessageDigest.getInstance("MD5").digest(key);
            }catch(NoSuchAlgorithmException e){
                throw new NoSuchAlgorithmException("Could not create MD5 hash");
            }

        }

        EncodeFinder(byte[] key, byte[] input) throws NoSuchAlgorithmException{
            this.key=key;
            this.input=input;
            try{
                this.hash=MessageDigest.getInstance("MD5").digest(key);
            }catch(NoSuchAlgorithmException e){
                throw new NoSuchAlgorithmException("Could not create MD5 hash");
            }
        }

        public byte[] findDataCTR() throws Exception{
            byte[] encData = {};
            for(int i=0; i<input.length; i+=16){
                encData = AESEncryptor.decryptCTR(key, ctr, Arrays.copyOfRange(input,i,input.length));
                if(testBlob(encData)){
                    break;
                }
            }
            if(!testBlob(encData)){
                throw new Exception("Could not find blob");
            }
            for(int i=hash.length; i<encData.length; i++){
                if(testBlob(encData,i)){
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

        public byte[] findDataECB() throws Exception{
            byte[] encData = {};
            for(int i=0; i<input.length; i+=16){
                encData = AESEncryptor.decryptECB(key, Arrays.copyOfRange(input,i,input.length));
                if(testBlob(encData)){
                    break;
                }
            }
            if(!testBlob(encData)){
                throw new Exception("Could not find blob");
            }
            for(int i=hash.length; i<encData.length; i++){
                if(testBlob(encData,i)){
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

        public boolean testBlob(byte[] data){
            return Arrays.equals(hash, Arrays.copyOfRange(data,0,hash.length));
        }

        public boolean testBlob(byte[] data, int offset){
            return Arrays.equals(hash,Arrays.copyOfRange(data,offset,offset+hash.length));
        }

        public boolean validate(byte[] data, byte[] validationData){
            return Arrays.equals(data,validationData);
        }

    }

    private static class AESEncryptor{
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
}