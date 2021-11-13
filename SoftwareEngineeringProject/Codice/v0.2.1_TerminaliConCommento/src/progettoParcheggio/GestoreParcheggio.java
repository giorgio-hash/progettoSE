package progettoParcheggio;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GestoreParcheggio implements Parcheggio
{
   private static final int PARKING_SIZE = 100; // Capacità massima del parcheggio.

   private ArrayList<Ticket> parcheggio; // Array di oggetti contenente le risorse.
   
   public ParcheggioSimulato simparcheggio;
   
   private GestoreSbarra sbarraEntrante; // Variabile per alzamento/abbassamento sbarra parcheggio
   private GestoreSbarra sbarraUscente;
   private Semaphore semaforo; // Mutua esclusione per sezione critica entrata e uscita di più macchine contemporaneamente.
   private Semaphore entrante; // Conta parcheggi pieni.
   private Semaphore uscente; // Conta parcheggi vuoti.
   private GestorePagamento payChecker; // Variabile per stampa pagamento avvenuto
   
   // Costruttore del GestoreParcheggio.
   public GestoreParcheggio()
   {
      // Inizialmente il parcheggio è vuoto.
      parcheggio = new ArrayList<Ticket>(); // Inizializzo il parcheggio a capacità 100.
      
      //parcheggio simulato condiviso da tutti
      simparcheggio = new ParcheggioSimulato();
      simparcheggio.init();
      
      semaforo = new Semaphore(1); // Nessun thread è attivo sul buffer.
      entrante = new Semaphore(0); // Essendo il parcheggio inizialmente vuoto avrò 0 locazioni piene.
      uscente = new Semaphore(PARKING_SIZE); // Essendo il parcheggio inizialmente vuoto avrò PARKING_SIZE locazione vuote cioè pari alla capacità massima.
      sbarraEntrante = new GestoreSbarra(); // Gestore di innalzamento e abbassamento della sbarra,
      sbarraUscente = new GestoreSbarra();
      payChecker = new GestorePagamento(); // Inizializzazione variabile pagamento avvenuto
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
	sbarraEntrante.up(); // Innalzamento della sbarra.
	
	//creazione del ticket
	Ticket ticket = new Ticket();
	
	parcheggio.add(ticket); // Aggiunga ticket
	 
    try {
		ParcheggioSimulato.semaforo.acquire();
		ParcheggioSimulato.parcheggio.add(ticket);//Aggiunta ticket al parcheggio di simulazione
	    ParcheggioSimulato.semaforo.release();
    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	
	
	System.out.println("Una macchina è entrata " + "\nticket: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size())+"\nticket: " + parcheggio.get(parcheggio.size()-1).getBarcode());	
	sbarraEntrante.down(); // Abbassamento sbarra.
	semaforo.release(); // Rilascio semaforo per fine sezione critica.
	entrante.release(); // Sveglia del thread AutoUscente e lo avvisa che è presente
	// un parcheggio pieno che può essere lasciato in qualsiasi momento.
   }

   // // Metodo remove eseguito dal thread AutoUscente.
   public int remove(Ticket ticket) {
	
    // SEZIONE CRITICA: rimozione di un'auto dal parcheggio.
	   
	  //verifica del pagamento
    if( ! payChecker.pagamento(payChecker.prezzoTicket(ticket.getReceiveTime())) ) {
    	semaforo.release(); // Rilascio semaforo per fine sezione critica.
        uscente.release(); // Sveglia L'AutoEntrante e lo avvisa che è presente
    	return 1;
    }
    	
    sbarraUscente.up(); // Innalzamento sbarra.
    
    parcheggio.remove(find(ticket)); // Rimozione ticket
    
    try {
		ParcheggioSimulato.semaforo.acquire();
	    ParcheggioSimulato.parcheggio.remove(ParcheggioSimulato.find(ticket));//componente della simulazione
	    System.out.println("ticket rimosso da parcheggio virtuale. size : " + ParcheggioSimulato.parcheggio.size() );
	    ParcheggioSimulato.semaforo.release();
    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    
    System.out.println("Una macchina è uscita alle: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size()) + "\nTicket eliminato: " + ticket.getBarcode() + " --- entrato alle " + ticket.getReceiveTime());
    sbarraUscente.down(); // Abbassamento sbarra.
    semaforo.release(); // Rilascio semaforo per fine sezione critica.
    uscente.release(); // Sveglia L'AutoEntrante e lo avvisa che è presente
    // un parcheggio vuoto che può essere riempito.    
    //return item; // Ritorna l'auto uscita.
    
    return 0;
   }
   
   /**
    * prima fase della rimozione del ticket: controlla se questo sia effettivamente ancora nel sistema
    * (tramite corrispondenza del barcode). Altrimenti, comunica che non c'è, restituendo False
    * **/
   public boolean check4Remove(Ticket ticket) {
	   
	   System.out.print("\nTicket consegnato:" + ticket + "\n");
	   
	   try {
	    	entrante.acquire(); // l'AutoUscente deve acquisire un parcheggio
			// pieno per svuotarlo. Di conseguenza andiamo a fare un acquire
			// riguardante entrante.
	  		semaforo.acquire(); // Acquisisco semaforo per mutua esclusione.
	  		
	  		 if( find(ticket) != null )
	  		   return true;
	  	   else {
	  		   System.out.println("\nbiglietto non riconosciuto.\n\n");
	  		   entrante.release();
	  		   semaforo.release();
	  		   return false;
	  	   }
	  		
	  	} catch (InterruptedException e) {
	  		e.printStackTrace();
	  		return false;
	  	}   
   }
   
   /**per qualche motivo i ticket deserializzati "cambiano identità" agli occhi del sistema...
   perciò esiste questa funzione
   **/
   private Ticket find(Ticket ticket){
	   
	   for(int i=0;i<parcheggio.size();i++) {
		   
		   if(  parcheggio.get(i).getBarcode() == ticket.getBarcode()  )
			   return parcheggio.get(i);
		   
	   }
	   
	   return null;
   }
   
}

