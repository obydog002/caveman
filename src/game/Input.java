package src.game;

import java.awt.event.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Input implements KeyListener
{
	private final int KeysLength = KeyEvent.CHAR_UNDEFINED + 1; // all possible key strokes
	// 0 - not held, 1 - held (but not first click), 2 - held and first click
	private AtomicInteger keys[];
	
	private ConcurrentLinkedQueue<KeyEventPair> keys_queue;

	public Input()
	{
		keys = new AtomicInteger[KeysLength];
		for (int i = 0; i < KeysLength; i++)
		{
			keys[i] = new AtomicInteger(0); 
		}
		keys_queue = new ConcurrentLinkedQueue<>();
	}

	public void keyPressed(KeyEvent ke) 
	{
		int val = keys[ke.getKeyCode()].get();
		if (val == 0)
		{
			keys[ke.getKeyCode()].set(2); // a 'click' event
			keys_queue.add(new KeyEventPair(ke.getKeyCode(), KeyEventPair.KeyEventType.CLICKED));
		}
		else 
		{
			keys_queue.add(new KeyEventPair(ke.getKeyCode(), KeyEventPair.KeyEventType.PRESSED));
		}
	}

	public void keyReleased(KeyEvent ke) 
	{
		keys[ke.getKeyCode()].set(0);
		keys_queue.add(new KeyEventPair(ke.getKeyCode(), KeyEventPair.KeyEventType.RELEASED));
	}
	
	public void keyTyped(KeyEvent ke)
	{
		// empty
	}

	// returns null if no event
	public KeyEventPair pop_next_event()
	{
		return keys_queue.poll();
	}

    public boolean key_pressed(int code)
    {
        return keys[code].get() != 0;
    }
    
    public boolean key_clicked(int code)
    {
        int val = keys[code].get();
        if (val == 2)
        {
            keys[code].decrementAndGet();
            return true;
        }
        return false;
    }
}
