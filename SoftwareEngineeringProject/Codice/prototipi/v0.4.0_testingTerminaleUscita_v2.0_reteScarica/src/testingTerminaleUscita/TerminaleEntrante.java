package testingTerminaleUscita;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

public class TerminaleEntrante {

	private static final int PORT = 1051; 
	private static Semaphore pulsante = null;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Ticket ticket = null;
		pulsante = new Semaphore(0);
		
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

	        // creazione stream di output su socket
	        OutputStreamWriter osw = new OutputStreamWriter( socket.getOutputStream() );
	        BufferedWriter bw = new BufferedWriter(osw);
	        out = new PrintWriter(bw, true);

	        //ObjectOutputStream obj_out = new ObjectOutputStream( socket.getOutputStream() );
		    ObjectInputStream obj_in = new ObjectInputStream( socket.getInputStream() );
		    
		    
	      //***************************FINE ZONA VARIABILI**********************************
	        

	        // QUI INIZIA IL VERO PROGRAMMA
	        
	        
	        Thread.sleep(10000); //TEMPO DI SETUP (per garantire la sincronia)
	        
	        
	        while (true){
	           
	        	System.out.println("attendo ticket...");
	        	ticket = (Ticket)obj_in.readObject();//attendo dati dal thread AutoEntrante
	        	
	        	//ticket conservato nel buffer, fino a che un cliente non preme un pulsante
	        	//Bisogna rendere bloccante l'attesa del tiket!!(semaforo?)
	        	System.out.println("ticket disponibile");
	        	SleepUtilities.nap();
	        	pulsantePremuto();
	        	
	        	
	        	attendiPulsante();
	        	//terminale stampa, cliente riceve il biglietto
	        	
		    	out.println(1);
		    	out.flush();
		   		
	        }
	    }
	    catch (UnknownHostException e) {
	        System.err.println("Don't know about host "+ addr);
	        System.exit(1);
	    } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to: " + addr);
	        System.exit(1);
	    } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    finally{
		    System.out.println("EchoClient: closing...");
		    out.close();
		    in.close();
		    socket.close();
	    }
	

	}
	
	
	
	
	private static void attendiPulsante() throws InterruptedException {
		
		pulsante.acquire();
		
	}
	
	//questo metodo viene chiamato solo all'esterno!!
	public static void pulsantePremuto() {
		
		pulsante.release();
		stampaTicket();
	}
	
	
	public static void stampaTicket() {
		
		System.out.println("Ticket stampato");
		//qui dovrebbe esserci il codice per l'attivazione del componente di stampa ticket
		
	}
	
	

}
