package gestori;
import java.sql.SQLException;


import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import utilities.BackupDB;
import utilities.Ticket;

public class GestoreParcheggio
{
   private static final int PARKING_SIZE = 5; // Capacit� massima del parcheggio.

   private ArrayList<Ticket> parcheggio; // Array di oggetti contenente le risorse.
   
   private GestoreSbarra sbarraEntrante; // Variabile per alzamento/abbassamento sbarra parcheggio
   private GestoreSbarra sbarraUscente;
   
   private Semaphore semaforo; // Mutua esclusione per sezione critica entrata e uscita di pi� macchine contemporaneamente.
   
   private Semaphore uscente; // Conta parcheggi vuoti.
   private Semaphore permesso;
   
   private GestorePagamento payChecker; // Variabile per stampa pagamento avvenuto
   
   private float prezzoticket;
   
   private BackupDB db;
   
   
   
   
   // Costruttore del GestoreParcheggio, senza database
   public GestoreParcheggio()
   {
     
	   init();
	   uscente = new Semaphore(PARKING_SIZE); // Essendo il parcheggio inizialmente vuoto avr� PARKING_SIZE locazione vuote cio� pari alla capacit� massima.
	   
   }
   
   //Costruttore del GestoreParcheggio con Backup
   public GestoreParcheggio(BackupDB db) throws SQLException {
	   
	   init();
	   
	   this.db = db; //connette a database
	   
	   parcheggio = this.db.getBackup();//recupera dati dal database
	   
	   uscente = new Semaphore(PARKING_SIZE - parcheggio.size()); // indica il numero di locazioni vuote rimanenti
	  
	   
		System.out.println("\n\nStato del backup...."); //stampa stato del backup
		for(int i=0; i < parcheggio.size(); i++)
			System.out.println(parcheggio.get(i));
		System.out.println("\n");
		
   }
   

   public int getPermesso(){

	if( uscente.availablePermits() == 0 ) {
		return 0;
	}else	
		return 1;
   }


   public void attendiPermesso() throws InterruptedException{
	permesso.acquire();
   }

   public void daiPermesso(){
	permesso.release();
   }

   // Metodo insert eseguito dal thread AutoEntrante.
   public void insert(Ticket ticket) {
      
	   
	try {
		uscente.acquire(); // L'AutoEntrante deve acquisire un parcheggio
							// vuoto per riempirlo. Di conseguenza andiamo a fare un acquire
								// riguardante uscente.
							// Nel momento in cui uscennte.acquire raggiunge il valore 0, 
							// il parcheggio sar� pieno e si stopper� l'esecuzione di questo
							// metodo.
		
		semaforo.acquire(); // Acquisisco semaforo per mutua esclusione.
		
		
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	  
	// SEZIONE CRITICA: aggiunta di una auto nel parcheggio.
	sbarraEntrante.up(); // Innalzamento della sbarra.
	
	//creazione del ticket
	
	parcheggio.add(ticket); // Aggiunga ticket
	
	try {
		db.insert(""+ticket.getBarcode(), ticket.getReceiveTime().getYear(), ticket.getReceiveTime().getMonthValue(), ticket.getReceiveTime().getDayOfMonth(), ticket.getReceiveTime().getHour(), ticket.getReceiveTime().getMinute());
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	
		//ParcheggioSimulato.parcheggio.add(ticket);//Aggiunta ticket al parcheggio di simulazione
    
	System.out.println("Una macchina � entrata " + "\nticket: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size())+"\nticket: " + parcheggio.get(parcheggio.size()-1).getBarcode());	
	
	sbarraEntrante.down(); // Abbassamento sbarra.
	semaforo.release(); // Rilascio semaforo per fine sezione critica.
	
   }

   
   
   public int paga(Ticket ticket, String metodo, String somma){
	   
	   Ticket T = find(ticket);
	   //verifica del pagamento
	    if( ! payChecker.pagamento(prezzoticket, metodo, somma) ) 
	    	return -1; //problema nel pagamento
	    
	    
	    return remove( T );
	   
   }
   
   
   // Metodo remove eseguito dal thread AutoUscente.
   public int remove(Ticket ticket){
	
   
	try {
		semaforo.acquire();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}   
	
	 // SEZIONE CRITICA: rimozione di un'auto dal parcheggio.
    sbarraUscente.up(); // Innalzamento sbarra.
    
    parcheggio.remove(ticket); // Rimozione ticket
    
    try {
		db.extract(""+ticket.getBarcode());
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
    
    System.out.println("Una macchina � uscita alle: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size()) + "\nTicket eliminato: " + ticket.getBarcode() + " --- entrato alle " + ticket.getReceiveTime());
    sbarraUscente.down(); // Abbassamento sbarra.
    semaforo.release(); // Rilascio semaforo per fine sezione critica.
    uscente.release(); // Sveglia L'AutoEntrante e lo avvisa che � presente
    					// un parcheggio vuoto che pu� essere riempito.    
    if( uscente.availablePermits() == 1 )
		daiPermesso();
    
    return 1;
   }
   
   
   /**
    * prima fase della rimozione del ticket: controlla se questo sia effettivamente ancora nel sistema
    * (tramite corrispondenza del barcode). Altrimenti, comunica che non c'�, restituendo False
    * **/
   public boolean check4Remove(Ticket ticket) {
	   
	   System.out.print("\nTicket consegnato:" + ticket + "\n");
	   
	   try {
								// pieno per svuotarlo. Di conseguenza andiamo a fare un acquire
								// riguardante entrante.
	  		semaforo.acquire(); // Acquisisco semaforo per mutua esclusione.
	  		
	  		Ticket ticketsrc = find(ticket);
	  		
	  		 if( ticketsrc != null ){
	  			 prezzoticket = payChecker.prezzoTicket(ticketsrc.getReceiveTime().toString());
	  			semaforo.release(); // Rilascio semaforo per fine sezione critica.
	  		    return true;
	  		 }else {
	  		   System.out.println("\nbiglietto non riconosciuto.\n\n");
	  		   semaforo.release();
	  		   return false;
	  	   }
	  		
	  	} catch (InterruptedException e) {
	  		e.printStackTrace();
	  		return false;
	  	}   
   }
   
   
   public float getCheckedTicketPrice() {
	   return prezzoticket;
   }
   
   
   
   /**per qualche motivo i ticket deserializzati "cambiano identit�" agli occhi del sistema...
   perci� esiste questa funzione
   **/
   private Ticket find(Ticket ticket){
	   
	   for(int i=0;i<parcheggio.size();i++) {
		   
		   if(  parcheggio.get(i).getBarcode() == ticket.getBarcode()  )
			   return parcheggio.get(i);
		   
	   }
	   
	   return null;
   }
   
   
   
   
   
   private void init() {
	   // Inizialmente il parcheggio � vuoto.
	      parcheggio = new ArrayList<Ticket>(); // Inizializzo il parcheggio a capacit� 100.
	      
	      semaforo = new Semaphore(1); // Nessun thread � attivo sul buffer.
	      permesso = new Semaphore(1);
	      
	      sbarraEntrante = new GestoreSbarra(); // Gestore di innalzamento e abbassamento della sbarra,
	      sbarraUscente = new GestoreSbarra();
	      payChecker = new GestorePagamento(); // Inizializzazione variabile pagamento avvenuto
	      
   }
  
   
   
}
