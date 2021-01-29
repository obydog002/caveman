package game;

import java.util.ArrayList;
import java.awt.event.*;

public class Game
{
	// game states
	enum State
	{
		LOADING_MAP, READY, GAMEPLAY, PAUSE, WIN, DIE
	}
	State state;
	
	static int normCol = 0xffff921e;
	static int passCol = 0xff4cff67;
	static int deadCol = 0xffff0f33;
	
	int time = 2*Constants.SECOND;
	
	ArrayList<Entity> entities;
	ArrayList<Entity> garbage;
	Entity exit;
	Player player;
	Bitmap background;
	
	Input input;
	
	int clubs = 0;
	
	boolean win = false;
	
	int level;
	String levelName = "";
	String word = "";
	int wordCol = normCol;
	
	public static final int LEVELIMIT = 100;
	public static String levelNames[] = {"beginning","clubber lang","annoying dudes","walls: be careful","uh oh"};
	
	public Game(int level, Input input)
	{
		this.level = level;
		this.input = input;
		background = Art.rockBackground;
		entities = new ArrayList();
		garbage = new ArrayList();
		
		// tick time for movement
		// 80 tickzs
		player = new Player(-1,-1,12,Art.player);
		exit = new Entity(-10,-10,false,Art.exit);
		
		state = State.LOADING_MAP;
	}
	
	// change this and editor
	public void loadMap()
	{
		clubs = 0;
		entities.clear();
		Bitmap currentLevel = Art.loadBitmap("res/maps/level" + level + ".png");
		levelName = levelNames[level-1];
		for (int i = 0; i < Main.WIDTH; i++)
		{
			for (int q = 0; q < Main.HEIGHT - 1; q++)
			{
				int col = currentLevel.pixels[i + q*Main.WIDTH];
				if (col == 0xff000000)
				{
					if (Main.rng.nextInt(40) > 0)
						entities.add(new Entity(i,q,false,Art.rock));
					else
						entities.add(new Entity(i,q,false,Art.rock2));
				}
				else if (col == 0xff808080)
				{
					entities.add(new MovableWall(i,q,false,new boolean[] {false,false,false,false},Art.boulder));
				}
				else if (col == 0xff262fff)
					entities.add(new MovableWall(i,q,false,new boolean[] {true,false,false,false},Art.boulderl));
				else if (col == 0xfff3000a)
					entities.add(new MovableWall(i,q,false,new boolean[] {false,true,false,false},Art.boulderu));
				else if (col == 0xffffed00)
					entities.add(new MovableWall(i,q,false,new boolean[] {false,false,true,false},Art.boulderr));
				else if (col == 0xff00b7ba)
					entities.add(new MovableWall(i,q,false,new boolean[] {false,false,false,true},Art.boulderd));
				else if (col == 0xfff76b20)
					entities.add(new MovableWall(i,q,false,new boolean[] {true,false,true,false},Art.boulderlr));
				else if (col == 0xff9500db)
					entities.add(new MovableWall(i,q,false,new boolean[] {false,true,false,true},Art.boulderud));
				else if (col == 0xff2d6700)
					entities.add(new MovableWall(i,q,false,new boolean[] {true,true,true,true},Art.boulderlurd));
				else if (col == 0xff22b14c)
				{
					player.move(i,q);
					entities.add(player);
				}
				else if (col == 0xff445566)
				{
					exit.move(i,q);
					entities.add(exit);
				}
				else if (col == 0xff226611)
				{ 
					Entity club = new Entity(i,q,false,Art.club);
					club.setPass();
					club.setPush();
					club.id = 100;
					entities.add(club);
				}
				else if (col == 0xffed1c24)
					entities.add(new Mob(i,q,45,true,true,true,Art.red));
				else if (col == 0xffb5e61d)
					entities.add(new Mob(i,q,45,false,false,false,Art.green));
				else if (col == 0xfffff423)
					entities.add(new Mob(i,q,45,false,true,false,Art.yellow));
			}
		}
	}
	
	public void tick()
	{
		switch (state)
		{
			case LOADING_MAP: 
					word = "level: " + level + " go!";
					loadMap();
					state = State.READY;
					time = 2*Constants.SECOND;
					break;
					
			case READY: 
					if (time > 0)
						time--;
					else
					{
						state = State.GAMEPLAY;
						player.dead = false;
						win = false;
						time = Constants.SECOND;
					}
					break;

			case GAMEPLAY: 
					if (input.pause.press_initial())
					{
						state = State.PAUSE;
						word = "paused! press esc to unpause";
						wordCol = normCol;
					}
					else
						word = "";
					
					gameTick();
					
					if (time > 0)
						time--;
					
					if (win)
					{
						time = 2*Constants.SECOND;
						
						wordCol = passCol;
						word = "passed!";
						
						if (level < LEVELIMIT)
							level++;
						
						state = State.WIN;
					}
					else if (player.dead)
					{
						player.move(-1,-1);
						
						wordCol = deadCol;
						word = "dead!";
						
						state = State.DIE;
						time = 2*Constants.SECOND;
					}
					break;
			case WIN:
			case DIE:
					if (time > 0)
						time--;
					else
					{
						time = 2*Constants.SECOND;
						state = State.LOADING_MAP;
					}
					break;
			case PAUSE: 
					if (input.pause.press_initial())
					{
						state = State.GAMEPLAY;
					}
					break;
		}
	}
	
	/*public void tick()
	{
		// start state
		if (state == 0)
		{
			state = 1;
			time = 2*Constants.SECOND;
			wordCol = normCol;
			word = "level: " + level + " go!";
			loadMap();
		} 
		
		// dead and ready
		if (state == 1)
		{
			if (time > 0)
				time--;
			else
			{
				state = 2;
				player.dead = false;
				win = false;
				time = Constants.SECOND;
			}
		} // game play state
		else if (state == 2)
		{
			if (input.pause.press_initial())
			{
				state = 5;
				word = "paused! press p to unpause";
				wordCol = normCol;
				time = 2*Constants.SECOND;
			}
			else
				word = "";
			
			gameTick();
			
			if (time > 0)
				time--;
			
			if (win)
			{
				time = 2*Constants.SECOND;
				state = 4;
				wordCol = passCol;
				word = "passed!";
				if (level < LEVELIMIT)
					level++;
			}
			else if (player.dead)
			{
				player.move(-1,-1);
				state = 3;
				time = 2*Constants.SECOND;
				wordCol = deadCol;
				word = "dead!";
			}
		}
		else if (state == 3)
		{
			if (time > 0)
				time--;
			else
			{
				state = 1;
				time = 2*Constants.SECOND;
				wordCol = normCol;
				word = "level: " + level + " go!";
				loadMap();
			}
		}
		else if (state == 4)
		{
			if (time > 0)
				time--;
			else
			{
				state = 1;
				time = 2*Constants.SECOND;
				wordCol = normCol;
				word = "level: " + level + " go!";
				loadMap();
			}
		} // pause state
		else if (state == 5)
		{
			if (input.pause.press_initial())
			{
				state = 2;
				time = Constants.SECOND;
			}
		}
		

	}*/
	
	public void gameTick()
	{
		for (Entity e : entities)
		{
			e.tick(this);
		}
		
		int playerX = player.getX();
		int playerY = player.getY();
		for (Entity e : entities)
		{		
			if (e.getX() == playerX && e.getY() == playerY)
			{
				if (e.isHostile()) player.dead = true;
				else if (e == exit) win = true;
				else if (e.id == 100)
				{
					e.dead = true;
					clubs++;
				}
			}
			if (e.dead && e != player)
			{
				garbage.add(e);
			}
		}
		entities.removeAll(garbage);
		garbage.clear();
	}
}