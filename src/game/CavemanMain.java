package src.game;

import java.awt.image.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.*;

import javax.swing.*;

import java.util.Random;

import java.util.function.Function;
import java.util.LinkedList;

public class CavemanMain extends Canvas implements Runnable, MouseMotionListener, MouseListener
{
	private boolean running;
	private Thread thread;
	
	private JFrame parent;
	
	private AbstractInput input;
	
	public AbstractInput getInput() {
		return input;
	}

	public static Random rng;
	
	private Function exit_action;
	private LinkedList<Control> processes;

	public CavemanMain(JFrame parent)
	{	
		this.parent = parent;
		this.processes = new LinkedList<>();

		LogicalInput logicalConverter = new LogicalInput();
		logicalConverter.set_pair(KeyEvent.VK_A, 		LogicalKey.LEFT);	
		logicalConverter.set_pair(KeyEvent.VK_LEFT, 	LogicalKey.LEFT);
		logicalConverter.set_pair(KeyEvent.VK_W, 		LogicalKey.UP);
		logicalConverter.set_pair(KeyEvent.VK_UP, 		LogicalKey.UP);
		logicalConverter.set_pair(KeyEvent.VK_D, 		LogicalKey.RIGHT);
		logicalConverter.set_pair(KeyEvent.VK_RIGHT, 	LogicalKey.RIGHT);
		logicalConverter.set_pair(KeyEvent.VK_S, 		LogicalKey.DOWN);
		logicalConverter.set_pair(KeyEvent.VK_DOWN, 	LogicalKey.DOWN);
		logicalConverter.set_pair(KeyEvent.VK_SPACE, 	LogicalKey.ACTION);
		logicalConverter.set_pair(KeyEvent.VK_P, 		LogicalKey.PAUSE);
		logicalConverter.set_pair(KeyEvent.VK_R, 		LogicalKey.RESTART);
		logicalConverter.set_pair(KeyEvent.VK_Q, 		LogicalKey.QUIT);
		logicalConverter.set_pair(KeyEvent.VK_Q, LogicalKey.LEFT2);
		logicalConverter.set_pair(KeyEvent.VK_E, LogicalKey.RIGHT2);

		input = new KeyboardInput(logicalConverter);
		
		addKeyListener((KeyboardInput)input);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public JFrame getFrame() {
		return this.parent;
	}
	/*public void set_main_menu()
	{
		menu = new MainMenu(this, input, parent.getWidth(), parent.getHeight());
	}

	// newcampaign menu
	public void set_new_campaign_menu()
	{
		input.keyqueue_reset();
		menu = new NewCampaignMenu(this, input);
	}*/
	
	
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
		if (exit_action != null) {
			exit_action.apply(null);
		}
		parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
	}

	public void set_exit_action(Function exit_action) 
	{
		this.exit_action = exit_action;
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
		double start = ms_get_current_time();
		double ms_amount_slept = 0;
		while (ms_amount_slept < ms_to_sleep)
		{
			try
			{
				Thread.sleep(1);
				ms_amount_slept += 1;
			}
			catch (Exception e)
			{

			}
		}
		return ms_get_current_time() - start;
	}

	private boolean exitImmediately = false;
	private BufferStrategy strategy;
	public void run()
	{
		final double SimFramesPerSecond = 60.0d;
		final double MsPerSimFrame = 1000.0 / SimFramesPerSecond;
		double sim_lag = 0;

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

			if (fps_time + 1000 < ms_current)
			{
				System.out.println(render_ticks + " " + sim_ticks + " amount slept: " + time_slept);
				time_slept = 0;
				render_ticks = 0;
				sim_ticks = 0;
				fps_time = ms_current;
			}

			while (sim_lag >= MsPerSimFrame)
			{
				tick();
				sim_ticks++;
				render();
				render_ticks++;

				sim_lag -= MsPerSimFrame;
			}

			time_slept += sleep(MsPerSimFrame - sim_lag);

			if (processes.size() == 0 || exitImmediately)
			{
				exit();
			}
		}
	}
	
	public void add_process(Control c) {
		processes.push(c);
	}

	public void tick()
	{
		if (processes.size() > 0) {
			if (processes.peek().requestExit()) {
				processes.pop();
			} else if (processes.peek().requestFullExit()) {
				exitImmediately = true;
			}
		}

		if (processes.size() > 0) {
			processes.peek().tick();
		}
	}

	public void render()
	{		
		if (processes.size() > 0) {
			Graphics g = strategy.getDrawGraphics();
			processes.peek().render(g, getWidth(), getHeight());
			g.dispose();
			strategy.show();
		}
	}

	// mouse motion listener methods
	public void mouseDragged(MouseEvent e)
	{
		if (processes.size() > 0) {
			processes.peek().mouseDragged(e);
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		if (processes.size() > 0) {
			processes.peek().mouseMoved(e);
		}
	}
	
	// mouse listener methods
	public void mouseClicked(MouseEvent e)
	{
		if (processes.size() > 0) {
			processes.peek().mouseClicked(e);
		}
	}
	
	public void mouseEntered(MouseEvent e)
	{
		if (processes.size() > 0) {
			processes.peek().mouseEntered(e);
		}
	}
	
	public void mouseExited(MouseEvent e)
	{
		if (processes.size() > 0) {
			processes.peek().mouseExited(e);
		}
	}
	
	public void mousePressed(MouseEvent e)
	{
		if (processes.size() > 0) {
			processes.peek().mousePressed(e);
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		if (processes.size() > 0) {
			processes.peek().mouseReleased(e);
		}
	}
}
