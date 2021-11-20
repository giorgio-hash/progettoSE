package testingExodia;

public class ViewEntrante {
	
	private ControllerEntrante controller;
	
	public ViewEntrante(ControllerEntrante controller) {
		
		this.controller = controller;
		
	}
	
	public void premiPulsante() {
		
		System.out.println( "Ticket erogato: " + controller.erogaTicket() );
		
	}
	

}
