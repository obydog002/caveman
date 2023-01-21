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

    public static void fill_bordered_rect(Graphics g, int x, int y, int width, int height, int border_width, int border_height, Color outside, Color inner)
    {
        fill_rect(g, x, y, width, height, outside);
        fill_rect(g, x + border_width, y + border_height, width - 2*border_width, height - 2*border_height, inner);
    }
}