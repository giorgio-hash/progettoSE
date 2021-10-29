package progettoParcheggio;

public class SleepEntrante
{
	private static final int NAP_TIME = 5;
	
	public static void nap() {
		nap(NAP_TIME);
	}

	public static void nap(int duration) {
       	int sleeptime = (int) (NAP_TIME * Math.random() );
       	try { 
       		Thread.sleep(sleeptime*3000);
       	}
       	catch (InterruptedException e) {
       		
       	}
	}
}
