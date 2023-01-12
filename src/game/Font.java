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

	private static HashMap<Integer, Point> char_pos_map;
	static
	{
		char_pos_map = new HashMap<>();
		char_pos_map.put(33, new Point(192, 96)); // !
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


	public void draw_string(String line, int x, int y)
	{
		
	}
}
