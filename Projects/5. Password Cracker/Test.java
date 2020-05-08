import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
 
public class Test {

  public static ArrayList<String> mangleList = new ArrayList<String>();

  public static void main(String [] args) throws IOException {
    String fileName = args[0];
    FileReader fileReader = new FileReader(fileName);
    ArrayList<String> dictList = new ArrayList<String>();
 
    try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      while((line = bufferedReader.readLine()) != null) {
        dictList.add(line);
      }
    }

    System.out.println("Size: " + dictList.size());
    for(int i = 0; i < dictList.size(); i++) {
      String s = dictList.get(i).toString();
      printPermutn(s);
    }
    /*
    String s = "Emil";
    printPermutn(s);
    */
    System.out.println("Size of mangleList = " + mangleList.size());
  }

  
  private static void printPermutn(String string){
    int [] factorials = new int[string.length()+1];
    factorials[0] = 1;
    for (int i = 1; i<=string.length();i++) {
        factorials[i] = factorials[i-1] * i;
    }

    for (int i = 0; i < factorials[string.length()]; i++) {
        String onePermutation="";
        String temp = string;
        int positionCode = i;
        for (int position = string.length(); position > 0 ;position--){
            int selected = positionCode / factorials[position-1];
            onePermutation += temp.charAt(selected);
            positionCode = positionCode % factorials[position-1];
            temp = temp.substring(0,selected) + temp.substring(selected+1);
        }
        //System.out.println(onePermutation);
        mangleList.add(onePermutation);
    }
}
  } 