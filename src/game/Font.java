package src.game;

import java.awt.image.BufferedImage;
import java.awt.Graphics;

public class Font
{
    private BufferedImage alphabet;
    public Font(BufferedImage alphabet, int inside_col, int outside_col)
    {
        this.alphabet = write_alphabet_colors(alphabet, inside_col, outside_col);
    }

    public final static int InsideReplaceCol = (0x0000FFFF << 8);
	public final static int OutsideReplaceCol = (0x00A29EFF << 8);
	private static BufferedImage write_alphabet_colors(BufferedImage img, int inside_col, int outside_col)
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
		return img;
	}
}
