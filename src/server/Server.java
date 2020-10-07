package server;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.rmi.server.UnicastRemoteObject;
//import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
//import java.util.logging.Level;
//import java.util.logging.Logger;
public class Server {
	
	/**
	 * a client ping all the server and 
	 * choose the server with the lowest ping as the primary server. 
	 * When a new client wants to join the server, 
	 * he first randomly connects the server to download the metadata file. 
	 * Then he pings all the servers to pick the lowest one as the primary 
	 * server.
	 */
	private static final long serialVersionUID = 1L;
	private static String METADATA_FILE_NAME = "files_metadata.txt";
	private static String REPLICA_FILE_NAME = "replicaServers.txt";
	private static int Heart_Beat_Rate = 1000;
	private AtomicInteger transID ,timeStamp;
	
	public Server() throws RemoteException{
		/**
		 * TODO Set enviroments
		 */
	}
	
	@Override
	public ReplicaLoc[] read(String fileName){
		/**
		 * @param fileName
		 * @return primary replica of given file
		 * check if the primary replica server is not available, then choose another primray replica for that file
		 */
		return ReplicaLoc[0];
	}
	
	private void assignNewPrimraryReplica(String fileName) {
		/**
		 * @param fileName
		 * handle when primay server fails, assign a new one
		 */
	}
	
	@Override
	public WriteMsg write(FileContent data) {
		/**
		 * @param fileConet object
		 * @return writeMsg
		 * Do write opeartion
		 */
	}
	@Override
	public FileContent read(String fileName) {
		/**
		 * @param fileName
		 * Files are read entirely. 
		 * When a client sends a file name to the primary replicaServer, 
		 * the entire file is returned to the client. 
		 * The client initially contacts the master node to 
		 * get the IP of the replicaServer acting as primary for the file that it wants to read. 
		 */
	}
	public boolean commit(long txnID, long numOfMsgs) {
		/**
		 * @param txnID, numOfMsgs
		 * @return true-success
		 * 
		 */
	}
	public boolean updateFile(String fileName, ArrayList<byte[]> chunkData) {
		/**
		 * @param fileName, chunkData-set 16kb first
		 * @return true-success
		 */
	}
	public boolean abort(long txnID) {
		/**
		 * @param success abort always as no update in done on the other replicas
		 * @return true-success
		 */
	}
	public String updateLoad(String fileName, String path) {
		/**
		 * @param fileName, upload path
		 * @return a string "r", where r = 0 for success, 1 for error.
		 */
	}
	public String delete(String filename) {
		/**
		 * TODO @return a string "r", where r = 0 for success, 1 for error.
		 */
		return "r";
	}
	public String open(String filename){
		/**
		 * TODO open a file.
		 * @param filename
		 * @return null
		 */
		return null;
	}
	
	public String close(String filename) {
		/**
		 * TODO close a file by given filename
		 * @param filename in replca
		 * @return null
		 */
		return null;
	}
	
	public String findLocation() {
		/**
		 * @return @return a string "r", where r = replica IP address for success, 1 for error.
		 */
		return "r";
	}
	private ReplicaLoc[] getRandomReplica() {
		/**
		 * Use for client first connect with random server to get replica locations
		 * @return replica locations
		 */
	}
	
	private void runAllReplicas() {
		/**
		 * filling the maps from the presistant metadata files on disk
		 * reading the replica servers metadata
		 */
	}
	class HeartBeatTask extends TimerTask{
		@Override
		public void run() {
		/**
		 * check heartbeat works
		 */
		}
	}


}
