package progettoParcheggio;

public class AutoUscente implements Runnable
{
	
	Ticket ticket = null;
	
   public AutoUscente(Parcheggio b) {
      buffer = b;
   }

   private Parcheggio buffer;
   
   public void run(){
	   while (true)
   		{
   			SleepEntrante.nap();
   		
   			
   			if( ParcheggioSimulato.parcheggio.size() > 0 ){
	   			ticket = ParcheggioSimulato.parcheggio.get( (int)(Math.random()*ParcheggioSimulato.parcheggio.size()) - 1 );
	   			
	   			if(buffer.check4Remove(ticket)) 
	   					System.out.println(buffer.remove(ticket)? "Pagamento avvenuto con successo" : "errore nel pagamento");
	   		}
   		}
   }
}
