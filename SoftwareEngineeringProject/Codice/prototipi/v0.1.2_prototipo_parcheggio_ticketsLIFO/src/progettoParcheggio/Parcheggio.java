package progettoParcheggio;

public interface Parcheggio {

	/**
	 * insert an item into the Buffer.
	 * Note this may be either a blocking
	 * or non-blocking operation.
	 */
	public abstract void insert();

	/**
	 * remove an item from the Buffer.
	 * Note this may be either a blocking
	 * or non-blocking operation.
	 */
	public abstract void remove();
}
