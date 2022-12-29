package src.game;

import java.awt.image.BufferedImage;

public class MovableWall extends Entity
{
	boolean[] dir; // a boolean array with 4 elements dictating whether this wall can be moved 0-left,1-up,2-right,3-down.
	public MovableWall(int x, int y, boolean hostile, boolean[] dir, BufferedImage image)
	{
		super(x,y,hostile,image);
		this.dir = dir;
		setPush();
		setBreakable();
	}
	
	public boolean tryPush(Game game, int direction)
	{
		if (!dir[direction]) return false;
		Entity e[] = checkDirection(game);
		if (e[direction] == null || e[direction].tryPush(game, direction)) 
		{
			translate(direction, 1);
			return true;
		}
		return false;
	}
} 