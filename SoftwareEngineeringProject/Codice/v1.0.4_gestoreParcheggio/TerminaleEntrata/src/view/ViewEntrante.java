package view;

import controller.ControllerEntrante;
import utilities.Ticket;

public class ViewEntrante {
	
	private ControllerEntrante controller;
	
	private CardLayoutEntrata cle;
	
	public ViewEntrante(ControllerEntrante controller) {
		
		this.controller = controller;
		cle = new CardLayoutEntrata();
	}
	
	public void premiPulsante() throws InterruptedException {
		
		cle.acquire();
		Ticket ticket = controller.erogaTicket();
		if( ticket != null ){

			cle.getPanel(1).setText("Erogazione ticket in corso...");
			System.out.println( "Ticket erogato: " + ticket);
			cle.timerNreset(3000);
		}else{
			cle.getPanel(1).setText("Parcheggio pieno! Riprova");
			cle.timerNreset(3000);
		}
		
		
		
	}
	

}
