import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Arrays;

public class PasswordCrack {

    public static ArrayList<String> nameList;
    AtomicInteger c = new AtomicInteger();

    public static ArrayList<String> getDict(String dictionary) throws IOException {

        Scanner sc = new Scanner(new File(dictionary));
        ArrayList<String> temp = new ArrayList<String>();

        while (sc.hasNextLine()) {
            temp.add(sc.nextLine());
        }

        sc.close();

        return temp;

    }

    public static HashMap<String, String> getPasswords(String passwords) throws IOException {

        Scanner sc = new Scanner(new File(passwords));
        HashMap<String, String> temp = new HashMap<String, String>();
        nameList = new ArrayList();

        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String splitted[] = line.split(":");
            String salt = splitted[1].substring(0, 2);
            String encryptedPassword = splitted[1];
            String[] username = splitted[4].split(" ");
            /*
             * System.out.println("name = " + username[0]); System.out.println("password = "
             * + encryptedPassword); System.out.println("salt = " + salt + "\n");
             */
            temp.put(encryptedPassword, salt);

            nameList.add(username[0]);
        }

        sc.close();

        return temp;

    }

    public void checkPass(int id, int threads, ArrayList dictList) {

        for (int i = id; i < dictList.size(); i += threads) {
            System.out.println("Thread nr: " + id + " word = " + dictList.get(i));
        }

    }

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: <dictionary> <passwords>");
            System.exit(1);
        }

        PasswordCrack pc = new PasswordCrack();

        String dictionary = args[0];
        String passwords = args[1];

        ArrayList<String> dictList = new ArrayList<String>();
        HashMap<String, String> userPasswords = new HashMap<>();

        try {
            dictList = getDict(dictionary);
            userPasswords = getPasswords(passwords);

        } catch (Exception e) {
        }

        dictList.addAll(nameList);

        for (int i = 0; i < nameList.size(); i++)
            System.out.println(nameList.get(i));

        // System.out.println(Arrays.asList(userPasswords));

        // int threads = 4;
        // pc.checkPass(1, threads, dictList);

        // for (int id = 0; id < threads; id++)
        // pc.checkPass(id, threads, dictList);
    }
}

class worker extends Thread {

    int id;
    int threads;
    ArrayList<String> dictList;
    PasswordCrack pCrack;

    public worker(int id, int threads, ArrayList<String> dictList, PasswordCrack pCrack) {
        this.id = id;
        this.threads = threads;
        this.dictList = dictList;
        this.pCrack = pCrack;
    }

    public void run() {

        pCrack.checkPass(id, threads, dictList);
    }
}