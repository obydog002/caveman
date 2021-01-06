package game;

public class Screen extends Bitmap
{
	public final int scale;
	
	private int unscaled_width;
	private int unscaled_height;
	
	private int inv_height;
	
	public Screen(int width, int height, int scale)
	{
		super(width*scale, height*scale);
		this.unscaled_width = width;
		this.unscaled_height = height;
		this.scale = scale;
		
		inv_height = (unscaled_height - 1)*scale;
	}
	
	public void render(Game game)
	{
		
		draw(game.background,0,0);
		draw(Art.toolBar,0,inv_height);
		for (Entity e : game.entities)
		{
			draw(e.getImage(),Main.SCALE * e.getX(),Main.SCALE * e.getY());
		}
		
		draw(Art.club,0,inv_height);
		String clubs = ":" + game.clubs;
		draw(clubs,32,inv_height,0xff444444);
		draw(game.levelName,(int)(Main.SCALE *(Main.WIDTH / 2.0 - 0.5*(game.levelName.length() - 1))),inv_height,0xff444444);
		
		if (game.state != Game.State.READY)
		{
			darken(.2);
			draw(game.word,(int)(Main.SCALE *(Main.WIDTH / 2.0 - 0.5*(game.word.length() - 1))),Main.SCALE * (Main.HEIGHT/2 - 1),game.wordCol);
		}
	}
	
}