package testingExodia;

import prog.io.ConsoleInputManager;

public class ViewUscente {

	private ControllerUscente controller;
	
	private ConsoleInputManager in;
	
	
	public ViewUscente( ControllerUscente controller ) {
		
		this.controller = controller;
		in = new ConsoleInputManager();
		
	}
	
	public void richiestaPagamento() throws InterruptedException {
		
		controller.setTicket();
		
		if(controller.activePayment()) {
			System.out.println("PAGAMENTO ABILITATO!");
			if( controller.sendPayment(in.readLine("\nIBAN: "), in.readLine("password: ") ) )
				System.out.println("pagamento completato! buona giornata!");
			else
				System.out.println("C'è stato un problema col pagamento: transazione abortita");
		
		}else
			System.out.println("Biglietto non riconosciuto");
		
		
	}
	
}
