package src.game;

public class KeyEventPair
{
	public enum KeyEventType
	{
		RELEASED,
		PRESSED,
		CLICKED
	};

    	public LogicalKey logicalKey;
	public int rawCode;
    	public KeyEventType type;

    	public KeyEventPair(LogicalKey logicalKey, int rawCode, KeyEventType type)
    	{
        	this.logicalKey = logicalKey;
		this.rawCode = rawCode;
        	this.type = type;
    	}
}
