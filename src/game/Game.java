package src.game;

import src.file.Level;

import java.util.Random;

import java.util.ArrayList;
import java.awt.Graphics;

public class Game extends Control
{
	// game states
	enum State
	{
		WAITING, READY, GAMEPLAY, PAUSE, WIN, DIE,
	}
	State state;
	
	static int normCol = 0xffff921e;
	static int passCol = 0xff4cff67;
	static int deadCol = 0xffff0f33;
	
	int time = 2*GameConstants.SimFramesPerSecond;

	ArrayList<Entity> garbage;
	
	Entity exit;
	Player player;
	
	AbstractInput input;
	CavemanMain main;
	
	int clubs = 0;
	
	boolean win = false;

	String word = "";
	int wordCol = normCol;
	
	// score 
	int score = 0;

	public static Random rng = new Random();
	
	GameLevel gameLevel;

	private boolean requestExit = false;
	public boolean requestExit() {
		return requestExit;
	}

	public Game(AbstractInput input, CavemanMain main, Level level)
	{
		this.input = input;
		this.main = main;

		garbage = new ArrayList<Entity>();
		
		player = new Player(-1,-1,GameConstants.PlayerTicksPerMovement,Art.player,input);
		exit = new Entity(-10,-10,false,Art.exit);
		this.gameLevel = new GameLevel(level, player, exit);

		state = State.WAITING;
		word = "level: " + gameLevel.name + " go!";
	}

	
	public void setReady() {
		this.state = State.READY;
	}
	
	public void tick()
	{
		switch (state)
		{
			case WAITING: 
					break;
			case READY: 
					if (time > 0)
						time--;
					else
					{
						state = State.GAMEPLAY;
						player.dead = false;
						win = false;
						time = GameConstants.SimFramesPerSecond;
					}
					break;

			case GAMEPLAY: 
					if (input.key_clicked(LogicalKey.PAUSE))
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
						time = 2*GameConstants.SimFramesPerSecond;
						
						wordCol = passCol;
						word = "passed!";
						
						// TODO - convey we won upstairs somehow
						
						state = State.WIN;
					}
					else if (player.dead)
					{
						player.move(-1,-1);
						
						wordCol = deadCol;
						word = "dead!";
						
						clubs = 0;
						
						state = State.DIE;
						time = 2*GameConstants.SimFramesPerSecond;
					}
					break;
			case WIN:
			case DIE:
					if (time > 0)
						time--;
					else
					{
						time = 2*GameConstants.SimFramesPerSecond;
						state = State.READY;
					}
					break;
			case PAUSE: 
					if (input.key_clicked(LogicalKey.PAUSE))
					{
						state = State.GAMEPLAY;
					}
					if (input.key_clicked(LogicalKey.QUIT))
					{
						requestExit = true;
					}
					if (input.key_clicked(LogicalKey.RESTART))
					{
						player.dead = true;
						player.move(-1,-1);
						
						wordCol = deadCol;
						word = "dead!";
						
						clubs = 0;
						
						state = State.DIE;
						time = 2*GameConstants.SimFramesPerSecond;
					}	
					break;
		}
	}
	
	// tick for the game
	public void gameTick()
	{
		for (Entity e : gameLevel.entities)
		{
			e.tick(this);
		}
		
		int playerX = player.getX();
		int playerY = player.getY();
		for (Entity e : gameLevel.entities)
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
		
		gameLevel.entities.removeAll(garbage);
		garbage.clear();
	}

	private void render_toolbar(Graphics g, int x_left, int y_top, int width, int height)
	{
		g.drawImage(Art.toolBar, x_left, y_top, width, height, null);

		g.drawImage(Art.club, x_left, y_top, height, height, null);
		String clubs_str = ":" + this.clubs;
		Art.font.draw_string(g, clubs_str, x_left + height, y_top, height, height, Style.item_color_pair);
		Art.font.draw_string_centered(g, gameLevel.name, x_left + width/2, y_top + height/2, height, height, Style.neutral_color_pair);
	}

	public void render(Graphics g, int width, int height)
	{
		int game_height = height - GameConstants.ToolBarHeight;
		g.clearRect(0, 0, width, height);
		gameLevel.render(g, 0, 0, width, game_height);
		render_toolbar(g, 0, game_height, width, GameConstants.ToolBarHeight);

		int letter_size = GameConstants.ToolBarHeight;
		if (state != State.GAMEPLAY)
		{
			Draw.darken(g, 0, 0, width, height);
		}

		if (state == State.READY)
		{
			Art.font.draw_string_centered(g, "ready!", width/2, game_height/2, letter_size, letter_size, Style.win_color_pair);
		}
		else if (state == State.DIE)
		{
			Art.font.draw_string_centered(g, "oops! you died!", width/2, game_height/2, letter_size, letter_size, Style.lose_color_pair);
		}
		else if (state == State.WIN)
		{
			Art.font.draw_string_centered(g, "yay! you won!", width/2, game_height/2, letter_size, letter_size, Style.win_color_pair);
		}
		else if (state == State.PAUSE)
		{
			Art.font.draw_string_centered(g, "press q to quit", width/2, game_height/2 - letter_size, letter_size, letter_size, Style.neutral_color_pair);
			Art.font.draw_string_centered(g, "r to restart level or", width/2, game_height/2, letter_size, letter_size, Style.neutral_color_pair);
			Art.font.draw_string_centered(g, "p to resume game", width/2, game_height/2 + letter_size, letter_size, letter_size, Style.neutral_color_pair);
		}
	}
}
