package connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPConnector {

    private final int PORT = 22228;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public TCPConnector(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            socket = new Socket(inetAddress, PORT);
            setStreams();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TCPConnector() {
        Thread waitingConnectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                waitConnection();
            }
        });
        waitingConnectionThread.start();
        outputStream.writeUTF(getInitWord());
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

    private String getInitWord() {

    }
}
