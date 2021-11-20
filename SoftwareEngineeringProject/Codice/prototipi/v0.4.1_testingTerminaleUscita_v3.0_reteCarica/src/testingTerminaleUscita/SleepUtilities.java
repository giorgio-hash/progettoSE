package testingTerminaleUscita;

public class SleepUtilities
{
	private static final int NAP_TIME = 7;
	
	public static void nap() {
		nap(NAP_TIME);
	}

	public static void nap(int duration) {
		int sleeptime = (int) (NAP_TIME * Math.random()*1000 );
        try { 
        	Thread.sleep(sleeptime);
        }
        catch (InterruptedException e) {
        	
        }
	}
}
