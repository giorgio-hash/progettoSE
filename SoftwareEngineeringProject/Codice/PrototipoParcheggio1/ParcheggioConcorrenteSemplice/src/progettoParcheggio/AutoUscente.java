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
    	 //System.out.println("Un autista è in attesa di uscire dal parcheggio. ");
    	 SleepEntrante.nap();

         //System.out.println("Il thread numero: " + Thread.currentThread().getName() + " è uscito dal parcheggio" );

         message = (Date)buffer.remove();
      }
   }
}
