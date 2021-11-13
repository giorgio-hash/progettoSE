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
    	  //System.out.println("Un autista è in attesa dell'erogazione del ticket.. ");
    	  SleepUtilities.nap();
    	  
    	  message = new Date();
    	  //System.out.println("Il thread numero: "+ Thread.currentThread().getName() + "è entrato nel parcheggio" + message);

    	  buffer.insert(message);
      }
   }
}