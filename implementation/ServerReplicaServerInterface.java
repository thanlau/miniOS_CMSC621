
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList; 

public interface ServerReplicaServerInterface extends Remote{
	
 public boolean updateFile(String user, String filename, String content) throws RemoteException, IOException;
 
 public boolean createFile(String user, String filename, String content) throws RemoteException,IOException;
 
 public boolean deleteFile(String user, String filename) throws RemoteException,IOException;
 
 public boolean restoreFile(String user, String filename) throws RemoteException,IOException;
 
 public boolean appendFile(String user, String filename, String content) throws RemoteException,IOException;

 public boolean lockFile(String user, String filename) throws RemoteException,IOException;


}

