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
      System.out.println("EchoServer: started ");
	  System.out.println("Server Socket: " + serverSocket);
   }
   
   
   public void run(){
		  
		try {
			// Bloccante finch� non avviene una connessione
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
		    
		    ObjectOutputStream obj_out = new ObjectOutputStream( clientSocket.getOutputStream() );
		    ObjectInputStream obj_in = new ObjectInputStream( clientSocket.getInputStream() );
		    
		    
		    String str ;
		    String[] data;
		    String[] ora; 
		    LocalDateTime time;
		  //***************************FINE  ZONA VARIABILI**********************************
		    
		    
		    
		    //testing della connessione tra terminale e Thread
		    //ciclo di ricezione dal client e invio di risposta
		    while (true) {
		    	
		    	
		    	//SIMULAZIONE
		    	
		    	SleepEntrante.nap();
		    	
		    	
		    	
		    	/* N.B.: per una migliore simulazione, � consigliabile far partire il terminale d'uscita 
		    	* dopo almeno 5 macchine entrate: la lista static di ParcheggioSimulato non gestisce ancora
		    	* correttamente i conflitti, quindi � normale che molti biglietti inseriti avranno esito 2
		    	* (non riconosciuto) poich� c'� asincronia rispetto al buffer di Parcheggio
		    	* (in poche parole, diversi biglietti dati al terminale d'uscita, nella simulazione, saranno
		    	* "biglietti fantasma" che di fatto appartengono a gente che � gi� uscita dal parcheggio! )
		    	*/
		    	try {
					ParcheggioSimulato.semaforo.acquire();
					System.out.println("ParcheggioSimulato.parcheggio.size() = " + ParcheggioSimulato.parcheggio.size());
			    	if( ParcheggioSimulato.parcheggio.size() > 0 )
			   			ticket = ParcheggioSimulato.parcheggio.get( (int)(Math.random()*ParcheggioSimulato.parcheggio.size()) );	
			   		ParcheggioSimulato.semaforo.release();
			   		
		    	}catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	//out.println(ticket); vecchio test
		   		
		   		obj_out.writeObject(ticket);
		   		obj_out.flush();
		    	//SIMULAZIONE
		    	
		    	ticket = (Ticket)obj_in.readObject(); //attendo dati dal terminale
		    	
		    	/*vecchio test per ticket formato string
		    	str = in.readline();
		    	System.out.println("AutoUscente: " +str);  simpatico, per evidenziare l'interleaving tra auto che entrano e che escono
		    	
		    	data = str.split("/")[2].split("T")[0].split("-");
		    	ora = str.split("/")[2].split("T")[1].split(":");
		    	ticket = new Ticket(str.split("/")[1] , LocalDateTime.of(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]),Integer.parseInt(ora[0]),Integer.parseInt(ora[1]),Integer.parseInt("0"),Integer.parseInt("7")));
		    	*/
		    	
		    	if(buffer.check4Remove(ticket)) 
   					out.println( buffer.remove(ticket) );
		    	else
		    		out.println( 2 );
		    		
		    	out.flush();
		    	//codici di protocollo: 0=pagamento avvenuto , 1=problemi nel pagamento, 2=ticket non riconosciuto
		    	
		    }
	  }
	  catch(IOException | ClassNotFoundException e){
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
