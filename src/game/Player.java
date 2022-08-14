package src.game;

import java.awt.event.*;

public class Player extends Mob
{
	boolean[] keys;
	int direction;
	int clubTimer;
	
	public Player(int x, int y, int timeToMove, Bitmap image)
	{
		super(x,y,timeToMove,false,true,false,image);
		direction = -1;
		clubTimer = 0;
	}
	
	public void tick(Game game)
	{	
		if (game.input.use.press_initial() && game.clubs > 0)
		{
			Entity e[] = checkDirection(game);
			if (lastdir != -1 && e[lastdir] != null && e[lastdir].breakable())
			{
				game.clubs--;
				e[lastdir].dead = true;
			}
		}
		
		if (game.input.die.clicked)
		{
			dead = true;
		}
		
		if (time > 0)
		{
			time--;
		}
		else
		{
			if (game.input.left.down)
				direction = 0;
			else if (game.input.up.down)
				direction = 1;
			else if (game.input.right.down)
				direction = 2;
			else if (game.input.down.down)
				direction = 3;
		
			if (direction > -1)
			{
				time = timeToMove;
				lastdir = direction;
				Entity dir[] = checkDirection(game);
				tryMove(game, dir[direction], direction);
				direction = -1;
			}
		}
	}
	
	//overriden
	public boolean checkMove(Game game, Entity e)
	{
		return e == null || e.passable() || e.isHostile() || e == game.exit || e.pushable();
	}
}