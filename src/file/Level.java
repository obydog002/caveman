package src.file;

public class Level
{
	// are actually in bytes!
	public int width;
	public int height;
	public int entities[];
	
	public String name;
	
	public Level(int width, int height, int entities[], String name)
	{
		this.width = width;
		this.height = height;
		this.entities = entities;
		this.name = name;
	}
}
