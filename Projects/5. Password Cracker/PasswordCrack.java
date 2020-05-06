import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;

public class PasswordCrack {

    public static ArrayList<String> nameList;
    public static HashMap<String, String> userPasswords;
    ArrayList<String> hashes = new ArrayList<String>();
    AtomicInteger c = new AtomicInteger();

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

    public String checkPassword(String word, int id) {

        Iterator<String> iterator = userPasswords.keySet().iterator();

        while (iterator.hasNext()) {

            String password = iterator.next();
            String hash = jcrypt.crypt(userPasswords.get(password), word);

            if (userPasswords.containsKey(hash) && !hashes.contains(hash)) {
                c.incrementAndGet();
                System.out.println(c + ": Thread nr: " + id + " found a match: " + word + ": hash: " + hash);
                hashes.add(hash);
            }
        }
        return word;
    }

    public void mangle(int id, ArrayList<String> dictList) {

        //System.out.println("Thread: " + id + " Size of dict: " + dictList.size());

        for (int w = 0; w < dictList.size(); w++) {

        String word = "";
        //System.out.println(word);


         for(int i = 1; i <= 13; i++) {
            System.out.println("first loop before switch: " + word);
            switch(i) {

                case 1:
                    word = (dictList.get(w).toString());
                break;
                case 2:
                   word = (duplicate(dictList.get(w).toString()));
                   break;
                case 3: 
                    word = (reverse(dictList.get(w).toString()));
                    break;
                case 4:
                    word = (toLower(dictList.get(w).toString()));
                    break;
                case 5:
                    word = (toUpper(dictList.get(w).toString()));
                    break;
                case 6:
                    word = (capitalize(dictList.get(w).toString()));
                    break;
                case 7:
                    word = (ncapitalize(dictList.get(w).toString()));
                    break;
                case 8:
                    word = (mirror1(dictList.get(w).toString()));
                    break;
                case 9:
                    word = (mirror2(dictList.get(w).toString()));
                    break;
                case 10:
                    word = (toggle(dictList.get(w).toString()));
                    break;
                case 11:
                    word = (toggle2(dictList.get(w).toString()));
                    break;
                case 12:
                    for(int e = 0; e<=9; e++) {
                        word = (addNumberFirst(dictList.get(w).toString(), e));
                    }
                    break;
                case 13:
                    for(int e = 0; e<=9; e++) {
                        word = (addNumberLast(dictList.get(w).toString(), e));
                    }
                    break;
            }

             ArrayList<String> list = new ArrayList<String>();
            for(int j = 1; j <= 13; j++) {
                switch(j) {

                    case 1:
                        list.add(word);
                        System.out.println("second loop: original: " + word);
                    break;
                    case 2:
                       list.add(duplicate(word));
                       System.out.println("second loop: duplicate: " + duplicate(word));
                       break;
                    case 3: 
                        list.add(reverse(word));
                        System.out.println("second loop: reverse: " + reverse(word));
                        break;
                    case 4:
                        list.add(toLower(word));
                        System.out.println("second loop: toLower: " + toLower(word));
                        break;
                    case 5:
                        list.add(toUpper(word));
                        System.out.println("second loop: toUpper: " + toUpper(word));
                        break;
                    case 6:
                        list.add(capitalize(word));
                        System.out.println("second loop: capitalize: " + capitalize(word));
                        break;
                    case 7:
                        list.add(ncapitalize(word));
                        System.out.println("second loop: ncapitalize: " + ncapitalize(word));
                        break;
                    case 8:
                        list.add(mirror1(word));
                        System.out.println("second loop: mirror1: " + mirror1(word));
                        break;
                    case 9:
                        list.add(mirror2(word));
                        System.out.println("second loop: mirror2: " + mirror2(word));
                        break;
                    case 10:
                        list.add(toggle(word));
                        System.out.println("second loop: toggle: " + toggle(word));
                        break;
                    case 11:
                        list.add(toggle2(word));
                        System.out.println("second loop: toggle2: " + toggle2(word));
                        break;
                    case 12:
                        for(int e = 0; e<=9; e++) {
                            list.add(addNumberFirst(word, e));
                            System.out.println("second loop: addNumberFirst: " + addNumberFirst(word, e));
                        }
                        break;
                    case 13:
                        for(int e = 0; e<=9; e++) {
                            list.add(addNumberLast(word, e));
                            System.out.println("second loop: addNumberLast: " + addNumberLast(word, e));
                        }
                        break;
                }
            }
            System.out.println("Size of list: " + list.size());


            }
         }
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

        try {
            dictList = getDict(dictionary);
            getPasswords(passwords);

        } catch (Exception e) {
        }

        /*
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

        pCrack.mangle(1, dictList);
            /*
        for (int id = 0; id < threads; id++) {
            final Worker worker = new Worker(id, threads, dictList, pCrack);
            worker.start();
        }*/
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
        pCrack.mangle(id, splitted);
    }
}