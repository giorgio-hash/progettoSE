package testingExodia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

public class SocketUscente extends Thread{

	public static final int PORT = 1050; 
	
	private Ticket ticket = null;
	
	private Socket socket=null;
    private BufferedReader in=null;
    private PrintWriter out=null;
    
    private InetAddress addr;
    
    private Semaphore block;
    private Semaphore risp;
    
    private String str;
    private String dati_pagamento;
    
    public SocketUscente(String[] args) throws UnknownHostException {
    	
    	if (args.length == 0) addr = InetAddress.getByName(null);
		else addr = InetAddress.getByName(args[0]);
    	
    	block = new Semaphore(0);
    	risp = new Semaphore(0);
    } 
    
	
	@Override
	public void run(){
		// TODO Auto-generated method stub
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
	
		        ObjectOutputStream obj_out = new ObjectOutputStream( socket.getOutputStream() );
			    ObjectInputStream obj_in = new ObjectInputStream( socket.getInputStream() );
		        
		      //***************************FINE ZONA VARIABILI**********************************
		        
	
		        // QUI INIZIA IL VERO PROGRAMMA
		        
		        
		        Thread.sleep(10000); //TEMPO DI SETUP (per garantire la sincronia)
		        
		        
		        while (true){
		        	
		        	block.acquire();
		        	obj_out.writeObject(ticket);
		        	
		        	//N.B.: socket realizzato con l'assunto di una connessione perfetta
		        	
		        	str = in.readLine();
		        	risp.release();
		        	
		        	//N.B.: tra le intenzioni future ci sarebbe il criptaggio dati
		        	if(Integer.parseInt(str) == 1) {

			        	block.acquire();
			        	out.println(dati_pagamento);
			        	
			        	str = in.readLine();
			        	risp.release();	
		        		
		        	}
		        }
		    }
		    catch (UnknownHostException e) {
		        System.err.println("Don't know about host "+ addr);
		        System.exit(1);
		    } catch (IOException e) {
		        System.err.println("Couldn't get I/O for the connection to: " + addr);
		        System.exit(1);
		    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    finally{
			    System.out.println("EchoClient: closing...");
			    out.close();
			    try {
					in.close();
					socket.close();
			    } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
		    }
	}	    
	
	
	// CHIAMATO ALL'ESTERNO DA Controller
	 public void setTicket( Ticket ticket ) {
	    	
	    	this.ticket = ticket;
	    	block.release();
	    	
	    }
	 
	 public String response() throws InterruptedException {
		 risp.acquire();
		 return str;
		 
	 }
	 
	 public void sendPayment(String IBAN, String pswd) {
		 
		 dati_pagamento = "IBAN:"+IBAN+":PSWD:"+pswd;
		 block.release();
		 
	 }
	 
}
