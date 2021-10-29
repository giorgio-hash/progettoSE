package progettoParcheggio;

import java.time.LocalDateTime;

public class GestorePagamento {
	
	
	final float tariffa = 0.5f;
	
	
	
	public GestorePagamento() {
		
		
	}
	
	public boolean pagamento(float conto) {
		
		//int esito = (int) (Math.random()*2);
		int esito = 1;
		
		if( esito == 1 )
			return true;
		else
			return false;
		
	}
	
	
	// Metodo per il calcolo del prezzo del ticket
	public float prezzoTicket(String ticketTime) {
			
			// Stringhe per conversione tempo del ticket erogato
			String[] tempoTT = ticketTime.split("T")[1].split(":");
			int oreTT = Integer.parseInt(tempoTT[0]);
			int minutiTT = Integer.parseInt(tempoTT[1]);
			
			
			// Stringhe per conversione tempo attuale
			String[] adesso = LocalDateTime.now().toString().split("T")[1].split(":");
			int ore = Integer.parseInt(adesso[0]);
			int minuti = Integer.parseInt(adesso[1]);
			
			// Operazione per ottenere i secondi totali di utilizzo del posto del parcheggio
			int contoOre = (ore - oreTT)*3600;
			int contoMinuti = (minuti - minutiTT)*60;
			int sommaConto = contoOre+contoMinuti;
			
			
			// Le tariffe di pagamento sono di 0,05 cent al minuto
			float conto = (sommaConto/60)*tariffa;
						
			// Stampo il conto a video
			System.out.print("Il conto da pagare è: " + conto + " " + sommaConto + "\n");
			
			return conto;
		}
	
	
}
