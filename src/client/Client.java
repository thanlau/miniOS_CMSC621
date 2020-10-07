/**
 * 
 */
package client;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.util.Scanner;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import baseinterface.FileContent;
import baseinterface.MessageNotFoundException;
import baseinterface.ReplicaLoc;
import baseinterface.ReplicaServerClientInterface;
import baseinterface.WriteMsg;


public class Client {
	
	public Client() {
		/**
		 * TODO: Any actions needed
		 */
	}
	
	public void read(String filename) {
		/**
		 * Connection with replca to read file
		 */
	}
	
	public WriteResponse write(FileContent file) {
		/**
		 * Connection with replica to write. 
		 * @param a generic FileContent object
		 * @return WriteResponse object
		 * 
		 */
		return null;
	}
	
	public boolean commit(WriteResponse response) {
		/**
		 * At the end of the transaction, the client issues a commit request
		 */
		boolean successCommit = false;
		return successCommit;
		
		
	}
	
	public boolean abort(WriteResponse response) {
		/**
		 * TODO client can abort the transaction
		 */
		return false;
	}
	
	public String delete(String filename) {
		/**
		 * TODO @return a string "r", where r = 0 for success, 1 for error.
		 */
		return "r";
	}
	
	public String upload(String filename, String path) {
		/**
		 * @param filename in local machine
		 * @param upload path in replica
		 * TODO @return a string "r", where r = 0 for success, 1 for error.
		 */
		return "path";
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
	
	public ReplicaServerClientInterface gethandle(ReplicaLoc primrayReplica) throws IOException{
		/**
		 * TODO return replica info
		 * Below is the example from AmrHendy
		 */
//		String replicaName = primrayReplica.getName();
//		String replicaAdd = primrayReplica.getIp();
//		int replicaPort = primrayReplica.getPort();
//		System.setProperty("java.rmi.server.hostname", replicaAdd);
//		Registry reg = LocateRegistry.getRegistry(replicaAdd, replicaPort);
//		return (ReplicaServerClientInterface) reg.lookup(replicaName);
	}
	
	public void executeTransaction(String transactionFilePath) throws IOException{
		/**
		 * @TODO read the transaction, then execute it
		 */
	}
	
	

}
