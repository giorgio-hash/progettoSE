package progettoParcheggio;

public class Launcher
{
	public static void main(String args[]) {
		Parcheggio server = new GestoreParcheggio();

      		// now create the producer and consumer threads
      		Thread entranteThread = new Thread(new AutoEntrante(server));
      		Thread uscenteThread = new Thread(new AutoUscente(server));

      		entranteThread.start();
      		uscenteThread.start();
	}
}

