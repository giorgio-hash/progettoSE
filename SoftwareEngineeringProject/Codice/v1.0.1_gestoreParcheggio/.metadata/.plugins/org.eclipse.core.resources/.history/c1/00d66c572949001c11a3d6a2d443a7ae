package testingExodia;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Random;

public class Ticket implements Serializable{

	private long barcode;
	private LocalDateTime receiveTime;
	
	private Random rand = new Random();
	
	public Ticket() {
	
		receiveTime = LocalDateTime.of(LocalDateTime.now().getYear(),LocalDateTime.now().getMonth(),LocalDateTime.now().getDayOfMonth(),LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(),0);
		barcode = rand.nextLong();
		
		if(barcode < 0)
			barcode = Long.parseUnsignedLong(  (""+barcode).substring(1)  );
		
	}
	
	public Ticket( String barcode, LocalDateTime receiveTime ) {
		
		this.barcode = Long.parseLong(barcode);
		this.receiveTime = receiveTime;
		
	}
	
	//questo cotruttore serve solo per il terminale d'uscita
	public Ticket(String barcode) {
		
		this.barcode = Long.parseLong(barcode);
		this.receiveTime = LocalDateTime.now();
		
	}
	
	public long getBarcode() {
		return barcode;
	} 
	
	public LocalDateTime getReceiveTime() {
		return receiveTime;
	}
	
	public String toString() {
		return "Ticket[/"+ barcode +"/"+ receiveTime.toString() +"/]";
	}
	
}
