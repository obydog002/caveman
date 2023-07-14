package src.game;

import java.awt.image.BufferedImage;

public class Entity
{
	// entity position
	private int x;
	private int y;
	int id = 0;
	
	private BufferedImage image;
	private boolean passable; //dictates whether it can be passed
	private boolean hostile; // whether it can kill the player
	private boolean pushable; // whether it can be pushed
	private boolean breakable; // whether it can be broken with a club
	boolean dead;
	
	public Entity(int x, int y, boolean hostile, BufferedImage image)
	{
		this.x = x;
		this.y = y;
		this.image = image;
		this.hostile = hostile;
		breakable = false;
		passable = false;
		pushable = false;
		dead = false;
	}
	public boolean breakable()
	{
		return breakable;
	}
	public void setBreakable()
	{
		breakable = !breakable;
	}
	public boolean pushable()
	{
		return pushable;
	}
	public void setPush()
	{
		pushable = !pushable;
	}
	
	public boolean tryPush(Game game, int direction)
	{
		if (!pushable) return false;
		Entity e[] = checkDirection(game);
		if (e[direction] == null || e[direction].tryPush(game, direction)) 
		{
			translate(direction, 1);
			return true;
		}
		return false;
	}
	
	public Entity[] checkDirection(Game game) // 0 - left, 1 - up, 2 - right, 3 - down
	{
		Entity ok[] = new Entity[4]; // if theres an entity it will be an object from the list, otherwise null
		for (int i = 0; i < 4; i++)
			ok[i] = null;
		int x = this.getX();
		int y = this.getY();
		for (Entity e : game.entities)
		{
			if (e == this) continue;
			Entity l = (e.getX() == x - 1 && e.getY() == y) ? e : null;
			Entity u = (e.getX() == x && e.getY() == y - 1) ? e : null;
			Entity r = (e.getX() == x + 1 && e.getY() == y) ? e : null;
			Entity d = (e.getX() == x && e.getY() == y + 1) ? e : null;
			
			if (ok[0] == null)
				ok[0] = l;
			if (ok[1] == null)
				ok[1] = u;
			if (ok[2] == null)
				ok[2] = r;
			if (ok[3] == null)
				ok[3] = d;
			
		}
		return ok;
	}
	
	public void setPass()
	{
		passable = !passable; 
	}
	public boolean isHostile()
	{
		return hostile;
	}
	public void setHostile()
	{
		hostile = !hostile;
	}
	public boolean passable()
	{
		return passable;
	}
	public void tick(Game game)
	{
		return;
	}
	public void translate(int dir, int scale)
	{
		switch (dir)
		{
			case 0 : move(this.x - scale,this.y);
					 break;
			case 1 : move(this.x,this.y - scale);
					 break;
			case 2 : move(this.x + scale,this.y);
					 break;
			case 3 : move(this.x,this.y + scale);
					 break;
		}
	}
	
	public void move(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
}
