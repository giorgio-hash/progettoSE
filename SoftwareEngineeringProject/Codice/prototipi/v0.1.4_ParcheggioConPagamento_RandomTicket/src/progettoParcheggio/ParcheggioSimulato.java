package progettoParcheggio;

import java.util.ArrayList;

public class ParcheggioSimulato {

	// la keyword "static" fa sì che questo array venga condiviso da tutti
	public static ArrayList<Ticket> parcheggio;
	
	
	public ParcheggioSimulato() {
		
	}
	
	public void init() {
		parcheggio = new ArrayList<Ticket>();
	}
	
}
