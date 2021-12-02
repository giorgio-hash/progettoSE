package view;

import controller.ControllerUscente;
import prog.io.ConsoleInputManager;

public class ViewUscente {

	private ControllerUscente controller;
	
	private ConsoleInputManager in;
	
	private CardLayoutUscita clu;
	
	public ViewUscente( ControllerUscente controller ) {
		
		this.controller = controller;
		in = new ConsoleInputManager();
		clu = new CardLayoutUscita();
	}
	
	
	
	
	public void richiestaPagamento() throws InterruptedException {
		
		clu.acquire();
		controller.setTicket();
		
		if(controller.activePayment()) {
			clu.getPanel(1).setText("Pagamento abilitato!  Costo: " + controller.getPrezzoTicket());
			System.out.println("PAGAMENTO ABILITATO! Importo: " + controller.getPrezzoTicket());
			String str1 = in.readLine("\nMetodo di pagamento: ");
			double importo = in.readDouble("importo: ") ;
			clu.getPanel(1).setText("Attendi...");
			if( controller.sendPayment(str1,importo) ) {
				clu.getPanel(1).setText("Pagamento completato! Buona giornata!");
				clu.timerNreset(3000);
				System.out.println("pagamento completato! buona giornata!");
			}else {
				clu.getPanel(1).setText("Problemi col pagamento. Riprova");
				clu.timerNreset(3000);
				System.out.println("C'è stato un problema col pagamento: transazione abortita");
			}
		}else {
			System.out.println("Biglietto non riconosciuto");
			clu.getPanel(1).setText("Biglietto non riconosciuto!");
			clu.timerNreset(3000);
		}
		
	}
	
	
}
