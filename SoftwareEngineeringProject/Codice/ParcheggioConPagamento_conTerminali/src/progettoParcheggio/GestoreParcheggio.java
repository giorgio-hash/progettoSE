package progettoParcheggio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import progettoParcheggio.GestorePagamento;

public class GestoreParcheggio implements Parcheggio
{
   private static final int PARKING_SIZE = 100; // Capacità massima del parcheggio.

   private ArrayList<Ticket> parcheggio; // Array di oggetti contenente le risorse.
   private GestoreSbarra sbarra; // Variabile per alzamento/abbassamento sbarra parcheggio
   private Semaphore semaforo; // Mutua esclusione per sezione critica entrata e uscita di più macchine contemporaneamente.
   private Semaphore entrante; // Conta parcheggi pieni.
   private Semaphore uscente; // Conta parcheggi vuoti.
   private GestorePagamento payCheck; // Variabile per stampa pagamento avvenuto
   
   // Costruttore del GestoreParcheggio.
   public GestoreParcheggio()
   {
      // Inizialmente il parcheggio è vuoto.
      parcheggio = new ArrayList<Ticket>(); // Inizializzo il parcheggio a capacità 100.
      semaforo = new Semaphore(1); // Nessun thread è attivo sul buffer.
      entrante = new Semaphore(0); // Essendo il parcheggio inizialmente vuoto avrò 0 locazioni piene.
      uscente = new Semaphore(PARKING_SIZE); // Essendo il parcheggio inizialmente vuoto avrò PARKING_SIZE locazione vuote cioè pari alla capacità massima.
      sbarra = new GestoreSbarra(); // Gestore di innalzamento e abbassamento della sbarra,
      payCheck = new GestorePagamento();
   }

   // Metodo insert eseguito dal thread AutoEntrante.
   public void insert() {
      
	try {
		uscente.acquire(); 
		semaforo.acquire();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	  
	// SEZIONE CRITICA: aggiunta di una auto nel parcheggio.
	sbarra.upEntr(); // Innalzamento della sbarra.
	parcheggio.add(new Ticket()); // Aggiunga ticket
	System.out.println("Una macchina è entrata " + "\nticket: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size())+"\nticket: " + parcheggio.get(parcheggio.size()-1).getBarcode());	sbarra.down(); // Abbassamento sbarra.
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
    	
    	if( ! payCheck.pagamento(payCheck.prezzoTicket(out.getReceiveTime())) ) {
    		entrante.release();
    		//return 1;
 	   		
    	}	
    	
    	semaforo.acquire(); // Acquisisco semaforo per mutua esclusione.
  	} catch (InterruptedException e) {
  		e.printStackTrace();
  	}
      
    // SEZIONE CRITICA: rimozione di un'auto dal parcheggio.
    sbarra.ticketConsegnato(); // Stampa
    sbarra.upUsc(); 
    Ticket out = parcheggio.remove(parcheggio.size()-1); // Rimozione ticket
    System.out.println("Una macchina è uscita alle: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size()) + "\nTicket eliminato: " + out.getBarcode() + " --- entrato alle " + out.getReceiveTime());
    sbarra.down(); 
    semaforo.release(); 
    //rilascio il semaforo globale...ma non quello dell'uscente! (va ancora gestito il pagamento)

}

