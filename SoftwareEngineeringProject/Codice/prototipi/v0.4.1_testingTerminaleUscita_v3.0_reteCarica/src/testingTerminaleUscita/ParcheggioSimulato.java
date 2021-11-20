package testingTerminaleUscita;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ParcheggioSimulato {

	// la keyword "static" fa sì che questo array venga condiviso da tutti
	/**
	 * parcheggio statico "virtuale", utile per la simulazione ( simula il comportamento dei clienti )
	 */
	public static ArrayList<Ticket> parcheggio;
	
	public static Semaphore semaforo;
	
	public ParcheggioSimulato() {
		
	}
	
	public void init() {
		parcheggio = new ArrayList<Ticket>();
		semaforo = new Semaphore(1);
	}
	
	
	/**anche parcheggioSimulato ha bisogno della propria funzione di find
	in pratica: i Ticket generati dall'ObjectInputStream sono oggetti con ID diverso da quelli di
	GestoreParcheggio.parcheggio, che a loro volta hanno ID diverso rispetto a
	ParcheggioSimulato.parcheggio. Di conseguenza, devo trovare gli oggetti all'interno di ogni ArrayList
	"per somiglianza" ( noi però sappiamo essere di fatto più copie dello stesso biglietto )
	**/
	public static Ticket find(Ticket ticket){
		   
		   for(int i=0;i<parcheggio.size();i++) {
			   
			   if(  parcheggio.get(i).getBarcode() == ticket.getBarcode()  )
				   return parcheggio.get(i);
			   
		   }
		   
		   return null;
	   }
}
