import java.io.*;
import java.net.*;

public class Server
{
    //This function will start the server at a port number
    public void start_server(int port)
    {
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            System.out.println("Server is listening on port " + port);
            while (true)
            {
                //Accept connection from Client
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                System.out.println(socket.getInetAddress());

                // Creating a new thread for each Client connected
                new ServerThread(socket).start();
            }
        }
        catch (IOException ex)
        {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Server server = new Server();

        //The port number is 5000
        server.start_server(5000);
    }
}