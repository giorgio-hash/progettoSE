package testingExodia;

import java.util.TimerTask;

public class TimeChanger1 extends TimerTask{

	private CardLayoutEntrata cle;
	
	public TimeChanger1(CardLayoutEntrata cle) {
		this.cle = cle;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		cle.changePanel("Panel 1");
		this.cancel(); //sto dicendo a TimeChanger : finita l'esecuzione, ammazzati
	}

}
