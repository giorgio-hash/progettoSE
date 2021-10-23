package progettoParcheggio;
import java.util.concurrent.Semaphore;

public class GestoreParcheggio implements Parcheggio
{
   private static final int PARKING_SIZE = 100; // Capacità massima del buffer.

   private int in; // points to the next free position in the buffer
   private int out; // points to the next full position in the buffer
   private Object[] parcheggio; // Array di oggetti contenente le risorse.
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
      parcheggio = new Object[PARKING_SIZE];
      semaforo = new Semaphore(1); // Nessun thread è attivo sul buffer.
      entrante = new Semaphore(0); // Essendo il buffer inizialmente vuoto
      // avrò 0 locazioni piene.
      uscente = new Semaphore(PARKING_SIZE); // Essendo il buffer inizialmente
      // vuoto avrò BUFFER_SIZE locazione vuote cioè pari alla capacità
      // massima.
      //cu = 100;
      ce = 0;
   }

   // Metodo insert eseguito dal thread produttore.
   public void insert(Object item) {
      
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
	System.out.print("Ticket ritirato. Innalzamento sbarra in corso.. ");
	parcheggio[in] = item;
      in = (in + 1) % PARKING_SIZE;
      ce++;
      //cu--;

      System.out.println("Una macchina è entrata " + item + " Posti occupati nel parcheggio = " + (getLength(parcheggio)));
	
      semaforo.release(); // Rilascio mutex per fine sezione critica.
      entrante.release(); // Sveglia il consumatore e lo avvisa che è presente
      // una locazione piena che può essere consumata. 
      // Rilascia una locazione piena.
   }

   // // Metodo insert eseguito dal thread consumatore.
   public Object remove() {
      Object item = null; // Creo un oggetto.

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
    System.out.print("Ticket consegnato, pagamento avvenuto. Innalzamento sbarra in corso.. ");
    item = parcheggio[out];
      parcheggio[out]=null; // quando la macchina esce la locazione definita
      // quindi la andiamo noi a forzare imponendola = null, politica LIFO
      out = (out + 1) % PARKING_SIZE;
      //cu++;
      //ce--;
	  
      System.out.println("Una macchina è uscita " + item + " Posti liberi nel parcheggio = " + (getLengthV(parcheggio)));
    
      semaforo.release(); // Rilascio mutex per fine sezione critica.
      uscente.release(); // Sveglia il produttore e lo avvisa che è presente
      // una locazione vuota che può essere riempita. 
      // Rilascia una locazione vuota.
    
    return item; // Ritorna l'oggetto tolto.
   }
   
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

