import java.io.*;
import java.net.*;

public class ServerThread extends Thread
{
    private Socket socket;
    private static String PATH = "F:/Courses/AOS/Project/dev/Directory";

    public ServerThread(Socket socket)
    {
        this.socket = socket;
    }

    // Function to Create File
    private String create(String user, String filename, String content)
    {
        String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".txt";
        File directory = new File(directoryName);
        String[] filetext = content.split("\\$\\%\\^");

        //If directory doesn't exist then it creates
        if (! directory.exists())
        {
            directory.mkdir();
        }
        //Creating File
        File file = new File(directoryName + "/" + fileName);
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < filetext.length; i++)
            {
                bw.write(filetext[i]);
                bw.newLine();
            }
            
            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "File Created Successfully";
    }

    // Function to Read File
    private String read(String user, String filename)
    {
        String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".txt";
        File file = new File(directoryName + "/" + fileName);

        //If File exists then it reads File else returns File Not Present
        if (file.exists())
        {
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String text;
                String content = "";
                while ((text=br.readLine()) != null)
                {
                    content = content + text + "$%^";
                }
                br.close();
                return content;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            return "File Not Present";
        }
        return "File Not Present";
    }

    // Function to Delete File
    private String delete(String user, String filename)
    {
        String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".txt";
        File file = new File(directoryName + "/" + fileName);
        //If file present then it deletes the file
        if (file.delete())
        {
            return "File Deleted Successfully";
        }
        return "File Could Not be Deleted";
    }

    public void run()
    {
        try
        {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text = "";
            while (!text.equals("Over"))
            {
                String output_text;
                text = reader.readLine();
                System.out.println(text);
                String[] splited = text.split("\\s+");
                String operation = splited[0];
        
                //If operation == 1 call Create File
                if (operation.equals("1"))
                {
                    String[] x = text.split("\\s+",4);
                    output_text = this.create(x[1], x[2], x[3]);
                    System.out.println(output_text);
                    writer.println(output_text);
                }

                //If operation == 2 call Read File
                else if (operation.equals("2"))
                {
                    output_text = this.read(splited[1],splited[2]);
                    writer.println(output_text);
                }

                //If operation == 3 call Delete File
                else if (operation.equals("3"))
                {
                    output_text = this.delete(splited[1],splited[2]);
                    System.out.println(output_text);
                    writer.println(output_text);
                }
                
            }
            socket.close();

        }
        catch (IOException ex)
        {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
