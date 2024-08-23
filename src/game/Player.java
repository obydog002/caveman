package src.game;

import java.util.Queue;
import java.util.ArrayList;

import java.awt.image.BufferedImage;

public class Player extends Mob
{
	boolean[] keys;

	private int currentPosInMovementQueue;
	private ArrayList<KeyEventPair> movementQueue; 
	private AbstractInput input;
	public Player(int x, int y, int timeToMove, BufferedImage image, AbstractInput input)
	{
		super(x,y,timeToMove,false,true,false,image);
		this.input = input;
		this.currentPosInMovementQueue = -1;
		this.movementQueue = new ArrayList<>();
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
			else if (event.is_key(LogicalKey.LEFT) || event.is_key(LogicalKey.UP) || event.is_key(LogicalKey.RIGHT) || event.is_key(LogicalKey.DOWN)) 
			{
				if (event.is_clicked())
					movementQueue.add(event);
			}
		}
		
		if (time == 0) 
		{
			boolean finished = false;
			while (!finished) 
			{
				if (movementQueue.size() > 0) 
				{
					if (currentPosInMovementQueue == -1 || currentPosInMovementQueue >= movementQueue.size()) {
						currentPosInMovementQueue = 0;
						finished = true;
					}
	
					KeyEventPair candidateMove = movementQueue.get(currentPosInMovementQueue);
					if (candidateMove.is_clicked() || (candidateMove.is_held() && input.key_held(candidateMove.logicalKey)))
					{
						int potentialDirection = -1;
						if (candidateMove.is_key(LogicalKey.LEFT))
							potentialDirection = 0;
						else if (candidateMove.is_key(LogicalKey.UP))
							potentialDirection = 1;
						else if (candidateMove.is_key(LogicalKey.RIGHT))
							potentialDirection = 2;
						else if (candidateMove.is_key(LogicalKey.DOWN))	
							potentialDirection = 3;				
						lastdir = potentialDirection;
						Entity dir[] = checkDirection(game);
						if (tryMove(game, dir[potentialDirection], potentialDirection))
						{
							time = timeToMove;
							finished = true;
						}

						if (candidateMove.is_clicked()) 
							candidateMove.type = KeyEventPair.KeyEventType.PRESSED; 
						currentPosInMovementQueue++;
					}
					else
						movementQueue.remove(currentPosInMovementQueue);
						
				}
				else 
				{
					currentPosInMovementQueue = -1;
					finished = true;
				}
			}
		}
	}
	
	//overriden
	public boolean checkMove(Game game, Entity e)
	{
		return e == null || e.passable() || e.isHostile() || e == game.exit || e.pushable();
	}
}
