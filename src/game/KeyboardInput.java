package src.game;

import java.awt.event.*;

// simple class represent keyboard input for example if inputting names
public class KeyboardInput implements KeyListener
{
	// keyboard a-z keys, space, bspace, enter
	// space at 26,
	// backspace at 27,
	// enter at  28,
	// numerals at 29 - 39 (exclusive)
	public int[] keys;
	
	public KeyboardInput()
	{
		keys = new int[39];
	}
	
	public void keyPressed(KeyEvent ke) 
	{
		int index = map_key(ke);
		
		if (index != -1)
			keys[index]++;
	}

	public void keyReleased(KeyEvent ke) 
	{
		int index = map_key(ke);
		
		if (index != -1)
			keys[index] = 0;
	}
	
	// maps the key event to the index for usage in the keys array,
	// or returns -1 if the event is not supported.
	public int map_key(KeyEvent ke)
	{
		int code = ke.getKeyCode();
		
		if (code == KeyEvent.VK_SPACE)
			return 26;
		else if (code == KeyEvent.VK_ENTER)
			return 27;
		else if (code == KeyEvent.VK_BACK_SPACE)
			return 28;
		else if (code >= KeyEvent.VK_A && code <= KeyEvent.VK_Z)
			return code - KeyEvent.VK_A;
		else if (code >= KeyEvent.VK_0 && code <= KeyEvent.VK_9)
			return 29 + code - KeyEvent.VK_0;
		else 
			return -1;
	}
	
	public void keyTyped(KeyEvent ke)
	{
		// empty
	}
}