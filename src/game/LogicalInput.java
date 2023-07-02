package src.game;

import java.util.HashMap;

public class LogicalInput
{
    	private HashMap<Integer, LogicalKey> key_map;

    	public LogicalInput()
    	{
        	key_map = new HashMap<>();
    	}

    	public LogicalKey get_logical_key(int key_code)
    	{
		LogicalKey logical_key = key_map.get(key_code);
		if (logical_key == null)
			return LogicalKey.NOOP;
        	return logical_key;
    	}

	public void set_pair(int key_code, LogicalKey logical_key) 
	{
		key_map.put(key_code, logical_key);	
	}
}
