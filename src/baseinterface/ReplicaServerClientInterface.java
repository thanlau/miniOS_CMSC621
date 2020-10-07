package baseinterface;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.rmi.Remote;
//import java.rmi.RemoteException;

public class ReplicaServerClientInterface {
	/**
	 * 
	 * @param txnID
	 *            : the ID of the transaction to which this message relates
	 * @param msgSeqNum
	 *            : the message sequence number. Each transaction starts with
	 *            message sequence number 1.
	 * @param data
	 *            : data to write in the file
	 * @return message with required info
	 * @throws IOException
	 * @throws RemoteException
	 */
	public WriteAck write(long txnID, long msgSeqNum, FileContent data) {
		return null;
	}
	
	public FileContent read(String fileName) {
		return null;
	}
	
	/**
	 * 
	 * @param txnID
	 *            : the ID of the transaction to which this message relates
	 * @param numOfMsgs
	 *            : Number of messages sent to the server
	 * @return true for acknowledgment
	 * @throws MessageNotFoundException
	 * @throws RemoteException
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	public boolean commit(long txnID, long numOfMsgs) {
		return false;
	}
	
	/**
	 * * @param txnID: the ID of the transaction to which this message relates
	 * 
	 * @return true for acknowledgment
	 * @throws RemoteException
	 */
	public boolean abort(long txnID) throws RemoteException {
		return false;
	}
	
	/**
	 * Read file from server
	 * 
	 * @param fileName
	 * @return the addresses of  of its different replicas
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws RemoteException
	 * @throws MessageNotFoundException 
	 */
	public ReplicaLoc[] read(String fileName);

	/**
	 * Start a new write transaction
	 * 
	 * @param fileName
	 * @return the requiref info
	 * @throws RemoteException
	 * @throws IOException
	 * @throws NotBoundException 
	 * @throws MessageNotFoundException 
	 */
	public WriteMsg write(FileContent data);
	
	public void registerSlaves(String fileName, ArrayList<ReplicaLoc> slaveReplicas);
	
	public boolean checkAlive() throws RemoteException;
}
