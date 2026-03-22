package src.game;

import java.awt.event.InputEvent;

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
	public int modifiers;

    	public KeyEventPair(LogicalKey logicalKey, int rawCode, KeyEventType type)
    	{
        	this.logicalKey = logicalKey;
		this.rawCode = rawCode;
        	this.type = type;
		this.modifiers = 0;
    	}

	public KeyEventPair(LogicalKey logicalKey, int rawCode, KeyEventType type, int modifiers)
	{
		this.logicalKey = logicalKey;
		this.rawCode = rawCode;
		this.type = type;
		this.modifiers = modifiers;
	}

	public boolean is_ctrl_or_meta() {
		return (modifiers & (InputEvent.CTRL_DOWN_MASK | InputEvent.META_DOWN_MASK)) != 0;
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
