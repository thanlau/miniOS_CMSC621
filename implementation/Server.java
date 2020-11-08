
import java.io.*;
import java.net.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import Replica;
import ServerReplicaServerInterface;
import client.HeartBeat;

public class Server extends Replica
{
    //This function will start the server at a port number
    public void start_server(int port)
    {
        try (ServerSocket serverSocket = new ServerSocket(port))
        {
            new HeartBeat().start();
            System.out.println("Server is listening on port " + port);
            while (true)
            {
                //Accept connection from Client
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                System.out.println("Client Address : " + socket.getInetAddress());

                // Creating a new thread for each Client connected
                new ServerThread(socket).start();
            }
        }
        catch (IOException ex)
        {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Server server = new Server();

        //The port number is 5000
        server.start_server(5000);
        try {
			bindRMI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void bindRMI() throws IOException {
		String replicaName = "";
		ArrayList<String> replicaIP = readServers();
		int port = 5000;
		try { 
	         // Instantiating the implementation class 
			for(int i = 0; i < replicaIP.size(); i++) {
//				System.setProperty("java.rmi.server.hostname", replicaIP.get(i));
				Replica obj = new Replica(); 
	
		    
		         // Exporting the object of implementation class  
		         // (here we are exporting the remote object to the stub) 
				ServerReplicaServerInterface stub = (ServerReplicaServerInterface) UnicastRemoteObject.exportObject(obj, 0);  
		         
		         // Binding the remote object (stub) in the registry 
		        Registry registry = LocateRegistry.getRegistry(replicaIP.get(i), port); 
		         
		        registry.bind("Replica"+i, stub);  
			}
	         System.err.println("Server ready"); 
	      } catch (Exception e) { 
	         System.err.println("Server exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
	}
	
	public static ArrayList<String> readServers() throws IOException
    {
        String PATH = "serverslist.txt";
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