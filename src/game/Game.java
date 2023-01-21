package src.game;

import java.util.Random;

import src.file.FileManager;
import src.file.Level;
import src.file.CampaignSave;

import java.util.ArrayList;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

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
	
	private static final int Second = 60;
	int time = 2*Second;
	
	ArrayList<Entity> entities;

	ArrayList<Entity> garbage;
	
	Entity exit;
	Player player;
	BufferedImage background;
	
	Input input;
	GameMain main;
	
	int clubs = 0;
	
	boolean win = false;

	String word = "";
	int wordCol = normCol;
	
	// score 
	int score = 0;
	
	// current level_name
	public String level_name = "";
	
	// campaign may be null, if it is 
	// we are just running 1 level.
	private CampaignSave campaign;
	// some relevant variables for the campaign stuff
	// unused in the case there is just a level
	private int max_level = -1;
	private String marker;
	
	public static Random rng = new Random();
	
	int level_width = 20;
	int level_height = 20;

	// default constructor
	// shouldnt be used as it has no level or campaign information
	public Game(Input input, GameMain main)
	{
		this.input = input;
		this.main = main;
		
		background = Art.rockBackground;
		entities = new ArrayList();
		garbage = new ArrayList();
		
		player = new Player(-1,-1,10,Art.player);
		exit = new Entity(-10,-10,false,Art.exit);
		
		state = State.LOADING_MAP;
	}
	
	// this constructor will initialize the game to run the level supplied,
	// after finished display score screen
	public Game(Input input, GameMain main, Level level)
	{
		this(input, main);
		
		load_level(level);
	}
	
	// this constructor initializes the game to run through a campaign,
	// made of distinct levels
	// and will display a end score screen when its over
	public Game(Input input, GameMain main, CampaignSave campaign)
	{
		this(input, main);
		
		this.campaign = campaign;
		
		set_campaign();
	}
	
	// sets the campaign info to go
	public void set_campaign()
	{
		marker = new String(campaign.marker);
		
		max_level = FileManager.get_campaign_max_level(FileManager.RES_PATH + "maps/campaign/", marker);
		
		// if its -1 it could not be read properly. 
		// so just throw a run time error to crash
		if (max_level == -1)
			throw new RuntimeException("Could not read campaign file!");
	}
	
	// loads the next level from the campaign
	// will return 0 if it loads right,
	// -1 if there is any other errors
	public int load_level_from_campagin()
	{
		Level level = FileManager.read_cv_level(FileManager.RES_PATH + "maps/campaign/" + marker + "/", "lev" + campaign.level + ".CVL");
		
		load_level(level);
		
		return 0;
	}
	
	public void load_level(Level level)
	{
		entities.clear();
		
		level_name = level.name;
		
		level_width = level.width;
		level_height = level.height;
		word = "level: " + level_name + " go!";
		
		for (int i = 0; i < level.entities.length; i++)
		{
			int xx = i % level.width;
			int yy = i / level.width;
			
			switch (level.entities[i])
			{
				case 1: entities.add(new Entity(xx, yy, false, Art.rock));
						break; // unbreakable wall1
				case 2: entities.add(new Entity(xx, yy, false, Art.rock2));
						break; // unbreakable wall2
				case 3: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,false,false,false},Art.boulder));
						break; // boulder
				case 4: entities.add(new MovableWall(xx,yy,false,new boolean[] {true,false,false,false},Art.boulderl));
						break; // boulder left
				case 5: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,true,false,false},Art.boulderu));
						break; // boulder up
				case 6: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,false,true,false},Art.boulderr));
						break; // boulder right
				case 7: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,false,false,true},Art.boulderd));
						break; // boulder down
				case 8: entities.add(new MovableWall(xx,yy,false,new boolean[] {true,false,true,false},Art.boulderlr));
						break; // boulder left-right
				case 9: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,true,false,true},Art.boulderud));
						break; // boulder up-down
				case 10:entities.add(new MovableWall(xx,yy,false,new boolean[] {true,true,true,true},Art.boulderlurd));
						break; // boulder all
				case 11:player.move(xx,yy);
						entities.add(player);
						break; // player spawn
				case 12:exit.move(xx,yy);
						entities.add(exit);
						break; // exit 
				case 13:Entity club = new Entity(xx,yy,false,Art.club);
						club.setPass();
						club.setPush();
						club.id = 100;
						entities.add(club);
						break; // club
				case 14:entities.add(new Mob(xx,yy,45,false,false,false,Art.green));
						break; // green mammoth
				case 15:entities.add(new Mob(xx,yy,45,false,true,false,Art.yellow));
						break; // yellow mammoth
				case 16:entities.add(new Mob(xx,yy,45,true,true,true,Art.red));
						break; // red mammoth
			}
		}
	}
	
	public void tick()
	{
		switch (state)
		{
			case LOADING_MAP: 
					// if we are running a campaign
					if (campaign != null)
						load_level_from_campagin();
					
					state = State.READY;
					time = 2*Second;
					break;
					
			case READY: 
					if (time > 0)
						time--;
					else
					{
						state = State.GAMEPLAY;
						player.dead = false;
						win = false;
						time = Second;
					}
					break;

			case GAMEPLAY: 
					if (input.pause.press_initial())
					{
						state = State.PAUSE;
						word = "paused! press esc to unpause, q to quit, r to restart the level";
						wordCol = normCol;
					}
					else
						word = "";
					
					gameTick();
					
					if (time > 0)
						time--;
					
					if (win)
					{
						time = 2*Second;
						
						wordCol = passCol;
						word = "passed!";
						
						if (campaign != null)
						{
							// campaign is over
							if (campaign.level == max_level)
							{
								campaign.level = 1;	
							}
							else
							{
								campaign.level++;
							}
						}
						else
						{
							//need to give score screen and return
						}
						
						state = State.WIN;
					}
					else if (player.dead)
					{
						player.move(-1,-1);
						
						wordCol = deadCol;
						word = "dead!";
						
						clubs = 0;
						
						state = State.DIE;
						time = 2*Second;
					}
					break;
			case WIN:
			case DIE:
					if (time > 0)
						time--;
					else
					{
						time = 2*Second;
						state = State.LOADING_MAP;
					}
					break;
			case PAUSE: 
					if (input.pause.press_initial())
					{
						state = State.GAMEPLAY;
					}
					if (input.quit.press_initial())
					{
						main.set_main_menu();
					}
					if (input.die.press_initial())
					{
						player.dead = true;
						player.move(-1,-1);
						
						wordCol = deadCol;
						word = "dead!";
						
						clubs = 0;
						
						state = State.DIE;
						time = 2*Second;
					}	
					break;
		}
	}
	
	// tick for the game
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

	private void render_game(Graphics g, int width, int height)
	{
		g.drawImage(background, 0, 0, width, height, null);

		double ent_stride_width = ((double)width)/level_width;
		double ent_stride_height = ((double)height)/level_height;
		for (Entity e : entities)
		{
			int ent_x = (int)(ent_stride_width * e.getX());
			int ent_y = (int)(ent_stride_height * e.getY());
			int next_x = (int)(ent_stride_width * (e.getX() + 1));
			int next_y = (int)(ent_stride_height * (e.getY() + 1));
			int ent_width = next_x - ent_x;
			int ent_height = next_y - ent_y;
			g.drawImage(e.getImage(), ent_x, ent_y, ent_width, ent_height, null);
		}
	}

	private void render_toolbar(Graphics g, int x_left, int y_top, int width, int height)
	{
		g.drawImage(Art.toolBar, x_left, y_top, width, height, null);

		int tool_bar_unit_length = height;
		g.drawImage(Art.club, x_left, y_top, tool_bar_unit_length, tool_bar_unit_length, null);
		String clubs_str = ":" + this.clubs;
		Art.item_font.draw_string(g, clubs_str, x_left + tool_bar_unit_length, y_top, tool_bar_unit_length, tool_bar_unit_length);
		Art.item_font.draw_string_centered(g, level_name, x_left + width/2, y_top, tool_bar_unit_length, tool_bar_unit_length);
	}

	private final static int ToolBarUnitLength = 30;
	public void render(Graphics g, int width, int height)
	{
		int tool_bar_unit_length = width/32;
		int game_height = height - tool_bar_unit_length;
		render_game(g, width, game_height);
		render_toolbar(g, 0, game_height, width, tool_bar_unit_length);
	}
}
