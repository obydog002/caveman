package src.game;

import java.util.Queue;
import java.util.LinkedList;

import java.awt.image.BufferedImage;

public class Player extends Mob
{
	boolean[] keys;

	private Queue<Integer> movementQueue;	
	private AbstractInput input;
	public Player(int x, int y, int timeToMove, BufferedImage image, AbstractInput input)
	{
		super(x,y,timeToMove,false,true,false,image);
		this.input = input;
		this.movementQueue = new LinkedList<>();
	}
	
	public void tick(Game game)
	{	
		if (input.key_clicked(LogicalKey.RESTART))
		{
			dead = true;
		}
		if (time > 0)
		{
			time--;
		}

		KeyEventPair event;
		while ((event = input.keyqueue_get_next()) != null) 
		{
			if (event.is_key(LogicalKey.ACTION) && event.is_clicked() && game.clubs > 0) 
			{
				Entity e[] = checkDirection(game);
				if (lastdir != -1 && e[lastdir] != null && e[lastdir].breakable())
				{
					game.clubs--;
					e[lastdir].dead = true;
				}
			}
			if (event.is_clicked()) 
			{
				if (event.is_key(LogicalKey.LEFT))
					movementQueue.add(0);
				else if (event.is_key(LogicalKey.UP))
					movementQueue.add(1);
				else if (event.is_key(LogicalKey.RIGHT))
					movementQueue.add(2);
				else if (event.is_key(LogicalKey.DOWN))
					movementQueue.add(3);
			}
			if (event.is_released()) 
			{
				Integer nextMove = movementQueue.peek();
				if (nextMove != null)
				{
					if (nextMove == 0 && event.is_key(LogicalKey.LEFT))
						movementQueue.remove();
					else if (nextMove == 1 && event.is_key(LogicalKey.UP))
						movementQueue.remove();
					else if (nextMove == 2 && event.is_key(LogicalKey.RIGHT))
						movementQueue.remove();
					else if (nextMove == 3 && event.is_key(LogicalKey.DOWN))
						movementQueue.remove();
				}
			}
		}
		
		if (time == 0) 
		{
			Integer potentialMove = -1;
			while ((potentialMove = movementQueue.poll()) != null) 
			{
				if (potentialMove == 0) 
				{
					if (input.key_held(LogicalKey.LEFT))
						movementQueue.add(0);		
				}
				else if (potentialMove == 1)
				{
					if (input.key_held(LogicalKey.UP))
						movementQueue.add(1);
				}
				else if (potentialMove == 2) 
				{
					if (input.key_held(LogicalKey.RIGHT))
						movementQueue.add(2);
				}
				else if (potentialMove == 3)
				{
					if (input.key_held(LogicalKey.DOWN))
						movementQueue.add(3);
				}
				lastdir = potentialMove;
				Entity dir[] = checkDirection(game);
				if (tryMove(game, dir[potentialMove], potentialMove)) 
				{
					time = timeToMove;
					break;
				}
					
			}
		}
		/*else
		{
			if (input.key_held(LogicalKey.LEFT))
				direction = 0;
			else if (input.key_held(LogicalKey.UP))
				direction = 1;
			else if (input.key_held(LogicalKey.RIGHT))
				direction = 2;
			else if (input.key_held(LogicalKey.DOWN))
				direction = 3;
		
			if (direction > -1)
			{
				time = timeToMove;
				lastdir = direction;
				Entity dir[] = checkDirection(game);
				tryMove(game, dir[direction], direction);
				direction = -1;
			}
		}*/
	}
	
	//overriden
	public boolean checkMove(Game game, Entity e)
	{
		return e == null || e.passable() || e.isHostile() || e == game.exit || e.pushable();
	}
}
