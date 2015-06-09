package connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Created by Виктор on 08.06.2015.
 */
public class HTTPWorker {

    private String servletURL;

    public HTTPWorker(String url) {
        servletURL = url;
    }

    public String dictionaryConsistWord(String word) {
        try {
            return sendRequest("Check", URLEncoder.encode(word, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "NO";
    }

    public String getInitWord() {
        String responseWord = sendRequest("NewWord", "");
        if (responseWord == null)
            return "балда";

        try {
            responseWord = URLDecoder.decode(responseWord, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "балда";
        }

        return responseWord;
    }

    private String sendRequest(String type, String word) {
        try {
            final String USER_AGENT = "Mozilla/5.0";

            URL serverUrl = new URL(servletURL + "?type=" + type + "&word=" + word);
            HttpURLConnection httpURLConnection = (HttpURLConnection) serverUrl.openConnection();

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = httpURLConnection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String responseWord;
            responseWord = in.readLine();
            in.close();

            return responseWord;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  null;
    }
}
