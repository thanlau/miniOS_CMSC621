import java.io.*;
import java.net.*;
import java.util.logging.*;

public class ServerThread extends Thread
{
    private Socket socket;
    private static String PATH = "..//Directory";

    public ServerThread(Socket socket)
    {
        this.socket = socket;
    }
    //function to record write opearation log
    public static synchronized void log(String command, String transactionID, String sequenceNumber, String data) 
    {
	assert(file != null);
		
	String message = command + " " + transactionID + " " + sequenceNumber + " " + data.length() + "\n" + data; 
		
	try {
		file.append(message);
	} catch (FileNotFoundException e) {
		System.out.println("File not found error occured when adding \"" + message + "\" to recovery log");
	} catch (IOException e) {
		System.out.println("IO error occured while adding \"" + message + "\" to recovery log");
	}
    }
    //function to delete log
    public static void deleteLog() {
	String directoryName = PATH.concat("/"+user);
	File logFile = new File(directoryName + file.getFileName());
	logFile.delete();
		
    }

    //Function to write Logs
    private static void log(String user, String filename, String operation, String message) throws IOException, SecurityException
    {
        String directoryName = PATH.concat("/"+user);
        File directory = new File(directoryName);
        if(! directory.exists())
            directory.mkdir();
        boolean append = true;
        FileHandler handler = new FileHandler(directoryName + "/"+ user + ".log", append);
        Logger logger = Logger.getLogger("UserLog");
        logger.addHandler(handler);
        logger.setUseParentHandlers(false);
        logger.info(operation + "    " + filename + "    " +message);

    }

    // Function to Create File
    private String create(String user, String filename, String content) throws IOException
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
        log(user,filename,"Create","File Created Successfully");
        return "File Created Successfully";
    }

    // Function to Read File
    private String read(String user, String filename) throws IOException
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
                log(user,filename,"Read","File Read Successfully");
                return content;
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            log(user,filename,"Read","File Not Present");
            return "File Not Present";
        }
        return "File Not Present";
    }

    // Function to Delete File
    private String delete(String user, String filename) throws IOException
    {
        String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".txt";
        File file = new File(directoryName + "/" + fileName);

        //If file present then it deletes the file
        if (file.exists())
        {
            String tempName = "..//Deleted/" + user;
            File directory = new File(tempName);
            if (! directory.exists())
                directory.mkdir();
            file.renameTo(new File(tempName + "/"+file.getName()));
            log(user,filename,"Delete","File Deleted Successfully");
            return "File Deleted Successfully";
        }
        else
        {
            log(user,filename,"Delete","File Not Present");
            return "File Not Present";
        }
    }
    

    //Function to Restore File
    private String restore(String user, String filename) throws IOException
    {
        String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".txt";
        File file = new File("..//Deleted/" + user+"/"+filename + ".txt");
        if (file.exists())
        {
            file.renameTo(new File(directoryName + "/" + fileName));
            log(user,filename,"Restore","File Restored");
            return "File Restored";
        }
        else
        {
            log(user,filename,"Restore","File Doesn't Exist");
            return "File Doesn't Exist";
        }
        
    }

    //Function to Write File
    private String write(String user, String filename, String content) throws IOException{
    	String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".txt";
        File directory = new File(directoryName);
        String[] filetext = content.split("\\$\\%\\^");

        //If directory doesn't exist then it creates
        if (! directory.exists())
        {
            directory.mkdir();
        }
        
        File file = new File(directoryName + "/" + fileName);
        file.getParentFile().mkdirs();
        file.createNewFile();   	
	    try{
	        FileWriter fw = new FileWriter(file, true);
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

        return "File Wrote Successfully";
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
                //If operation == 4 call Write/Append File
                else if(operation.equals("4")) {
                	 String[] x = text.split("\\s+",4);
                     output_text = this.write(x[1], x[2], x[3]);
                     System.out.println(output_text);
                     writer.println(output_text);
                }
                //If operation == 5 call Restore File
                else if(operation.equals("5"))
                {
                    output_text = this.restore(splited[1],splited[2]);
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
