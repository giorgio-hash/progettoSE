package progettoParcheggio;

public interface Parcheggio {
	
	public abstract void insert();
	public abstract boolean remove(Ticket ticket);
	public abstract boolean check4Remove(Ticket ticket);
}
