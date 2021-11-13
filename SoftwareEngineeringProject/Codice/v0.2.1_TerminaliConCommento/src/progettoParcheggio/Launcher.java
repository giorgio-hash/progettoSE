package progettoParcheggio;

import java.io.IOException;

public class Launcher
{
	public static void main(String args[]) {
		Parcheggio server = new GestoreParcheggio();
		
		try {
			Thread entranteThread = new Thread(new AutoEntrante(server));
			Thread uscenteThread = new Thread(new AutoUscente(server));
			
			
	      	uscenteThread.start();
	      	entranteThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	
      	
	}
}

