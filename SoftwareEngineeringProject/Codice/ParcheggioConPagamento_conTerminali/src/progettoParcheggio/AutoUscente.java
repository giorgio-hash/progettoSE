package progettoParcheggio;

import java.io.IOException;
import java.net.ServerSocket;

public class AutoUscente implements Runnable
{
	private ServerSocket serverSocket;
	private Parcheggio buffer;
	private final int PORT = 1051;
	
   public AutoUscente(Parcheggio b) {
      buffer = b;
   }
   
   public void run()
   {
	   while (true) {
	   
	   
	   try {
		serverSocket = new ServerSocket(PORT);
	

			System.out.println("EchoServer: started ");
			System.out.println("Server Socket: " + serverSocket);
		
		
		
		 
			 buffer.remove();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
}
