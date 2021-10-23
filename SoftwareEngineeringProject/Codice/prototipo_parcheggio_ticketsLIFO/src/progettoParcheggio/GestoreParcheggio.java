package progettoParcheggio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GestoreParcheggio implements Parcheggio
{
   private static final int PARKING_SIZE = 10; // Capacit� massima del buffer.

   private int in; // points to the next free position in the buffer
   private int out; // points to the next full position in the buffer
   private ArrayList<Ticket> parcheggio; // Array di oggetti contenente le risorse.
   //private int cu;
   private int ce;

   private Semaphore semaforo; // Mutua esclusione per sezione critica.
   private Semaphore entrante; // Conta le risorse di tipo locazioni piene.
   private Semaphore uscente; // Conta le risorse di tipo locazioni vuote.
   
   // Costruttore del BoundedBuffer.
   public GestoreParcheggio()
   {
      // buffer is initially empty
      in = 0;
      out = 0;
      parcheggio = new ArrayList<Ticket>();
      semaforo = new Semaphore(1); // Nessun thread � attivo sul buffer.
      entrante = new Semaphore(0); // Essendo il buffer inizialmente vuoto
      // avr� 0 locazioni piene.
      uscente = new Semaphore(PARKING_SIZE); // Essendo il buffer inizialmente
      // vuoto avr� BUFFER_SIZE locazione vuote cio� pari alla capacit�
      // massima.
      //cu = 100;
      ce = 0;
   }

   // Metodo insert eseguito dal thread produttore.
   public void insert() {
      
	try {
		uscente.acquire(); // Il produttore deve acquisire una locazione
		// vuota per riempirla. Di conseguenza andiamo a fare un acquire
		// riguardante empty.
		// Nel momento in cui empty.acquire raggiunge il valore 0, 
		// il buffer sar� pieno e si stopper� l'esecuzione di questo
		// metodo.
		semaforo.acquire(); // Acquisisco mutex per mutua esclusione.
		// Va inserito il pi� vicino possibile alla sezione critica 
		// perch� genera restringimento delle operazioni, in quanto 
		// si fissano su un solo thread alla volta, creando imbuti
		// che rallentano il tutto.
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	  
	  // SEZIONE CRITICA: aggiunta di un oggetto nel buffer.
	System.out.print("\n\nTicket ritirato. Innalzamento sbarra in corso.. \n");
	
	parcheggio.add(new Ticket());
	
	  //parcheggio[in] = item;
     //in = (in + 1) % PARKING_SIZE;
      ce++;
      //cu--;

      System.out.println("Una macchina � entrata " + (parcheggio.size())+"\nticket: " + LocalDateTime.now() + " Posti occupati nel parcheggio = " + (parcheggio.size())+"\nticket: " + parcheggio.get(parcheggio.size()-1).getBarcode());
	
      semaforo.release(); // Rilascio mutex per fine sezione critica.
      entrante.release(); // Sveglia il consumatore e lo avvisa che � presente
      // una locazione piena che pu� essere consumata. 
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
		// Va inserito il pi� vicino possibile alla sezione critica 
		// perch� genera restringimento delle operazioni, in quanto 
		// si fissano su un solo thread alla volta, creando imbuti
		// che rallentano il tutto.
  	} catch (InterruptedException e) {
  		e.printStackTrace();
  	}
      
       // SEZIONE CRITICA: rimozione di un oggetto dal buffer.
    System.out.print("\n\nTicket consegnato, pagamento avvenuto. Innalzamento sbarra in corso.. \n");
    
    Ticket out = parcheggio.remove(parcheggio.size()-1);
    //item = parcheggio[out];
      //parcheggio[out]=null; // quando la macchina esce la locazione definita
      // quindi la andiamo noi a forzare imponendola = null, politica LIFO
      //out = (out + 1) % PARKING_SIZE;
      //cu++;
      //ce--;
	  
      System.out.println("Una macchina � uscita " + LocalDateTime.now() + " Posti liberi nel parcheggio = " + (parcheggio.size()) + "\nTicket eliminato: " + out.getBarcode() + " --entrato alle " + out.getReceiveTime());
    
      semaforo.release(); // Rilascio mutex per fine sezione critica.
      uscente.release(); // Sveglia il produttore e lo avvisa che � presente
      // una locazione vuota che pu� essere riempita. 
      // Rilascia una locazione vuota.
    
    //return item; // Ritorna l'oggetto tolto.
   }
   
   
   //inutili senza buffer
   public static <T> int getLength(T[] arr){
	    int count = 0;
	    for(T el : arr)
	        if (el != null)
	            count++;
	    return count;
   }
   
   public static <T> int getLengthV(T[] arr){
	    int count = 0;
	    for(T el : arr)
	        if (el == null)
	            count++;
	    return count;
   }
}

