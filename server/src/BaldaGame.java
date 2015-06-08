import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Random;

public class BaldaGame extends HttpServlet{

    private final int alphabetSize = 32;
    private ArrayList<int[]> dictionaryTrie;
    private ArrayList<String> startWords = new ArrayList<String>();
    private String dictionaryFilePath = "c:/OOP/Game_KSIS4/server/src/dictionary.txt";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        String word = req.getParameter("word");
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/plain");

        if (type.equals("NewWord")) {
            Random randomGenerator = new Random();
            int randomIndex = randomGenerator.nextInt(startWords.size());
            out.write(startWords.get(randomIndex));
        } else if (dictionaryConsist(word)) {
            out.write("YES");
        } else {
            out.write("NO");
        }

        out.close();
    }

    private boolean dictionaryConsist(String word) {
        int curVertex = 0;

        for (int i = 0; i < word.length(); i++) {
            int letterNumber = getLetterNumber(word.charAt(i));
            if (dictionaryTrie.get(curVertex)[letterNumber] == 0) {
                return false;
            }
            else {
                curVertex = dictionaryTrie.get(curVertex)[letterNumber];
            }
        }

        return dictionaryTrie.get(curVertex)[alphabetSize] == 1;
    }

    @Override
    public void init() throws ServletException{
        System.out.println("Start trie building...");
        dictionaryTrie = new ArrayList<int[]>();
        dictionaryTrie.add(new int[alphabetSize + 1]);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(dictionaryFilePath));
            String currentWord;
            while ((currentWord = reader.readLine()) != null) {
                if (!currentWord.matches(".+-.+")) {
                    addWordToTrie(currentWord);
                    if (currentWord.length() == 5)
                        startWords.add(currentWord);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Trie building done. Trie size: " + String.valueOf(dictionaryTrie.size()));
    }

    private void addWordToTrie(String word) {
        int curVertex = 0;
        for (int i = 0; i < word.length(); i++) {
            int letterNumber = getLetterNumber(word.charAt(i));

            if (dictionaryTrie.get(curVertex)[letterNumber] == 0) {
                dictionaryTrie.add(new int[alphabetSize + 1]);
                dictionaryTrie.get(curVertex)[letterNumber] = dictionaryTrie.size() - 1;
                curVertex = dictionaryTrie.size() - 1;
            }
            else {
                curVertex = dictionaryTrie.get(curVertex)[letterNumber];
            }
        }
        dictionaryTrie.get(curVertex)[alphabetSize] = 1;
    }

    private int getLetterNumber(char letter) {
        int letterNumber;
        if (letter == 'ё')
            letterNumber = (int)'е' - (int)'а';
        else
            letterNumber = letter - 'а';

        return letterNumber;
    }
}
