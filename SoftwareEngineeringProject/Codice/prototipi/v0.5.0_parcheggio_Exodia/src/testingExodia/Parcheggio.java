package testingExodia;

public interface Parcheggio {
	
	public abstract Ticket generateTicket();
	public abstract void insert(Ticket ticket);
	public abstract int remove(Ticket ticket);
	public abstract boolean check4Remove(Ticket ticket);
	public abstract int paga(Ticket ticket, String IBAN, String pswd);
}
