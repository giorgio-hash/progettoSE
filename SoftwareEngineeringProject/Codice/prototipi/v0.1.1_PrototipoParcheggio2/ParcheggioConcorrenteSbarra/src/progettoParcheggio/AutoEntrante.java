package progettoParcheggio;/**
 * This is the producer thread for the bounded buffer problem.
 *
 * Figure 7.14
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Sixth Edition
 * Copyright John Wiley & Sons - 2003.
 */


import java.util.*;

public class AutoEntrante implements Runnable
{
   public AutoEntrante(Parcheggio b) {
      buffer = b;
   }

   private Parcheggio buffer;
   
   public void run()
   {
   Date message;
   
   	while (true) {
   		SleepUtilities.nap();
   		message = new Date();
   		buffer.insert(message);
   	}
   }
}