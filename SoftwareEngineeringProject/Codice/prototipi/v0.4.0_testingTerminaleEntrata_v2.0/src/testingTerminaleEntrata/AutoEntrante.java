package testingTerminaleEntrata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AutoEntrante implements Runnable
{

	public static final int PORT = 1051; 
	
	private Ticket ticket = null;
	//private Parcheggio buffer;
	private ServerSocket serverSocket = null;
	
	//Dichiarazioni di alcune variabili locali per gli stream di I/O e il socket client
	private Socket clientSocket=null;
	private BufferedReader in=null;
	
	/*
   public AutoEntrante(Parcheggio b) throws IOException {
      buffer = b;
      serverSocket = new ServerSocket(PORT);
      System.out.println("EchoServer D'ENTRATA: started ");
	  System.out.println("Server Socket D'ENTRATA: " + serverSocket);
   }
	 */
	
	public AutoEntrante() throws IOException {
		
		serverSocket = new ServerSocket(PORT);
	      System.out.println("EchoServer D'ENTRATA: started ");
		  System.out.println("Server Socket D'ENTRATA: " + serverSocket);
		
	}
	
   
   public void run()
   {
	// Bloccante finch� non avviene una connessione
	    try {
	    	
			clientSocket = serverSocket.accept();
		
			System.out.println("(SERVER_ENTRATA)Connection accepted: "+ clientSocket);
		    
			  //***************************ZONA VARIABILI**********************************
			    
			    // Creazione stream di input da clientSocket
			    // Creiamo uno stream di caratteri a partire dallo stream di byte associato alla socket.
			    InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
			    // Creiamo uno stream che fornisce meccanismo di bufferizzazione (per avere maggiore efficienza nell'operazione di I/O)
			    in = new BufferedReader(isr);
			    
			    ObjectOutputStream obj_out = new ObjectOutputStream( clientSocket.getOutputStream() );
			   
			   String esito;
			  //***************************FINE  ZONA VARIABILI**********************************
			    
			    
			    
			    //testing della connessione tra terminale e Thread
			    //ciclo di ricezione dal client e invio di risposta
			   while (true) {
				   
				   System.out.println("\nGenerazione ticket...");
				   //ticket = buffer.generateTicket();
				   ticket = new Ticket();
				   
				   System.out.println("Ticket generato: " + ticket);
				   //qui il ticket verrebbe inviato al terminale d'entrata
				   obj_out.writeObject(ticket);
				   obj_out.flush();
				   
				   
				   esito = in.readLine();
				   //una volta preso, il ticket restituisce qui un msg di avvenuta ricezione
				   if(Integer.parseInt(esito) == 1 )
				   {
					 //viene chiamata la funzione per l'aggiornamento del buffer di parcheggio
					   System.out.println("Ticket preso!");
					   //buffer.insert(ticket);
				   }
				   
				   
				   
			   }
			
	    
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
   }
}