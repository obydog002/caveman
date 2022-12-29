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
	// block number * width/height of block
	public int width = 51;

	public int height = 27 + 1; // 1 because item screen at the bottom
	
	private boolean running;
	private Thread thread;
	
	private JFrame parent;
	private Game game = null;
	
	// current menu, could be options, whatever
	private src.menu.Menu menu;
	
	// seperate screen to draw menus
	private Screen menu_screen;
	private int[] menu_pixels;
	
	private BufferedImage menu_img;
	
	private Screen screen;
	private BufferedImage img;
	private int[] pixels;
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
		
		menu_screen = new Screen(width, height, Constants.SCALE);
		menu_img = new BufferedImage(Constants.SCALE* width, Constants.SCALE * height, BufferedImage.TYPE_INT_RGB);
		menu_pixels = ((DataBufferInt)menu_img.getRaster().getDataBuffer()).getData();
		
		menu = new MainMenu(this, input, menu_screen.get_width(), menu_screen.get_height());
		
		reset_pixels(width, height);
		
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
		menu = new MainMenu(this, input, menu_screen.get_width(), menu_screen.get_height());
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
	
	public void reset_pixels(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		screen = new Screen(width, height, Constants.SCALE);
		
		img = new BufferedImage(Constants.SCALE* width, Constants.SCALE * height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
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
			System.out.println("stop1");
			thread.join();
			System.out.println("stop2");
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

	public void run()
	{
		double ms_previous = ms_get_current_time();
		final double MsPerUpdate = 10.0d;
		double lag = 0.0;
		double fps_time = ms_previous;
		int render_ticks = 0;
		int system_ticks = 0;
		int counter = 0;

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
				fps_time = ms_previous;
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
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		// menu should be drawn
		if (menu != null)
		{
			menu.render(menu_screen);
			
			for (int i = 0; i < menu_pixels.length; i++)
			{
				menu_pixels[i] = menu_screen.pixels[i];
			}
			
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(menu_img, 0, 0, getWidth(), getHeight(), null);
		}
		else // game is going now so draw it
		{
			/*screen.render(game);
		
			for (int i = 0; i < Constants.SCALE*Constants.SCALE*width*height; i++)
			{
				pixels[i] = screen.pixels[i];
			}
		
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);*/
			game.render(g, getWidth(), getHeight());
		}
		
		g.dispose();
		bs.show();
	}
}
