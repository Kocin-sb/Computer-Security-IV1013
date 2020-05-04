import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

public class PasswordCrack {

    public static ArrayList<String> nameList;
    public static HashMap<String, String> userPasswords;

    public static ArrayList<String> getDict(String dictionary) throws IOException {

        ArrayList<String> temp = new ArrayList<String>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dictionary))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                temp.add(line);
            }
        }
        return temp;
    }

    public static void getPasswords(String passwords) throws IOException {

        Scanner sc = new Scanner(new File(passwords));
        userPasswords = new HashMap<String, String>();
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
            userPasswords.put(encryptedPassword, salt);

            nameList.add(username[0]);
        }

        sc.close();
    }

    public void checkPassword(String word, int id) {

        ArrayList<String> toRemove = new ArrayList<String>();
        Iterator<String> iterator = userPasswords.keySet().iterator();

        while (iterator.hasNext()) {

            String password = iterator.next();
            String hash = jcrypt.crypt(userPasswords.get(password), word);

            if (userPasswords.containsKey(hash)) {
                System.out.println("Thread nr: " + id + " found a match: " + word);
                toRemove.add(hash);
            }
        }
        while (iterator.hasNext()) {
            String line = iterator.next();
            for (String p : toRemove) {
                if (line.contains(p))
                    iterator.remove();
            }
        }

    }

    public void crackPassword(int id, int threads, ArrayList<String> dictList) {

        for (int i = id; i < dictList.size(); i += threads) {
            checkPassword(dictList.get(i).toString(), id);
            // System.out.println("Thread nr: " + id + " word = " + word);
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

        try {
            dictList = getDict(dictionary);
            getPasswords(passwords);

        } catch (Exception e) {
        }

        dictList.addAll(nameList);

        /*
         * for (int i = 0; i < dictList.size(); i++)
         * System.out.println(dictList.get(i));
         * 
         * for (int i = 0; i < nameList.size(); i++)
         * System.out.println(nameList.get(i));
         * 
         * System.out.println(Arrays.asList(userPasswords));
         */

        int threads = 4;

        System.out.println("Size of dictList: " + dictList.size());

        for (int id = 0; id < threads; id++) {
            final Worker worker = new Worker(id, threads, dictList, pCrack);
            worker.start();
        }
    }
}

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

        pCrack.crackPassword(id, threads, dictList);
    }
}