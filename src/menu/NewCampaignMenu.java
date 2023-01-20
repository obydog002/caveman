package src.menu;

import java.awt.Graphics;
import java.awt.Color;
import src.file.CampaignSave;

import src.game.KeyboardInput;
import src.game.GameMain;
import src.game.Screen;
import src.game.Constants;

public class NewCampaignMenu extends Menu
{
	String header = "your name player?";
	
	// reference to input
	private KeyboardInput input;
	
	private GameMain main;
	
	// 0: enter name screen
	// 1: choose campaign (if applicable)
	// 2: pass control back to GameMain for running game
	private int state = 0;
	
	// padding for boxes
	private int padding_x = 5;
	private int padding_y = 5;
	
	// input from the user
	String name;
	int name_length;
	
	// track time
	int tick_time = 30;
	
	// delay to prevent keyboard spam from previous enter
	boolean start_delay = true;
	
	// toggle to draw char line
	boolean draw_character_line = true;
	
	// warning to the user if they try to submit a zero length name
	// for use to draw for a certain time only
	int warning_timer = 0;
	
	// for the future - to decide whether we just start with the base campaign
	// or we give the option to allow for choices
	boolean ask_campaign = false;
	
	String campaign_marker = "CMG0";
	
	public NewCampaignMenu(GameMain main, KeyboardInput input)
	{
		this.main = main;
		
		this.input = input;
		
		this.name = "";
		this.name_length = 0;
	}
	
	public void tick()
	{
		if (state == 0)
		{
			if (!start_delay)
			{
				// a - z
				for (int i = 0; i < 26; i++)
				{
					if (input.keys[i] > 0 && name_length <= 32)
					{
						input.keys[i]--;
						char c = (char)(i + 'a');
						
						name += c;
						name_length++;
						
						warning_timer = 0;
					}
				}
				
				// 0 - 9
				for (int i = 0; i < 10; i++)
				{
					int jj = i + 29;
					if (input.keys[jj] > 0 && name_length <= 32)
					{
						input.keys[jj]--;
						char c = (char)(i + '0');
						
						name += c;
						name_length++;
						
						warning_timer = 0;
					}
				}
				
				// space
				if (input.keys[26] > 0 && name_length <= 32)
				{
					input.keys[26]--;
					
					name += ' ';
					name_length++;
					
					warning_timer = 0;
				}
				
				// enter
				if (input.keys[27] > 0)
				{
					// empty name, warn the user
					if (name_length == 0)
					{
						input.keys[27] = 0;
						
						// 2 seconds
						warning_timer = 120;
					}
					else
					{
						input.keys[27] = 0;
						
						if (ask_campaign)
							state = 1;
						else
							state = 2;
					
						start_delay = true;
					}
				}
				
				// backspace
				if (input.keys[28] > 0 && name_length > 0)
				{
					input.keys[28]--;
					
					name = name.substring(0, name_length - 1);
					name_length--;
				}
			}
			
			// process timer
			if (tick_time > 0)
				tick_time--;
			else
			{
				start_delay = false;
				tick_time = 30;
				
				draw_character_line = !draw_character_line;
			}
			
			if (warning_timer > 0)
				warning_timer--;
		}
		else if (state == 1)
		{
			
		}
		else if (state == 2)
		{
			main.set_campaign(new CampaignSave(campaign_marker.getBytes(), 5, name));
		}
	}

	/*
	int header_scale = 2;
	int header_col = 0xFFFFFFFF;
		
	int h2_scale = 1;
	int h2_col = 0xFFAAEEAA;
	
	public void render(Screen screen)
	{
		screen.clear(0);
		
		int width = screen.get_width();
		int height = screen.get_height();
		
		// name selection
		if (state == 0)
		{
			int xx = width/2 - (int)(0.5*header.length()*Constants.SCALE*header_scale);
			
			screen.draw(header, xx, height/16, header_col, header_scale);
			
			String h2 = "type name max length 32 characters";
			xx = width/2 - (int)(0.5*h2.length()*Constants.SCALE*h2_scale);
			
			screen.draw(h2, xx, 3*height/16, h2_col, h2_scale);
			
			int max_chars = 32;
			
			int box_width = max_chars*Constants.SCALE;
			int box_height = Constants.SCALE;
			
			xx = width/2 - (int)(0.5*box_width);
			int yy = 4*height/16;
			
			int box_inner_col = 0xFFBCBCBC;
			int box_outer_col = 0xFF919191;
			int box_outer_scale = 6;
		
			screen.draw_box(xx - padding_x, yy - padding_y, box_width + 2*padding_x, box_height + 2*padding_y, box_inner_col, box_outer_col, box_outer_scale);
			
			screen.draw(name, xx, yy, 0xFFDD1111, 1);
			
			if (draw_character_line)
			{
				xx += name_length*Constants.SCALE;
				screen.draw_box(xx, yy, 4, box_height, 0xFF000000, 0xFF000000, 0);
			}
			
			h2 = "press enter to continue";
			
			xx = width/2 - (int)(0.5*h2.length()*Constants.SCALE*h2_scale);
			
			yy = 5*height/16;
			screen.draw(h2, xx, yy, h2_col, h2_scale);
			
			if (warning_timer > 0)
			{
				h2 = "cannot continue with an empty name!";
				
				xx = width/2 - (int)(0.5*h2.length()*Constants.SCALE*h2_scale);
				
				yy = 7*height/16;
				screen.draw(h2, xx, yy, 0xFFFF0000, h2_scale);
			}
		}
		else
		{
			
		}
	
	}
	*/
	public void render(Graphics g, int width, int height)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
	}
}