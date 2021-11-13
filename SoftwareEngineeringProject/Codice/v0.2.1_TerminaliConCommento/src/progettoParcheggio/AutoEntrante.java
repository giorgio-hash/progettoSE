package progettoParcheggio;

public class AutoEntrante implements Runnable
{
   public AutoEntrante(Parcheggio b) {
      buffer = b;
   }

   private Parcheggio buffer;
   
   public void run()
   {
	   while (true) {
		   SleepUtilities.nap();
		   buffer.insert();
	   }
   }
}