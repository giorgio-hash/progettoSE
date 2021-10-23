package progettoParcheggio;
/**
 * This is the consumer thread for the bounded buffer problem.
 *
 * Figure 7.15
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Sixth Edition
 * Copyright John Wiley & Sons - 2003.
 */

//import java.util.*;

public class AutoUscente implements Runnable
{
   public AutoUscente(Parcheggio b) {
      buffer = b;
   }

   public void run(){

     while (true)
      {
         //System.out.println("Consumer napping");
	 SleepEntrante.nap();

         // consume an item from the buffer
         //System.out.println("Consumer wants to consume.");

         buffer.remove();
                 
      }
   }

   private Parcheggio buffer;
}
