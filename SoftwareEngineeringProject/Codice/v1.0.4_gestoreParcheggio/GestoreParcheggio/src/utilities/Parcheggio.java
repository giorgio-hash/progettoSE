package utilities;

public interface Parcheggio {
	
	public abstract void insert(Ticket ticket);
	public abstract int remove(Ticket ticket);
	public abstract boolean check4Remove(Ticket ticket);
	public abstract float getCheckedTicketPrice();
	public abstract int paga(Ticket ticket, String metodo, String somma);
	public abstract int getPermesso();
	public abstract void attendiPermesso() throws InterruptedException;
	public abstract void daiPermesso();
	
}
