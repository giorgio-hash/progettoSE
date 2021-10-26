package progettoParcheggio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GestoreParcheggio implements Parcheggio
{
   private static final int PARKING_SIZE = 10; // Capacità massima del buffer.

   private ArrayList<Ticket> parcheggio; // Array di oggetti contenente le risorse.
   
   private Semaphore semaforo; // Mutua esclusione per sezione critica.
   private Semaphore entrante; // Conta le risorse di tipo locazioni piene.
   private Semaphore uscente; // Conta le risorse di tipo locazioni vuote.
   
   // Costruttore del BoundedBuffer.
   public GestoreParcheggio()
   {
      // buffer is initially empty
      parcheggio = new ArrayList<Ticket>();
      semaforo = new Semaphore(1); // Nessun thread è attivo sul buffer.
      entrante = new Semaphore(0); // Essendo il buffer inizialmente vuoto
      // avrò 0 locazioni piene.
      uscente = new Semaphore(PARKING_SIZE); // Essendo il buffer inizialmente
      // vuoto avrò BUFFER_SIZE locazione vuote cioè pari alla capacità
      // massima.
   }

   // Metodo insert eseguito dal thread produttore.
   public void insert() {
      
	try {
		uscente.acquire(); // Il produttore deve acquisire una locazione
		// vuota per riempirla. Di conseguenza andiamo a fare un acquire
		// riguardante empty.
		// Nel momento in cui empty.acquire raggiunge il valore 0, 
		// il buffer sarà pieno e si stopperà l'esecuzione di questo
		// metodo.
		semaforo.acquire(); // Acquisisco mutex per mutua esclusione.
		// Va inserito il più vicino possibile alla sezione critica 
		// perchè genera restringimento delle operazioni, in quanto 
		// si fissano su un solo thread alla volta, creando imbuti
		// che rallentano il tutto.
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	  
	  // SEZIONE CRITICA: aggiunta di un oggetto nel buffer.
	System.out.print("Ticket ritirato. Innalzamento sbarra in corso.. \n");
	
	parcheggio.add(new Ticket());

      System.out.println("Una macchina è entrata " + "\nOre: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + (parcheggio.size())+"\nticket: " + parcheggio.get(parcheggio.size()-1).getBarcode());
      System.out.println("\n\n");
      
      semaforo.release(); // Rilascio mutex per fine sezione critica.
      entrante.release(); // Sveglia il consumatore e lo avvisa che è presente
      // una locazione piena che può essere consumata. 
      // Rilascia una locazione piena.
   }

   // // Metodo insert eseguito dal thread consumatore.
   public void remove() {
      //Object item = null; // Creo un oggetto.

    try {
    	entrante.acquire(); // Il consumatore deve acquisire una locazione
		// piena per svuotarla. Di conseguenza andiamo a fare un acquire
		// riguardante full.
  		semaforo.acquire(); // Acquisisco mutex per mutua esclusione.
		// Va inserito il più vicino possibile alla sezione critica 
		// perchè genera restringimento delle operazioni, in quanto 
		// si fissano su un solo thread alla volta, creando imbuti
		// che rallentano il tutto.
  	} catch (InterruptedException e) {
  		e.printStackTrace();
  	}
      
       // SEZIONE CRITICA: rimozione di un oggetto dal buffer.
    System.out.print("Ticket consegnato, pagamento avvenuto. Innalzamento sbarra in corso.. \n");
    
    Ticket out = parcheggio.remove(parcheggio.size()-1);
	  
      System.out.println("Una macchina è uscita " + "\nOre: " + LocalDateTime.now() + "\nPosti occupati nel parcheggio = " + parcheggio.size() + "\nTicket eliminato: " + out.getBarcode() + " --entrato alle " + out.getReceiveTime());
      System.out.println("\n\n");
      semaforo.release(); // Rilascio mutex per fine sezione critica.
      uscente.release(); // Sveglia il produttore e lo avvisa che è presente
      // una locazione vuota che può essere riempita. 
      // Rilascia una locazione vuota.
   }
   
}

