package src.menu;

import src.file.FileManager;
import src.file.CampaignSave;

import src.game.GameMain;
import src.game.AbstractInput;
import src.game.LogicalKey;
import src.game.Art;
import src.game.Draw;
import src.game.Style;

import java.util.ArrayList;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.KeyEvent;

public class MainMenu extends Menu
{
	String header = "caveman";
	private static final String AllOptions[] = {"New game", "Continue game", "Editor", "Quit"};
	
	// GameMain reference
	private GameMain main;
	
	// AbstractInput reference
	private AbstractInput input;
	
	int border_width = 1;
	int border_height = 1;
	int button_scale = 32;

	// arraylist of options and their bounding boxes
	ArrayList<String> options;
	ArrayList<Rectangle> options_bounding_boxes;
	
	// selection
	int selection = 0;
	
	// we include the continue campaign button if there is already an existing file
	private boolean existing_campaign;

	public MainMenu(GameMain main, AbstractInput input, int width, int height)
	{	
		this.main = main;
		
		this.input = input;
		
		this.existing_campaign = false;
		
		this.options = new ArrayList<>();
		this.options_bounding_boxes = new ArrayList<>();
		
		for (int i = 0; i < AllOptions.length; i++)
		{
			if (i != 1 || existing_campaign)
				options.add(AllOptions[i]);
		}
		
		int options_max_length = 0;
		for (String s : options)
		{
			int length = s.length();
			if (length > options_max_length)
				options_max_length = length;
		}
		
		for (int i = 0; i < options.size();  i++)
		{
			int xx = width/2 - options_max_length * button_scale/2;
			int yy = ((4+i)*height)/16;
			int r_width = options_max_length*button_scale;
			int r_height = button_scale;
			
			options_bounding_boxes.add(new Rectangle(xx, yy, r_width, r_height));
		}
	}
	
	private void process_menu_selection(int selection)
	{
		String option = options.get(selection);
		if (option.equals("New game")) {
			main.set_new_campaign_menu();
		} else if (option.equals("Continue game")) {
			System.out.println("Continue game");
		} else if (option.equals("Editor")) {
		} else if (option.equals("Quit")) {
			main.request_exit();
		}
	}

	public void tick()
	{
		// move selection up the menu
		if (input.key_clicked(LogicalKey.UP))
		{
			selection = (selection + options.size() - 1) % options.size();
		} // move selection down the menu
		else if (input.key_clicked(LogicalKey.DOWN))
		{
			selection = (selection + 1) % options.size();
		}
		
		// process selection choice
		if (input.key_clicked(LogicalKey.ACTION))
		{
			process_menu_selection(selection);
		}
	}

	public void render(Graphics g, int width, int height)
	{
		Draw.fill_rect(g, 0, 0, width, height, Color.BLACK);

		Art.font.draw_string_centered(g, header, width/2, height/16 + 2*34, 32*4, 32*4, Style.title_color_pair);

		for (int i = 0; i < options.size(); i++)
		{
			String opt = options.get(i);
			Rectangle rect = options_bounding_boxes.get(i);

			if (selection == i)
			{
				Draw.fill_bordered_rect(g, rect.x, rect.y, rect.width, rect.height, 2, 2, SelectedBoxOutsideCol, SelectedBoxInnerCol, true);
			}
			else
			{
				Draw.fill_bordered_rect(g, rect.x, rect.y, rect.width, rect.height, 2, 2, BoxOutsideCol, BoxInnerCol, true);
			}

			Art.font.draw_string_centered(g, opt, rect.x + rect.width/2, rect.y + rect.height/2, 32, 32, Style.option_color_pair);
		}
	}
}
