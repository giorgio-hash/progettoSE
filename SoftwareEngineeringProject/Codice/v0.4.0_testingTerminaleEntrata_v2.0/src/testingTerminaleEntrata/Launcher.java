package testingTerminaleEntrata;

import java.io.IOException;

public class Launcher {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Thread autoEntrante = new Thread( new AutoEntrante() );
		
		autoEntrante.start();
		
	}

}
