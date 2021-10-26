package progettoParcheggio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GestoreParcheggio implements Parcheggio
{
   private static final int PARKING_SIZE = 100; // Capacità massima del parcheggio.

   private ArrayList<Ticket> parcheggio; // Array di oggetti contenente le risorse.
   private GestoreSbarra sbarra; // Variabile per alzamento/abbassamento sbarra parcheggio
   private Semaphore semaforo; // Mutua esclusione per sezione critica entrata e uscita di più macchine contemporaneamente.
   private Semaphore entrante; // Conta parcheggi pieni.
   private Semaphore uscente; // Conta parcheggi vuoti.
   private String oraT; // Contiene l'orario di erogazione del ticket
   private Pagamento avvenuto; // Variabile per stampa pagamento avvenuto
   
   // Costruttore del GestoreParcheggio.
   public GestoreParcheggio()
   {
      // Inizialmente il parcheggio è vuoto.
      parcheggio = new ArrayList<Ticket>(); // Inizializzo il parcheggio a capacità 100.
      semaforo = new Semaphore(1); // Nessun thread è attivo sul buffer.
      entrante = new Semaphore(0); // Essendo il parcheggio inizialmente vuoto avrò 0 locazioni piene.
      uscente = new Semaphore(PARKING_SIZE); // Essendo il parcheggio inizialmente vuoto avrò PARKING_SIZE locazione vuote cioè pari alla capacità massima.
      sbarra = new GestoreSbarra(); // Gestore di innalzamento e abbassamento della sbarra,
      avvenuto = new Pagamento(); // Inizializzazione variabile pagamento avvenuto
   }

   // Metodo insert eseguito dal thread AutoEntrante.
   public void insert() {
      
	try {
		uscente.acquire(); // L'AutoEntrante deve acquisire un parcheggio
		// vuoto per riempirlo. Di conseguenza andiamo a fare un acquire
		// riguardante uscente.
		// Nel momento in cui uscennte.acquire raggiunge il valore 0, 
		// il parcheggio sarà pieno e si stopperà l'esecuzione di questo
		// metodo.
		semaforo.acquire(); // Acquisisco semaforo per mutua esclusione.
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	  
	// SEZIONE CRITICA: aggiunta di una auto nel parcheggio.
	sbarra.upEntr(); // Innalzamento della sbarra.
	parcheggio.add(new Ticket()); // Aggiunga ticket
	System.out.println("Una macchina è entrata " + "\nticket: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size())+"\nticket: " + parcheggio.get(parcheggio.size()-1).getBarcode());	sbarra.down(); // Abbassamento sbarra.
	oraT = LocalDateTime.now().toString(); // Presa orario corrente
	semaforo.release(); // Rilascio semaforo per fine sezione critica.
	entrante.release(); // Sveglia del thread AutoUscente e lo avvisa che è presente
	// un parcheggio pieno che può essere lasciato in qualsiasi momento.
   }

   // // Metodo remove eseguito dal thread AutoUscente.
   public void remove() {
    try {
    	entrante.acquire(); // l'AutoUscente deve acquisire un parcheggio
		// pieno per svuotarlo. Di conseguenza andiamo a fare un acquire
		// riguardante entrante.
  		semaforo.acquire(); // Acquisisco semaforo per mutua esclusione.
  	} catch (InterruptedException e) {
  		e.printStackTrace();
  	}
      
    // SEZIONE CRITICA: rimozione di un'auto dal parcheggio.
    sbarra.ticketConsegnato(); // Stampa
    pagamentoTicket(oraT); // Metodo per calcolo conto da pagare
    avvenuto.pagamentoAvvenuto(); // Stampa
    sbarra.upUsc(); // Innalzamento sbarra.
    Ticket out = parcheggio.remove(parcheggio.size()-1); // Rimozione ticket
    System.out.println("Una macchina è uscita alle: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size()) + "\nTicket eliminato: " + out.getBarcode() + " --- entrato alle " + out.getReceiveTime());
    sbarra.down(); // Abbassamento sbarra.
    semaforo.release(); // Rilascio semaforo per fine sezione critica.
    uscente.release(); // Sveglia L'AutoEntrante e lo avvisa che è presente
    // un parcheggio vuoto che può essere riempito.    
    //return item; // Ritorna l'auto uscita.
   }
   
   // Metodo per il pagamento del ticket.
   public String pagamentoTicket(String ticketTime) {
		String conto = "";
		
		// Stringhe per conversione tempo del ticket erogato
		String[] split1TT = ticketTime.split("T");
		String tempoTT = split1TT[1];
		String[] split2TT = tempoTT.split(":");
		String oreTT = split2TT[0];
		String minutiTT = split2TT[1];
		String secondiTT = split2TT[2];
		
		// Stringhe per conversione tempo attuale
		String newAdesso = LocalDateTime.now().toString();
		String[] split1 = newAdesso.split("T");
		String tempo = split1[1];
		String[] split2 = tempo.split(":");
		String ore = split2[0];
		String minuti = split2[1];
		String secondi = split2[2];
		
		// Conversione delle stringhe in double per calcolo conto
		double oreTTint = Double.parseDouble(oreTT);
		double minutiTTint = Double.parseDouble(minutiTT);
		double secondiTTint = Double.parseDouble(secondiTT);
		double oreint = Double.parseDouble(ore);
		double minutiint = Double.parseDouble(minuti);
		double secondiint = Double.parseDouble(secondi);
		
		// Operazione per ottenere i secondi totali di utilizzo del posto del parcheggio
		double contoOre = (oreint - oreTTint)*3600;
		double contoMinuti = (minutiint - minutiTTint)*60;
		double contoSecondi = (secondiint - secondiTTint);
		double sommaConto = contoOre+contoMinuti+contoSecondi;
		
		// Le tariffe di pagamento sono di 0,05 cent al minuto
		final double tariffa = 0.05;
		double contoFinale = (sommaConto/60)*tariffa;
		
		// Converto il risultato del conto in stringa
		conto = Double.toString(contoFinale);
		
		// Stampo il conto a video
		System.out.print("Il conto da pagare è: " + conto);
		
		return conto;
	}
}

