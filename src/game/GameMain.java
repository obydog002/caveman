package src.game;

import src.file.FileManager;
import src.file.Level;
import src.file.CampaignSave;

import src.menu.*;

import java.awt.image.*;
import java.awt.*;

import javax.swing.*;

import java.util.Random;

public class GameMain extends Canvas implements Runnable
{
	// block number * width/height of block
	public int width = 51;

	public int height = 27 + 1; // 1 because item screen at the bottom
	
	private boolean running;
	private Thread thread;
	
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
	
	public GameMain()
	{	
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
	
	// newcampaign menu
	public void set_new_campaign_menu()
	{
		menu = new NewCampaignMenu(this, keyboard_input);
	}
	
	// sets the editor
	public void set_editor()
	{
		
	}
	
	// Absolutely not the right way to do this, but oh well
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
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		int frames = 0;
		double unprocessedSeconds = 0;
		long lastTime = System.nanoTime();
		double secondsPerTick = 1.0 / (double)Constants.TICKS_PER_SECOND;
		int tickCount = 0;

		while (running) 
		{
			long now = System.nanoTime();
			long passedTime = now - lastTime;
			lastTime = now;
			if (passedTime < 0) passedTime = 0;
			if (passedTime > 1000000000) passedTime = 1000000000;
			unprocessedSeconds += passedTime / 1000000000.0;
			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) 
			{
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				/*if (tickCount % Constants.TICKS_PER_SECOND == 0) 
				{
					System.out.println(frames + " fps");
					lastTime += 1000;
					frames = 0;
				}*/
			}
			if (ticked) 
			{
				render();
				frames++;
			} 
			/*else 
			{
				try 
				{
					Thread.sleep(1);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}*/
		}
	}
	
	public void tick()
	{
		input.tick();
		
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
			screen.render(game);
		
			for (int i = 0; i < Constants.SCALE*Constants.SCALE*width*height; i++)
			{
				pixels[i] = screen.pixels[i];
			}
		
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		}
		
		g.dispose();
		bs.show();
	}
}