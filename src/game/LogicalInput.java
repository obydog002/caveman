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
        return key_map.get(key_code);
    }
}