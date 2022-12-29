package src.game;

import src.menu.Menu;

public class Screen extends Bitmap
{
	public final int scale;
	
	private int unscaled_width;
	private int unscaled_height;
	
	public Screen(int width, int height, int scale)
	{
		super(width*scale, height*scale);
		this.unscaled_width = width;
		this.unscaled_height = height;
		this.scale = scale;
	}
	
	public void render(Game game)
	{
		/*int inv_height = (unscaled_height - 1)*scale;
		
		draw(game.background,0,0, 1);
		draw(Art.toolBar,0,inv_height, 1);
		for (Entity e : game.entities)
		{
			draw(e.getImage(),scale * e.getX(),scale * e.getY(), 1);
		}
		
		draw(Art.club,0,inv_height, 1);
		String clubs = ":" + game.clubs;
		draw(clubs,32,inv_height,0xff444444, 1);
		draw(game.level_name,(int)(scale *(unscaled_width / 2.0 - 0.5*(game.level_name.length() - 1))),inv_height,0xff444444, 1);
		
		if (game.state != Game.State.GAMEPLAY)
		{
			darken(.2);
			draw(game.word,(int)(scale *(unscaled_width / 2.0 - 0.5*(game.word.length() - 1))),unscaled_width * (unscaled_height/2 - 1),game.wordCol, 1);
		}*/
	}

	
	// returns scaled width
	public int get_width()
	{
		return scale * unscaled_width;
	}
	
	// returns scaled height
	public int get_height()
	{
		return scale * unscaled_height;
	}
}