package src.game;

public class Bitmap
{
	// colors that the bitmap drawing will ignore, resulting in completely transparent at that location
	public static final int IGNORE_COL = 0xFEFF00FF;
	public static final int IGNORE_COL2 = 0xFEFFFF00;
	
	// color inside the char to be replaced with char color
	public static final int CHAR_INSIDE_COL = 0xFE00FFFF;
	
	// color bordering the char
	public static final int CHAR_OUTSIDE_COL = 0xFEA29EFF;
	
	
	public final int width;
	public final int height;
	public final int[] pixels;
	
	public Bitmap(int width, int height)
	{
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
	}
	
	// clear all pixels to a colour
	public void clear(int col)
	{
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i] = col;
		}
	}
	
	// darken an individual pixel and return it
	// 1 wont darken, 
	// 0 completely black
	public int darken(int col, double scale)
	{
		int r = (col >> 16) & 0x000000ff;
		int g = (col >> 8) & 0x000000ff;
		int b = col & 0x000000ff;
		r *= scale;
		g *= scale;
		b *= scale;
		
		return 0xFF000000 + (r << 16) + (g << 8) + b;
	}
	
	// darken all pixels by a fraction 
	// 1 wont darken, 
	// 0 completely black
	public void darken(double scale)
	{
		for (int i = 0; i < pixels.length; i++)
		{
			int col = pixels[i];
			int r = (col >> 16) & 0x000000FF;
			int g = (col >> 8) & 0x000000FF;
			int b = col & 0x000000FF;
			r *= scale;
			g *= scale;
			b *= scale;
			
			pixels[i] = 0xFF000000 + (r << 16) + (g << 8) + b;
		}
	}
	
	// draw word
	public void draw(String word, int xOffs, int yOffs, int col, int scale)
	{
		for (int i = 0; i < word.length(); i++)
		{
			drawChar((int)word.charAt(i),xOffs + scale*Constants.SCALE*i,yOffs,col, scale);
		}
	}
	
	// draw individual char
	// TODO - GET RID OF ALL THESE MAGIC NUMBERS!!! USE ENUM OR SOMETHING IDIOT
	public void drawChar(int c, int xOffs, int yOffs, int col, int scale)
	{
		int bitx = 288;
		int bity = 96;
		if (c == 32) // SPACE
		{
			return;
		}
		else if (c == 33) // EXPLANATION MARK
		{
			bitx = 192;
			bity = 96;
		}
		else if (c == 58) // COLON
		{
			bitx = 224;
			bity = 96;
		}
		else if (c >= 48 && c < 58) // digits
		{
			int z = c - 48;
			bitx = 32*z;
			bity = 0;
		}
		else if (c == 63) // QUESTION MARK
		{
			bitx = 256;
			bity = 96;
		}
		else if (c >= 97 && c < 123) // capitals
		{
			int z = (c - 97) % 10;
			int w = 1 + (c - 97) / 10;
			bitx = 32*z;
			bity = 32*w;
		}
		
		for (int y = 0; y < 32; y++)
		{
			int yPix = scale*y + yOffs;
			if (yPix < 0 || yPix >= height) continue;
			for (int x = 0; x < 32; x++)
			{
				int xPix = scale*x + xOffs;
				if (xPix < 0 || xPix >= width) continue;
				
				int src = Art.alphabet.pixels[(bitx + x) + (bity + y) * Art.alphabet.width];
				if (src != IGNORE_COL && src != IGNORE_COL2)
				{
					if (src == CHAR_INSIDE_COL) 
						src = col;
					else if (src == CHAR_OUTSIDE_COL)
						src = darken(col, 0.8);
					
					for (int i = 0; i < scale; i++)
					{
						for (int j = 0; j < scale; j++)
						{
							pixels[xPix + i + (yPix + j)*width] = src;
						}
					}
				}
			}
		}
	}
	
	// draw a box with a border colour, and inside colour, and scale for outside border
	// from (x,y) to (x + width, y + width)
	public void draw_box(int xOffs, int yOffs, int box_width, int box_height, int col, int border_col, int border_scale)
	{
		// draw inside
		for (int y = border_scale; y < box_height - border_scale; y++)
		{
			int y_pix = yOffs + y;
			if (y_pix < 0 || y_pix >= height) continue;
			for (int x = border_scale; x < box_width - border_scale; x++)
			{
				int x_pix = xOffs + x;
				if (x_pix < 0 || x_pix >= width) continue;
				pixels[x_pix + y_pix*width] = col;
			}
		}
		
		// draw outside
		int sign = 1;
		for (int i = 0; i < 2; i++)
		{
			for (int y = 0; y < border_scale; y++)
			{
				int y_pix = yOffs + sign*y + i*(box_height-1);
				if (y_pix < 0 || y_pix >= height) continue;
				for (int x = 0; x < box_width; x++)
				{
					int x_pix = xOffs + x;
					if (x_pix < 0 || x_pix >= width) continue;
					pixels[x_pix + y_pix*width] = border_col;
				}
			}
			
			sign *= -1;
		}
		
		sign = 1;
		for (int i = 0; i < 2; i++)
		{
			for (int y = 0; y < box_height; y++)
			{
				int y_pix = yOffs + y;
				if (y_pix < 0 || y_pix >= height) continue;
				for (int x = 0; x < border_scale; x++)
				{
					int x_pix = xOffs + sign*x + i*(box_width - 1);
					if (x_pix < 0 || x_pix >= width) continue;
					pixels[x_pix + y_pix*width] = border_col;
				}
			}
			
			sign *= -1;
		}
	}
	
	// draw bitmap
	// at xoffs and yoffs, with scale multipled - to make bigger
	public void draw(Bitmap bitmap, int xOffs, int yOffs, int scale)
	{
		for (int y = 0; y < bitmap.height; y++)
		{
			int yPix = scale*y + yOffs;
			if (yPix < 0 || yPix >= height) continue;
			for (int x = 0; x < bitmap.width; x++)
			{
				int xPix = scale*x + xOffs;
				if (xPix < 0 || xPix >= width) continue;
				
				int src = bitmap.pixels[x + y * bitmap.width];
				if (src != IGNORE_COL && src != IGNORE_COL2)
				{
					for (int i = 0; i < scale; i++)
					{
						for (int j = 0; j < scale; j++)
						{
							pixels[xPix + i + (yPix + j)*width] = src;
						}
					}
				}
			}
		}
	}
}