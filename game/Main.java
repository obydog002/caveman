package game;

import java.awt.image.*;
import java.awt.*;

import javax.swing.*;

import java.util.Random;

public class Main extends Canvas implements Runnable
{
	// controls how big each block is
	public static final int SCALE = 32; 
	// block number * width/height of block
	public static final int WIDTH = 51;

	public static final int HEIGHT = 27 + 1; // 1 because item screen at the bottom

	public static final int TICKS_PER_SECOND = 90;
	
	private boolean running;
	private Thread thread;
	
	private Game game;
	private Screen screen;
	private BufferedImage img;
	private int[] pixels;
	private Input input;
	
	public static Random rng;
	
	public Main(int level)
	{
		Dimension size = new Dimension(SCALE*WIDTH, SCALE*HEIGHT);
		setSize(size);
		setPreferredSize(size);
			
		input = new Input();
		
		game = new Game(level, input);
		
		screen = new Screen(WIDTH, HEIGHT, SCALE);
			
		img = new BufferedImage(SCALE*WIDTH, SCALE*HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
		
		addKeyListener(input);
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
			if (passedTime > 100000000) passedTime = 100000000;
			unprocessedSeconds += passedTime / 1000000000.0;
			boolean ticked = false;
			while (unprocessedSeconds > secondsPerTick) 
			{
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % Constants.TICKS_PER_SECOND == 0) 
				{
					//System.out.println(frames + " fps");
					lastTime += 1000;
					frames = 0;
				}
			}
			if (ticked) 
			{
				render();
				frames++;
			} 
			else 
			{
				try 
				{
					Thread.sleep(1);
					System.out.println("hmm?");
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void tick()
	{
		input.tick();
		game.tick();
	}
	
	public void render()
	{
		BufferStrategy bs = getBufferStrategy();
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		screen.render(game);
		
		for (int i = 0; i < SCALE*SCALE*WIDTH*HEIGHT; i++)
		{
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] arg)
	{
		int level = 1;
		rng = new Random();
		if (arg.length > 0)
		{
			level = Integer.parseInt(arg[0]);
		}
		Main game = new Main(level); 
		JFrame frame = new JFrame("caveman");
		
		frame.add(game);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		game.start();
	}
	
}