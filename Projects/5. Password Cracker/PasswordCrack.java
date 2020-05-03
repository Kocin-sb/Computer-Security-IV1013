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

        for (int i = id; i < 1000; i += threads) {
            c.getAndIncrement();
            System.out.println("Thread nr: " + id + " c = " + c);
            // System.out.println("Thread nr: " + id + " word = " + dictList.get(i));
        }

    }

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: <dictionary> <passwords>");
            System.exit(1);
        }

        PasswordCrack pCrack = new PasswordCrack();

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

        // for (int i = 0; i < nameList.size(); i++)
        // System.out.println(nameList.get(i));

        for (int i = 0; i < dictList.size(); i++)
            System.out.println(dictList.get(i));

        // System.out.println(Arrays.asList(userPasswords));

        int threads = 4;
        /*
         * for (int id = 0; id < threads; id++) { final Worker worker = new Worker(id,
         * threads, dictList, pCrack); worker.start();
         */

    }

}
// }

class Worker extends Thread {

    int id;
    int threads;
    ArrayList<String> dictList;
    PasswordCrack pCrack;

    public Worker(int id, int threads, ArrayList<String> dictList, PasswordCrack pCrack) {
        this.id = id;
        this.threads = threads;
        this.dictList = dictList;
        this.pCrack = pCrack;
    }

    public void run() {

        pCrack.checkPass(id, threads, dictList);
    }
}