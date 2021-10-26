package progettoParcheggio;

import java.time.LocalDateTime;
import java.util.Random;

public class Ticket {

	private long barcode;
	private LocalDateTime receiveTime;
	
	private Random rand = new Random();
	
	public Ticket() {
	
		receiveTime = LocalDateTime.now();
		barcode = rand.nextLong();
		
		if(barcode < 0)
			barcode = Long.parseUnsignedLong(  (""+barcode).substring(1)  );
		
	}
	
	public long getBarcode() {
		return barcode;
	} 
	
	public String getReceiveTime() {
		return receiveTime.toString();
	}
	
	
}
