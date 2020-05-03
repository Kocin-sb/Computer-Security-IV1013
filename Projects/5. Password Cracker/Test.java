import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
 
public class Test {
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
  }
}