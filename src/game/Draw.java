package src.game;

import java.awt.Graphics;
import java.awt.Color;

public class Draw
{
    public static void fill_rect(Graphics g, int x, int y, int width, int height, Color color)
    {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    // normalize - make the top-left corner the start of the inner box, not the outer box
    public static void fill_bordered_rect(Graphics g, int x, int y, int width, int height, int border_width, int border_height, Color outside, Color inner, boolean normalize)
    {
        if (normalize)
        {
            fill_rect(g, x - border_width, y - border_height, width + 2*border_width, height + 2*border_height, outside);
            fill_rect(g, x, y, width, height, inner);
        }
        else
        {
            fill_rect(g, x, y, width, height, outside);
            fill_rect(g, x + border_width, y + border_height, width - 2*border_width, height - 2*border_height, inner);
        }
    }

	private final static Color DarkenColor = new Color(0.0f, 0.0f, 0.0f, .3f);
    // darken a rectangle of space on the screen
    public static void darken(Graphics g, int x, int y, int width, int height)
    {
        fill_rect(g, x, y, width, height, DarkenColor);
    }
}
