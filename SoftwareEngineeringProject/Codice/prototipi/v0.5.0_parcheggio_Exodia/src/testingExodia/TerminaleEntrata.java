package testingExodia;

import java.net.UnknownHostException;

public class TerminaleEntrata {

	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		// TODO Auto-generated method stub
		
		SocketEntrante socket = new SocketEntrante(args);

		ControllerEntrante  controller = new ControllerEntrante(socket);
		
		ViewEntrante view = new ViewEntrante(controller);
		
		socket.start();
		
		while(true) {
			
			Thread.sleep( (long) (700*Math.random()*100) ) ;
			view.premiPulsante();
			
		}
		
		
	}

}
