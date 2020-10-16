import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HeartBeat
{
    public static void sendPingRequest(String ipAddress) throws UnknownHostException, IOException
    {
        InetAddress system = InetAddress.getByName(ipAddress);
        System.out.println("Sending Ping Request to " + ipAddress);
        Date start = new Date();
        if (system.isReachable(5000))
        {
            System.out.println("Host is reachable");
            Date end =new Date();
            System.out.println(end.getTime() - start.getTime());
        }
        else
        {
            System.out.println("Sorry ! We can't reach to this host");
        }
    }

    public static void findip() throws UnknownHostException
    {
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Address:- " + inetAddress.getHostAddress());
        System.out.println("Host Name:- " + inetAddress.getHostName());
    }

    public static void main(String []args) throws IOException, InterruptedException
    {
        while (true)
        {
            sendPingRequest("54.161.145.16");
            sendPingRequest("54.165.226.147");
            findip();
            TimeUnit.SECONDS.sleep(5);
        }
    }
}