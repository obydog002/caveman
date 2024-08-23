package src.game;

import src.file.FileManager;
import src.file.Level;
import src.file.CampaignSave;

import src.menu.*;

import java.awt.image.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;

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
	
	private AbstractInput input;
	
	public static Random rng;
	
	public GameMain(JFrame parent)
	{	
		this.parent = parent;

		LogicalInput logicalConverter = new LogicalInput();
		logicalConverter.set_pair(KeyEvent.VK_A, LogicalKey.LEFT);	
		logicalConverter.set_pair(KeyEvent.VK_LEFT, LogicalKey.LEFT);
		logicalConverter.set_pair(KeyEvent.VK_W, LogicalKey.UP);
		logicalConverter.set_pair(KeyEvent.VK_UP, LogicalKey.UP);
		logicalConverter.set_pair(KeyEvent.VK_D, LogicalKey.RIGHT);
		logicalConverter.set_pair(KeyEvent.VK_RIGHT, LogicalKey.RIGHT);
		logicalConverter.set_pair(KeyEvent.VK_S, LogicalKey.DOWN);
		logicalConverter.set_pair(KeyEvent.VK_DOWN, LogicalKey.DOWN);
		logicalConverter.set_pair(KeyEvent.VK_SPACE, LogicalKey.ACTION);
		logicalConverter.set_pair(KeyEvent.VK_P, LogicalKey.PAUSE);
		logicalConverter.set_pair(KeyEvent.VK_R, LogicalKey.RESTART);
		logicalConverter.set_pair(KeyEvent.VK_Q, LogicalKey.QUIT);

		input = new KeyboardInput(logicalConverter);

		set_main_menu();
		
		addKeyListener((KeyboardInput)input);
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
		input.keyqueue_reset();
		menu = new NewCampaignMenu(this, input);
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

	// sleeps upto a maximum amount
	// returns the ms in real time this method consumed.
	private double sleep(double ms_to_sleep)
	{
		final double MsSleepBuffer = 0.5;
		double prev = ms_get_current_time();
		double diff = 0;
		while ((diff = ms_get_current_time() - prev) < ms_to_sleep - MsSleepBuffer)
		{
			if (diff < ms_to_sleep - 1 - MsSleepBuffer)
			{
				try
				{
					Thread.sleep(1);
				}
				catch (Exception e)
				{

				}
			}
		}
		return ms_get_current_time() - prev;
	}

	private BufferStrategy strategy;
	public void run()
	{
		final double SimFramesPerSecond = 120.0d;
		final double MsPerSimFrame = 1000.0 / SimFramesPerSecond;
		double sim_lag = 0;
		final double RenderFramesPerSecond = 60.0d;
		final double MsPerRenderFrame = 1000.0 / RenderFramesPerSecond;
		double render_lag = 0;

		double ms_previous = ms_get_current_time();

		double fps_time = ms_previous;
		double time_slept = 0;
		int render_ticks = 0;
		int sim_ticks = 0;

		createBufferStrategy(2);
		strategy = getBufferStrategy();
		{
			Graphics g = strategy.getDrawGraphics();
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		

		while (running)
		{
			double ms_current = ms_get_current_time();
			double ms_elapsed = ms_current - ms_previous;

			ms_previous = ms_current;

			sim_lag += ms_elapsed;
			render_lag += ms_elapsed;

			if (fps_time + 1000 < ms_current)
			{
				double percentage_slept = 100 * time_slept / 1000;
				System.out.println(render_ticks + " " + sim_ticks + " percentage slept:" + percentage_slept + "%");
				time_slept = 0;
				render_ticks = 0;
				sim_ticks = 0;
				fps_time = ms_current;
			}

			while (sim_lag >= MsPerSimFrame)
			{
				tick();
				sim_ticks++;

				sim_lag -= MsPerSimFrame;
			}

			while (render_lag >= MsPerRenderFrame)
			{
				render();
				render_ticks++;

				render_lag -= MsPerRenderFrame;
			}

			// sleep to save some cycles
			// figure out which is coming next - render or sim update?
			double time_to_sleep_ms = Math.min(MsPerSimFrame - sim_lag, MsPerRenderFrame - render_lag);
			time_slept += sleep(time_to_sleep_ms);

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
