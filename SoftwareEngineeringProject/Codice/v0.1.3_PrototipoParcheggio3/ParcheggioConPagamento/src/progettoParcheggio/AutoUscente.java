package progettoParcheggio;

public class AutoUscente implements Runnable
{
   public AutoUscente(Parcheggio b) {
      buffer = b;
   }

   private Parcheggio buffer;
   
   public void run(){
	   while (true)
   		{
   			SleepEntrante.nap();
   			buffer.remove();
   		}
   }
}
