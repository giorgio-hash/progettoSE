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
		
		float prezzo;
		double importo;
		
		clu.acquire();
		controller.setTicket();
		
		if(controller.activePayment()) {
	
			prezzo = controller.getPrezzoTicket();
			clu.changePanel("Panel 3");
			clu.getPanel(2).setText("Pagamento abilitato!  Costo: " + prezzo + " �");
			System.out.println("PAGAMENTO ABILITATO! Importo: " + prezzo);
			clu.acquire();
			String str1 = ( (ChoiceButtonPanel) clu.getPanel(2)).getChoice();
			clu.changePanel("Panel 2");
			if( str1 == "annulla" ) {  //se si vuole annullare il pagamento...
				System.out.println("metodo di pagamento: " + str1);
				importo = 0 ;
				clu.getPanel(1).setText("Attendi...");
				if( !controller.sendPayment(str1,importo) ) {
					clu.getPanel(1).setText("annullo...");
					clu.timerNreset(3000);
					System.out.println("annullo...");
				}
			}
			else
				{
				clu.getPanel(1).setText("Prego eseguire pagamento in "+ str1 +" di " + prezzo + " �" );
				System.out.println("metodo di pagamento: " + str1);
				 importo = in.readDouble("importo: ") ;
				clu.getPanel(1).setText("Attendi...");	
				if( controller.sendPayment(str1,importo) ) {	
					clu.getPanel(1).setText("Pagamento completato! Erogazione resto: " + (importo - prezzo) );
					clu.timerNreset(3000);
					System.out.println("pagamento completato! Erogazione resto: " + (importo - prezzo) );
							
				}else {
					clu.getPanel(1).setText("Problemi col pagamento. Riprova"); //se si ha inserito fondi insufficienti ( o altri problemi dell'infrastruttura pagamenti ad es.: carta rifiutata )
																				//per il lettore dei contanti si suppone che questo abbia un timer che, allo scadere, d� l'importo inserito in input
					clu.timerNreset(3000);
					System.out.println("C'� stato un problema col pagamento: transazione abortita");
				}			
			}
				
		}else {
			System.out.println("Biglietto non riconosciuto");
			clu.getPanel(1).setText("Biglietto non riconosciuto!");
			clu.timerNreset(3000);
		}
		
	}
	
	
}




