package testingTerminaleEntrata;

public class ControllerEntrante {
	
	private SocketEntrante socket;
	
	public ControllerEntrante( SocketEntrante socket ) {
		
		this.socket = socket;
		
	}
	
	
	public Ticket erogaTicket() {
		
		System.out.println("macchina stampa il ticket");
		
		Ticket ticket = socket.getTicket();
		
		socket.ticketRequest();
		
		return ticket;
		
	}
	

}
