package src.game;

import java.awt.image.BufferedImage;

public class Mob extends Entity
{
	// Movable entity, such as the player or a mammoth.
	public int timeToMove; // dictates how many game ticks before a move is made.
	public int time;
	boolean canMoveBlocks;
	boolean track; // track mobs will try to follow the player.
	boolean crushable;
	boolean lastMove;
	
	int lastdir = -1;
	public Mob(int x, int y, int timeToMove, boolean hostile, boolean canMoveBlocks, boolean track, BufferedImage image)
	{
		super(x,y,hostile,image);
		this.timeToMove = timeToMove;
		this.time = 0;
		this.canMoveBlocks = canMoveBlocks;
		this.track = track;
		crushable = true; // for all mobs
		dead = false;
		lastMove = false;
	}

	public boolean tryPush(Game game, int direction) // check if this can be pushed.
	{
		if (!crushable) return false;
		else
		{
			Entity e[] = checkDirection(game);
			if (e[direction] == null)
				translate(direction, 1);
			else
				dead = true;
		}
		return true;
	}

	public boolean push(Game game, Entity e, int dir) // check if push is possible with the current direction
	{
		if (!canMoveBlocks || !e.pushable()) return false;
		return e.tryPush(game, dir);
		
	}

	public boolean checkMove(Game game, Entity e)
	{
		return e == null || e.passable() || (e == game.player && isHostile()) || e.pushable();
	}
	
	public void runAimlessly(Game game)
	{
		Entity[] dir = checkDirection(game);
		int direction = 0;
		int possible_move_count = 0;
		for (int i = 0; i < 4; i++)
			if (checkMove(game, dir[i])) 
				possible_move_count++;
		
		if (possible_move_count > 0)
		{
			int x = Game.rng.nextInt(possible_move_count);
			int c = 0;
			for (int q = 0; q < 4; q++)
			{
				if ( checkMove(game, dir[q]) )
				{
					if (c == x)
					{
						direction = q;
						break;
					}
					c++;
				}
			}
			if (lastMove && lastdir != -1 && checkMove(game, dir[lastdir]) && Game.rng.nextInt(25) > 1)
				direction = lastdir;
			lastdir = direction;
			lastMove = tryMove(game, dir[direction], direction);	
		}
		else // no move this tick since its blocked from all sides
			lastdir = -1;
			
	}
	
	public void findPlayer(Game game) // tracks the player
	{
		int playerX = game.player.getX();
		int playerY = game.player.getY();
		int x = this.getX();
		int y = this.getY();
		int dirX = (x > playerX) ? 0 : 2;
		int dirY = (y > playerY) ? 1 : 3;
		int moveX = (dirX == 0) ? -1 : 1;
		int moveY = (dirY == 1) ? -1 : 1;
		int cand_x_pos = moveX + x;
		int cand_y_pos = moveY + y;

		// Taxicab distance
		double horz_dist = Math.abs(cand_x_pos - playerX) + Math.abs(y - playerY);
		double vert_dist = Math.abs(cand_y_pos - playerY) + Math.abs(x - playerX);
		Entity dir[] = checkDirection(game);

		if (horz_dist < vert_dist) // try horizontal first
		{
			if (tryMove(game, dir[dirX], dirX))
				return;
			
			if (tryMove(game, dir[dirY], dirY))
				return;
		}
		else if (horz_dist == vert_dist) // randomly pick
		{
			int r = Game.rng.nextInt(2);
			if (r == 0)
			{
				if (tryMove(game, dir[dirX], dirX))
					return;

				if (tryMove(game, dir[dirY], dirY))
					return;
			}
			else
			{
				if (tryMove(game, dir[dirY], dirY))
					return;

				if (tryMove(game, dir[dirX], dirX))
					return;
			}
		}
		else  // try vertical first
		{
			if (tryMove(game, dir[dirY], dirY))
				return;

			if (tryMove(game, dir[dirX], dirX))
				return;
		}
	}

	public boolean tryMove(Game game, Entity e, int direction)
	{
		if (checkMove(game, e))
		{
			if (e != null && e.pushable() && !e.passable())
			{
				if (push(game, e, direction))
				{
					translate(direction,1);
					return true;
				}
			}
			else
			{
				translate(direction,1);
				return true;
			}
		}
		return false;
	}

	public void tick(Game game)
	{
		if (time > 0)
		{
			time--;
		}
		else
		{
			time = timeToMove;
			if (track)
				findPlayer(game);
			else
			{
				runAimlessly(game);
			}
		}
	}
}