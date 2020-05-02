import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class PasswordCrack {

    public static ArrayList<String> getDict(String dictionary) throws IOException {

        Scanner sc = new Scanner(new File(dictionary));
        ArrayList<String> temp = new ArrayList<String>();

        while (sc.hasNextLine()) {
            temp.add(sc.nextLine());
        }

        sc.close();

        return temp;

    }

    public static void getPasswords(String passwords) {

    }

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: <dictionary> <passwords>");
            System.exit(1);
        }

        PasswordCrack pc = new PasswordCrack();

        String dictionary = args[0];
        String passwords = args[1];

        try {
            ArrayList<String> dictList = getDict(dictionary);
            // getPasswords(passwords);
            
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

}