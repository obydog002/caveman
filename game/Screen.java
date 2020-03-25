package game;

public class Screen extends Bitmap
{
	public Screen(int width, int height)
	{
		super(width, height);
	}
	public void render(Game game)
	{
		draw(game.background,0,0);
		draw(Art.toolBar,0,864);
		for (Entity e : game.entities)
		{
			draw(e.getImage(),Main.SCALE * e.getX(),Main.SCALE * e.getY());
		}
		draw(Art.club,0,864);
		String clubs = ":" + game.clubs;
		draw(clubs,32,864,0xff444444);
		draw(game.levelName,(int)(Main.SCALE *(Main.WIDTH / 2.0 - 0.5*(game.levelName.length() - 1))),864,0xff444444);
		if (game.state != 2)
		{
			darken(.2);
			draw(game.word,(int)(Main.SCALE *(Main.WIDTH / 2.0 - 0.5*(game.word.length() - 1))),Main.SCALE * (Main.HEIGHT/2 - 1),game.wordCol);
		}
	}
	
}