package progettoParcheggio;

import java.io.IOException;
import java.net.ServerSocket;

public class AutoEntrante implements Runnable
{
	
	private ServerSocket serverSocket;
	private Parcheggio buffer;
	private final int PORT = 1050;
	
   public AutoEntrante(Parcheggio b) {
      buffer = b;
   }
   
   public void run()
   {
	   while (true) {
	   
	   
	   try {
		serverSocket = new ServerSocket(PORT);
	

		System.out.println("EchoServer: started ");
		System.out.println("Server Socket: " + serverSocket);
		
		
		
		 
			   buffer.insert();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	}
}