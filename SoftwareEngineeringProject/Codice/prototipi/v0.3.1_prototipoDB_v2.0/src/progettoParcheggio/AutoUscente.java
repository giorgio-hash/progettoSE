package progettoParcheggio;

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
import java.time.LocalDateTime;

/**
 * 
 * @author Giorgio Chirico, Raffaele Di Maio
 *
 *La classe si occupa della comunicazione del sistema centrale col terminale per il ritiro dei biglietti e la
 *gestione delle transazioni
 */
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
      System.out.println("EchoServer D'USCITA: started ");
	  System.out.println("Server Socket D'USCITA: " + serverSocket);
   }
   
   
   public void run(){
		  
		try {
			// Bloccante finchè non avviene una connessione
		    clientSocket = serverSocket.accept();
		    System.out.println("(SERVER_USCITA) Connection accepted: "+ clientSocket);
		    
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
		    
		    ObjectOutputStream obj_out = new ObjectOutputStream( clientSocket.getOutputStream() );
		    ObjectInputStream obj_in = new ObjectInputStream( clientSocket.getInputStream() );
		    
		  //***************************FINE  ZONA VARIABILI**********************************
		    
		    
		    
		    //testing della connessione tra terminale e Thread
		    //ciclo di ricezione dal client e invio di risposta
		    while (true) {
		    	
		    	
		    	//SIMULAZIONE
		    	
		    	SleepEntrante.nap();
		    	
		    	
		    	
		    	/* N.B.: per una migliore simulazione, è consigliabile far partire il terminale d'uscita 
		    	* dopo almeno 5 macchine entrate: la lista static di ParcheggioSimulato non gestisce ancora
		    	* correttamente i conflitti, quindi è normale che molti biglietti inseriti avranno esito 2
		    	* (non riconosciuto) poichè c'è asincronia rispetto al buffer di Parcheggio
		    	* (in poche parole, diversi biglietti dati al terminale d'uscita, nella simulazione, saranno
		    	* "biglietti fantasma" che di fatto appartengono a gente che è già uscita dal parcheggio! )
		    	*/
		    	
					ParcheggioSimulato.semaforo.acquire(); //LA SIMULAZIONE DEVE FUNZIONARE SOLO SE ESISTONO BIGLIETTI
			    	if( ParcheggioSimulato.parcheggio.size() > 0 ) {
			    		System.out.println("THREAD D'USCITA : ParcheggioSimulato.parcheggio.size() = " + ParcheggioSimulato.parcheggio.size());
			   			ticket = ParcheggioSimulato.parcheggio.get( (int)(Math.random()*ParcheggioSimulato.parcheggio.size()) );	
			    	
			   			obj_out.writeObject(ticket);
				   		obj_out.flush();
				    	//SIMULAZIONE
				   		ParcheggioSimulato.semaforo.release();
				   		
				    	ticket = (Ticket)obj_in.readObject(); //attendo dati dal terminale
				    	
				    	if(buffer.check4Remove(ticket)) 
		   					out.println( buffer.remove(ticket) );
				    	else
				    		out.println( 2 );
				    		
				    	out.flush();
				    	//codici di protocollo: 0=pagamento avvenuto , 1=problemi nel pagamento, 2=ticket non riconosciuto
			    	}
			    	else ParcheggioSimulato.semaforo.release();
			   	
		    }
	  }
	  catch(IOException | ClassNotFoundException e){
	    System.err.println("Accept failed");
	    System.exit(1);
	  } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
