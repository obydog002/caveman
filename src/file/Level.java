package src.file;

public class Level
{
	public String name;
	public int width;
	public int height;
	public int entities[];
	
	public Level(int width, int height, int entities[], String name)
	{
		this.width = width;
		this.height = height;
		this.entities = entities;
		this.name = name;
	}

	private int unbreakableWall() {
		return 1;
	}

	public Level(int width, int height, String name) {
		this.width = width;
		this.height = height;
		this.name = name;
		entities = new int[width*height];
		for (int row = 0; row < height; row++) {
			int ii = row * width;
			int jj = (row + 1) * width - 1;
			entities[ii] = unbreakableWall();
			entities[jj] = unbreakableWall();
		}
		for (int col = 0; col < width; col++) {
			int ii = col;
			int jj = col + width*(height - 1);
			entities[ii] = unbreakableWall();
			entities[jj] = unbreakableWall();
		}
	}
}
