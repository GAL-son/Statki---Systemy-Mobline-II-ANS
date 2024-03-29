import java.net.*;
import java.io.*;

public class HttpRequestHandler {
    public final int port;
    private ServerSocket serverSocket;

    static enum ResponseType {
        OK,
        NOT_FOUND
    };

    HttpRequestHandler(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public Socket getClientSocket() throws IOException
    {
        Socket s = serverSocket.accept();
        s.setKeepAlive(true);
        return s;
    }

    public String getHTTPRequest(Socket clientSocket) throws IOException {        

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String message = "";
       
        String s;
        while((s = in.readLine())!= null) {
            message += s;
            if(s.isEmpty()){
                break;
            }
        }
        return message;
    }

    public void sendResponse(Socket clientSocket, String response, ResponseType type) throws IOException {
        OutputStream client = clientSocket.getOutputStream();
        
        switch (type) {
            case OK:
                client.write("HTTP/1.1 200 OK\r\n".getBytes());
                break;
            case NOT_FOUND:
                client.write("HTTP/1.1 404 NOT FOUND\r\n".getBytes());
                break;
            default:
                break;
        }

        client.write("\r\n".getBytes());
        client.write(response.getBytes());
        client.write("\r\n\r\n".getBytes());

        client.flush();
        client.close();
    }  

}
