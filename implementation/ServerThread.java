

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.*;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  


public class ServerThread extends Thread
{
    private Socket socket;
    private static String PATH = "..//Directory";
    public static Lease lease;
	private Map<String,	 List<ServerReplicaServerInterface> > filesReplicaMap; //replicas where files that this replica is its master are replicated  
	private ArrayList<String> replicaList;
    public ServerThread(Socket socket)
    {
        this.socket = socket;
    }

    //Function to write Logs
    private synchronized static void log(String user, String filename, String operation, String message) throws IOException, SecurityException
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

    public static void replicate(String operation,String user,String filename,String content)
    {
        try {  
            // Getting the registry 
            ArrayList<String> replicaIP = readServers();
            boolean success;
            int port = 50000;
            // Looking up the registry for the remote object 
             for(int i = 0; i < replicaIP.size(); i++) {
                System.setProperty("java.rmi.server.hostname", replicaIP.get(i));
                Registry registry = LocateRegistry.getRegistry(replicaIP.get(i), port); 
                ServerReplicaServerInterface replica = (ServerReplicaServerInterface) registry.lookup("Replica"); 
           
                // Calling the remote method using the obtained object 
                if (operation == "write")
                {
                    success = replica.updateFile(user, filename, content);
                }
                else if (operation == "append")
                {
                    success = replica.appendFile(user, filename, content);
                }
                else if (operation == "create")
                {
                    success = replica.createFile(user, filename, content);
                }
                else if (operation == "delete")
                {
                    success = replica.deleteFile(user, filename);
                }
                else if (operation == "lockfile")
                {
                    success = replica.lockFile(user, filename);
                }
                else if (operation == "restore")
                {
                    success = replica.restoreFile(user, filename);
                }
                if(success = false) {
                   System.out.println("write err happens at" + " replica" + i);
                }
                System.out.println("write success at" + " replica" + i);
            }
            
            // System.out.println("Remote method invoked"); 
         } catch (Exception e) {
            System.err.println("Client exception: " + e.toString()); 
            e.printStackTrace(); 
         } 
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
        if (file.exists())
        {
            log(user,filename,"Create","File Already Exists.");
            return "File Already Exists.To update content use write operation";
        }
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
        replicate("create",user,fileName,content);
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
            replicate("delete",user,fileName,"");
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
            replicate("restore",user,fileName,"");
            return "File Restored";
        }
        else
        {
            log(user,filename,"Restore","File Doesn't Exist");
            return "File Doesn't Exist";
        }
        
    }

    //Function to append File
    private String append(String user, String filename, String content) throws IOException{
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
        if(!(lock(user,filename)))
        {
            return "Locked";
        }
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
        replicate("append",user,fileName,content);
        return "File Wrote Successfully";
    }


    private String write(String user, String filename, String content) throws IOException
    {
        String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".txt";
        String[] filetext = content.split("\\$\\%\\^");
	

        //Creating File
        File file = new File(directoryName + "/" + fileName);

        if ( !(lease.isAlive()))
            return "Lease Period Over. Please Request for Update Again";
        try{
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < filetext.length; i++)
            {
                bw.write(filetext[i]);
                bw.newLine();
            }
            
            bw.close();
            //unlock the file after write
            unlock(user, filename);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        log(user,filename,"Write","File Updated Successfully");
        //call replica
        replicate("write",user,filename,content);
        return "File Updated Successfully";
    }

    // Function to Read File
    private String readforwrite(String user, String filename) throws IOException
    {
         String directoryName = PATH.concat("/"+user);
         String fileName = filename + ".txt";
         File file = new File(directoryName + "/" + fileName);
 
         //If File exists then it reads File else returns File Not Present
         if (file.exists())
         {
            try
            {
                //check if file is locked
                if(!(lock(user,filename)))
                {
                    return "Locked";
                }
                replicate("lockfile",user,filename,"");
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

     //Function to Lock File
     private synchronized static boolean lock(String user, String filename) throws IOException
    {
        String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".lck";
        File file = new File(directoryName + "/" + fileName);
        Boolean lock;
        //Checks if file is locked by some other user
        if (file.exists())
        {
            lock = false;
            log(user,filename,"Lock","File Locked");
        }
        //Else create lock
        else
        {
            file.createNewFile();
            lock = true;
            //Create a timer thread of 10 mins (Lease Period)
            lease = new Lease(user,filename);
            lease.start();
            log(user,filename,"Lock","Lock Successful");
        }
        return lock;
    }

    //Function to Unlock File
    private synchronized static boolean unlock(String user, String filename) throws IOException
    {
        String directoryName = PATH.concat("/"+user);
        String fileName = filename + ".lck";
        File file = new File(directoryName + "/" + fileName);
        Boolean unlock=false;
        
        if (file.exists())
        {
            file.delete();
            //Destroy timer thread of 10 mins (Lease Period Removal)
            lease.shutdown();
            unlock = true;
            log(user,filename,"Unlock","Unlock Successful");
        }
        return unlock;
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
                //System.out.println(text);
                String[] splited = text.split("\\s+");
                String operation = splited[0];
        
                //If operation == 1 call Create File
                if (operation.equals("1"))
                {
                    String[] x = text.split("\\s+",4);
                    output_text = this.create(x[1], x[2], x[3]);
                    //System.out.println(output_text);
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
                    //System.out.println(output_text);
                    writer.println(output_text);
                }
                //If operation == 4 call Append File
                else if(operation.equals("4")) {
                	 String[] x = text.split("\\s+",4);
                     output_text = this.append(x[1], x[2], x[3]);
                     //System.out.println(output_text);
                     writer.println(output_text);
                }
                //If operation == 5 call Restore File
                else if(operation.equals("5"))
                {
                    output_text = this.restore(splited[1],splited[2]);
                    //System.out.println(output_text);
                    writer.println(output_text);
                }
                else if(operation.equals("6"))
                {
                    String[] x = text.split("\\s+",4);
                    output_text = this.write(x[1], x[2], x[3]);
                    //System.out.println(output_text);
                    writer.println(output_text);
                }
                else if(operation.equals("9"))
                {
                    output_text = this.readforwrite(splited[1],splited[2]);
                    //output_text = "Locked";
                    //System.out.println(output_text);
                    writer.println(output_text);
                }
                else if(operation.equals("Over"))
                {
                    //System.out.println("Connection Closed with Client : " + socket.getInetAddress());
                    writer.println("Connection Closed");
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

    public static ArrayList<String> readServers() throws IOException
    {
        String PATH = "AvailableServers.txt";
        File file = new File(PATH);
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<String> ip = new ArrayList<String>();
        String text;
        while ((text=br.readLine()) != null)
        {
            ip.add(text);
        }
        br.close();
        return ip;
        
    }
	
}
