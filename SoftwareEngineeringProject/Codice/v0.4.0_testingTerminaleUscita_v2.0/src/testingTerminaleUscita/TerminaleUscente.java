package testingTerminaleUscita;

import java.net.UnknownHostException;

public class TerminaleUscente {

	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		// TODO Auto-generated method stub

		SocketUscente socket = new SocketUscente(args);
		ControllerUscente controller = new ControllerUscente(socket);
		ViewUscente view = new ViewUscente(controller);
		
		socket.start();
		while(true) {
			
			Thread.sleep((long) (500*Math.random()*10));
			view.richiestaPagamento();
			
			
		}
	}

}
