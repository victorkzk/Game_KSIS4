package connection;

import javafx.scene.control.TitledPane;

import java.io.*;
import java.net.*;

public class TCPConnector {

    private final int PORT = 22228;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String servletUrl = "http://localhost:8080/BaldaGame";
    private String initWord;

    public TCPConnector(String ip) {
        try {
            socket = new Socket(ip, PORT);
            servletUrl = "http://" + ip + ":8080/BaldaGame";
            setStreams();
            initWord = inputStream.readUTF();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TCPConnector() {
        try {
            waitConnection();
            initWord = getInitWordFromServer();
            outputStream.writeUTF(initWord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitConnection() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            setStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStreams() {
        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getInitWordFromServer() {
        HTTPWorker httpWorker = new HTTPWorker(servletUrl);
        return httpWorker.getInitWord();
    }

    public String getInitWord() {
        return initWord;
    }

    public boolean checkWord(String word) {
        HTTPWorker httpWorker = new HTTPWorker(servletUrl);
        return httpWorker.dictionaryConsistWord(word).equals("YES");
    }

    public void sendMove(String word, String letter, int indexI, int indexJ) {
        try {
            outputStream.writeUTF(word);
            outputStream.writeUTF(letter);
            outputStream.writeInt(indexI);
            outputStream.writeInt(indexJ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response waitMove(final TitledPane titledPane) {
        final Response response = new Response();
        Thread waitMoveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response.word = inputStream.readUTF();
                    response.letter = inputStream.readUTF();
                    response.indexI = inputStream.readInt();
                    response.indexJ = inputStream.readInt();
                    titledPane.setText("ass");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        waitMoveThread.start();
        return null;
    }

    public class Response {
        int indexI;
        int indexJ;
        String letter;
        String word;
    }
}
