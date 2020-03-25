package game;

public class Bitmap
{
	public final int width;
	public final int height;
	public final int[] pixels;
	
	public Bitmap(int width, int height)
	{
		this.width = width;
		this.height = height;
		pixels = new int[width*height];
	}
	public void darken(double scale) //darken all pixels by a fraction eg 1 wont darken, 0 completely black
	{
		for (int i = 0; i < pixels.length; i++)
		{
			int col = pixels[i];
			int r = (col >> 16) & 0x000000ff;
			int g = (col >> 8) & 0x000000ff;
			int b = col & 0x000000ff;
			r *= scale;
			g *= scale;
			b *= scale;
			pixels[i] = 0xff000000 + (r << 16) + (g << 8) + b;
		}
	}
	public void draw(String word, int xOffs, int yOffs, int col)
	{
		for (int i = 0; i < word.length(); i++)
		{
			drawChar((int)word.charAt(i),xOffs + Main.SCALE*i,yOffs,col);
		}
	}
	public void drawChar(int c, int xOffs, int yOffs, int col)
	{
		int bitx = 256;
		int bity = 96;
		if (c == 32)
		{
			bitx = 288;
			bity = 96;
		}
		else if (c == 33)
		{
			bitx = 192;
			bity = 96;
		}
		else if (c == 58)
		{
			bitx = 224;
			bity = 96;
		}
		else if (c >= 48 && c < 58)
		{
			int z = c - 48;
			bitx = 32*z;
			bity = 0;
		}
		
		else if (c >= 97 && c < 123)
		{
			int z = (c - 97) % 10;
			int w = 1 + (c - 97) / 10;
			bitx = 32*z;
			bity = 32*w;
		}
		
		for (int y = 0; y < 32; y++)
		{
			int yPix = y+yOffs;
			if (yPix < 0 || yPix >= height) continue;
			for (int x = 0; x < 32; x++)
			{
				int xPix = x+xOffs;
				if (xPix < 0 || xPix >= width) continue;
				
				int src = Art.alphabet.pixels[(bitx + x) + (bity + y) * Art.alphabet.width];
				if (src != 0xffffff00 && src != 0xffff00ff)
				{
					if (src == 0xff00ffff) src = col;
					pixels[xPix + yPix*width] = src;
				}
			}
		}
	}
	public void draw(Bitmap bitmap, int xOffs, int yOffs)
	{
		for (int y = 0; y < bitmap.height; y++)
		{
			int yPix = y+yOffs;
			if (yPix < 0 || yPix >= height) continue;
			for (int x = 0; x < bitmap.width; x++)
			{
				int xPix = x+xOffs;
				if (xPix < 0 || xPix >= width) continue;
				
				int src = bitmap.pixels[x + y * bitmap.width];
				if (src != 0xffffff00 && src != 0xffff00ff)
					pixels[xPix + yPix*width] = src;
			}
		}
	}
}