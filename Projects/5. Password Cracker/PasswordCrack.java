import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class PasswordCrack {

    AtomicInteger c = new AtomicInteger();
    public static ArrayList<String> nameList;
    public static CopyOnWriteArrayList<String> userPasswords;
    public static char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static ArrayList<String> getDict(String dictionary) {

        ArrayList<String> temp = new ArrayList<String>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dictionary))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                temp.add(line);
            }
        } catch(Exception e) {
            System.out.println("\nAn error occured while reading from file " + dictionary); 
            System.exit(1);
        }
        return temp;
    }

    public static void getPasswords(String passwords) {

        userPasswords = new CopyOnWriteArrayList<String>(); 
        nameList = new ArrayList();
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(passwords))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String splitted[] = line.split(":");
                String encryptedPassword = splitted[1];
                String[] username = splitted[4].split(" ");
            
                userPasswords.add(encryptedPassword);
            
                nameList.add(username[0]);
            }
        } catch(Exception e) {
            System.out.println("\nAn error occured while reading from file " + passwords); 
            System.exit(1);
        }
    }

    public String checkPassword(String word, int id) {

        Iterator<String> iterator = userPasswords.iterator();

        while (iterator.hasNext()) {

            String password = iterator.next();
            String hash = jcrypt.crypt(password, word);

            if (userPasswords.contains(hash)) {
                c.incrementAndGet();
                System.out.println(c + ": Thread nr: " + id + " found a match: " + word + ": hash: " + hash);
                userPasswords.remove(hash);
                System.out.println("Length of users: " + userPasswords.size());
            }
        }
        //System.out.println(word);
        return word;
    }

    public void mangle(ArrayList<String> dictList, int id) {

        ArrayList<String> mangleList = new ArrayList<String>();
        //System.out.println("Thread: " + id + " Size of dict: " + dictList.size());

        for (int i = 0; i < dictList.size(); i++) {

            String word = dictList.get(i).toString();

            if(word.length() != 0) {

                mangleList.add(checkPassword(toLower(word), id));
                mangleList.add(checkPassword(toUpper(word), id));
                mangleList.add(checkPassword(capitalize(word), id));
                mangleList.add(checkPassword(ncapitalize(word), id));
                mangleList.add(checkPassword(reverse(word), id));
                mangleList.add(checkPassword(mirror1(word), id));
                mangleList.add(checkPassword(mirror2(word), id));
                mangleList.add(checkPassword(toggle(word), id));
                mangleList.add(checkPassword(toggle2(word), id));

                // If the word is bigger than eight, a duplicate word or a added letter won't change the hash.
                if (word.length() <= 8) {
                    mangleList.add(checkPassword(deleteLast(word), id));
                    mangleList.add(checkPassword(deleteFirst(word), id));
                    mangleList.add(checkPassword(duplicate(word), id));
                    /*for(int j = 0; j<9; j++) {
                        mangleList.add(checkPassword(addNumberFirst(word, j), id));
                        mangleList.add(checkPassword(addNumberLast(word, j), id));
                    }*/
                    for(int k =0; k<26; k++) {
                        checkPassword(addLetterLast(word, k), id);
                        checkPassword(addLetterFirst(word, k), id);
                        checkPassword(addLetterLastCap(word, k), id);
                        checkPassword(addLetterFirstCap(word, k), id);
                    }
                }
            }
        }
        mangle(mangleList, id);
    }

    public static String addLetterLast(String word, int i) {
        char c = letters[i];
        return word + c;
    }

    public static String addLetterFirst(String word, int i) {
        char c = letters[i];
        return c + word;
    }

    public static String addLetterLastCap(String word, int i) {
        String c = String.valueOf(letters[i]);
        return word + c.toUpperCase();
    }

    public static String addLetterFirstCap(String word, int i) {
        String c = String.valueOf(letters[i]);
        return c.toUpperCase() + word;
    }

    public String addNumberFirst(String word, int i) { 
        return String.valueOf(i) + word;
    }
    
    public String addNumberLast(String word, int i) { 
        return word + String.valueOf(i);
    }

    public String toUpper(String word) {
        return word.toUpperCase();
    }

    public String toLower(String word) {
        return word.toLowerCase();
    }

    public String deleteLast(String word) {
        return word.substring(0, word.length() - 1);
    }

    public String deleteFirst(String word) {
        return word.substring(1);
    }

    public String reverse(String word) {
        return new StringBuilder(word).reverse().toString();
    }

    public String duplicate(String word) {
        return word + word;
    }

    public String mirror1(String word) {
        return reverse(word) + word;
    }

    public String mirror2(String word) {
        return word + reverse(word);
    }

    public String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public String ncapitalize(String word) {
        return word.substring(0, 1).toLowerCase() + word.substring(1).toUpperCase();
    }

    public String toggle(String word) {
        String toggled = "";

        for (int i = 0; i < word.length(); i++) {
            if (i % 2 == 0) {
                toggled += word.substring(i, i + 1).toUpperCase();
            } else {
                toggled += word.substring(i, i + 1);
            }
        }
        return toggled;
    }

    public String toggle2(String word) {
        String toggled = "";
        for (int i = 0; i < word.length(); i++) {
            if (i % 2 != 0) {
                toggled += word.substring(i, i + 1).toUpperCase();
            } else {
                toggled += word.substring(i, i + 1);
            }
        }
        return toggled;
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

        dictList = getDict(dictionary);
        getPasswords(passwords);

        dictList.addAll(nameList);
        
        // Add common passwords
        dictList.add("1234");
        dictList.add("12345");
        dictList.add("123456");
        dictList.add("1234567");
        dictList.add("12345678");
        dictList.add("123456789");
        dictList.add("1234567890");
        dictList.add("qwerty");
        dictList.add("abc123");
        dictList.add("111111");
        dictList.add("1qaz2wsx");
        dictList.add("letmein");
        dictList.add("qwertyuiop");
        dictList.add("starwars");
        dictList.add("login");
        dictList.add("passw0rd");
        /*
        
         * for (int i = 0; i <= 999; i++) { String s = String.valueOf(i);
         * dictList.add(s); }
         * 
         * for (int i = 0; i < dictList.size(); i++)
         * System.out.println(dictList.get(i));
         * 
         * for (int i = 0; i < nameList.size(); i++)
         * System.out.println(nameList.get(i));
         * 
         * System.out.println(Arrays.asList(userPasswords));
         */

        int threads = 8;

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
        
        ArrayList<String> splitted = new ArrayList<String>();
        
        for (int i = id; i < dictList.size(); i += threads) {
            pCrack.checkPassword(dictList.get(i).toString(), id);
            splitted.add(dictList.get(i).toString());
        }
        pCrack.mangle(splitted, id);
    }
}