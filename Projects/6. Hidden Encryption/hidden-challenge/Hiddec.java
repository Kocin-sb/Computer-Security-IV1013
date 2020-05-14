import java.util.Arrays;


public class Hiddec {

    public static byte[] key, ctr, input;
    static String output;

    
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
                    ctr = stringToHex(splitted[1]);
                    break;
                
                case INPUT_FLAG:
                    input = stringToHex(splitted[1]);;
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







    public static void main(String args[]) {

        if(args.length < 3) {
            System.out.println("Usage: --key=KEY --ctr=CTR --input=INPUT --output=OUTPUT");
            System.exit(1);
        }

        loadArgs(args);

    }
} 