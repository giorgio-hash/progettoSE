package concorrenziali;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import utilities.Parcheggio;
import utilities.Ticket;

public class AutoEntrante implements Runnable
{

	public static final int PORT = 1051; 
	
	private int permesso;
	private Ticket ticket = null;
	private Parcheggio buffer;
	private ServerSocket serverSocket = null;
	
	//Dichiarazioni di alcune variabili locali per gli stream di I/O e il socket client
	private Socket clientSocket=null;
	private BufferedReader in=null;
	private PrintWriter out=null;
	
   public AutoEntrante(Parcheggio b) throws IOException {
      buffer = b;
      serverSocket = new ServerSocket(PORT);
      System.out.println("EchoServer D'ENTRATA: started ");
	  System.out.println("Server Socket D'ENTRATA: " + serverSocket);
   }

   
   public void run()
   {
	// Bloccante finchè non avviene una connessione
	    try {
			clientSocket = serverSocket.accept();
		
			System.out.println("(SERVER_ENTRATA)Connection accepted: "+ clientSocket);
		    
			  //***************************ZONA VARIABILI**********************************
			    
			    // Creazione stream di input da clientSocket
			    // Creiamo uno stream di caratteri a partire dallo stream di byte associato alla socket.
			    InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
			    OutputStreamWriter osw = new OutputStreamWriter(clientSocket.getOutputStream());
			    // Creiamo uno stream che fornisce meccanismo di bufferizzazione (per avere maggiore efficienza nell'operazione di I/O)
			    in = new BufferedReader(isr);
			    out = new PrintWriter(osw);
			    
			    ObjectOutputStream obj_out = new ObjectOutputStream( clientSocket.getOutputStream() );
			    ObjectInputStream obj_in = new ObjectInputStream( clientSocket.getInputStream() );
			    
			   String esito = null;
			  //***************************FINE  ZONA VARIABILI**********************************
			    
			    
			    
			    //testing della connessione tra terminale e Thread
			    //ciclo di ricezione dal client e invio di risposta
			   while (true) {
				   
				   permesso = buffer.getPermesso(); // bloccante
				   System.out.println("permesso: " + permesso);
				   out.println(permesso);
				   out.flush();
				   if( permesso == 1 ){
				   	ticket = (Ticket) obj_in.readObject();
					buffer.insert(ticket);
				   }else{
					buffer.attendiPermesso();
				   }
				  
				   
			   }
			
	    
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
   }
}