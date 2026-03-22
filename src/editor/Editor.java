package src.editor;

import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import src.file.FileManager;
import src.file.Level;
import src.game.AbstractInput;
import src.game.Art;
import src.game.CavemanMain;
import src.game.Control;
import src.game.Draw;
import src.game.GameConstants;
import src.game.KeyEventPair;
import src.game.LogicalKey;
import src.game.Style;
import src.menu.EscapeMenu;

public class Editor extends Control {
	private AbstractInput input;
	private CavemanMain main;
	private Level level;
	private int selection = 0;
	private int x_selection = 0;
	private int y_selection = 0;
	private int screenWidth = 0;
	private int screenHeight = 0;
	private int levelHeight = 0;
	private double entity_width = 0;
	private double entity_height = 0;

	private String savedFilePath = null;

	public Editor(AbstractInput input, CavemanMain main)
	{
		this.input = input;
		this.main = main;
		this.level = new Level(32, 20, "");
	}

	public void setLevel(Level l) {
		this.level = l;
		this.x_selection = 0;
		this.y_selection = 0;
		this.savedFilePath = null;
	}

	private void doSave() {
		if (savedFilePath != null) {
			FileManager.writeCVLevel(new File(savedFilePath), level);
		} else {
			FileDialog fd = new FileDialog(main.getFrame(), "Save Level", FileDialog.SAVE);
			fd.setFile("level.caveman");
			fd.setVisible(true);
			String dir = fd.getDirectory();
			String file = fd.getFile();
			if (file != null) {
				if (!file.endsWith(".caveman")) file += ".caveman";
				savedFilePath = dir + file;
				FileManager.writeCVLevel(new File(savedFilePath), level);
			}
		}
	}

	private void set_x_selection(int v) {
		if (v < 0) {
			x_selection = this.level.width - 1;
		} else {
			x_selection = (v + 1) % this.level.width;
		}
	}
	private void set_y_selection(int v) {
		if (v < 0) {
			y_selection = this.level.height - 1;
		} else {
			y_selection = (v + 1) % this.level.height;
		}
	}

	public void tick() { 
		if (input.key_held(LogicalKey.RIGHT)) {
			x_selection = (x_selection + 1) % this.level.width;
		}
		if (input.key_held(LogicalKey.DOWN)) {
			y_selection = (y_selection + 1) % this.level.height;
		}
		if (input.key_held(LogicalKey.LEFT)) {
			if (x_selection == 0) {
				x_selection = this.level.width - 1;
			} else {
				x_selection -= 1;
			}
		}
		if (input.key_held(LogicalKey.UP)) {
			if (y_selection == 0) {
				y_selection = this.level.height - 1;
			} else {
				y_selection -= 1;
			}
		}

		if (input.key_clicked(LogicalKey.LEFT2)) {
			if (selection == 0) {
				selection = 16;
			} else {
				selection--;
			}
		}

		if (input.key_clicked(LogicalKey.RIGHT2)) {
			if (selection == 16) {
				selection = 0;
			} else {
				selection++;
			}
		}

		if (input.key_held(LogicalKey.ACTION)) {
			int index = x_selection + y_selection * level.width;
			level.entities[index] = selection;
		}

		KeyEventPair pair;
		while ((pair = input.keyqueue_get_next()) != null) {
			if (pair.is_clicked()) {
				if (pair.rawCode == KeyEvent.VK_ESCAPE) {
					main.add_process(buildEscapeMenu());
					return;
				} else if (pair.rawCode == KeyEvent.VK_S && pair.is_ctrl_or_meta()) {
					doSave();
				}
			}
		}
	}

	private EscapeMenu buildEscapeMenu() {
		ArrayList<String> opts = new ArrayList<>();
		ArrayList<Runnable> acts = new ArrayList<>();

		opts.add("resume");      acts.add(null);
		opts.add("new level");   acts.add(() -> setLevel(new Level(32, 20, "")));
		opts.add("save level");  acts.add(() -> doSave());
		opts.add("load level");  acts.add(() -> {
			FileDialog fd = new FileDialog(main.getFrame(), "Load Level", FileDialog.LOAD);
			fd.setVisible(true);
			String dir = fd.getDirectory();
			String file = fd.getFile();
			if (file != null) {
				setLevel(FileManager.readCVLevel(new File(dir + file)));
			}
		});

		if (main.hasPreviousProcess()) {
			opts.add("main menu");
			acts.add(() -> shouldExit = true);
		}

		return new EscapeMenu(input, "editor", opts.toArray(new String[0]), acts.toArray(new Runnable[0]), this);
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
		this.entity_width = ((double)width)/level.width;
		this.entity_height = ((double)height)/level.height;
		for (int i = 0; i < level.entities.length; i++) {
			int xx = i % level.width;
			int yy = i / level.width;
			int ent_x = (int)(entity_width * xx);
			int ent_y = (int)(entity_height * yy);
			int next_x = (int)(entity_width * (xx + 1));
			int next_y = (int)(entity_height * (yy + 1));
			int ent_width = next_x - ent_x;
			int ent_height = next_y - ent_y;
			g.drawImage(indexToImage(level.entities[i]), ent_x, ent_y, ent_width, ent_height, null);
			if (xx == x_selection && yy == y_selection) {
				g.drawImage(indexToImage(selection), ent_x, ent_y, ent_width, ent_height, null);
				Draw.draw_rect(g, ent_x, ent_y, ent_width, ent_height, Art.SelectionColor, 5);
			}
		}
	}

	private void render_selection(Graphics g, int i, int x, int y, int width, int height, boolean primary) {
		int xx = x;
		int yy = y;
		int ww = width;
		int hh = height;
		if (!primary) {
			double size = 0.8;
			ww = (int)(width*size);
			hh = (int)(height*size);
			xx += (1 - size) * width / 2;
			yy += (1 - size) * height / 2;
		}
		g.drawImage(indexToImage(i), xx, yy, ww, hh, null);
	}

	private void render_toolbar(Graphics g, int x, int y, int width, int height) {
		g.drawImage(Art.toolBar, x, y, width, height, null);
		if (selection == 0) {
			render_selection(g, 16, x, y, height, height, false);
		} else {
			render_selection(g, selection - 1, x, y, height, height, false);
		}
		render_selection(g, selection, height + x, y, height, height, true);
		render_selection(g, selection + 1, 2*height + x, y, height, height, false);
		Art.font.draw_string(g, "esc: menu", 3 * height + 10, y + (height - 12) / 2, 12, 12, Style.neutral_color_pair);
	}

	public void render(Graphics g, int width, int height) {
		this.screenHeight = height;
		this.screenWidth = width;
		this.levelHeight = height - GameConstants.ToolBarHeight;
		g.drawImage(Art.rockBackground, 0, 0, screenWidth, screenHeight, null);
		render_level(g, 0, 0, screenWidth, levelHeight);
		render_toolbar(g, 0, levelHeight, screenWidth, GameConstants.ToolBarHeight);
	}

	private boolean shouldExit = false;

	public boolean requestExit() {
		return shouldExit;
	}

	public boolean requestFullExit() {
		return false;
	}

	// mouse motion listener methods
	public void mouseDragged(MouseEvent e)
	{
		if (entity_width > 0 && entity_height > 0) {
			this.x_selection = (int)(e.getX() / entity_width);
			this.y_selection = (int)(e.getY() / entity_height);
		}
		level.entities[x_selection + y_selection * level.width] = selection;
	}
	
	public void mouseMoved(MouseEvent e)
	{
		if (entity_width > 0 && entity_height > 0) {
			this.x_selection = (int)(e.getX() / entity_width);
			this.y_selection = (int)(e.getY() / entity_height);
		}
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
