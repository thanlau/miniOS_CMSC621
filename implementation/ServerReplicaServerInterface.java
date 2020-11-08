
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList; 

public interface ServerReplicaServerInterface extends Remote{
	/**
	 * @param txnID: the transaction ID of the msg
	 * @param seqNum: the msg sequence number beginning with 1
	 * @param data: data to be write into a file
	 * @return message with required info
	 * @throws IOException
	 * @throws RemoteException
	 */
    public boolean updateFile(String user, String filename, String[] content) throws RemoteException, IOException;


}
