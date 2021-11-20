package testingExodia;

import prog.io.ConsoleInputManager;

public class ControllerUscente {
	
	private SocketUscente socket;
	
	private ConsoleInputManager in;
	
	private float prezzoTicket;
	
	
	public ControllerUscente(SocketUscente socket) {
		
		this.socket = socket;
		
		in = new ConsoleInputManager();
		
	}
	
	
	//inserisco da console perch� non ho un lettore carte...
	public void setTicket( ) {
		
		Ticket ticket = new Ticket(in.readLine("codice biglietto: "));
		
		socket.setTicket(ticket);
		
	}
	
	public boolean activePayment() throws InterruptedException {
		
		prezzoTicket = Float.parseFloat(socket.response());
		if(prezzoTicket != -1) 
			return true;
		else
			return false; //ticket non riconosciuto
		
	}
	
	
	public float getPrezzoTicket() {
		
		return prezzoTicket;
		
	}
	
	
	
	public boolean sendPayment(String metodo, double somma) throws InterruptedException {
		
		socket.sendPayment(metodo,somma);
		
		if(Integer.parseInt(socket.response()) == 1)
			return true;
		else
			return false; //problemi nel pagamento
		
	}

}
