package src.game;

import java.util.HashMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Font
{
	static class Point
	{
		public int x;
		public int y;
		public Point(int _x, int _y)
		{
			this.x = _x;
			this.y = _y;
		}
	}

	// initialize alphabet position map
	private static HashMap<Integer, Point> char_pos_map;
	static
	{
		char_pos_map = new HashMap<>();
		char_pos_map.put(33, new Point(192, 96)); // !
		char_pos_map.put(58, new Point(224, 96)); // :
		for (int i = 0; i < 10; i++) // digits
		{
			int digit = i + 48;
			char_pos_map.put(digit, new Point(32*i, 0));
		}
		char_pos_map.put(63, new Point(256, 96)); // ?
		for (int i = 0; i < 26; i++) // lower case and upper case letters (drawn the same)
		{
			int upper_case_char = i + 65;
			int x = (upper_case_char - 65) % 10;
			int y = 1 + (upper_case_char - 65)/10;
			x *= 32;
			y *= 32;
			char_pos_map.put(upper_case_char, new Point(x, y));

			int lower_case_char = i + 97;
			x = (lower_case_char - 97) % 10;
			y = 1 + (lower_case_char - 97)/10;
			x *= 32;
			y *= 32;
			char_pos_map.put(lower_case_char, new Point(x, y));
		}
	}

    private BufferedImage alphabet;
    public Font(BufferedImage alphabet, int inside_col, int outside_col)
    {
		this.alphabet = alphabet;
        write_alphabet_colors(this.alphabet, inside_col, outside_col);
    }

    public final static int InsideReplaceCol = (0x0000FFFF << 8);
	public final static int OutsideReplaceCol = (0x00A29EFF << 8);
	private void write_alphabet_colors(BufferedImage img, int inside_col, int outside_col)
	{
		int width = img.getWidth();
		int height = img.getHeight();
		int pixels[] = new int[width * height];
		img.getRGB(0, 0, width, height, pixels, 0, width);
		for (int i = 0; i < pixels.length; i++)
		{
			int shifted = pixels[i] << 8;
			if (shifted == InsideReplaceCol)
			{
				pixels[i] = inside_col;
			}
			else if (shifted == OutsideReplaceCol)
			{
				pixels[i] = outside_col;
			}
		}
		img.setRGB(0, 0, width, height, pixels, 0, width);
	}

	public void draw_char(Graphics g, char c, int x, int y, int letter_width, int letter_height)
	{
		Point tex_coord = char_pos_map.get((int)c);
		if (tex_coord != null)
		{
			g.drawImage(alphabet, x, y, x + letter_width, y + letter_height, tex_coord.x, tex_coord.y, tex_coord.x + 32, tex_coord.y + 32, null);
		}
	}

	public void draw_string(Graphics g, String line, int x, int y, int letter_width, int letter_height)
	{
		char chars[] = line.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			draw_char(g, chars[i], x, y, letter_width, letter_height);
			x += letter_width;
		}
	}

	// draw the text centered around x.
	// This only does x centering.
	public void draw_string_centered(Graphics g, String line, int x, int y, int letter_width, int letter_height)
	{
		draw_string(g, line, x - line.length()*letter_width/2, y, letter_width, letter_height);
	}
}
