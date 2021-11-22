package testingExodia;

public class ViewEntrante {
	
	private ControllerEntrante controller;
	
	private CardLayoutEntrata cle;
	
	public ViewEntrante(ControllerEntrante controller) {
		
		this.controller = controller;
		cle = new CardLayoutEntrata();
	}
	
	public void premiPulsante() {
		
		cle.acquire();
		cle.getPanel(1).setText("Erogazione ticket in corso...");
		System.out.println( "Ticket erogato: " + controller.erogaTicket() );
		cle.timerNreset(3000);
		
	}
	

}