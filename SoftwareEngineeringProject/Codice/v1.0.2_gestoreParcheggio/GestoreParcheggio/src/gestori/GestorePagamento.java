package gestori;

import java.time.LocalDateTime;

/**
 * 
 * @author Giorgio Chirico, raffaele di maio
 *
 * Questa classe si occupa della gestione delle transazioni monetarie (calcolo costo, esito pagamento)
 */
public class GestorePagamento {
	
	
	final float tariffa = 0.5f;
	
	private float conto;
	
	public GestorePagamento() {
		
		
	}
	
	/**
	 * processa il pagamento e restituisce un esito
	 * 
	 * @param conto : float
	 * @param IBAN : String
	 * @param pswd : String
	 * @return boolean
	 * 
	 * 
	 */
	public boolean pagamento(float conto, String metodo, String somma) {
		
		if(  (conto - Float.parseFloat(somma) ) > 0 || metodo.contentEquals("annulla") )
			return false;
		else
			return true;
		
	}
	
	
	// Metodo per il calcolo del prezzo del ticket
	
	/**
	 * 
	 * @param ticketTime = ora di erogazione del ticket ( oggetto LocalDateTime )
	 * @return float
	 * 
	 * 
	 * Questa funzione calcola il prezzo del ticket in base all'ora in cui � stato erogato
	 */
	public float prezzoTicket(String ticketTime) {
			
			// Stringhe per conversione tempo del ticket erogato
			String[] tempoTT = ticketTime.split("T")[1].split(":");
			int oreTT = Integer.parseInt(tempoTT[0]);
			int minutiTT = Integer.parseInt(tempoTT[1]);
			
			String[] tempoGG = ticketTime.split("T")[0].split("-");
			int giornoGG = Integer.parseInt(tempoGG[2]);
			
			String today = LocalDateTime.now().toString();
			// Stringhe per conversione tempo attuale
			String[] adesso = today.split("T")[1].split(":");
			int ore = Integer.parseInt(adesso[0]);
			int minuti = Integer.parseInt(adesso[1]);
			
			String[] oggi = today.split("T")[0].split("-");
			int giorno = Integer.parseInt(oggi[2]);
			
			// Operazione per ottenere i secondi totali di utilizzo del posto del parcheggio
			int contoOre = ( ( (giorno - giornoGG)*24 + ore ) - oreTT)*3600; //si ottengono le ore complessive e si convertono in secondi
			int contoMinuti = (minuti - minutiTT)*60; //si ottengono i minuti complessivi e si convertono in secondi
			int sommaConto = contoOre+contoMinuti;
				
			
			
			
			// Le tariffe di pagamento sono di 0,05 cent al minuto
			conto = (sommaConto/60)*tariffa;
						
			// Stampo il conto a video
			System.out.print("Il conto da pagare �: " + conto + " " + sommaConto + "\n");
			
			return conto;
		}
	
	
}