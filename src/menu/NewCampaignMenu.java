package src.menu;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;

import src.file.CampaignSave;

import src.game.AbstractInput;
import src.game.GameMain;
import src.game.Art;
import src.game.Draw;
import src.game.Style;
import src.game.KeyEventPair;

public class NewCampaignMenu extends Menu
{
	String header = "your name player?";
	
	// reference to input
	private AbstractInput input;
	
	private GameMain main;
	
	// 0: enter name screen
	// 1: choose campaign (if applicable)
	// 2: pass control back to GameMain for running game
	private int state = 0;
	
	// input from the user
	String name;
	
	// track time
	int tick_time = 30;
	
	// toggle to draw char line
	boolean draw_character_line = true;
	
	// warning to the user if they try to submit a zero length name
	// for use to draw for a certain time only
	int warning_timer = 0;
	
	String campaign_marker = "CMG0";
	
	public NewCampaignMenu(GameMain main, AbstractInput input)
	{
		this.main = main;
		
		this.input = input;
		
		this.name = "";
	}
	
	public void tick()
	{
		if (state == 0)
		{
			KeyEventPair event;
			while ((event = input.keyqueue_get_next()) != null)
			{
				if (event.type == KeyEventPair.KeyEventType.PRESSED || event.type == KeyEventPair.KeyEventType.CLICKED)
				{
					if (event.rawCode == KeyEvent.VK_ENTER)
					{
						if (name.length() == 0)
						{
							warning_timer = 120;
						}
						else
						{
							state = 2;
						}
					}
					else if (event.rawCode == KeyEvent.VK_BACK_SPACE)
					{
						if (name.length() > 0)
							name = name.substring(0, name.length() - 1);
					}
					else if (name.length() < 32) // normal keys
					{
						name += (char)event.rawCode;

						warning_timer = 0;
					}
				}
			}

			if (warning_timer > 0)
				warning_timer--;

			if (tick_time > 0)
				tick_time--;
			else
			{
				tick_time = 30;
				draw_character_line = !draw_character_line;
			}
		}
		else 
		{
			main.set_campaign(new CampaignSave(campaign_marker.getBytes(), 1, name));
		}
	}

	/*
	public void tick()
	{
		if (state == 0)
		{
			if (!start_delay)
			{
				// a - z
				for (int i = 0; i < 26; i++)
				{
					if (input.keys[i] && name_length <= 32)
					{
						input.keys[i] = false;
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
					if (input.keys[jj] && name_length <= 32)
					{
						input.keys[jj] = false;
						char c = (char)(i + '0');
						
						name += c;
						name_length++;
						
						warning_timer = 0;
					}
				}
				
				// space
				if (input.keys[26] && name_length <= 32)
				{
					input.keys[26] = false;
					
					name += ' ';
					name_length++;
					
					warning_timer = 0;
				}
				
				// enter
				if (input.keys[27])
				{
					// empty name, warn the user
					if (name_length == 0)
					{
						input.keys[27] = false;
						
						// 2 seconds
						warning_timer = 120;
					}
					else
					{
						input.keys[27] = false;
						
						if (ask_campaign)
							state = 1;
						else
							state = 2;
					
						start_delay = true;
					}
				}
				
				// backspace
				if (input.keys[28] && name_length > 0)
				{
					input.keys[28] = false;
					
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
		}
	}
	*/

	public void render(Graphics g, int width, int height)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		// name selection
		if (state == 0)
		{
			Art.font.draw_string_centered(g, header, width/2, height/16 + 32, 32*2, 32*2, Style.title_color_pair);
			String h2 = "type name max length 32 characters";
			Art.font.draw_string_centered(g, h2, width/2, 3*height/16 + 32/2, 32, 32, Style.option_color_pair);
			int max_chars = 32;
			int enter_box_width = max_chars * 32;
			int enter_box_height = 32;
			Color box_inner_col = new Color(0xbc, 0xbc, 0xbc);
			Color box_outer_col = new Color(0x91, 0x91, 0x91);
			int xx = width/2 - enter_box_width/2;
			int yy = 4*height/16;
			// box for input
			Draw.fill_bordered_rect(g, xx, yy, enter_box_width, enter_box_height, 6, 6, box_outer_col, box_inner_col, true);
			Art.font.draw_string(g, name, xx, yy, 32, 32, Style.neutral_color_pair);

			if (draw_character_line)
			{
				xx += name.length()*32;
				Draw.fill_rect(g, xx, yy, 4, enter_box_height, Color.BLACK);
			}

			h2 = "press enter to continue";

			xx = width/2;
			
			yy = 5*height/16 + 32/2;
			Art.font.draw_string_centered(g, h2, xx, yy, 32, 32, Style.option_color_pair);
			
			if (warning_timer > 0)
			{
				h2 = "cannot continue with an empty name!";
				
				xx = width/2; 
				
				yy = 7*height/16 + 32/2;
				Art.font.draw_string_centered(g, h2, xx, yy, 32, 32, Style.option_color_pair);
			}
		}
	}
}
