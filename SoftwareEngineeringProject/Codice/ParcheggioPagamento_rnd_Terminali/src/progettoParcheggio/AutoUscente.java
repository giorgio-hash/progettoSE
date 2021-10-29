package progettoParcheggio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class AutoUscente implements Runnable
{

	public static final int PORT = 1050; 
	
	private Ticket ticket = null;
	private Parcheggio buffer;
	private ServerSocket serverSocket = null;
	
	//Dichiarazioni di alcune variabili locali per gli stream di I/O e il socket client
	private Socket clientSocket=null;
	private BufferedReader in=null;
	private PrintWriter out=null;
		
	
   public AutoUscente(Parcheggio b) throws IOException {
      buffer = b;
      serverSocket = new ServerSocket(PORT);
      System.out.println("EchoServer: started ");
	  System.out.println("Server Socket: " + serverSocket);
   }
   
   
   public void run(){
		  
		try {
			// Bloccante finchè non avviene una connessione
		    clientSocket = serverSocket.accept();
		    System.out.println("Connection accepted: "+ clientSocket);
		    
		  //***************************ZONA VARIABILI**********************************
		    
		    // Creazione stream di input da clientSocket
		    // Creiamo uno stream di caratteri a partire dallo stream di byte associato alla socket.
		    InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
		    // Creiamo uno stream che fornisce meccanismo di bufferizzazione (per avere maggiore efficienza nell'operazione di I/O)
		    in = new BufferedReader(isr);
	
		    //Similmente: creazione stream di output su clientSocket (per l'echo!)
		    OutputStreamWriter osw = new OutputStreamWriter(clientSocket.getOutputStream());
		    BufferedWriter bw = new BufferedWriter(osw);
		    out = new PrintWriter(bw, true);
		    
		    String str ;
		    String[] data;
		    String[] ora; 
		    LocalDateTime time;
		  //***************************FINE  ZONA VARIABILI**********************************
		    
		    
		    
		    //testing della connessione tra terminale e Thread
		    //ciclo di ricezione dal client e invio di risposta
		    while (true) {
		    	
		    	
		    	//SIMULAZIONE
		    	if( ParcheggioSimulato.parcheggio.size() > 0 ){
		   			ticket = ParcheggioSimulato.parcheggio.get( (int)(Math.random()*ParcheggioSimulato.parcheggio.size()) );	
		    	out.println(ticket);
		    	//SIMULAZIONE
		    	
		    	
		    	str = in.readLine(); //attendo dati dal terminale
		    	System.out.println("AutoUscente: " +str);
		    	data = str.split("/")[2].split("T")[0].split("-");
		    	ora = str.split("/")[2].split("T")[1].split(":");
		    	ticket = new Ticket(str.split("/")[1] , LocalDateTime.of(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(ora[0]),Integer.parseInt(ora[1]),Integer.parseInt("0"),Integer.parseInt("7")));
		    	
		    	if(buffer.check4Remove(ticket)) 
   					out.println( buffer.remove(ticket) );
		    	else
		    		out.println( 2 );
		    		
		    	//codici di protocollo: 0=pagamento avvenuto , 1=problemi nel pagamento, 2=ticket non riconosciuto
		    	}
		    }
	  }
	  catch(IOException e){
	    System.err.println("Accept failed");
	    System.exit(1);
	  }
	  // chiusura di stream e socket
	  finally{
	    System.out.println("EchoServer: closing...");
	    try {
	    	out.close();
		    in.close();
			clientSocket.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
   }
}
