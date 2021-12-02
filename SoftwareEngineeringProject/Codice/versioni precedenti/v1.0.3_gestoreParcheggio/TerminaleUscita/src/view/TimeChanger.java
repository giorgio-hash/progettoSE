package view;

import java.util.TimerTask;

public class TimeChanger extends TimerTask{
	
	private CardLayoutUscita clu;
	
	public TimeChanger(CardLayoutUscita clu) {
		this.clu = clu;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		clu.changePanel("Panel 1");
		this.cancel(); //sto dicendo a TimeChanger : finita l'esecuzione, ammazzati
	}

}
