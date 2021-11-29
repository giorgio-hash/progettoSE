package controller;

import model.SocketEntrante;
import utilities.Ticket;

public class ControllerEntrante {
	
	private SocketEntrante socket;
	
	public ControllerEntrante( SocketEntrante socket ) {
		
		this.socket = socket;
		
	}
	
	
	public Ticket erogaTicket() throws InterruptedException {
		
		System.out.println("macchina stampa il ticket");
		
		Ticket ticket = socket.getTicket();
		
		socket.ticketRequest();
		
		return ticket;
		
	}
	

}
