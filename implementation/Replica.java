
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

//import server.Lease;

public class Replica implements ServerReplicaServerInterface{
	private static String PATH = "..//Directory";
	public static Lease lease;
	

	@Override
	public boolean updateFile(String user, String filename, String content) throws RemoteException, IOException {
		// TODO Auto-generated method stub
				System.out.println("Hello from Update Replica");
				String directoryName = PATH.concat("/"+user);
		        String fileName = filename;
		        String[] filetext = content.split("\\$\\%\\^");

		        //Creating File
		        File file = new File(directoryName + "/" + fileName);


		        //if ( !(lease.isAlive()))
		            //return false;

// 		        if ( !(lease.isAlive()))
// 		            return false;
				
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
		            //unlock(user, filename);
		        }
		        catch (IOException e)
		        {
		            e.printStackTrace();
		        }
		        return true;
		        
			}
	@Override
	public boolean createFile(String user, String filename, String content) throws IOException{
		String directoryName = PATH.concat("/"+user);
        String fileName = filename;
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
//            log(user,filename,"Create","File Already Exists.");
            return false;
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
//        log(user,filename,"Create","File Created Successfully");
        return true;
	}
	@Override
	 public boolean deleteFile(String user, String filename) throws IOException{
		String directoryName = PATH.concat("/"+user);
        String fileName = filename;
        File file = new File(directoryName + "/" + fileName);

        //If file present then it deletes the file
        if (file.exists())
        {
            String tempName = "..//Deleted/" + user;
            File directory = new File(tempName);
            if (! directory.exists())
                directory.mkdir();
            file.renameTo(new File(tempName + "/"+file.getName()));
//            log(user,filename,"Delete","File Deleted Successfully");
            return true;
        }
        else
        {
//            log(user,filename,"Delete","File Not Present");
            return false;
        }
	}
	 @Override
	 public boolean restoreFile(String user, String filename) throws IOException{
		 String directoryName = PATH.concat("/"+user);
	        String fileName = filename;
	        File file = new File("..//Deleted/" + user+"/"+filename);
	        if (file.exists())
	        {
	            file.renameTo(new File(directoryName + "/" + fileName));
//	            log(user,filename,"Restore","File Restored");
	            return true;
	        }
	        else
	        {
//	            log(user,filename,"Restore","File Doesn't Exist");
	            return false;
	        }
	 }
	 @Override
	 public boolean appendFile(String user, String filename, String content) throws IOException{
		 String directoryName = PATH.concat("/"+user);
	        String fileName = filename;
	        File directory = new File(directoryName);
	        String[] filetext = content.split("\\$\\%\\^");

	        //If directory doesn't exist then it creates
	        if (! directory.exists())
	        {
	            directory.mkdir();
	        }
	        
	        File file = new File(directoryName + "/" + fileName);
//	        if(!(lock(user,filename)))
//	        {
//	            return "Locked";
//	        }
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

	        return true;
	 }
	
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
        }
        return unlock;
    }

}
