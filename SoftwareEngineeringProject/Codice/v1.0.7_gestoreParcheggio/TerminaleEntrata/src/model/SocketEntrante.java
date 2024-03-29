package model;

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

import prog.io.FileInputManager;
import utilities.Ticket;


public class SocketEntrante extends Thread{

	private final int PORT = Integer.parseInt(getAddressName().split(":")[1]);
	
	
	private Semaphore block = null;
	private Semaphore allow = null;
	
	
	private Ticket ticket;
	private int permesso;
	
	private Socket socket=null;
   	 private BufferedReader in=null;
    	private PrintWriter out=null;
    
   	 private InetAddress addr;
	
	
	public SocketEntrante (String[] args) throws UnknownHostException {
		
		 block = new Semaphore(0);
		 allow = new Semaphore(0);
		 
		addr = InetAddress.getByName( getAddressName().split(":")[0] );
			
		 System.out.println(addr);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	   

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
	        
	        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
	        in = new BufferedReader(isr);
		    
		    
	        ObjectOutputStream obj_out = new ObjectOutputStream( socket.getOutputStream() );
		    ObjectInputStream obj_in = new ObjectInputStream( socket.getInputStream() );
		    
		    
	      //***************************FINE ZONA VARIABILI**********************************
	        

	        // QUI INIZIA IL VERO PROGRAMMA
	        
	        
	        Thread.sleep(10000); //TEMPO DI SETUP (per garantire la sincronia)
	        
	        
	        while (true){
	           
			permesso = Integer.parseInt(in.readLine());
			
			System.out.println("permesso: " + permesso);
			
			if( permesso == 1 ){

				ticket = new Ticket();
				allow.release();
				attendiPulsante();

				obj_out.writeObject(ticket);

			}
		   		
	        }
	    }
	    catch (UnknownHostException e) {
	        System.err.println("Don't know about host "+ addr);
	        System.exit(1);
	    } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to: " + addr);
	        System.exit(1);
	    }catch (InterruptedException e) {
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
	
		
		public Ticket getTicket() throws InterruptedException {
		
			allow.acquire();	
			return ticket;
			
		}
		
		private void attendiPulsante() throws InterruptedException {
			
			block.acquire();
			
		}
		
		//questo metodo viene chiamato solo all'esterno!!
		public void ticketRequest() {
			
			System.out.println("rilascio: " + block.availablePermits());
			
			block.release();
			
		
		}


		public int getPermesso(){
			return permesso;
		}

		
		public String getAddressName() {
			
			FileInputManager file = new FileInputManager("destIPen.txt");
			
			String str = file.readLine();
			
			return str;
		}
}
