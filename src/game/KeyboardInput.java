package src.game;

import java.awt.event.*;
import java.util.HashMap;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyboardInput implements KeyListener, AbstractInput
{
	private final int LogicalKeysLength = 8 + 1; // all possible key strokes - TODO FIX
	// 0 - not held, 1 - held (but not first click), 2 - held and first click
	private AtomicInteger keys[];
	private ConcurrentLinkedQueue<KeyEventPair> keys_queue;

	// convert keyboard strokes to logical keys
	private LogicalInput logicalConverter;

	// TODO - fix this. This is a dirty, dirty hack!!
	private HashMap<LogicalKey, Integer> logToRaw;
	private HashMap<Integer, LogicalKey> rawToLog;

	public KeyboardInput(LogicalInput logicalConverter)
	{
		// TODO - use somethin gbetter moron
		logToRaw = new HashMap<>();
		rawToLog = new HashMap<>();
		logToRaw.put(LogicalKey.LEFT, 0);
		logToRaw.put(LogicalKey.UP, 1);
		logToRaw.put(LogicalKey.RIGHT, 2);
		logToRaw.put(LogicalKey.DOWN, 3);
		logToRaw.put(LogicalKey.ACTION, 4);
		logToRaw.put(LogicalKey.PAUSE, 5);
		logToRaw.put(LogicalKey.RESTART, 6);
		logToRaw.put(LogicalKey.QUIT, 7);
		logToRaw.put(LogicalKey.NOOP, 8);

		rawToLog.put(0, LogicalKey.LEFT);
		rawToLog.put(1, LogicalKey.UP);
		rawToLog.put(2, LogicalKey.RIGHT);
		rawToLog.put(3, LogicalKey.DOWN);
		rawToLog.put(4, LogicalKey.ACTION);
		rawToLog.put(5, LogicalKey.PAUSE);
		rawToLog.put(6, LogicalKey.RESTART);
		rawToLog.put(7, LogicalKey.QUIT);
		rawToLog.put(8, LogicalKey.NOOP);

		keys = new AtomicInteger[LogicalKeysLength];
		for (int i = 0; i < LogicalKeysLength; i++)
		{
			keys[i] = new AtomicInteger(0); 
		}
		keys_queue = new ConcurrentLinkedQueue<>();
		this.logicalConverter = logicalConverter;
	}

	public void keyPressed(KeyEvent ke) 
	{
		int key_code = ke.getKeyCode();
		LogicalKey logicalKey = logicalConverter.get_logical_key(key_code);
		int hack = logToRaw.get(logicalKey);
		int val = keys[hack].get();
		if (val == 0)
		{
			keys[hack].set(2); // a 'click' event
			keys_queue.add(new KeyEventPair(logicalKey, key_code, KeyEventPair.KeyEventType.CLICKED));
		}
		else 
		{
			keys_queue.add(new KeyEventPair(logicalKey, key_code, KeyEventPair.KeyEventType.PRESSED));
		}
	}

	public void keyReleased(KeyEvent ke) 
	{
		LogicalKey logicalKey = logicalConverter.get_logical_key(ke.getKeyCode());
		int hack = logToRaw.get(logicalKey);
		keys[hack].set(0);
		keys_queue.add(new KeyEventPair(logicalKey, ke.getKeyCode(), KeyEventPair.KeyEventType.RELEASED));
	}
	
	public void keyTyped(KeyEvent ke)
	{
		// empty
	}

    	public boolean key_held(LogicalKey key)
    	{
		int hack = logToRaw.get(key);
        	return keys[hack].get() != 0;
    	}

    	public boolean key_clicked(LogicalKey key)
    	{
		int hack = logToRaw.get(key);
        	int val = keys[hack].get();
        	if (val == 2)
        	{
            		keys[hack].decrementAndGet();
            		return true;
        	}
        	return false;
    	}

	public KeyEventPair keyqueue_get_next()
	{
		return keys_queue.poll();
	}

	public void keyqueue_reset() 
	{
		keys_queue.clear();
	}
}
