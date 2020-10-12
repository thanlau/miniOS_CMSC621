import java.net.*;
import java.io.*;

public class Client
{
    public String read_input() throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String content = "";
        String line = "";
        while (!line.equals("End"))
        {
            line = br.readLine();
            if (line.equals("End"))
            {
                break;
            }
            content = content + line + "$%^";
        }
        return content;
    }

    // Function to read the file string setn from server
    public void read_output(String content)
    {
        String[] filetext = content.split("\\$\\%\\^");
        for (int i = 0; i < filetext.length; i++)
        {
                System.out.println(filetext[i]);
        }
    }

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
                text = console.readLine("Enter \n 1: To Create File (1 <user> <filename>)\n 2: To Read File (2 <user> <filename>) \n 3: To Delete File (3 <user> <filename>\n\n");
                if (text.charAt(0)=='1')
                {
                    String content = "";
                    String line="";
                    System.out.println("Enter File Text. To end type End");
                    content = this.read_input();
                    System.out.println(content);
                    text = text + " " +content;
                }
                writer.println(text);
                InputStream input = socket.getInputStream();
                //System.out.println(text);
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                
                String server_output = reader.readLine();
                if (text.charAt(0)=='2')
                {
                    this.read_output(server_output);
                }
                else
                {
                    System.out.println(server_output);
                }
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