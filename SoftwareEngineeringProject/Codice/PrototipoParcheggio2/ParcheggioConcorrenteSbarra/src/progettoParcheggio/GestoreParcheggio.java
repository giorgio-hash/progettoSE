package progettoParcheggio;
import java.util.concurrent.Semaphore;

public class GestoreParcheggio implements Parcheggio
{
   private static final int PARKING_SIZE = 100; // Capacità massima del parcheggio.

   private int in; // variabile di appoggio che identifica il prossimo parcheggio libero.
   private int out; // variabile di appoggio che identifica il prossimo parcheggio pieno.
   private Object[] parcheggio; // Array di oggetti contenente le risorse, cioè i posti del parcheggio.
   private GestoreSbarra sbarra;
   private Semaphore semaforo; // Mutua esclusione per sezione critica entrata e uscita di più macchine contemporaneamente.
   private Semaphore entrante; // Conta parcheggi pieni.
   private Semaphore uscente; // Conta parcheggi vuoti.
   
   // Costruttore del GestoreParcheggio.
   public GestoreParcheggio()
   {
      // Inizialmente il parcheggio è vuoto.
      in = 0; // Inizializzo variabili d'appoggio a 0.
      out = 0;
      parcheggio = new Object[PARKING_SIZE]; // Inizializzo il parcheggio a capacità 100.
      semaforo = new Semaphore(1); // Nessun thread è attivo sul buffer.
      entrante = new Semaphore(0); // Essendo il parcheggio inizialmente vuoto avrò 0 locazioni piene.
      uscente = new Semaphore(PARKING_SIZE); // Essendo il parcheggio inizialmente vuoto avrò PARKING_SIZE locazione vuote cioè pari alla capacità massima.
      sbarra = new GestoreSbarra(); // Gestore di innalzamento e abbassamento della sbarra,
   }

   // Metodo insert eseguito dal thread AutoEntrante.
   public void insert(Object item) {
      
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
	parcheggio[in] = item; // Inserimento macchina nel parcheggio.
	in = (in + 1) % PARKING_SIZE; // Avanzamento dell'indice del prossimo parcheggio libero.
	System.out.println("Una macchina è entrata " + item + " Posti occupati nel parcheggio = " + (getLength(parcheggio)));
	sbarra.down(); // Abbassamento sbarra.
	semaforo.release(); // Rilascio semaforo per fine sezione critica.
	entrante.release(); // Sveglia del thread AutoUscente e lo avvisa che è presente
	// un parcheggio pieno che può essere lasciato in qualsiasi momento.
   }

   // // Metodo remove eseguito dal thread AutoUscente.
   public Object remove() {
      Object item = null; // Creo un oggetto.

    try {
    	entrante.acquire(); // l'AutoUscente deve acquisire un parcheggio
		// pieno per svuotarlo. Di conseguenza andiamo a fare un acquire
		// riguardante entrante.
  		semaforo.acquire(); // Acquisisco semaforo per mutua esclusione.
  	} catch (InterruptedException e) {
  		e.printStackTrace();
  	}
      
    // SEZIONE CRITICA: rimozione di un'auto dal parcheggio.
    sbarra.upUsc(); // Innalzamento sbarra.
    item = parcheggio[out]; // Prelevo l'auto uscente.
    parcheggio[out]=null; // Quando la macchina esce, il parcheggio definito
    // va impostato a vuoto.
    out = (out + 1) % PARKING_SIZE; // Avanzamento dell'indice del prossimo parcheggio pieno.
    System.out.println("Una macchina è uscita " + item + " Posti liberi nel parcheggio = " + (getLengthV(parcheggio)));
    sbarra.down(); // Abbassamento sbarra.
    semaforo.release(); // Rilascio semaforo per fine sezione critica.
    uscente.release(); // Sveglia L'AutoEntrante e lo avvisa che è presente
    // un parcheggio vuoto che può essere riempito.    
    return item; // Ritorna l'auto uscita.
   }
   
   // Metodo per contare le auto dentro al parcheggio.
   public static <T> int getLength(T[] arr){
	    int count = 0;
	    for(T el : arr)
	        if (el != null)
	            count++;
	    return count;
   }
   
   // Metodo per contare i posti liberi nel parcheggio.
   public static <T> int getLengthV(T[] arr){
	    int count = 0;
	    for(T el : arr)
	        if (el == null)
	            count++;
	    return count;
   }
   
   
}

