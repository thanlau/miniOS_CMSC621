
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;

import server.Lease;

public class Replica implements ServerReplicaServerInterface{
	private static String PATH = "..//Directory";
	public static Lease lease;
	

	@Override
	public boolean updateFile(String user, String filename, String[] content) throws RemoteException, IOException {
		// TODO Auto-generated method stub
				String directoryName = PATH.concat("/"+user);
		        String fileName = filename + ".txt";
		        //String[] filetext = content.split("\\$\\%\\^");

		        //Creating File
		        File file = new File(directoryName + "/" + fileName);

		        if ( !(lease.isAlive()))
		            return false;
		        try{
		            FileWriter fw = new FileWriter(file.getAbsoluteFile());
		            BufferedWriter bw = new BufferedWriter(fw);
		            for (int i = 0; i < content.length; i++)
		            {
		                bw.write(content[i]);
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
