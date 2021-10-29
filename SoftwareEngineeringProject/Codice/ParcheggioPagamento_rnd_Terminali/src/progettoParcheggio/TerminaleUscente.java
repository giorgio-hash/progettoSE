package progettoParcheggio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

public class TerminaleUscente {

	public static final int PORT = 1050; 
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	    
		Ticket ticket = null;
		
		Socket socket=null;
	    BufferedReader in=null;
	    PrintWriter out=null;
	    
	    InetAddress addr;
	    
		if (args.length == 0) addr = InetAddress.getByName(null);
		else addr = InetAddress.getByName(args[0]);
		    
		    try {
		    	
		    	//***************************ZONA VARIABILI**********************************
			  
		        // creazione socket
		    	socket = new Socket(addr, PORT);
		        
		        System.out.println("EchoClient: started");
		        System.out.println("Client Socket: "+ socket);
	
		        // creazione stream di input da socket
		        InputStreamReader isr = new InputStreamReader( socket.getInputStream() );
		        in = new BufferedReader(isr);
	
		        // creazione stream di output su socket
		        OutputStreamWriter osw = new OutputStreamWriter( socket.getOutputStream() );
		        BufferedWriter bw = new BufferedWriter(osw);
		        out = new PrintWriter(bw, true);
	
		        // creazione stream di input da tastiera per la lettura da tastiera
		        //stdIn = new BufferedReader(new InputStreamReader(System.in));
		        //String userInput;
		        int esito;
		        String str ;
			    String[] data;
			    String[] ora; 
			    LocalDateTime time;
		      //***************************FINE ZONA VARIABILI**********************************
		        
	
		        // ciclo di lettura da tastiera, invio al server e stampa a video della risposta
		        // E QUI INIZIA IL VERO PROGRAMMA
		        while (true){
		            /*userInput = stdIn.readLine();
		            out.println(userInput);
		            if (userInput.equals("END")) break;
		            System.out.println("Echo: " + in.readLine());
		        	*/
		        	
		        	SleepEntrante.nap();
		        	
		        	
		        	//SIMULAZIONE
		        	str = in.readLine(); //attendo dati dal thread AutoUscente
			    	System.out.println("terminale: " + str);
		        	data = str.split("/")[2].split("T")[0].split("-");
			    	ora = str.split("/")[2].split("T")[1].split(":");
			    	str = str.split("/")[1];
			    	time = LocalDateTime.of(Integer.parseInt(data[0]),Integer.parseInt(data[1]),Integer.parseInt(data[2]), Integer.parseInt(ora[0]), Integer.parseInt(ora[1]));
			    	ticket = new Ticket( str, time );
			    	// SIMULAZIONE
			    	
			    	
			    	//CLIENTE D� BIGLIETTO AL TERMINALE. ATTENDE PROCESSO
			   		out.println(ticket);
			   				
			   		str = in.readLine();
			   		System.out.println("esito: " + str);
			   		esito = Integer.parseInt(str);
				   	if(esito == 0)
				   		System.out.println("\npagamento concluso, buona giornata\n");
				   	else if( esito == 1 )
				   		System.out.println("\nproblemi nel pagamento, riprova\n");
				   	else if( esito == 2 )
				   		System.out.println("\nbiglietto non riconosciuto, riprova\n");		
		        	
		        }
		    }
		    catch (UnknownHostException e) {
		        System.err.println("Don't know about host "+ addr);
		        System.exit(1);
		    } catch (IOException e) {
		        System.err.println("Couldn't get I/O for the connection to: " + addr);
		        System.exit(1);
		    }
		    finally{
			    System.out.println("EchoClient: closing...");
			    out.close();
			    in.close();
			    socket.close();
		    }
		
	}	    
}
