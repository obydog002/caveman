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
	
	public boolean is_key(LogicalKey logicalKey) {
		return this.logicalKey == logicalKey;
	}
	
	public boolean is_held() {
		return this.type != KeyEventType.RELEASED;
	}

	public boolean is_clicked() {
		return this.type == KeyEventType.CLICKED;
	}

	public boolean is_released() {
		return this.type == KeyEventType.RELEASED;
	}
}
