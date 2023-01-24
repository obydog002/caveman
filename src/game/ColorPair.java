package src.game;

import java.awt.Color;

public class ColorPair
{
    public Color first;
    public Color second;

    public ColorPair(Color first, Color second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() 
    {
        return first.hashCode() + second.hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof ColorPair && ((ColorPair)other).first == this.first && ((ColorPair)other).second == this.second;
    }
}