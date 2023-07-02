package src.game;

public interface AbstractInput {
	public boolean key_held(LogicalKey key);
	public boolean key_clicked(LogicalKey key);
	
	// should return null if queue is empty
	public KeyEventPair keyqueue_get_next();  
	// completely clear all entries
	public void keyqueue_reset();
}
