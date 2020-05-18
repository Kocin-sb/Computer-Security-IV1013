import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import java.util.Random; 
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class Hidenc {

    static Cipher cipher;
    static byte[] globalCTR;
    static boolean isCTR = false;

    static byte[] hash(byte[] array) throws NoSuchAlgorithmException {
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(array);
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

    static byte[] readFile(String input) {
        
        byte[] byteArray = null;
        try {
            byteArray = Files.readAllBytes(Paths.get(input));
        } 
        catch(IOException e) {
            System.out.println("\nAn error occured while reading from file: " + input);
            System.exit(1);
        }
        return byteArray;
    }

    static void writeFile(byte[] data, String output) {
        try {
          Files.write(Paths.get(output), data);
        } catch (IOException e) {
            System.out.println("\nAn error occured while writing to file: " + output);
            System.exit(1);
        }
    }

    static void init(byte[] key) throws Exception{

            if(isCTR) {
                cipher = Cipher.getInstance("AES/CTR/NoPadding");
                IvParameterSpec ivSpec = new IvParameterSpec(globalCTR);
                SecretKeySpec sKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.ENCRYPT_MODE, sKey, ivSpec);
            }
            else {
                cipher = Cipher.getInstance("AES/ECB/NoPadding");
                SecretKeySpec sKey = new SecretKeySpec(key, "AES");
                cipher.init(Cipher.ENCRYPT_MODE, sKey);
            }
    }

    static byte[] pad(byte[] data, int size, int offset){

            Random rnd = new Random();
            byte[] blob = new byte[size];
            rnd.nextBytes(blob);
            for(int i=0; i<data.length; i++) {
                blob[i+offset] = data[i];
            }
            return blob;
    }

    static byte[] encrypt(byte[] blob) throws BadPaddingException, IllegalBlockSizeException {
        return cipher.doFinal(blob);
    }

    static List<Byte> add(List<Byte> blobList, byte[] toAdd) {
        for(byte b : toAdd)
            blobList.add(b);

        return blobList;
    }

    static byte[] createBlob(byte[] input, byte[] key, int size, int offset)throws Exception{

        List<Byte> blobList = new ArrayList<>(input.length + 3*key.length);
        init(key);

        blobList = add(blobList, hash(key));
        blobList = add(blobList, input);
        blobList = add(blobList, hash(key));
        blobList = add(blobList, hash(input));
        
        byte[] blob = new byte[blobList.size()];

        for(int i = 0; i < blobList.size(); i++)
            blob[i] = blobList.get(i);

        blob = pad(encrypt(blob), size, offset);

        return blob;
    }

    static Map<String, String> getArgs(String args[]) {

        Map<String, String> argsList = new HashMap<String, String>();

        for (String arg: args) {
            String[] argument = arg.split("=");
            switch (argument[0]) {
                case "--key":
                argsList.put("key", argument[1]);
                break;
                
                case "--ctr":
                argsList.put("ctr", argument[1]);
                break;
                
                case "--input":
                argsList.put("input", argument[1]);
                break;
                
                case "--output":
                argsList.put("output", argument[1]);
                break;

                case "--offset":
                argsList.put("offset", argument[1]);
                break;

                case "--template":
                argsList.put("template", argument[1]);
                break;

                case "--size":
                argsList.put("size", argument[1]);
                break;
            }
        }
        if(argsList.containsKey("size") && argsList.containsKey("template")) {
            System.out.println("Only one of --size and --template can be specified");
            System.exit(1);
        }
        if(!argsList.containsKey("size") && !argsList.containsKey("template")) {
            System.out.println("At least one of --size and --template must be specified");
            System.exit(1);
        }
        if(argsList.containsKey("ctr")) {
            isCTR = true;
            globalCTR = stringToHexByteArray(argsList.get("ctr"));
        }
        return argsList;
    }

    static Map<String, Integer> setOffsetAndSize(Map<String, String> argsList) {

        Map<String, Integer> map = new HashMap<String, Integer>();

        if(argsList.containsKey("template")) {
            map.put("size", readFile(argsList.get("template")).length);
        }

        else if(argsList.containsKey("size")) {
            map.put("size", Integer.parseInt(argsList.get("size")));
        }

        if(argsList.containsKey("offset")) {
            map.put("offset", Integer.parseInt(argsList.get("offset")));
        }

        else {
            int offset = 1;
            while((offset % 16) != 0){
                offset  = new Random().nextInt(map.get("size")/2);
            }
            map.put("offset", offset);
        }
        return map;
    }

    public static void main(String[] args) throws Exception{
        
        if(args.length < 4) {
            System.out.println("Usage: --key=KEY --ctr=CTR --input=INPUT --output=OUTPUT");
            System.exit(1);
        }
        
        int offset, size;
        byte[] blob, byteKey; 
        String key, input, output, template;
        Map<String, String> argsList = getArgs(args);
        Map<String, Integer> map = setOffsetAndSize(argsList);

        key = argsList.get("key");
        input = argsList.get("input");
        output = argsList.get("output");
        template = argsList.get("template");
        size = map.get("size");
        offset = map.get("offset");
        byteKey = stringToHexByteArray(key);
        
        System.out.println(key);
        System.out.println(argsList.get("ctr"));
        System.out.println(input);
        System.out.println(output);
        System.out.println("Offset: "+offset);
        System.out.println("Template: "+ template);
        System.out.println("Size: "+size);
        System.out.println("CTR: " + isCTR);

        blob = createBlob(readFile(input), byteKey, size, offset);
        writeFile(blob, output); 
    }
}