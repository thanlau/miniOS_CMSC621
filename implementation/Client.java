import java.net.*;
import java.io.*;

public class Client
{
    // This function will create a connection to server and send operations
    //The parameters are Server IP which is 127.0.0.1 and port of server is 5000
    public void start_client(String hostname, int port)
    {
        try (Socket socket = new Socket(hostname, port))
        {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            Console console = System.console();
            String text="";

            // To close connection type "Over"
            while (!text.equals("Over"))
            {
                text = console.readLine("Enter \n 1: To Create File (1 <user> <filename> <content>)\n 2: To Read File (2 <user> <filename>) \n 3: To Delete File (3 <user> <filename>\n\n");
                writer.println(text);
                InputStream input = socket.getInputStream();
                System.out.println(text);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String server_output = reader.readLine();
                System.out.println(server_output);
            }
            socket.close();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Server not found: " + ex.getMessage());
        }
        catch (IOException ex)
        {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    public static void main(String[] args)
    {
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        Client client = new Client();
        client.start_client(hostname, port);
    }
}