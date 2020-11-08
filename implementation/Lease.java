import java.io.*;
import java.util.logging.*;

public class Lease extends Thread
{
    private String user;
    private String filename;
    private static String PATH = "..//Directory";
    //private volatile boolean done = false;
    private Thread blinker;

    public Lease(String user, String filename)
    {
        this.user = user;
        this.filename = filename;
        
    }

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


            unlock = true;
            log(user,filename,"LeaseThread","Unlock Successful. Deleted by Lease Thread");
        }
        return unlock;
    }

    public void run()
    {
        Thread lease = Lease.currentThread();
        blinker = lease;
        while (blinker == lease)
        {
        try
            {
                Thread.sleep(30000);
                if (blinker != lease)
                    break;
                //System.out.println("Timer Working");
                unlock(user,filename);
                break;

            }
            catch(InterruptedException e)
            {
                System.out.println("Thread Stopped");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    

    public void shutdown()
    {
        blinker = null;
        //System.out.println("Thread Stopped");
    }
    
}
