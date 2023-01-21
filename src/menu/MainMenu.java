package src.menu;

import src.file.FileManager;
import src.file.CampaignSave;

import src.game.GameMain;
import src.game.Input;
import src.game.Art;
import src.game.Draw;

import java.util.ArrayList;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;

public class MainMenu extends Menu
{
	String header = "caveman";
	private static final String AllOptions[] = {"new campaign", "continue campaign", "quit"};

	// length of options
	int options_len = 0;
	
	// GameMain reference
	private GameMain main;
	
	// Input reference
	private Input input;
	
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

	public MainMenu(GameMain main, Input input, int width, int height)
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
		
		options_len = options.size();
	}
	
	public void tick()
	{
		// move selection up the menu
		if (input.up.press_initial())
		{
			selection = (selection + options_len - 1) % options_len;
		} // move selection down the menu
		else if (input.down.press_initial())
		{
			selection = (selection + 1) % options_len;
		}
		
		// process selection choice
		if (input.use.press_initial())
		{
			// correct for if there are existing campaigns
			if (existing_campaign)
			{
				if (selection == 1)
					selection = -2;
				else
					selection--;
			}
			
			if (selection == -2) // existing campaign continue
			{
				System.out.println("existing campaign");
			}
			else if (selection == 0) // new campaign
			{
				main.set_new_campaign_menu();
				
				/*byte[] campaign_marker = new byte[4];
				campaign_marker[0] = (byte)'C';
				campaign_marker[1] = (byte)'M';
				campaign_marker[2] = (byte)'G';
				campaign_marker[3] = (byte)'1';
				
				int level = 3;
				String name = "cptrock69";
				
				FileManager.write_campaign_save_file(FileManager.RES_PATH + "/saves/", "test.SAV", campaign_marker, level, name);*/
			}
			else if (selection == 1) // quit
			{
				main.request_exit();
			}
		}
	}

	public void render(Graphics g, int width, int height)
	{
		Draw.fill_rect(g, 0, 0, width, height, Color.BLACK);

		Art.title_font.draw_string_centered(g, header, width/2, height/16, 32*4, 32*4);

		for (int i = 0; i < options.size(); i++)
		{
			String opt = options.get(i);
			Rectangle rect = options_bounding_boxes.get(i);

			if (selection == i)
			{
				Draw.fill_bordered_rect(g, rect.x, rect.y, rect.width, rect.height, 1, 1, SelectedBoxOutsideCol, SelectedBoxInnerCol);
			}
			else
			{
				Draw.fill_bordered_rect(g, rect.x, rect.y, rect.width, rect.height, 1, 1, BoxOutsideCol, BoxInnerCol);
			}

			Art.option_font.draw_string_centered(g, opt, rect.x + rect.width/2, rect.y, 32, 32);
		}
	}
}
