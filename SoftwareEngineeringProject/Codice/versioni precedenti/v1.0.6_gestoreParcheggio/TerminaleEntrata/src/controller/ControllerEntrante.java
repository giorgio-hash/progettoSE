package controller;

import model.SocketEntrante;
import utilities.Ticket;

public class ControllerEntrante {
	
	private SocketEntrante socket;
	
	private Ticket ticket;
	
	
	public ControllerEntrante( SocketEntrante socket ) {
		
		this.socket = socket;
		
	}
	
	
	public Ticket erogaTicket() throws InterruptedException {	
		
		if( socket.getPermesso() == 1 ) {	
			ticket = socket.getTicket();
			socket.ticketRequest();
		}else {
			ticket = null;
		}
		
		setSemaforo(); //metto qui giusto per testare un output
			
		return ticket;
		
	}
	
	//metodo utilizzato dall'oggetto "semaforo"
	public void setSemaforo(){

		if( socket.getPermesso() == 0 )
			System.out.println("SEMAFORO ROSSO");
		else
			System.out.println("SEMAFORO VERDE");	

	}
}

