package client;
import baseinterface.ReplicaLoc;
import baseinterface.ReplicaServerClientInterface;
public class WriteResponse {
	/**
	 * the client first requests a new transaction ID from the primary server
	 * The server will generate and return a unique transaction ID along with the lease period, timestamp
	 * All subsequent write requests in the transaction will have a unique serial number.
	 */
	
	private long transactionId;
	private long messageSeqNumber;
	private ReplicaServerClientInterface replicaServer;
	private String fileName;
	
	public WriteResponse(long transactionId, long messageSeqNumber, ReplicaServerClientInterface replicaServer, String fileName) {
		super();
		this.transactionId = transactionId;
		this.messageSeqNumber = messageSeqNumber;
		this.replicaServer = replicaServer;
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public ReplicaServerClientInterface getReplicaServer() {
		return replicaServer;
	}

	public void setReplicaServer(ReplicaServerClientInterface replicaServer) {
		this.replicaServer = replicaServer;
	}

	public long getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	
	public long getMessageSeqNumber() {
		return messageSeqNumber;
	}
	
	public void setMessageSeqNumber(long messageSeqNumber) {
		this.messageSeqNumber = messageSeqNumber;
	}
	

}
