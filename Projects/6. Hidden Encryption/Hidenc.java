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

    static byte[] pad(byte[] data, int offset){
        Random rnd = new Random();
        byte[] blob = new byte[2048];
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

    static byte[] createBlob(byte[] input, byte[] key, int offset)throws Exception{

        List<Byte> blobList = new ArrayList<>(input.length + 3*key.length);
        init(key);

        blobList = add(blobList, hash(key));
        blobList = add(blobList, input);
        blobList = add(blobList, hash(key));
        blobList = add(blobList, hash(input));
        
        byte[] blob = new byte[blobList.size()];

        for(int i = 0; i < blobList.size(); i++)
            blob[i] = blobList.get(i);

        blob = pad(encrypt(blob), offset);

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
        return argsList;
    }

    public static void main(String[] args) throws Exception{
        
        if(args.length < 3) {
            System.out.println("Usage: --key=KEY --ctr=CTR --input=INPUT --output=OUTPUT");
            System.exit(1);
        }
        
        int offset, size;
        byte[] blob, byteKey, template; 
        String key, input, output;
        Map<String, String> argsList = getArgs(args);

        if(argsList.containsKey("ctr")) {
            isCTR = true;
            globalCTR = stringToHexByteArray(argsList.get("ctr"));
        }

        if(argsList.containsKey("size") && argsList.containsKey("template")) {
            System.out.println("Only one of --size and --template can be specified");
            System.exit(1);
        }

        if(argsList.containsKey("size")) {
            size = Integer.parseInt(argsList.get("size"));
            System.out.println(size);
        }

        key = argsList.get("key");
        input = argsList.get("input");
        output = argsList.get("output");
        template = readFile(argsList.get("template"));
        offset = Integer.parseInt(argsList.get("offset"));
        byteKey = stringToHexByteArray(key);
        
        System.out.println(key);
        System.out.println(argsList.get("ctr"));
        System.out.println(input);
        System.out.println(output);
        System.out.println(offset);
        System.out.println(template);
        System.out.println("CTR: " + isCTR);

        blob = createBlob(readFile(input), byteKey, offset);
        writeFile(blob, output); 
    }
}