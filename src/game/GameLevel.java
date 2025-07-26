package src.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import src.file.Level;

public class GameLevel {
    BufferedImage background;
    public String name;
	public int width;
	public int height;
    public ArrayList<Entity> entities;

    private void loadEntities(Level level, Player player, Entity exit) {
        for (int i = 0; i < level.entities.length; i++)
		{
			int xx = i % level.width;
			int yy = i / level.width;
			
			switch (level.entities[i])
			{
                case 0: // EMPTY
                        break;
				case 1: entities.add(new Entity(xx, yy, false, Art.rock));
						break; // unbreakable wall1
				case 2: entities.add(new Entity(xx, yy, false, Art.rock2));
						break; // unbreakable wall2
				case 3: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,false,false,false},Art.boulder));
						break; // boulder
				case 4: entities.add(new MovableWall(xx,yy,false,new boolean[] {true,false,false,false},Art.boulderl));
						break; // boulder left
				case 5: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,true,false,false},Art.boulderu));
						break; // boulder up
				case 6: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,false,true,false},Art.boulderr));
						break; // boulder right
				case 7: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,false,false,true},Art.boulderd));
						break; // boulder down
				case 8: entities.add(new MovableWall(xx,yy,false,new boolean[] {true,false,true,false},Art.boulderlr));
						break; // boulder left-right
				case 9: entities.add(new MovableWall(xx,yy,false,new boolean[] {false,true,false,true},Art.boulderud));
						break; // boulder up-down
				case 10:entities.add(new MovableWall(xx,yy,false,new boolean[] {true,true,true,true},Art.boulderlurd));
						break; // boulder all
				case 11:player.move(xx,yy);
						entities.add(player);
						break; // player spawn
				case 12:exit.move(xx,yy);
						entities.add(exit);
						break; // exit 
				case 13:Entity club = new Entity(xx,yy,false,Art.club);
						club.setPass();
						club.setPush();
						club.id = 100;
						entities.add(club);
						break; // club
				case 14:entities.add(new Mob(xx,yy,GameConstants.MammothTicksPerMovement,false,false,false,Art.green));
						break; // green mammoth
				case 15:entities.add(new Mob(xx,yy,GameConstants.MammothTicksPerMovement,false,true,false,Art.yellow));
						break; // yellow mammoth
				case 16:entities.add(new Mob(xx,yy,GameConstants.MammothTicksPerMovement,true,true,true,Art.red));
						break; // red mammoth
			}
		}
    }

    public GameLevel(Level level, Player player, Entity exit) {
        this.name = level.name;
        this.width = level.width;
        this.height = level.height;
        this.entities = new ArrayList<>();
        loadEntities(level, player, exit);
        this.background = Art.rockBackground;
    }

    public void render(Graphics g, int x, int y, int drawWidth, int drawHeight) {
        g.drawImage(background, x, y, drawWidth, drawHeight, null);

		double ent_stride_width = ((double)drawWidth)/width;
		double ent_stride_height = ((double)drawHeight)/height;
		for (Entity e : entities)
		{
			int ent_x = (int)(ent_stride_width * e.getX());
			int ent_y = (int)(ent_stride_height * e.getY());
			int next_x = (int)(ent_stride_width * (e.getX() + 1));
			int next_y = (int)(ent_stride_height * (e.getY() + 1));
			int ent_width = next_x - ent_x;
			int ent_height = next_y - ent_y;
			g.drawImage(e.getImage(), ent_x, ent_y, ent_width, ent_height, null);
		}
    }
}
