package src.editor;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.w3c.dom.events.MouseEvent;

import src.file.Level;
import src.game.AbstractInput;
import src.game.Art;
import src.game.CavemanMain;
import src.game.Control;
import src.game.GameConstants;
import src.game.Style;

public class Editor extends Control {
    private AbstractInput input;
    private CavemanMain main;
	private Level level;
	private int selection = 0;

    public Editor(AbstractInput input, CavemanMain main)
	{
		this.input = input;
		this.main = main;
		this.level = new Level(32, 20, "");
    }

    public void tick() {

    }

	private BufferedImage indexToImage(int i) {
		switch (i) {
				case 1: return Art.rock;
				case 2: return Art.rock2;
				case 3: return Art.boulder;
				case 4: return Art.boulderl;
				case 5: return Art.boulderu;
				case 6: return Art.boulderr;
				case 7: return Art.boulderd;
				case 8: return Art.boulderlr;
				case 9: return Art.boulderud;
				case 10:return Art.boulderlurd;
				case 11:return Art.player;
				case 12:return Art.exit;
				case 13:return Art.club;
				case 14:return Art.green; // mammoths
				case 15:return Art.yellow;
				case 16:return Art.red;
				default:return null;
			}
	}

	private void render_level(Graphics g, int x, int y, int width, int height) {
		double ent_stride_width = ((double)width)/level.width;
		double ent_stride_hight = ((double)height)/level.height;
		for (int i = 0; i < level.entities.length; i++) {
			int xx = i % level.width;
			int yy = i / level.width;
			int ent_x = (int)(ent_stride_width * xx);
			int ent_y = (int)(ent_stride_hight * yy);
			int next_x = (int)(ent_stride_width * (xx + 1));
			int next_y = (int)(ent_stride_hight * (yy + 1));
			int ent_width = next_x - ent_x;
			int ent_height = next_y - ent_y;
			g.drawImage(indexToImage(level.entities[i]), ent_x, ent_y, ent_width, ent_height, null);
		}
	}

	private void render_selection(Graphics g, int i, int x, int y, int width, int height) {
		g.drawImage(indexToImage(i), x, y, width, height, null);
	}

	private void render_toolbar(Graphics g, int x, int y, int width, int height) {
		g.drawImage(Art.toolBar, x, y, width, height, null);
		if (selection == 0) {
			render_selection(g, 16, x, y, height, height);
		} else {
			render_selection(g, selection - 1, x, y, height, height);
		}
		render_selection(g, selection, height + x, y, height, height);
		render_selection(g, selection + 1, 2*height + x, y, height, height);
	}

    public void render(Graphics g, int width, int height) {
		g.drawImage(Art.rockBackground, 0, 0, width, height, null);
		int levelHeight = height - GameConstants.ToolBarHeight;
		render_level(g, 0, 0, width, levelHeight);
		render_toolbar(g, 0, levelHeight, width, GameConstants.ToolBarHeight);
    }

    public boolean requestExit() {
        return false;
    }

    public boolean requestFullExit() {
        return false;
    }

    // mouse motion listener methods
	public void mouseDragged(MouseEvent e)
	{
		
	}
	
	public void mouseMoved(MouseEvent e)
	{
		
	}
	
	// mouse listener methods
	public void mouseClicked(MouseEvent e)
	{

	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mousePressed(MouseEvent e)
	{

	}
	
	public void mouseReleased(MouseEvent e)
	{
	
	}
}