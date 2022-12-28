package src.menu;

import src.file.FileManager;
import src.file.CampaignSave;

import src.game.Screen;
import src.game.GameMain;
import src.game.Input;
import src.game.Constants;
import src.game.Input;

import java.util.ArrayList;

import java.awt.Rectangle;

public class MainMenu extends Menu
{
	String header = "caveman";
	String all_options[] = {"new campaign", "continue campaign", "quit"};

	// length of options
	int options_len = 0;
	
	// GameMain reference
	private GameMain main;
	
	// Input reference
	private Input input;
	
	// arraylist of options and their bounding boxes
	ArrayList<String> options;
	ArrayList<Rectangle> options_bounding_boxes;
	
	// selection
	int selection = 0;
	
	// we include the continue campaign button if there is already an existing file
	private boolean existing_campaign;
	
	// get the biggest string for box stuff
	private int option_max_length = 0;
	
	// padding for boxes
	private int padding_x = 5;
	private int padding_y = 5;
	
	public MainMenu(GameMain main, Input input, int width, int height)
	{	
		this.main = main;
		
		this.input = input;
		
		this.existing_campaign = false;
		
		this.options = new ArrayList<>();
		this.options_bounding_boxes = new ArrayList<>();
		
		for (int i = 0; i < all_options.length; i++)
		{
			if (i != 1 || existing_campaign)
				options.add(all_options[i]);
		}
		
		for (String s : options)
		{
			int length = s.length();
			if (length > option_max_length)
				option_max_length = length;
		}
		
		int i = 0;
		
		for (String s : options)
		{
			int xx = width/2 - (int)(0.5*option_max_length * Constants.SCALE) - padding_x;
			int yy = ((4+i)*height)/16 - padding_y;
			int r_width = option_max_length*Constants.SCALE + 2*padding_x;
			int r_height = Constants.SCALE + 2*padding_y;
			
			options_bounding_boxes.add(new Rectangle(xx, yy, r_width, r_height));
			
			i++;
		}
		
		options_len = options.size();
	}
	
	public void render(Screen screen)
	{	
		// clear screen to black
		screen.clear(0);
		
		int width = screen.get_width();
		int height = screen.get_height();
		
		int header_scale = 4;
		int header_col = 0xFFE4864B;
		
		screen.draw(header, width/2 - (int)(0.5*header.length()*Constants.SCALE*header_scale), height/16, header_col, header_scale);
		
		int box_inner_col = 0xFFBCBCBC;
		int box_outer_col = 0xFF919191;
		int box_outer_scale = 6;

		int options_col = 0xFFE85353;
		int i = 0;
		for (String s : options)
		{
			Rectangle rect = options_bounding_boxes.get(i);
			
			if (selection == i)
			{
				screen.draw_box(rect.x, rect.y, rect.width, rect.height, screen.darken(box_inner_col, 0.5), screen.darken(box_outer_col, 0.5), box_outer_scale);
			}
			else
			{
				screen.draw_box(rect.x, rect.y, rect.width, rect.height, box_inner_col, box_outer_col, box_outer_scale);
			}
			
			int str_len = s.length();
			
			screen.draw(s, rect.x + padding_x, rect.y + padding_y, options_col, 1);
			
			i++;
		}
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
}
