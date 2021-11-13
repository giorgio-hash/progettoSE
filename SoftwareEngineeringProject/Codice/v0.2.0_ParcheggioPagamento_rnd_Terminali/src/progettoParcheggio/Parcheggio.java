package progettoParcheggio;

public interface Parcheggio {
	
	public abstract void insert();
	public abstract int remove(Ticket ticket);
	public abstract boolean check4Remove(Ticket ticket);
}
