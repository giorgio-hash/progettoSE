package launcher;

import java.net.UnknownHostException;

import controller.ControllerEntrante;
import model.SocketEntrante;
import view.ViewEntrante;

public class TerminaleEntrata {

	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		// TODO Auto-generated method stub
		
		SocketEntrante socket = new SocketEntrante(args);

		ControllerEntrante  controller = new ControllerEntrante(socket);
		
		ViewEntrante view = new ViewEntrante(controller);
		
		socket.start();
		
		while(true) {
			
			view.premiPulsante();
			
		}
		
		
	}

}
