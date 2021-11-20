package testingTerminaleUscita;

import prog.io.ConsoleInputManager;

public class ControllerUscente {
	
	private SocketUscente socket;
	
	private ConsoleInputManager in;
	
	public ControllerUscente(SocketUscente socket) {
		
		this.socket = socket;
		
		in = new ConsoleInputManager();
		
	}
	
	
	//inserisco da console perchè non ho un lettore carte...
	public void setTicket( ) {
		
		Ticket ticket = new Ticket(in.readLine("codice biglietto: "));
		
		socket.setTicket(ticket);
		
	}
	
	public boolean activePayment() throws InterruptedException {
		

		if(Integer.parseInt(socket.response()) == 1)
			return true;
		else
			return false; //ticket non riconosciuto
		
	}
	
	
	public boolean sendPayment(String IBAN, String pswd) throws InterruptedException {
		
		socket.sendPayment(IBAN,pswd);
		
		if(Integer.parseInt(socket.response()) == 1)
			return true;
		else
			return false; //problemi nel pagamento
		
	}

}
