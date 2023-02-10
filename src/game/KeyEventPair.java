package src.game;

public class KeyEventPair
{
	public enum KeyEventType
	{
		RELEASED,
		PRESSED,
		CLICKED
	};

    public int code;
    public KeyEventType type;

    public KeyEventPair(int code, KeyEventType type)
    {
        this.code = code;
        this.type = type;
    }
}