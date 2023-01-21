package src.game;

import src.file.FileManager;
import src.file.Level;
import src.file.CampaignSave;

import src.menu.*;

import java.awt.image.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import javax.swing.*;

import java.util.Random;

public class GameMain extends Canvas implements Runnable
{
	private boolean running;
	private Thread thread;
	
	private JFrame parent;
	private Game game = null;
	
	// current menu, could be options, whatever
	private src.menu.Menu menu;
	
	private Input input;
	
	// used for if input of whole keyboard is needed
	private KeyboardInput keyboard_input;
	
	public static Random rng;
	
	public GameMain(JFrame parent)
	{	
		this.parent = parent;
		// usual input for game play
		input = new Input();
		
		// keyboard input 
		keyboard_input = new KeyboardInput();

		set_main_menu();
		
		addKeyListener(input);
		addKeyListener(keyboard_input);	
	}
	
	// loads the game to use a campaign
	public void set_campaign(CampaignSave campaign)
	{
		game = new Game(input, this, campaign);
		menu = null;
	}
	
	// loads the game to play a single level,
	// for instance from custom levels
	public void set_game_level(Level level)
	{
		game = new Game(input, this, level);
		menu = null;
	}
	
	// changing menu functions

	public void set_main_menu()
	{
		menu = new MainMenu(this, input, parent.getWidth(), parent.getHeight());
	}

	// newcampaign menu
	public void set_new_campaign_menu()
	{
		menu = new NewCampaignMenu(this, keyboard_input);
	}
	
	// sets the editor
	public void set_editor()
	{
		
	}
	
	public synchronized void start()
	{
		if (running) return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		if (!running) return;
		running = false;
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public synchronized void exit()
	{
		running = false;
		parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
	}

	private boolean should_exit = false;
 	public void request_exit()
	{
		should_exit = true;
	}

	public final double OneMillion = 1000000.0d;
	public double ms_get_current_time()
	{
		return System.nanoTime() / OneMillion;
	}

	private BufferStrategy strategy;
	public void run()
	{
		double ms_previous = ms_get_current_time();
		final double MsPerUpdate = 10.0d;
		double lag = 0.0;
		double fps_time = ms_previous;
		int render_ticks = 0;
		int system_ticks = 0;
		int counter = 0;

		createBufferStrategy(2);
		strategy = getBufferStrategy();

		while (running)
		{
			double ms_current = ms_get_current_time();
			double ms_elapsed = ms_current - ms_previous;
			ms_previous = ms_current;
			lag += ms_elapsed;

			if (fps_time + 1000 < ms_current)
			{
				System.out.println(render_ticks + " " + system_ticks + " " + ms_elapsed);
				render_ticks = 0;
				system_ticks = 0;
				fps_time = ms_current;
			}

			while (lag >= MsPerUpdate)
			{
				input.tick();
				tick();

				system_ticks++;
				lag -= MsPerUpdate;
			}

			render();
			render_ticks++;
			try
			{
				Thread.sleep(2);
			}
			catch (Exception e)
			{
				
			}

			if (should_exit)
			{
				exit();
			}
		}
	}
	
	public void tick()
	{
		// do menu tick if there is a current menu
		if (menu != null)
		{
			menu.tick();
		}
		else
		{
			game.tick();
		}
	}

	public void render()
	{
		do
		{
			do
			{
				Graphics g = strategy.getDrawGraphics();
				
				// menu should be drawn
				if (menu != null)
				{
					menu.render(g, getWidth(), getHeight());
				}
				else 
				{
					game.render(g, getWidth(), getHeight());
				}
				g.dispose();
			} 
			while (strategy.contentsRestored());
			strategy.show();
		} 
		while (strategy.contentsLost());
	}
}
