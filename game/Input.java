package game;

import java.awt.event.*;

import java.util.ArrayList;

public class Input implements KeyListener
{
	public class Key 
	{
		public int presses, absorbs;
		public boolean down, clicked;

		public boolean press_initial = true;
		
		public int press_inital_count = -1;
		
		public Key() 
		{
			keys.add(this);
		}

		public void toggle(boolean pressed) 
		{
			if (pressed != down) 
			{
				down = pressed;
			}

			if (pressed) 
			{
				presses++;
				
				if (press_initial)
				{
					press_inital_count = 2;
					press_initial = false;
				}
			}
			else
			{
				press_initial = true;
			}
			
			/*if (press_initial)
			{
				System.out.println("\n\ndebug----------D3 B U GG");
				System.out.println("first press");
				System.out.println("debug----------o0 x d zz\n\n");
			}*/
		}
		
		public void tick() 
		{
			if (press_inital_count > 0)
				press_inital_count--;
			
			if (absorbs < presses) 
			{
				absorbs++;
				clicked = true;
			} 
			else 
				clicked = false;
		}
		
		// if this is the first 'press'
		public boolean press_initial()
		{
			return press_inital_count > 0;
		}
	}
	
	public ArrayList<Key> keys = new ArrayList<>();
	
	public Key up = new Key();
	public Key down = new Key();
	public Key right = new Key();
	public Key left = new Key();
	public Key use = new Key();
	public Key pause = new Key();
	public Key die = new Key();
	
	public void releaseAll() 
	{
		for (int i = 0; i < keys.size(); i++) 
		{
			keys.get(i).down = false;
		}
	}

	public void tick() 
	{
		for (int i = 0; i < keys.size(); i++) 
		{
			keys.get(i).tick();
		}
	}
	
	public void keyPressed(KeyEvent ke) 
	{
		toggle(ke, true);
	}

	public void keyReleased(KeyEvent ke) 
	{
		toggle(ke, false);
	}
	
	public void keyTyped(KeyEvent ke)
	{
		// empty
	}
	
	private void toggle(KeyEvent ke, boolean pressed)
	{
		if (ke.getKeyCode() == KeyEvent.VK_W) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_S) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_D) right.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_UP) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) use.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) pause.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_R) die.toggle(pressed);
	}
}