package progettoParcheggio;

import java.util.*;

public class AutoUscente implements Runnable
{
   public AutoUscente(Parcheggio b) {
      buffer = b;
   }

   private Parcheggio buffer;
   
   public void run(){
   Date message;

   	while (true)
   	{
   		SleepEntrante.nap();
   		message = (Date)buffer.remove();
   	}
   }
}
