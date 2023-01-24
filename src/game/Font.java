package src.game;

import java.util.HashMap;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.util.HashMap;

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

	private HashMap<ColorPair, BufferedImage> font_cache;

	private final int AlphabetWidth;
	private final int AlphabetHeight;
	private final int AlphabetType;
	private final int AlphabetPixels[];
    public Font(BufferedImage alphabet)
    {
		this.font_cache =  new HashMap<ColorPair, BufferedImage>();

		this.AlphabetWidth = alphabet.getWidth();
		this.AlphabetHeight = alphabet.getHeight();
		this.AlphabetType = alphabet.getType();
		this.AlphabetPixels = new int[this.AlphabetWidth * this.AlphabetHeight];
		alphabet.getRGB(0, 0, this.AlphabetWidth, this.AlphabetHeight, this.AlphabetPixels, 0, this.AlphabetWidth);
    }

    public final static int InsideReplaceCol = (0x0000FFFF << 8);
	public final static int OutsideReplaceCol = (0x00A29EFF << 8);
	private void write_alphabet_colors(BufferedImage alphabet, Color outside_col, Color inside_col)
	{
		int width = alphabet.getWidth();
		int height = alphabet.getHeight();
		int pixels[] = new int[width * height];
		alphabet.getRGB(0, 0, width, height, pixels, 0, width);
		for (int i = 0; i < pixels.length; i++)
		{
			int shifted = AlphabetPixels[i] << 8;
			if (shifted == InsideReplaceCol)
			{
				pixels[i] = inside_col.getRGB();
			}
			else if (shifted == OutsideReplaceCol)
			{
				pixels[i] = outside_col.getRGB();
			}
		}
		alphabet.setRGB(0, 0, width, height, pixels, 0, width);
	}

	private void draw_char(Graphics g, BufferedImage alphabet, char c, int x, int y, int letter_width, int letter_height)
	{
		Point tex_coord = char_pos_map.get((int)c);
		if (tex_coord != null)
		{
			g.drawImage(alphabet, x, y, x + letter_width, y + letter_height, tex_coord.x, tex_coord.y, tex_coord.x + 32, tex_coord.y + 32, null);
		}
	}

	private void draw_string(Graphics g, String line, int x, int y, int letter_width, int letter_height, Color outside_col, Color inside_col)
	{
		ColorPair pair = new ColorPair(outside_col, inside_col);
		BufferedImage alphabet = font_cache.get(pair);
		if (alphabet == null)
		{
			System.out.println("adding to cache, writing image");
			alphabet = new BufferedImage(AlphabetWidth, AlphabetHeight, AlphabetType);
			write_alphabet_colors(alphabet, outside_col, inside_col);
			font_cache.put(pair, alphabet);
		}

		char chars[] = line.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			draw_char(g, alphabet, chars[i], x, y, letter_width, letter_height);
			x += letter_width;
		}
	}

	// draw the text centered around x.
	// This only does x centering.
	private void draw_string_centered(Graphics g, String line, int x, int y, int letter_width, int letter_height, Color outside_col, Color inside_col)
	{
		draw_string(g, line, x - line.length()*letter_width/2, y, letter_width, letter_height, outside_col, inside_col);
	}

	// color pair versions
	// first elt is outside, 2nd elt is inner color
	public void draw_string(Graphics g, String line, int x, int y, int letter_width, int letter_height, ColorPair color_pair)
	{
		draw_string(g, line, x, y, letter_width, letter_height, color_pair.first, color_pair.second);
	}

	public void draw_string_centered(Graphics g, String line, int x, int y, int letter_width, int letter_height, ColorPair color_pair)
	{
		draw_string_centered(g, line, x, y, letter_width, letter_height, color_pair.first, color_pair.second);
	}
}
