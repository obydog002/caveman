package game;

import java.awt.event.*;

import java.util.ArrayList;

public class Input implements KeyListener, MouseMotionListener
{
	public class Key 
	{
		public int presses, absorbs;
		public boolean down, clicked;

		public Key() 
		{
			keys.add(this);
		}

		public void toggle(boolean pressed) 
		{
			if (pressed != down) 
				down = pressed;
			
			if (pressed) 
				presses++;
		}

		public void tick() 
		{
			if (absorbs < presses) 
			{
				absorbs++;
				clicked = true;
			} 
			else 
				clicked = false;
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
	
	public int x = 0;
	public int y = 0;
	
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
		
		if (ke.getKeyCode() == KeyEvent.VK_P) pause.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_B) die.toggle(pressed);
	}
	
	public void mouseDragged(MouseEvent e)
	{
		x = e.getX();
		y = e.getY();
	}
	public void mouseMoved(MouseEvent e)
	{
		x = e.getX();
		y = e.getY();
	}
	
	public void keyTyped(KeyEvent e)
	{
		// nothing
	}
}