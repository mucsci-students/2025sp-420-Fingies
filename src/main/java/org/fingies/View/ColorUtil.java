package org.fingies.View;

import java.awt.Color;
import java.util.Objects;

public class ColorUtil {
	
	private static Color darkestColor = new Color(100, 100, 100); // the darkest color allowed
	private static Color brightestColor = new Color(240, 240, 240); // the darkest color allowed
	
	public static Color colorOfString(String str)
	{
		switch(str.toLowerCase())
		{
			case "red":
			case "r":
				return clamp(Color.red);
			case "green":
			case "g":
				return clamp(Color.green);
			case "blue":
			case "b":
				return clamp(Color.blue);
			case "yellow":
				return clamp(Color.yellow);
			case "cyan":
				return clamp(Color.cyan);
			case "magenta":
				return clamp(Color.magenta);
			case "orange":
				return clamp(Color.orange);
			case "pink":
				return clamp(Color.pink);
			case "black":
				return darkestColor;
			case "white":
				return brightestColor;
			case "gray":
				return average(darkestColor, brightestColor);
			case "light_gray":
			case "lightgray":
				return average(brightestColor, average(brightestColor, darkestColor));
			case "dark_gray":
			case "darkgray":
				return average(darkestColor, average(darkestColor, brightestColor));
			default:
				int hash = Objects.hash(str);
		        int r = (Math.abs(hash) >> 16) & 0xFF;
		        int g = (Math.abs(hash) >> 8) & 0xFF;
		        int b = Math.abs(hash) & 0xFF;

		        // Clamp to minColor–240 range so that they aren't too dark
		        r = darkestColor.getRed() + (r % (brightestColor.getRed() - darkestColor.getRed() + 1)); 
		        g = darkestColor.getGreen() + (g % (brightestColor.getBlue() - darkestColor.getBlue() + 1));
		        b = darkestColor.getBlue() + (b % (brightestColor.getGreen() - darkestColor.getGreen() + 1));
		        // TODO: change color generation for dark mode
		        
		        return new Color(r, g, b);
		}
	}
	
	public static Color clamp(Color color) {
	    int red = Math.min(brightestColor.getRed(), Math.max(darkestColor.getRed(), color.getRed()));
	    int green = Math.min(brightestColor.getGreen(), Math.max(darkestColor.getGreen(), color.getGreen()));
	    int blue = Math.min(brightestColor.getBlue(), Math.max(darkestColor.getBlue(), color.getBlue()));
	    return new Color(red, green, blue);
	}

	public static Color average(Color color1, Color color2)
	{
		return new Color ((color1.getRed() + color2.getRed()) / 2, (color1.getGreen() + color2.getGreen()) / 2, (color1.getBlue() + color2.getBlue()) / 2);
	}

}
