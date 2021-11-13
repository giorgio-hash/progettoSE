package progettoParcheggio;

public class AutoEntrante implements Runnable
{

	private Parcheggio buffer;
	
   public AutoEntrante(Parcheggio b) {
      buffer = b;
   }

   
   public void run()
   {
	   while (true) {
		   SleepUtilities.nap();
		   buffer.insert();
	   }
   }
}